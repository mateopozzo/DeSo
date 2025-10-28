package ddb.deso.almacenamiento.DTO;


import ddb.deso.alojamiento.DatosCheckIn;

import java.time.LocalDate;

public class DatosCheckInDTO{


    private LocalDate fecha_hora_in;
    long idCheckIn;

    /**
     *
     * @param obj Instancia de Check in de transferencia
     */
    public DatosCheckInDTO(DatosCheckIn obj) {
        fecha_hora_in=obj.getFecha_hora_in();
        idCheckIn= obj.getId();
    }

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
