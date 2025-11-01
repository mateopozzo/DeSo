/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.alojamiento;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;

import ddb.deso.almacenamiento.DTO.AlojadoDTO;

/**
 * Representa a un **invitado** o acompañante, una persona alojada que no es
 * el huésped principal ni tiene responsabilidad de facturación.
 * <p>
 * Extiende la clase {@link Alojado}. Por regla de negocio, automáticamente se
 * asegura que el campo CUIT esté vacío ({@code null}) al ser inicializado,
 * ya que no manejan datos fiscales.
 * </p>
 */
public class Invitado extends Alojado {
    public Invitado (DatosAlojado da){
        this.datos=da;
        this.datos.getDatos_personales().setCUIT(null);
    }
    public Invitado (){
        this.datos=new DatosAlojado();
    }
    @Override
    public void completarDTO(AlojadoDTO dto) {
        return;
    }
}
