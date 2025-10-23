
import ddb.deso.*;
import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.almacenamiento.JSON.AlojadoDAOJSON;
import ddb.deso.alojamiento.Alojado;
import ddb.deso.alojamiento.DatosAlojado;
import ddb.deso.alojamiento.DatosContacto;
import ddb.deso.alojamiento.DatosPersonales;
import ddb.deso.alojamiento.DatosResidencia;
import ddb.deso.alojamiento.Huesped;
import ddb.deso.alojamiento.Invitado;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Clase para prueba de clase {@link Alojado} y clases descendientes
 * 
 * @author mat
 */
public class PoblacionDeAlojados {

    public PoblacionDeAlojados() {
    }
    
//    public Huesped crearUnHuesped(){
//        DatosResidencia dr = new DatosResidencia("3a", "villa anonima", "cordoba", "vieja argentina", "mefalta", 432, (long)5, "304"); 
//        DatosContacto dc = new DatosContacto(4742205, "polimorfismo@herencia.encap");
//        DatosPersonales dp = new DatosPersonales("knapsack", "estado transicion", "mundo", "extremo centro", "stalker profesional", "1234532", TipoDoc.PASAPORTE, "21-231-13857", new Date());
//        DatosAlojado da = new DatosAlojado(dc,dr,dp);
//        Huesped x = new Huesped();
//        x.setRazon_social("la razon de la vida");
//        x.setDatos(da);
//        return x;
//    }
//    
//    public Invitado crearUnInvitado(){
//        DatosResidencia dr = new DatosResidencia("yo", "soy", "un", "invitado!", "mefalta", 432, (long)5, "sopa"); 
//        DatosContacto dc = new DatosContacto(4742205, "polimorfismo@herencia.encap");
//        DatosPersonales dp = new DatosPersonales("asdf", "estado dfgh", "mundo", "no se", "badmington", "1234f.45", TipoDoc.PASAPORTE, "20-13857-12", new Date());
//        DatosAlojado da = new DatosAlojado(dc,dr,dp);
//        Invitado x = new Invitado();
//        x.setDatos(da);
//        return x;
//    }
//    
//    public void guardarUnAlojado(){
//        Huesped alocado = crearUnHuesped();
//        AlojadoDTO dto = new AlojadoDTO(alocado);
//        AlojadoDAOJSON guardar = new AlojadoDAOJSON();
//        guardar.crearAlojado(dto);
//        System.out.println("se deberia haber guardado un Huesped nuevo");
//    }
//    public void guardarAlojadoEInvitado(){
//        Huesped alocado = crearUnHuesped();
//        Invitado invitado = crearUnInvitado();
//        AlojadoDTO dto1 = new AlojadoDTO(alocado);
//        AlojadoDTO dto2 = new AlojadoDTO(invitado);
//        AlojadoDAOJSON guardar = new AlojadoDAOJSON();
//        guardar.crearAlojado(dto1);
//        guardar.crearAlojado(dto2);
//        System.out.println("Se guardaron con exito!");
//    }
//    public void borrarTodo(){
//        AlojadoDAOJSON dao = new AlojadoDAOJSON();
//        List<AlojadoDTO> x = dao.listarAlojados();
//        Iterator i = x.iterator();
//        while(i.hasNext()){
//            dao.eliminarAlojado((AlojadoDTO) i.next());
//        }
//        return;
//    }
}
