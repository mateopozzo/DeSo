package ddb.deso.almacenamiento.DTO;

import ddb.deso.negocio.TipoDoc;
import ddb.deso.negocio.alojamiento.DatosCheckOut;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * DTO-Clase check out
 * @author mat
 */
@Getter
@Setter
@NoArgsConstructor
public class DatosCheckOutDTO {
    /*  Getters y setters   */
    LocalDateTime fecha_hora_out;
    long idCheckOut;
    List<String> nroDoc;
    List<TipoDoc> tipoDoc;

    /**
     * @param obj Instancia de {@code DatosCheckOut} transferido
     */
    public DatosCheckOutDTO(DatosCheckOut obj) {
        fecha_hora_out=obj.getFecha_hora_out();
        idCheckOut= obj.getIdCheckOut();
        nroDoc = new ArrayList<>();
        tipoDoc = new ArrayList<>();
        for(var a : obj.getAlojado()){
            nroDoc.add(a.getNroDoc());
            tipoDoc.add(a.getTipoDoc());
        }
    }

}
