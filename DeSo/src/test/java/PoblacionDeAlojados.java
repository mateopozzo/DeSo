
import ddb.deso.*;
import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.JSON.AlojadoDAOJSON;
import ddb.deso.alojamiento.Alojado;
import ddb.deso.alojamiento.DatosAlojado;
import ddb.deso.alojamiento.DatosContacto;
import ddb.deso.alojamiento.DatosPersonales;
import ddb.deso.alojamiento.DatosResidencia;
import ddb.deso.alojamiento.Huesped;
import ddb.deso.alojamiento.Invitado;
import java.util.Date;
import java.util.List;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
<<<<<<< HEAD
 * Clase para prueba de clase {@link Alojado} y clases descendientes
 * 
 * @author mat
 */
public class PoblacionDeAlojados {

    public PoblacionDeAlojados() {
    }
    
    public Alojado crearUnHuesped(){
        DatosResidencia dr = new DatosResidencia("3a", "villa anonima", "cordoba", "vieja argentina", "mefalta", 432, (long)5, "304"); 
        DatosContacto dc = new DatosContacto(4742205, "polimorfismo@herencia.encap");
        DatosPersonales dp = new DatosPersonales("knapsack", "estado transicion", "mundo", "extremo centro", "stalker profesional", 1234532, TipoDoc.PASAPORTE, 13857, new Date());
        DatosAlojado da = new DatosAlojado(dc,dr,dp);
        Huesped x = new Huesped();
        x.setRazon_social("la razon de la vida");
        x.setDatos(da);
        return x;
    }
    
    public Alojado crearUnInvitado(){
        DatosResidencia dr = new DatosResidencia("yo", "soy", "un", "invitado!", "mefalta", 432, (long)5, "sopa"); 
        DatosContacto dc = new DatosContacto(4742205, "polimorfismo@herencia.encap");
        DatosPersonales dp = new DatosPersonales("asdf", "estado dfgh", "mundo", "no se", "badmington", 1234532, TipoDoc.PASAPORTE, 13857, new Date());
        DatosAlojado da = new DatosAlojado(dc,dr,dp);
        Invitado x = new Invitado();
        x.setDatos(da);
        return x;
    }
    
    public void guardarUnAlojado(){
        Alojado alocado = crearUnHuesped();
        AlojadoDAOJSON guardar = new AlojadoDAOJSON();
        guardar.crearAlojado(alocado);
        System.out.println("se deberia haber guardado un Huesped nuevo");
    }
    public void guardarAlojadoEInvitado(){
        Alojado alocado = crearUnHuesped();
        Alojado invitado = crearUnInvitado();
        AlojadoDAOJSON guardar = new AlojadoDAOJSON();
        guardar.crearAlojado(alocado);
        guardar.crearAlojado(invitado);
        System.out.println("Se guardaron con exito!");
    }

    void mostrarSiEsHuesped() {
        AlojadoDAOJSON leer = new AlojadoDAOJSON();
        List<Alojado> lista=leer.listarAlojados();
        for(Alojado a: lista){
            System.out.println((a instanceof Huesped) ? "1": "0");
        }
        
    }
}
