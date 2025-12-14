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

@Service
public class GestorContabilidad {

    private EstadiaDAO estadiaDAO;
    private FacturaDAO facturaDAO;
    private ResponsablePagoDAO responsablePagoDAO;
    private HabitacionDAO habitacionDAO;

    @Autowired
    public GestorContabilidad(EstadiaDAO estadiaDAO, FacturaDAO facturaDAO, ResponsablePagoDAO respDAO, HabitacionDAO habDAO) {
        this.estadiaDAO = estadiaDAO;
        this.facturaDAO = facturaDAO;
        this.responsablePagoDAO = respDAO;
        this.habitacionDAO = habDAO;
    }

    public DetalleFacturaDTO calcularDetalleFacturacion(Long nroHabitacion, LocalTime horaSalida) throws Exception {
        // ... (Este método queda igual que antes) ...
        // Para abreviar la respuesta, asumo que el código de calcularDetalleFacturacion
        // sigue siendo el mismo que te pasé antes y que funcionaba bien.
        
        // Si necesitas que te lo pegue completo de nuevo, avísame.
        // Aquí repito la lógica básica para que compile si copias todo:
        Habitacion hab = habitacionDAO.buscarPorNumero(nroHabitacion); 
        if (hab == null) throw new Exception("Habitación no encontrada");

        Estadia estadiaActual = null;
        List<Estadia> todas = estadiaDAO.listar();
        for (Estadia e : todas) {
            if (e.getHabitacion().getNroHab().equals(nroHabitacion)) {
                estadiaActual = e; 
                break; 
            }
        }
        if (estadiaActual == null) throw new Exception("No hay estadía activa para esta habitación");

        LocalDate inicio = estadiaActual.getFecha_inicio();
        LocalDate fin = LocalDate.now();
        long dias = ChronoUnit.DAYS.between(inicio, fin);
        if (dias == 0) dias = 1;

        double precioNoche = hab.getTarifa(); 
        double costoEstadia = dias * precioNoche;

        if (horaSalida.isAfter(LocalTime.of(11, 0)) && horaSalida.isBefore(LocalTime.of(18, 0))) {
            costoEstadia += (precioNoche * 0.5); 
        } else if (horaSalida.isAfter(LocalTime.of(18, 0))) {
            costoEstadia += precioNoche; 
        }

        List<Servicio> serviciosEntity = estadiaActual.getListaServicios();
        List<ServicioDTO> serviciosDTO = new ArrayList<>();
        double totalServicios = 0.0;

        if (serviciosEntity != null) {
            for (Servicio s : serviciosEntity) {
                double precio = calcularPrecioServicio(s.getTipo_servicio());
                ServicioDTO dto = new ServicioDTO();
                dto.setIdServicio(s.getIdServicio());
                dto.setTipoServicio(s.getTipo_servicio());
                dto.setPrecio(precio); 
                serviciosDTO.add(dto);
                totalServicios += precio;
            }
        }

        TipoFactura tipoSugerido = TipoFactura.B;

        return new DetalleFacturaDTO(
            estadiaActual.getIdEstadia(), 
            "Habitación " + hab.getNroHab() + " - " + hab.getTipo_hab(),
            costoEstadia,
            serviciosDTO,
            costoEstadia + totalServicios,
            tipoSugerido
        );
    }

    public FacturaDTO generarFactura(GenerarFacturaRequestDTO request) throws Exception {
        
        Long idEstadia = request.getIdEstadia();
        Long idResponsable = request.getIdResponsable();

        Estadia estadia = estadiaDAO.read(idEstadia); 
        ResponsablePago responsable = responsablePagoDAO.read(idResponsable);

        if (estadia == null || responsable == null) throw new Exception("Datos inválidos");

        // 1. Crear Entidad
        Factura nuevaFactura = new Factura();
        nuevaFactura.setFecha_factura(LocalDate.now()); 
        nuevaFactura.setDestinatario(responsable.getRazonSocial());

        TipoFactura tipo = TipoFactura.B;
        if (responsable.getCuit() != null && responsable.getCuit() > 0) { 
             tipo = TipoFactura.A;
        }
        nuevaFactura.setTipo_factura(tipo); 

        DetalleFacturaDTO detalle = calcularDetalleFacturacion(estadia.getHabitacion().getNroHab(), LocalTime.now());
        float total = detalle.getMontoTotal().floatValue();

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

       
        FacturaDTO facturaParaGuardar = new FacturaDTO(nuevaFactura);
        
        facturaDAO.crearFactura(facturaParaGuardar);

        // Devolvemos el DTO con los datos calculados
        return facturaParaGuardar; 
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