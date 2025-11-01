/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.habitaciones;

import ddb.deso.TipoServicio;

/**
 * Representa un **servicio** ofrecido en una habitación (o en el contexto
 * de una reserva).
 * <p>
 * Su propósito principal es encapsular el tipo de servicio ofrecido,
 * el cual es definido por el enumerador {@link TipoServicio}.
 * </p>
 *
 * @see ddb.deso.TipoServicio
 */
public class Servicio {
    private TipoServicio tipo_servicio;

    public Servicio() {
    }

    public TipoServicio getTipo_servicio() {
        return tipo_servicio;
    }

    public void setTipo_servicio(TipoServicio tipo_servicio) {
        this.tipo_servicio = tipo_servicio;
    }
}
