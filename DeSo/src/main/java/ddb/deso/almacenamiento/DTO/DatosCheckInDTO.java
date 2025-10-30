package ddb.deso.almacenamiento.DTO;


import ddb.deso.alojamiento.DatosCheckIn;

import java.time.LocalDate;

/**
 * DTO-Clase check in
 * @author mat
 */

public class DatosCheckInDTO{
    private LocalDate fecha_hora_in;
    long idCheckIn;

    /**
     * @param obj Instancia de {@code DatosCheckIn} transferido
     */
    public DatosCheckInDTO(DatosCheckIn obj) {
        fecha_hora_in=obj.getFecha_hora_in();
        idCheckIn= obj.getId();
    }

    /*  Getters y setters */
    public LocalDate getFecha_hora_in() {
        return fecha_hora_in;
    }

    public void setFecha_hora_in(LocalDate fecha_hora_in) {
        this.fecha_hora_in = fecha_hora_in;
    }

    public long getIdCheckIn() {
        return idCheckIn;
    }

    public void setIdCheckIn(long idCheckIn) {
        this.idCheckIn = idCheckIn;
    }
}
