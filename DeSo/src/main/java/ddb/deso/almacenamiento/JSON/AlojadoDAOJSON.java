/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.almacenamiento.JSON;
import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.alojamiento.Alojado;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
<<<<<<< HEAD
import ddb.deso.alojamiento.Huesped;
=======
>>>>>>> e3dc27ab692399d3bfe8476305487623d55af8e6
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * Esta clase implementa la interfaz DAO para:
 * <ul>
 *   <li>Creacion</li>
 *   <li>Modificacion</li>
 *   <li>Eliminacion</li>
 *   <li>Listado</li>
 * </ul>
 * de la clase abtracta Alojado en el sistema usando libreria GSON.
 * @author mat
 */
public class AlojadoDAOJSON implements AlojadoDAO {

    public AlojadoDAOJSON() {
    }
<<<<<<< HEAD
    /**
     * El archivo que contiene los datos se guarda en la carpeta <b>data</b> en el directorio raiz del proyecto
     */
    private final static String RUTA_ARCHIVO_JSON_ALOJADOS = Paths.get("").toAbsolutePath().resolve("data").resolve("Alojado.json").toString();
    
    /**
     * Escribe lista completa en JSON
     * 
     * @param listaAlojados es lista de Entidades de {@link Alojado} a persistir
     */
    private void escribirListaEnArchivo(List<Alojado> listaAlojados){
        try (FileWriter escribirJSON = new FileWriter(RUTA_ARCHIVO_JSON_ALOJADOS)) {
            Gson gson = new Gson();
            gson.toJson(listaAlojados,escribirJSON);
            escribirJSON.flush();
            escribirJSON.close();
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Crea un nuevo registro de {@link Alojado} y lo guarda en el archivo JSON.
     *
     * @param alojado el objeto a almacenar
     */
    @Override
    public void crearAlojado(Alojado alojado){
        List<Alojado> listaAlojados = listarAlojados();
        listaAlojados.add(alojado);
        escribirListaEnArchivo(listaAlojados);
    }
    
    @Override
    public void actualizarAlojado(Alojado alojado){
    }
    
    @Override
    public void eliminarAlojado(Alojado alojado){
    }
    
    /**
     * Devuelve la lista completa de alojados almacenados.
     *
     * @return una lista {@link Alojado}
     */
    @Override
    public List<Alojado> listarAlojados(){
        List<Alojado> listaAlojadosRetorno=null;
        System.out.println(RUTA_ARCHIVO_JSON_ALOJADOS);
        
        try(FileReader archivoJSON = new FileReader(RUTA_ARCHIVO_JSON_ALOJADOS)){
            Gson gson = new Gson();
            java.lang.reflect.Type listType = new TypeToken<ArrayList<Huesped>>(){}.getType();
            listaAlojadosRetorno = gson.fromJson(archivoJSON, listType);
            if(listaAlojadosRetorno==null)
                listaAlojadosRetorno = new ArrayList<>();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return listaAlojadosRetorno;
    }
    
    @Override
=======
    
    private final static String rutaArchivoJSON = Paths.get("").toAbsolutePath().resolve("data").resolve("Alojado.json").toString();
    
    @Override
    public void crearAlojado(Alojado alojado){
        Gson gson = new Gson();
        
        System.out.println(rutaArchivoJSON);
        
        try(FileWriter escribirJSON = new FileWriter(rutaArchivoJSON);){
            
            List<Alojado> listaAlojados = listarAlojados();
            
            listaAlojados.add(alojado);
            gson.toJson(listaAlojados,escribirJSON);
            escribirJSON.flush();
            escribirJSON.close();
            
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void actualizarAlojado(Alojado alojado){
    }
    
    @Override
    public void eliminarAlojado(Alojado alojado){
    }
    
    @Override
    public List<Alojado> listarAlojados(){
        Gson gson = new Gson();
        
        List<Alojado> listaAlojadosRetorno=null;
        
        System.out.println(rutaArchivoJSON);
        
        try(FileReader archivoJSON = new FileReader(rutaArchivoJSON)){
            java.lang.reflect.Type listType = new TypeToken<List<Alojado>>(){}.getType();
            listaAlojadosRetorno = gson.fromJson(archivoJSON, listType);
            if(listaAlojadosRetorno==null)
                listaAlojadosRetorno = new ArrayList<>();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return (ArrayList<Alojado>) listaAlojadosRetorno;
    }
    
    @Override
>>>>>>> e3dc27ab692399d3bfe8476305487623d55af8e6
    public Alojado buscarPorDNI(String documento, TipoDoc tipo){
        return new Alojado() {};
    }
}
