/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.alojamiento;

import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import lombok.*;
import jakarta.persistence.*;

/**
 * Clase **abstracta base** que representa a cualquier persona alojada o registrada
 * en el sistema (un huésped o un invitado).
 * <p>
 * Sirve como la raíz de la jerarquía para el concepto de "Alojado",
 * encapsulando los datos comunes (personales, de contacto, y residencia) y
 * definiendo métodos básicos de comportamiento y persistencia.
 * </p>
 *
 * @see Huesped
 * @see Invitado
 */

@Entity
@Data
@Table(name = "alojado")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_alojado", discriminatorType = DiscriminatorType.STRING)
public abstract class Alojado {

    @EmbeddedId
    private AlojadoID id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @MapsId
    @JoinColumns({
            @JoinColumn(name = "nro_doc", referencedColumnName = "nro_doc"),
            @JoinColumn(name = "tipo_doc", referencedColumnName = "tipo_doc")
    })
    protected DatosAlojado datos;

    public void checkIn(Alojado alojado) {
    }
    public void checkOut(Alojado alojado) {
    }
    public boolean esMayor() {
        return this.getDatos().getEdad() >= 18;
    }
    public abstract void completarDTO(AlojadoDTO dto);
}
