/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.alojamiento;

import java.util.Date;

/**
 *
 * @author mat
 */
public class DatosCheckOut {
    Date fecha_hora_iout;

    public DatosCheckOut(Date fecha_hora_iout) {
        this.fecha_hora_iout = fecha_hora_iout;
    }
    
    public Date getFecha_hora_iout() {
        return fecha_hora_iout;
    }
    public void setFecha_hora_iout(Date fecha_hora_iout) {
        this.fecha_hora_iout = fecha_hora_iout;
    }
}
