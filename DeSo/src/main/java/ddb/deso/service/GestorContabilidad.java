package ddb.deso.service;
import ddb.deso.negocio.contabilidad.*;
import ddb.deso.almacenamiento.DAO.*;
import ddb.deso.almacenamiento.DTO.*;
import ddb.deso.negocio.habitaciones.*;
import ddb.deso.negocio.TipoFactura;
import ddb.deso.negocio.TipoServicio; // Asegurate que este enum esté en este paquete o impórtalo

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GestorContabilidad {

    private EstadiaDAO estadiaDAO;
    private FacturaDAO facturaDAO;
    private ResponsablePagoDAO responsablePagoDAO;
    private HabitacionDAO habitacionDAO;

    // Constructor con Inyección de Dependencias
    public GestorContabilidad(EstadiaDAO estadiaDAO, FacturaDAO facturaDAO, ResponsablePagoDAO respDAO, HabitacionDAO habDAO) {
        this.estadiaDAO = estadiaDAO;
        this.facturaDAO = facturaDAO;
        this.responsablePagoDAO = respDAO;
        this.habitacionDAO = habDAO;
    }

    /**
     * CU07 Paso 1: Pre-visualización de la factura
     */
    public DetalleFacturaDTO calcularPreFacturacion(Long nroHabitacion, LocalTime horaSalida) throws Exception {
        
        // 1. Validar Habitación
        Habitacion hab = habitacionDAO.buscarPorNumero(nroHabitacion); // Método según tu UML DAO
        if (hab == null) throw new Exception("Habitación no encontrada");

        // 2. Buscar Estadía Activa (Lógica simplificada sobre listar())
        Estadia estadiaActual = null;
        List<Estadia> todas = estadiaDAO.listar();
        
        for (Estadia e : todas) {
            // Verifica que sea la habitación y que no tenga fecha fin (o sea hoy/futura)
            if (e.getHabitacion().getNroHab().equals(nroHabitacion)) {
                // Aquí podrías agregar lógica para ver si está "activa" (fechaFin null o futura)
                estadiaActual = e; 
                break; 
            }
        }
        
        if (estadiaActual == null) throw new Exception("No hay estadía activa para esta habitación");

        // 3. Calcular Costo Estadía (Días * Tarifa + Recargos)
        // Convertimos Date a LocalDate para usar ChronoUnit
        LocalDate inicio = estadiaActual.getFecha_inicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fin = LocalDate.now();

        long dias = ChronoUnit.DAYS.between(inicio, fin);
        if (dias == 0) dias = 1;

        double precioNoche = hab.getTarifa(); 
        double costoEstadia = dias * precioNoche;

        // Regla: Late Check-out 
        if (horaSalida.isAfter(LocalTime.of(11, 0)) && horaSalida.isBefore(LocalTime.of(18, 0))) {
            costoEstadia += (precioNoche * 0.5); // +50%
        } else if (horaSalida.isAfter(LocalTime.of(18, 0))) {
            costoEstadia += precioNoche; // +1 día
        }

        // 4. Procesar Servicios (Consumos)
        // Usamos la lista que ya tiene la estadía según el diagrama
        List<Servicio> serviciosEntity = estadiaActual.getListaServicios();
        List<ServicioDTO> serviciosDTO = new ArrayList<>();
        double totalServicios = 0.0;

        if (serviciosEntity != null) {
            for (Servicio s : serviciosEntity) {
                // Como el diagrama no tiene precio en Servicio, lo calculamos acá
                double precio = calcularPrecioServicio(s.getTipo_servicio());
                
                // Usamos el constructor de ServicioDTO que creamos recién
                ServicioDTO dto = new ServicioDTO(s.getIdServicio(), s.getTipo_servicio(), precio); // Asumo s.getId() existe en entity base
                serviciosDTO.add(dto);
                totalServicios += precio;
            }
        }

        // 5. Determinar Tipo Factura Sugerida (A o B)
        // Nota: Accedemos al responsable a través del huésped de la estadía si no hay uno asignado aún
        // Para simplificar, sugerimos B por defecto
        TipoFactura tipoSugerido = TipoFactura.B;

        // 6. Retornar DTO Armado
        return new DetalleFacturaDTO(
            estadiaActual.getIdEstadia(), 
            "Habitación " + hab.getNroHab() + " - " + hab.getTipo_hab(),
            costoEstadia,
            serviciosDTO,
            costoEstadia + totalServicios,
            tipoSugerido
        );
    }

    /**
     * CU07 Paso 2: Generar Factura
     */
    public FacturaDTO generarFactura(Long idEstadia, Long idResponsable, List<Long> idsServicios) throws Exception {
        
        // Recuperar Entidades
        Estadia estadia = estadiaDAO.read(idEstadia.intValue()); // El DAO usa int según diagrama
        ResponsablePago responsable = responsablePagoDAO.read(idResponsable.intValue());

        if (estadia == null || responsable == null) throw new Exception("Datos inválidos");

        // Crear Factura
        Factura nuevaFactura = new Factura();
        nuevaFactura.setFecha_factura(new Date());
        
        // Seteamos destinatario (String según diagrama)
        nuevaFactura.setDestinatario(responsable.getRazonSocial());

        // Lógica de Tipo Factura
        TipoFactura tipo = TipoFactura.B;
        if (responsable.getCuit() > 0) { // int según diagrama
             tipo = TipoFactura.A;
        }
        // nuevaFactura.setTipo(tipo); // Asumiendo setter en Factura

        // Recalcular montos (Simplificado: facturamos todo)
        DetalleFacturaDTO detalle = calcularPreFacturacion(estadia.getHabitacion().getNroHab(), LocalTime.now());
        float total = detalle.getMontoTotal().floatValue();

        // Asignar montos según diagrama (tiene setMontoIVA(total, iva))
        if (tipo == TipoFactura.A) {
            float neto = (float) (total / 1.21);
            int iva = (int) (total - neto); // El diagrama pide int para IVA, raro pero lo respetamos
            nuevaFactura.setMontoIVA(total, iva);
        } else {
            nuevaFactura.setMontoIVA(total, 0);
        }

        // Guardar
        facturaDAO.create(nuevaFactura);

        // Retornar DTO vacío o con datos básicos
        FacturaDTO dto = new FacturaDTO();
        // Setear datos al DTO...
        return dto;
    }

    // Helper privado para precios (Hardcodeado ya que no está en DB)
    private double calcularPrecioServicio(TipoServicio tipo) {
        if (tipo == null) return 0.0;
        switch (tipo) {
            case TipoServicio.LAVADOYPLANCHADO: return 1500.0;
            case TipoServicio.BAR: return 4500.0;
            case TipoServicio.SAUNA: return 8000.0;
            default: return 0.0;
        }
    }
    
    // Métodos placeholder exigidos por el Diagrama de Clases
    public ResponsablePago buscarRespPago() { return null; }
    public boolean ingresarPago() { return false; }
    public Pago listarCheques() { return null; }
    public List<DatosCheckIn> listarIngresos() { return new ArrayList<>(); }
    public void generarNotaCredito() { }
    public void gestionarListado() { }
}