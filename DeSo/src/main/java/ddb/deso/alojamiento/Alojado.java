/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.alojamiento;

/**
 *
 * @author mat
 */
public abstract class Alojado {
    private DatosAlojado datos;

    public void setDatos(DatosAlojado datos) {
        this.datos = datos;
    }
    public DatosAlojado getDatos() {
        return datos;
    }
    public void checkIn(Alojado alojado) {
        
    }
    public void checkOut(Alojado alojado) {
        
    }
    public boolean esMayor() {
        return this.getDatos().getEdad() >= 18; 
        
    }
}
