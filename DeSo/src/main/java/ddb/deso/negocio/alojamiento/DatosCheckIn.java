/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.negocio.alojamiento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.*;
import jakarta.persistence.*;

/**
 * Clase que encapsula los datos relacionados con el proceso de **Check-In** (Entrada).
 * Almacena la fecha y hora de la entrada y el identificador único del proceso.
 *
 * @author mat
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "check_in")
public class DatosCheckIn {
    /**
     * Fecha y hora del registro de Check-In.
     */
    private LocalDate fecha_hora_in;
    /**
     * Identificador único y autoincremental del registro de Check-In.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    long idCheckIn;

    /**
     * El {@link DatosAlojado} asociado a este registro de Check-In.
     * Se mapea usando las columnas de la clave compuesta (nro_doc, tipo_doc).
     */
    @ManyToMany(fetch = FetchType.LAZY)
    private List<DatosAlojado> alojado = new ArrayList<>();

    /**
     * Constructor para inicializar el registro de Check-In con la fecha y hora de entrada.
     *
     * @param fecha_hora_in Fecha y hora del Check-In.
     */
    public DatosCheckIn(LocalDate fecha_hora_in) {
        this.fecha_hora_in = fecha_hora_in;
    }

    public long getId() {
        return idCheckIn;
    }

    public void setId(long idCheckIn) {
        this.idCheckIn = idCheckIn;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DatosCheckIn that = (DatosCheckIn) o;
        return idCheckIn == that.idCheckIn;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idCheckIn);
    }
}
