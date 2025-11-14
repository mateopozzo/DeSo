/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.alojamiento;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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

public class Huesped extends Alojado{

    private String razon_social;

    public Huesped (DatosAlojado da){
        this.datos=da;
    }
    public DatosAlojado getDatos(){
        return this.datos; 
    }
    public void setRazon_social(String razon_social) {
        this.razon_social = razon_social;
    }

    public String getRazon_social() {
        return razon_social;
    }

    @Override
    public void completarDTO(AlojadoDTO dto) {
        dto.setRazon_social(razon_social);
    }
}





