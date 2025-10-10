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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author mat
 */
public class AlojadoDAOJSON implements AlojadoDAO {

    public AlojadoDAOJSON() {
    }
    
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
    public Alojado buscarPorDNI(String documento, TipoDoc tipo){
        return new Alojado() {};
    }
}
