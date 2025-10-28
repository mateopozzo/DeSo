/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.habitaciones;

import java.util.Date;

public class Reserva {
    private Date fecha_inicio;
    private Date fecha_fin;
    private String estado;

    public Reserva(Date fecha_inicio, Date fecha_fin, String estado) {
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.estado = estado;
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public Date getFecha_fin() {
        return fecha_fin;
    }

    public String getEstado() {
        return estado;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    
}
