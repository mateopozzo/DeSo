
package ddb.deso.service.alojamiento;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import jakarta.persistence.*;

/**
 * Representa a un **invitado** o acompañante, una persona alojada que no es
 * el huésped principal ni tiene responsabilidad de facturación.
 * <p>
 * Extiende la clase {@link Alojado}. Por regla de negocio, automáticamente se
 * asegura que el campo CUIT esté vacío ({@code null}) al ser inicializado,
 * ya que no manejan datos fiscales.
 * </p>
 */

@Entity
@DiscriminatorValue("Invitado")
public class Invitado extends Alojado {
    public Invitado (DatosAlojado da){
        super(da);
    }
    public Invitado (){
        this.datos=new DatosAlojado();
    }
    public void completarDTO(AlojadoDTO dto) {
        return;
    }
}
