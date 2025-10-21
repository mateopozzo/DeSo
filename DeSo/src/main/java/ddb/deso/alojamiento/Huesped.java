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

/**
 *
 * @author mat
 */
public class Huesped extends Alojado{

    private String razon_social;
    //private List <Huesped> huespedes= new LinkedList<>();
   // private Map<Long,TipoDoc> dniMap= new HashMap<>();


    public  Huesped (DatosAlojado da){
    this.datos=da;
    }
   public DatosAlojado getDatos(){
    return this.datos;
   }
/* 


    public void agregarHuesped (){
    
        huespedes.add(this);


    Long dni = this.datos.getDatosPersonales().getNroDoc();
    TipoDoc tipo = this.datos.getDatosPersonales().getTipoDoc();

    this.dniMap.put(dni,tipo);
}
public void eliminarHuesped (){
    huespedes.remove(this);
}

public Boolean dniExiste(){
    if (this.datos == null || this.datos.getDatosPersonales() == null) {
        System.out.println("ERROR: No se puede verificar un huésped sin datos.");
        return false;
    }
    Long dni = this.datos.getDatosPersonales().getNroDoc();
    TipoDoc tipo = this.datos.getDatosPersonales().getTipoDoc();


    Map.Entry<Long, TipoDoc> aux = new AbstractMap.SimpleEntry<>(dni,tipo);
    boolean coincide = this.dniMap.containsKey(aux.getKey()) &&
                       aux.getValue().equals(this.dniMap.get(aux.getKey()));

                           return coincide;
}
                           */
}





