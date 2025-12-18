package ddb.deso.almacenamiento.DTO;

import ddb.deso.negocio.TipoFactura;
import java.util.List;
import java.util.ArrayList;

/**
 * Representa los datos necesarios para el calculo de la factura
 * DTO utilizado en CU07 para la transferencia de datos plana entre el Front y el Back
 * */
public class DetalleFacturaDTO {

    // Identificador de la estadía que se está facturando
    private Long idEstadia;
    
    private String habitacion;
    
    // Costo calculado solo por el alojamiento (noches + recargos check-out)
    private Double costoEstadia;
    
    // Lista de consumos (bar, lavandería) convertidos a DTO para mostrarlos en la grilla
    private List<ServicioDTO> consumos;
    
    // Suma de costoEstadia + total de consumos 
    private Double montoTotal;
    
    // Sugerencia del sistema basada en la condición fiscal del responsable (A o B)
    private TipoFactura tipoFacturaSugerida;

    // Constructor vacío
    public DetalleFacturaDTO() {
        this.consumos = new ArrayList<>();
    }

    // Constructor completo
    public DetalleFacturaDTO(Long idEstadia, String habitacion, Double costoEstadia, List<ServicioDTO> consumos, Double montoTotal, TipoFactura tipoFacturaSugerida) {
        this.idEstadia = idEstadia;
        this.habitacion = habitacion;
        this.costoEstadia = costoEstadia;
        this.consumos = consumos;
        this.montoTotal = montoTotal;
        this.tipoFacturaSugerida = tipoFacturaSugerida;
    }


    public Long getIdEstadia() {
        return idEstadia;
    }

    public void setIdEstadia(Long idEstadia) {
        this.idEstadia = idEstadia;
    }

    public String getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(String habitacion) {
        this.habitacion = habitacion;
    }

    public Double getCostoEstadia() {
        return costoEstadia;
    }

    public void setCostoEstadia(Double costoEstadia) {
        this.costoEstadia = costoEstadia;
    }

    public List<ServicioDTO> getConsumos() {
        return consumos;
    }

    public void setConsumos(List<ServicioDTO> consumos) {
        this.consumos = consumos;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public TipoFactura getTipoFacturaSugerida() {
        return tipoFacturaSugerida;
    }

    public void setTipoFacturaSugerida(TipoFactura tipoFacturaSugerida) {
        this.tipoFacturaSugerida = tipoFacturaSugerida;
    }
    
    // Método helper opcional para agregar consumos uno a uno
    public void agregarConsumo(ServicioDTO consumo) {
        if (this.consumos == null) {
            this.consumos = new ArrayList<>();
        }
        this.consumos.add(consumo);
    }
}