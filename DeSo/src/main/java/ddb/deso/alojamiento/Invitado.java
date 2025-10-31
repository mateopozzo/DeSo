/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.alojamiento;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;

import ddb.deso.almacenamiento.DTO.AlojadoDTO;

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
