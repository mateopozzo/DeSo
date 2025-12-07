/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.service.habitaciones;

import ddb.deso.TipoServicio;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="servicio")
public class Servicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    long idServicio;

    private TipoServicio tipo_servicio;
}
