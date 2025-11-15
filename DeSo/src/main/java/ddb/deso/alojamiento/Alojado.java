package ddb.deso.alojamiento;/* ... (paquete e importaciones) ... */

import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "alojado")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_alojado", discriminatorType = DiscriminatorType.STRING)
public abstract class Alojado {

    // --- 1. VUELVE A AGREGAR ESTE CAMPO ---
    @EmbeddedId
    private AlojadoID id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALLType.ALL, orphanRemoval = true)
    @MapsId // <-- Esto SÍ es correcto y debe quedar


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