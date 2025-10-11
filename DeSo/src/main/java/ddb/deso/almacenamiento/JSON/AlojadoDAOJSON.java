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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import ddb.deso.alojamiento.Huesped;
import ddb.deso.alojamiento.Invitado;
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
        List<Alojado> listaAlojadosRetorno=new ArrayList<>();;
        System.out.println(RUTA_ARCHIVO_JSON_ALOJADOS);
        
        try(FileReader archivoJSON = new FileReader(RUTA_ARCHIVO_JSON_ALOJADOS)){
            Gson gson = new Gson();
            JsonArray arregloEnArchivoJSON = JsonParser.parseReader(archivoJSON).getAsJsonArray();
            for(JsonElement elementoJSON: arregloEnArchivoJSON){
                JsonObject objetoJSON = elementoJSON.getAsJsonObject();
                if(objetoJSON.has("razon_social")){
                    listaAlojadosRetorno.add(gson.fromJson(objetoJSON, Huesped.class));
                } else {
                    listaAlojadosRetorno.add(gson.fromJson(objetoJSON, Invitado.class));
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return listaAlojadosRetorno;
    }
    
    @Override
    public Alojado buscarPorDNI(String documento, TipoDoc tipo){
        return new Alojado() {};
    }
}
