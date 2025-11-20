package ddb.deso.alojamiento;

import ddb.deso.TipoDoc;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlojadoID implements Serializable {
    @Column(name = "nro_doc")
    private String nroDoc;

    @Column(name = "tipo_doc", length = 50)
    @Enumerated(EnumType.STRING)
    private TipoDoc tipoDoc;
}
