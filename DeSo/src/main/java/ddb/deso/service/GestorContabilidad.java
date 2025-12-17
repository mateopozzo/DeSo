package ddb.deso.service;

import ddb.deso.negocio.contabilidad.*;
import ddb.deso.almacenamiento.DAO.*;
import ddb.deso.almacenamiento.DTO.*; 
import ddb.deso.negocio.habitaciones.*;
import ddb.deso.negocio.TipoFactura;
import ddb.deso.negocio.TipoServicio;
import ddb.deso.negocio.alojamiento.Alojado;
import ddb.deso.negocio.alojamiento.DatosCheckIn;
import ddb.deso.service.excepciones.ResponsableMenorEdadExcepcion;

import ddb.deso.service.strategias.EstrategiaGuardadoFactura;
import ddb.deso.service.strategias.GuardarFacturaJSON;
import ddb.deso.service.strategias.GuardarFacturaPDF;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Consumer;

@Service
public class GestorContabilidad {

    private EstadiaDAO estadiaDAO;
    private FacturaDAO facturaDAO;
    private ResponsablePagoDAO responsablePagoDAO;
    private HabitacionDAO habitacionDAO;
    private AlojadoDAO alojadoDAO;
    Map<String, ? extends EstrategiaGuardadoFactura> estrategias = Map.of(
        "pdf", new GuardarFacturaPDF(),
        "json", new GuardarFacturaJSON()
    );

    @Autowired
    public GestorContabilidad(EstadiaDAO estadiaDAO, FacturaDAO facturaDAO, ResponsablePagoDAO respDAO, HabitacionDAO habDAO,AlojadoDAO alojadoDAO) {
        this.estadiaDAO = estadiaDAO;
        this.facturaDAO = facturaDAO;
        this.responsablePagoDAO = respDAO;
        this.habitacionDAO = habDAO;
        this.alojadoDAO= alojadoDAO;
    }
    
    //saco horaSalida porque no es necesario para buscar la estadia ahora

    public Estadia existeEstadia(Long nroHabitacion)throws Exception {
        
        Habitacion hab = habitacionDAO.buscarPorNumero(nroHabitacion); 
        if (hab == null) throw new Exception("Habitación no encontrada");

        Estadia estadiaActual = null;
        List<Estadia> todas = estadiaDAO.listar();
        LocalDate hoy = LocalDate.now(); 

        for (Estadia e : todas) {
            if ((e.getHabitacion().getNroHab().equals(nroHabitacion)) && 
                (e.getDatosCheckOut()==null)&&
                (!hoy.isBefore(e.getFecha_inicio())) &&
                (!hoy.isAfter(e.getFecha_fin()))){
                estadiaActual = e; 
                break; 
            }
        }
        if (estadiaActual == null) throw new Exception("No hay estadía activa para esta habitación");
       
    
        return estadiaActual;//se necesita la entidad para otra funcion, en otro lugar
                                //convierte a dto
    }



    public DetalleFacturaDTO calcularDetalleFacturacion(Long nroHabitacion, LocalTime horaSalida) throws Exception {
        
        Habitacion hab = habitacionDAO.buscarPorNumero(nroHabitacion); 
     
        Estadia estadiaActual=existeEstadia(nroHabitacion);
       
        LocalDate inicio = estadiaActual.getFecha_inicio();
        LocalDate fin = estadiaActual.getFecha_fin();

        long dias = ChronoUnit.DAYS.between(inicio, fin);

        if (dias == 0) {
            dias = 1; // Mínimo 1 día
        }
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
                double precio = calcularPrecioServicio(s.getTipo_servicio()); //decision de precios estaticos en funcion de back
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

        validarEdadResponsable(idResponsable);

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


        LocalTime  hora_checkout;
        //duda!!
        //asumo que fueron los datos check out en el huesped por un endpoint antrior

        hora_checkout =estadia.getDatosCheckOut().getFecha_hora_out().toLocalTime();
    
        
        DetalleFacturaDTO detalle = calcularDetalleFacturacion(estadia.getHabitacion().getNroHab(), hora_checkout);
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



private void validarEdadResponsable(Long cuitResponsable) throws ResponsableMenorEdadExcepcion {
        // Listar todos los alojados
        List<Alojado> alojados = alojadoDAO.listarAlojados();
        
        Optional<Alojado> personaEncontrada = alojados.stream()
                .filter(a -> {
                    if (a.getDatos() == null || 
                        a.getDatos().getDatos_personales() == null || 
                        a.getDatos().getDatos_personales().getCUIT() == null) {
                        return false;
                    }
                    
                    try {
                        // Convertimos el CUIT de String (Alojado) a Long (Responsable) para comparar
                        String cuitStr = a.getDatos().getDatos_personales().getCUIT();
                        return Long.parseLong(cuitStr) == cuitResponsable;
                    } catch (NumberFormatException e) {
                        return false; // Si el CUIT no es numérico, no coincide
                    }
                }) 
                .findFirst();

        if (personaEncontrada.isPresent()) {
            // Usamos el getter correcto para la fecha: getFechanac()
            LocalDate fechaNacimiento = personaEncontrada.get()
                                        .getDatos().getDatos_personales().getFechanac();
            
            if (fechaNacimiento != null) {
                int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
                if (edad < 18) {
                    throw new ResponsableMenorEdadExcepcion(
                        "El responsable es menor de edad (" + edad + "). Se requiere mayor de 18."
                    );
                }
            }
        }
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

    public void guardarFacturaSegunStrategy(FacturaDTO factura, String strat) {

        if(estrategias == null || estrategias.isEmpty()){
            estrategias = Map.of(
                    "pdf", new GuardarFacturaPDF(),
                    "json", new GuardarFacturaJSON()
            );
        }

        try {
            estrategias.get(strat).guardarFactura(factura);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}