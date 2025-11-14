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
    private String nroDoc;
    @Enumerated(EnumType.STRING)
    private TipoDoc tipoDoc;
}
