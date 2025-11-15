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

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_doc")
    private TipoDoc tipoDoc;
}
