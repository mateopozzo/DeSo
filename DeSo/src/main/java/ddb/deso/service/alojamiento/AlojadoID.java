package ddb.deso.service.alojamiento;

import ddb.deso.service.TipoDoc;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;
/**
 * Clase que representa el identificador compuesto de un {@link DatosAlojado}.
 * <p>
 * Es una clave primaria embebida que combina el número de documento y el tipo
 * de documento del alojado.
 * </p>
 *
 * @author mat
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlojadoID implements Serializable {
    /**
     * Número de documento de la persona alojada.
     */
    @Column(name = "nro_doc")
    private String nroDoc;

    /**
     * Tipo de documento de la persona alojada (ej: DNI, Pasaporte).
     */
    @Column(name = "tipo_doc", length = 50)
    @Enumerated(EnumType.STRING)
    private TipoDoc tipoDoc;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AlojadoID alojadoID = (AlojadoID) o;
        return Objects.equals(nroDoc, alojadoID.nroDoc) && tipoDoc == alojadoID.tipoDoc;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nroDoc, tipoDoc);
    }
}
