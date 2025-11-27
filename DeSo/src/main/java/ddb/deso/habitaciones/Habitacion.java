/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.habitaciones;

import ddb.deso.TipoHab;
import ddb.deso.EstadoHab;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;


/**
 * Representa una **Habitación** dentro del sistema de gestión hotelera.
 * <p>
 * Define las propiedades físicas y el estado operativo de la habitación,
 * incluyendo su tipo, tarifa, capacidad y número de identificación.
 * </p>
 *
 * @see ddb.deso.TipoHab
 * @see ddb.deso.EstadoHab
 */
@Getter
@Setter
@Entity
@Table(name="habitacion")
@NoArgsConstructor
public class Habitacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    private Long nroHab;
    private TipoHab tipo_hab;
    private EstadoHab estado_hab;
    private float tarifa;
    private int capacidad;

    public Habitacion(TipoHab tipo_hab, EstadoHab estado_hab, float tarifa, int capacidad) {
        this.tipo_hab = tipo_hab;
        this.estado_hab = estado_hab;
        this.tarifa = tarifa;
        this.capacidad = capacidad;
    }

    public Long getNroHab() {
        return nroHab;
    }

    public TipoHab getTipo_hab() {
        return tipo_hab;
    }

    public EstadoHab getEstado_hab() {
        return estado_hab;
    }

    public float getTarifa() {
        return tarifa;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setNroHab(Long nroHab) {
        this.nroHab = nroHab;
    }

    public void setTipo_hab(TipoHab tipo_hab) {
        this.tipo_hab = tipo_hab;
    }

    public void setEstado_hab(EstadoHab estado_hab) {
        this.estado_hab = estado_hab;
    }

    public void setTarifa(float tarifa) {
        this.tarifa = tarifa;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Habitacion that = (Habitacion) o;
        return Objects.equals(nroHab, that.nroHab);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nroHab);
    }
}
