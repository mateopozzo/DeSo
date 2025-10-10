
import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.JSON.AlojadoDAOJSON;
import ddb.deso.alojamiento.Alojado;
import ddb.deso.alojamiento.DatosAlojado;
import ddb.deso.alojamiento.DatosContacto;
import ddb.deso.alojamiento.DatosPersonales;
import ddb.deso.alojamiento.DatosResidencia;
import ddb.deso.alojamiento.Huesped;
import java.util.Date;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Clase dedicada a la prueba de operaciones de clase alojado y sus clases descendientes
 * 
 * @author mat
 */
public class PoblacionDeAlojados {

    public PoblacionDeAlojados() {
    }
    
    public Alojado crearUnAlojado(){
        DatosResidencia dr = new DatosResidencia("3a", "villa anonima", "cordoba", "vieja argentina", "mefalta", 432, (long)5, "304"); 
        DatosContacto dc = new DatosContacto(4742205, "polimorfismo@herencia.encap");
        DatosPersonales dp = new DatosPersonales("knapsack", "estado transicion", "mundo", "extremo centro", "stalker profesional", 1234532, TipoDoc.PASAPORTE, 13857, new Date());
        DatosAlojado da = new DatosAlojado(dc,dr,dp);
        Alojado x = new Huesped();
        x.setDatos(da);
        return x;
    }
    
    public void guardarUnAlojado(){
        Alojado alocado = crearUnAlojado();
        AlojadoDAOJSON guardar = new AlojadoDAOJSON();
        guardar.crearAlojado(alocado);
        
        System.out.println("se deberia haber guardado algou!");
    }
}
