package ddb.deso.service;

import ddb.deso.negocio.contabilidad.*;
import ddb.deso.almacenamiento.DAO.*;
import ddb.deso.almacenamiento.DTO.*;
import ddb.deso.negocio.habitaciones.*;
import ddb.deso.negocio.TipoFactura;
import ddb.deso.negocio.TipoServicio;
import ddb.deso.negocio.alojamiento.DatosCheckIn;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service // Agregada anotación para que Spring lo detecte
public class GestorContabilidad {

    private EstadiaDAO estadiaDAO;
    private FacturaDAO facturaDAO;
    private ResponsablePagoDAO responsablePagoDAO;
    private HabitacionDAO habitacionDAO;

    // Constructor con Inyección de Dependencias
    @Autowired // Opcional en versiones nuevas de Spring si es el único constructor
    public GestorContabilidad(EstadiaDAO estadiaDAO, FacturaDAO facturaDAO, ResponsablePagoDAO respDAO, HabitacionDAO habDAO) {
        this.estadiaDAO = estadiaDAO;
        this.facturaDAO = facturaDAO;
        this.responsablePagoDAO = respDAO;
        this.habitacionDAO = habDAO;
    }

    /**
     * CU07 Paso 1: Pre-visualización de la factura
     */
    public DetalleFacturaDTO calcularDetalleFacturacion(Long nroHabitacion, LocalTime horaSalida) throws Exception {
        
        // 1. Validar Habitación
        // Asumiendo que el método en DAO es buscarPorNumero o similar.
        // Si el DAO recibe Integer, castear nroHabitacion.intValue()
        Habitacion hab = habitacionDAO.buscarPorNumero(nroHabitacion); 
        if (hab == null) throw new Exception("Habitación no encontrada");

        // 2. Buscar Estadía Activa
        Estadia estadiaActual = null;
        List<Estadia> todas = estadiaDAO.listar();
        
        for (Estadia e : todas) {
            // Verifica habitación y que fecha_fin sea null o futura (lógica de "activa")
            // NOTA: Se asume que Habitacion tiene getNroHab(). Verificar nombre exacto en entidad.
            if (e.getHabitacion().getNroHab().equals(nroHabitacion)) { 
                estadiaActual = e; 
                break; 
            }
        }
        
        if (estadiaActual == null) throw new Exception("No hay estadía activa para esta habitación");

        // 3. Calcular Costo Estadía (Días * Tarifa + Recargos)
        // Estadia usa LocalDate, usamos ChronoUnit directamente.
        LocalDate inicio = estadiaActual.getFecha_inicio();
        LocalDate fin = LocalDate.now(); // Fecha actual para el cálculo

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
        List<Servicio> serviciosEntity = estadiaActual.getListaServicios();
        List<ServicioDTO> serviciosDTO = new ArrayList<>();
        double totalServicios = 0.0;

        if (serviciosEntity != null) {
            for (Servicio s : serviciosEntity) {
                double precio = calcularPrecioServicio(s.getTipo_servicio());
                
                // Constructor DTO: Asegurarse que coincida con tu clase ServicioDTO
                ServicioDTO dto = new ServicioDTO();
                dto.setIdServicio(s.getIdServicio());
                dto.setTipoServicio(s.getTipo_servicio());
                dto.setPrecio(precio); // Asumiendo que agregaste este campo al DTO
                
                serviciosDTO.add(dto);
                totalServicios += precio;
            }
        }

        // 5. Determinar Tipo Factura Sugerida
        TipoFactura tipoSugerido = TipoFactura.B;
        // Aquí podrías buscar al huésped principal si la estadía ya tiene uno,
        // o dejarlo por defecto B hasta que el usuario elija responsable.

        // 6. Retornar DTO Armado
        DetalleFacturaDTO detalle = new DetalleFacturaDTO();
        detalle.setIdEstadia(estadiaActual.getIdEstadia());
        detalle.setHabitacion("Habitación " + hab.getNroHab() + " - " + hab.getTipo_hab());
        detalle.setCostoEstadia(costoEstadia);
        detalle.setConsumos(serviciosDTO);
        detalle.setMontoTotal(costoEstadia + totalServicios);
        detalle.setTipoFacturaSugerida(tipoSugerido);
        
        return detalle;
    }

    /**
     * CU07 Paso 2: Generar Factura
     */
    public FacturaDTO generarFactura(GenerarFacturaRequestDTO request) throws Exception {
        // Extraer datos del request DTO
        Long idEstadia = request.getIdEstadia();
        Long idResponsable = request.getIdResponsable();
        
        // Recuperar Entidades
        // Nota: El diagrama dice int, pero los IDs suelen ser Long. Ajusta el cast si es necesario.
        Estadia estadia = estadiaDAO.read(idEstadia.intValue()); 
        ResponsablePago responsable = responsablePagoDAO.read(idResponsable);

        if (estadia == null || responsable == null) throw new Exception("Datos inválidos");

        // Crear Factura
        Factura nuevaFactura = new Factura();
        nuevaFactura.setFecha_factura(LocalDate.now()); // Usa LocalDate, no Date
        nuevaFactura.setDestinatario(responsable.getRazonSocial());

        // Lógica de Tipo Factura
        TipoFactura tipo = TipoFactura.B;
        if (responsable.getCuit() != null && responsable.getCuit() > 0) { 
             tipo = TipoFactura.A;
        }
        nuevaFactura.setTipo_factura(tipo); 

        // Recalcular montos (Simplificado: facturamos todo lo pendiente)
        DetalleFacturaDTO detalle = calcularDetalleFacturacion(estadia.getHabitacion().getNroHab(), LocalTime.now());
        float total = detalle.getMontoTotal().floatValue();

        // Asignar montos según la entidad Factura
        nuevaFactura.setImporte_total(total);
        
        if (tipo == TipoFactura.A) {
            float neto = (float) (total / 1.21);
            float iva = total - neto;
            nuevaFactura.setImporte_neto(neto);
            nuevaFactura.setImporte_iva(iva);
        } else {
            nuevaFactura.setImporte_neto(total);
            nuevaFactura.setImporte_iva(0);
        }

        // Guardar
        FacturaDTO dto = new FacturaDTO(nuevaFactura);
        facturaDAO.crearFactura(dto);

        return dto;
    }

    private double calcularPrecioServicio(TipoServicio tipo) {
        if (tipo == null) return 0.0;
        switch (tipo) {
            case LAVADOYPLANCHADO: return 1500.0;
            case BAR: return 4500.0;
            case SAUNA: return 8000.0;
            default: return 0.0;
        }
    }
    
    // Métodos placeholder 
    public ResponsablePago buscarRespPago() { return null; }
    public boolean ingresarPago() { return false; }
    public Pago listarCheques() { return null; }
    public List<DatosCheckIn> listarIngresos() { return new ArrayList<>(); }
    public void generarNotaCredito() { }
    public void gestionarListado() { }
}