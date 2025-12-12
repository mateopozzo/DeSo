/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.negocio.alojamiento;

import jakarta.persistence.*;
import lombok.*;

/**
 * Clase que almacena la información de **contacto** de una persona alojada.
 * Incluye el número de teléfono y la dirección de correo electrónico.
 * Esta clase es embebible en otras entidades ({@link DatosAlojado}).
 *
 * @author mat
 */
@EqualsAndHashCode(callSuper= false)
@Embeddable
@NoArgsConstructor
public class DatosContacto {
    /**
     * Número de teléfono de contacto.
     */
    private String telefono;
    /**
     * Dirección de correo electrónico de contacto.
     */
    private String email;

    /**
     * Constructor para inicializar los datos de contacto.
     *
     * @param telefono El número de teléfono.
     * @param email La dirección de correo electrónico.
     */
    public DatosContacto(String telefono, String email) {
        this.telefono = telefono;
        this.email = email;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
    
}
