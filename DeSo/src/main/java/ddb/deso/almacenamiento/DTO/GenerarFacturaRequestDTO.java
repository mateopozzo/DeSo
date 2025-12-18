package ddb.deso.almacenamiento.DTO;

import ddb.deso.negocio.TipoFactura;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerarFacturaRequestDTO {

    private Long idEstadia;
    private TipoFactura tipoFactura;
    private String destinatario;
    private boolean cobrarAlojamiento;

    private List<Long> idsConsumosAIncluir;

    private String responsableId;
    private String responsableTipo;
}