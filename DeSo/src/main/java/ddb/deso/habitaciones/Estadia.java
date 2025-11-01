/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.habitaciones;

import java.util.Date;


/**
 * Define el **período de tiempo** que un huésped pasa en el hotel.
 * <p>
 * Representa un simple rango de fechas (fecha de inicio y fecha de fin),
 * siendo un componente fundamental de una {@link Reserva}.
 * </p>
 *
 * @see ddb.deso.habitaciones.Reserva
 */
public class Estadia {
    private Date fecha_inicio;
    private Date fecha_fin;

    public Estadia(Date fecha_inicio, Date fecha_fin) {
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public Date getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }
    
    
}
