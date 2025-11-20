/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.alojamiento;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
/**
 * Clase que encapsula los datos relacionados con el proceso de **Check-Out** (Salida).
 * Almacena la fecha y hora de la salida y el identificador único del proceso.
 */

@Data
@NoArgsConstructor
@Entity
@Table(name = "check_out")
public class DatosCheckOut {
    LocalDate fecha_hora_out;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long idCheckOut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "nro_doc", referencedColumnName = "nro_doc"),
            @JoinColumn(name = "tipo_doc", referencedColumnName = "tipo_doc")
    })
    private DatosAlojado alojado;

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

    public void setFecha_hora_iout(LocalDate fecha_hora_out) {
        this.fecha_hora_out = fecha_hora_out;
    }
}
