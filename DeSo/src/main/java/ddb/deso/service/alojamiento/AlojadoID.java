package ddb.deso.service.alojamiento;

import ddb.deso.service.TipoDoc;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlojadoID implements Serializable {
    @Column(name = "nro_doc")
    private String nroDoc;

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
