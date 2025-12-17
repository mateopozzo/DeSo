package ddb.deso.almacenamiento.DTO;

import java.util.List;

public class GenerarFacturaRequestDTO {

    private Long idEstadia;
    private Long idResponsable;
    // Lista de IDs de servicios específicos a facturar (opcional, si null se factura todo)
    private List<Long> idsServicios;

    public GenerarFacturaRequestDTO() {
    }

    public GenerarFacturaRequestDTO(Long idEstadia, Long idResponsable, List<Long> idsServicios) {
        this.idEstadia = idEstadia;
        this.idResponsable = idResponsable;
        this.idsServicios = idsServicios;
    }

    public Long getIdEstadia() {
        return idEstadia;
    }

    public void setIdEstadia(Long idEstadia) {
        this.idEstadia = idEstadia;
    }

    public Long getIdResponsable() {
        return idResponsable;
    }

    public void setIdResponsable(Long idResponsable) {
        this.idResponsable = idResponsable;
    }

    public List<Long> getIdsServicios() {
        return idsServicios;
    }

    public void setIdsServicios(List<Long> idsServicios) {
        this.idsServicios = idsServicios;
    }
}