package ddb.deso.service.contabilidad;

import ddb.deso.service.alojamiento.DatosResidencia;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Representa a la **entidad o persona** legalmente responsable de efectuar un pago
 * o recibir una factura.
 * <p>
 * Almacena los datos fiscales y de contacto necesarios para fines de contabilidad
 * y facturación.
 * </p>
 *
 * @see DatosResidencia
 */

@Getter
@Setter
@Entity
@Table(name="responsable_de_pago")
@NoArgsConstructor
public class ResponsablePago {

    @Id
    private Long cuit;
    private String razonSocial;
    private DatosResidencia direccion;
    private int telefono;



    public ResponsablePago(String razonSocial, Long cuit, DatosResidencia direccion, int telefono) {
        this.razonSocial = razonSocial;
        this.cuit = cuit;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public String getRazonSocial() {
        return razonSocial;
    }
    public Long getCuit() {
        return cuit;
    }
    public DatosResidencia getDireccion() {
        return direccion;
    }
    public int getTelefono() {
        return telefono;
    }

}