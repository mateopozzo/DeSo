package ddb.deso.almacenamiento.DTO;

import ddb.deso.alojamiento.DatosCheckOut;

import java.time.LocalDate;


/**
 * DTO-Clase check out
 * @author mat
 */

public class DatosCheckOutDTO {
    LocalDate fecha_hora_out;
    long idCheckOut;

    /**
     * @param obj Instancia de {@code DatosCheckOut} transferido
     */
    public DatosCheckOutDTO(DatosCheckOut obj) {
        fecha_hora_out=obj.getFecha_hora_out();
        idCheckOut= obj.getId();
    }

    /*  Getters y setters   */
    public LocalDate getFecha_hora_out() {
        return fecha_hora_out;
    }

    public void setFecha_hora_out(LocalDate fecha_hora_out) {
        this.fecha_hora_out = fecha_hora_out;
    }

    public long getIdCheckOut() {
        return idCheckOut;
    }

    public void setIdCheckOut(long idCheckOut) {
        this.idCheckOut = idCheckOut;
    }
}
