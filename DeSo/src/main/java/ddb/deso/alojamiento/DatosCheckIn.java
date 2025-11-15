/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.alojamiento;

import java.time.LocalDate;
import lombok.*;
import jakarta.persistence.*;

/**
 * Clase que encapsula los datos relacionados con el proceso de **Check-In** (Entrada).
 * Almacena la fecha y hora de la entrada y el identificador único del proceso.
 */

@Data
@NoArgsConstructor
@Entity
@Table(name = "check_in")
public class DatosCheckIn {
    private LocalDate fecha_hora_in;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    long idCheckIn;

    @ManyToOne(fetch = FetchType.LAZY) // LAZY es buena práctica
    @JoinColumns({
            @JoinColumn(name = "nro_doc", referencedColumnName = "nro_doc"),
            @JoinColumn(name = "tipo_doc", referencedColumnName = "tipo_doc")
    })
    private DatosAlojado alojado;

    public DatosCheckIn(LocalDate fecha_hora_in) {
        this.fecha_hora_in = fecha_hora_in;
    }

    public long getId() {
        return idCheckIn;
    }

    public void setId(long idCheckIn) {
        this.idCheckIn = idCheckIn;
    }

    public LocalDate getFecha_hora_in() {
        return fecha_hora_in;
    }
    public void setFecha_hora_in(LocalDate fecha_hora_in_p) {
        this.fecha_hora_in = fecha_hora_in_p;
    }
    
}
