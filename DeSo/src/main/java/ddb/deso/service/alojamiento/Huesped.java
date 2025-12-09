/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.service.alojamiento;

import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Representa a un **huésped principal** que puede tener datos comerciales,
 * como la razón social, lo cual es relevante para la facturación.
 * <p>
 * Extiende la clase {@link Alojado} y añade funcionalidad específica para
 * el tratamiento de huéspedes principales, como la inclusión de razón social
 * en el DTO.
 * </p>
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("Huesped")
public class Huesped extends Alojado {

    private String razon_social;

    public Huesped (DatosAlojado da) {
        super(da);
    }

    public void setRazon_social(String razon_social) {
        this.razon_social = razon_social;
    }

    @Override
    public void completarDTO(AlojadoDTO dto) {
        dto.setRazon_social(razon_social);
    }
}





