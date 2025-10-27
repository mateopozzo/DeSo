/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.alojamiento;

public abstract class Alojado {

    protected DatosAlojado datos;

    // esto está raris - revisar
    public void setDatos(DatosAlojado datos){
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
