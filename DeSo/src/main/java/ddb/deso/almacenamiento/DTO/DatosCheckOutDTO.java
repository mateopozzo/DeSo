package ddb.deso.almacenamiento.DTO;

import ddb.deso.negocio.TipoDoc;
import ddb.deso.negocio.alojamiento.DatosCheckOut;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


/**
 * DTO-Clase check out
 * @author mat
 */
@Getter
@Setter
public class DatosCheckOutDTO {
    /*  Getters y setters   */
    LocalDateTime fecha_hora_out;
    long idCheckOut;
    String nroDoc;
    TipoDoc tipoDoc;

    /**
     * @param obj Instancia de {@code DatosCheckOut} transferido
     */
    public DatosCheckOutDTO(DatosCheckOut obj) {
        fecha_hora_out=obj.getFecha_hora_out();
        idCheckOut= obj.getIdCheckOut();
        nroDoc=obj.getAlojado().getNroDoc();
        tipoDoc=obj.getAlojado().getTipoDoc();
    }

}
