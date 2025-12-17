/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.negocio.alojamiento;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Clase que encapsula los datos relacionados con el proceso de **Check-Out** (Salida).
 * Almacena la fecha y hora de la salida y el identificador único del proceso.
 *
 * @author mat
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "check_out")
public class DatosCheckOut {
    /**
     * Fecha y hora del registro de Check-Out.
     */
    @Getter
    LocalDateTime fecha_hora_out;

    /**
     * Identificador único y autoincremental del registro de Check-Out.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long idCheckOut;

    /**
     * El {@link DatosAlojado} asociado a este registro de Check-Out.
     * Se mapea usando las columnas de la clave compuesta (nro_doc, tipo_doc).
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "checkout_alojados",
            joinColumns = @JoinColumn(name = "id_check_out"),
            inverseJoinColumns = {
                    @JoinColumn(name = "nro_doc", referencedColumnName = "nro_doc"),
                    @JoinColumn(name = "tipo_doc", referencedColumnName = "tipo_doc")
            }
    )
    private List<DatosAlojado> alojado = new ArrayList<>();

    public DatosCheckOut(LocalDateTime fecha_hora_out) {
        this.fecha_hora_out = fecha_hora_out;
    }

    public long getId() {
        return idCheckOut;
    }

    public void setId(long idCheckOut) {
        this.idCheckOut = idCheckOut;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DatosCheckOut that = (DatosCheckOut) o;
        return idCheckOut == that.idCheckOut;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idCheckOut);
    }
}
