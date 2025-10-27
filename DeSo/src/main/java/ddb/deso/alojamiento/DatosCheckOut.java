/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.alojamiento;

import java.time.LocalDate;

public class DatosCheckOut {
    LocalDate fecha_hora_out;
    long idCheckOut;

    public DatosCheckOut(LocalDate fecha_hora_out) {
        this.fecha_hora_out = fecha_hora_out;
    }

    public long getId() {
        return idCheckOut;
    }

    public void setId(long idCheckOut) {
        this.idCheckOut = idCheckOut;
    }

    public LocalDate getFecha_hora_out() {
        return fecha_hora_out;
    }

    public void setFecha_hora_iout(LocalDate fecha_hora_out_p) {
        this.fecha_hora_out = fecha_hora_out_p;
    }
}
