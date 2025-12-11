/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.almacenamiento.JSON;

import ddb.deso.almacenamiento.DAO.DatosCheckInDAO;
import ddb.deso.service.alojamiento.DatosCheckIn;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*
 Implementa la interfaz DAO para:
 <ul>
    <li>Creación</li>
    <li>Modificación</li>
    <li>Eliminación</li>
    <li>Listado</li>
 </ul>
 de la clase DatosCheckIn en el sistema usando libreria GSON.
 @author mat
 */
public class DatosCheckInDAOJSON implements DatosCheckInDAO {

    // Ruta del archivo que contiene los datos se guarda en la carpeta data en el directorio raiz del proyecto
    private final static String RUTA_ARCHIVO_JSON_CHECKIN = Paths.get("").toAbsolutePath().resolve("data").resolve("CheckIn.json").toString();
    private final ManejadorJson manejador;

    public DatosCheckInDAOJSON() {
        this.manejador = new ManejadorJson(Path.of(RUTA_ARCHIVO_JSON_CHECKIN), DatosCheckIn.class);
    }

    private void escribirListaEnArchivo(List<DatosCheckIn> listaCI){
        try {
            manejador.escribir(listaCI);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*
    * */
    @Override
    public void crearDatosCheckIn(DatosCheckIn datosCheckIn){
        List<DatosCheckIn> listaCI=listarDatosCheckIn();
        listaCI.add(datosCheckIn);
        escribirListaEnArchivo(listaCI);
    }
    /*
     * No implementado
     */
    @Override
    public void actualizarDatosCheckIn(DatosCheckIn datosCheckInPre, DatosCheckIn datosCheckIn){

    }

    // No implementado
    @Override
    public void eliminarDatosCheckIn(DatosCheckIn datosCheckIn){
    }
    
    @Override
    public List<DatosCheckIn> listarDatosCheckIn(){
        List<DatosCheckIn> listaCI=new ArrayList<>();

        try {
            listaCI = manejador.listar();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return listaCI;
    }
}
