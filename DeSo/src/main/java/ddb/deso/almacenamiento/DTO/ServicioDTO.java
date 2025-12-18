package ddb.deso.almacenamiento.DTO;

import ddb.deso.negocio.TipoServicio;

/**
 * Clase de transferencia de datos de tipo {@link ddb.deso.negocio.habitaciones.Servicio}
 */
public class ServicioDTO {
    
    private Long idServicio;
    private TipoServicio tipoServicio;
    
    // Atributo sugerido para el CU07 (Facturar) para mostrar el costo en pantalla,
    // aunque no figure explícitamente en el diagrama UML original.
    private Double precio; 

    public ServicioDTO() {
    }

    public ServicioDTO(Long idServicio, TipoServicio tipoServicio, Double precio) {
        this.idServicio = idServicio;
        this.tipoServicio = tipoServicio;
        this.precio = precio;
    }

    public Long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    public TipoServicio getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }
    
    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}