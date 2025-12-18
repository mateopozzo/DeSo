package ddb.deso.service;

import ddb.deso.negocio.contabilidad.*;
import ddb.deso.almacenamiento.DAO.*;
import ddb.deso.almacenamiento.DTO.*; 
import ddb.deso.negocio.habitaciones.*;
import ddb.deso.negocio.TipoFactura;
import ddb.deso.negocio.TipoServicio;
import ddb.deso.negocio.alojamiento.Alojado;
import ddb.deso.negocio.alojamiento.DatosCheckIn;
import ddb.deso.negocio.alojamiento.DatosCheckOut;
import ddb.deso.service.excepciones.ResponsableMenorEdadExcepcion;

import ddb.deso.service.estrategias.EstrategiaGuardadoFactura;
import ddb.deso.service.estrategias.GuardarFacturaJSON;
import ddb.deso.service.estrategias.GuardarFacturaPDF;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Gestor encargado de manerjar entidades referidas a la facturacion del hotel
 */
@Service
public class GestorContabilidad {

    private EstadiaDAO estadiaDAO;
    private FacturaDAO facturaDAO;
    private ResponsablePagoDAO responsablePagoDAO;
    private HabitacionDAO habitacionDAO;
    private AlojadoDAO alojadoDAO;

    /**
     * Estructura que apunta a las estrategias de guardado disponibles
     */
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

    /**
     * Funcion para consultar la existencia de alguna estadia facturable con numero de habitacion dado
     * @param nroHabitacion : Numero de habitacion a facturar
     * @return entidad de {@link Estadia} facturable, no culminada
     * @throws Exception
     */
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
       
    
        return estadiaActual;
    }


    /**
     * @param nroHabitacion : Numero de habitacion a detallar
     * @param horaSalida : Hora de salida de alojados
     * @return : entidad {@link DetalleFacturaDTO} con precios a pagar
     * @throws Exception
     */
    public DetalleFacturaDTO calcularDetalleFacturacion(Long nroHabitacion, LocalTime horaSalida) throws Exception {
        Estadia estadiaActual = existeEstadia(nroHabitacion);
        return calcularMontos(estadiaActual, null, true, horaSalida);
    }

    private DetalleFacturaDTO calcularMontos(Estadia estadia, List<Long> idsConsumosAIncluir,boolean cobrarAlojamiento, LocalTime horaSalida) {
        Habitacion hab = estadia.getHabitacion();
        LocalDate inicio = estadia.getFecha_inicio();
        LocalDate fin = estadia.getFecha_fin();
        
        LocalDateTime fechaHoraCombinada = LocalDateTime.of(fin, horaSalida);
        DatosCheckOut datosCheckOut = new DatosCheckOut();
        datosCheckOut.setFecha_hora_out(fechaHoraCombinada);

        long dias = ChronoUnit.DAYS.between(inicio, fin);

        if (dias == 0) dias = 1;

        double precioNoche = hab.getTarifa();
        double costoEstadia = 0.0;

        if (cobrarAlojamiento) {
            costoEstadia = dias * precioNoche;

            // late check out solo si cobrarAlojamiento true
            if (horaSalida.isAfter(LocalTime.of(11, 0)) && horaSalida.isBefore(LocalTime.of(18, 0))) {
                costoEstadia += (precioNoche * 0.5);
            } else if (horaSalida.isAfter(LocalTime.of(18, 0))) {
                costoEstadia += precioNoche;
            }
        }

        List<Servicio> serviciosEntity = estadia.getListaServicios();
        List<ServicioDTO> serviciosDTO = new ArrayList<>();
        double totalServicios = 0.0;

        if (serviciosEntity != null) {
            for (Servicio s : serviciosEntity) {
                if (idsConsumosAIncluir == null || idsConsumosAIncluir.contains(s.getIdServicio())) {

                    double precio = calcularPrecioServicio(s.getTipo_servicio());

                    ServicioDTO dto = new ServicioDTO();
                    dto.setIdServicio(s.getIdServicio());
                    dto.setTipoServicio(s.getTipo_servicio());
                    dto.setPrecio(precio);

                    serviciosDTO.add(dto);
                    totalServicios += precio;
                }
            }
        }

        TipoFactura tipoSugerido = TipoFactura.B;

        return new DetalleFacturaDTO(
            estadia.getIdEstadia(), 
            "Habitación " + hab.getNroHab() + " - " + hab.getTipo_hab(),
            costoEstadia,
            serviciosDTO,
            costoEstadia + totalServicios,
            tipoSugerido
        );
    }

    /**
     * Creacion de un responsable de pago si no existe
     * @param request : Entidad {@link GenerarFacturaRequestDTO} con los campos necesarios para crear al responsable
     * @return
     * @throws Exception
     */
    private ResponsablePago crearResponsableDesdeRequest(GenerarFacturaRequestDTO request) throws Exception {

        ResponsablePago nuevo = new ResponsablePago();

        nuevo.setCuit(Long.parseLong(request.getResponsableId()));
        nuevo.setRazonSocial(request.getDestinatario());

        return nuevo;
    }

    /**
     * Genera una factura linkeando la estadía, el responsable del pagoy agregando check out a los Alojados que participaron en la estadia
     * Por defecto el tipo de factura generada es B, en caso de factura A se recalcula el precio segun IVA
     * @param request : Objeto con datos necesarios para poder generar la factura
     * @return
     * @throws Exception
     */
    public FacturaDTO generarFactura(GenerarFacturaRequestDTO request) throws Exception {
        // id estadia
        Long idEstadia = request.getIdEstadia();
        // id responsable
        long idResponsable;
        try {
            idResponsable = Long.parseLong(request.getResponsableId());
        } catch (NumberFormatException e) {
            throw new Exception("ID de responsable inválido");
        }

        Estadia estadia = estadiaDAO.read(idEstadia);
        ResponsablePago responsable = responsablePagoDAO.read(idResponsable);

        if (estadia == null) {
            throw new Exception("Estadía inválida");
        }

        if (responsable == null) {
            responsable = crearResponsableDesdeRequest(request);
        }

        Factura nuevaFactura = new Factura();
        nuevaFactura.setFecha_factura(LocalDate.now());
        nuevaFactura.setDestinatario(responsable.getRazonSocial());

        TipoFactura tipo = (request.getTipoFactura() != null) ?
                TipoFactura.valueOf(String.valueOf(request.getTipoFactura())) :
                TipoFactura.B;

        nuevaFactura.setTipo_factura(tipo);

        LocalTime hora_checkout;
        if (estadia.getDatosCheckOut() != null) {
            hora_checkout = estadia.getDatosCheckOut().getFecha_hora_out().toLocalTime();
        } else {
            hora_checkout = LocalTime.now();
        }

        DetalleFacturaDTO detalle = calcularMontos(
                estadia,
                request.getIdsConsumosAIncluir(),
                request.isCobrarAlojamiento(),
                hora_checkout
        );

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

        return facturaParaGuardar;
    }


    /**
     * Verifica que el responsable del pago sea mayor de edad
     * @param cuitResponsable
     * @throws ResponsableMenorEdadExcepcion
     */
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

    /**
     * @param tipo : Tipo de servicio tomado por huesped
     * @return
     */
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

    /**
     * Funcion que llama al patron Strategy adecuado según la estrategia de guardado que se desea aplicar
     *
     * @param factura : Factura para generar informe
     * @param strat : Estrategia de generacion de archivo
     * @return arreglo con los datos generados
     */
    public byte[] guardarFacturaSegunStrategy(FacturaDTO factura, String strat) {
        return estrategias.get(strat).guardarFactura(factura);
    }
}