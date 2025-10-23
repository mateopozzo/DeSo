/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.almacenamiento.JSON;
import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.alojamiento.Alojado;
import java.util.ArrayList;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import java.io.IOException;
import java.nio.file.Path;
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
    /**
     * Ruta archivo que contiene los datos se guarda en la carpeta <b>data</b> en el directorio raiz del proyecto
     */
    private final static String RUTA_ARCHIVO_JSON_ALOJADOS = Paths.get("").toAbsolutePath().resolve("data").resolve("Alojado.json").toString();
    private ManejadorJson manejador;
    public AlojadoDAOJSON() {
        this.manejador = new ManejadorJson(Path.of(RUTA_ARCHIVO_JSON_ALOJADOS), AlojadoDTO.class);
        //System.out.println(RUTA_ARCHIVO_JSON_ALOJADOS);
    }
    
    
    /**
     * Escribe lista completa en JSON
     * 
     * @param listaAlojados es lista de Entidades de {@link Alojado} a persistir
     */
    private void escribirListaEnArchivo(List<AlojadoDTO> listaAlojados){
        try {
            manejador.escribir(listaAlojados);
        } catch (IOException ex) {
            ex.printStackTrace();
        } 
    }
    
    /**
     * Crea un nuevo registro de {@link Alojado} y lo guarda en el archivo JSON.
     *
     * @param alojado el objeto a almacenar
     */
    @Override
    public void crearAlojado(AlojadoDTO alojado){
        List<AlojadoDTO> listaAlojados = listarAlojados();
        listaAlojados.add(alojado);
        escribirListaEnArchivo(listaAlojados);
    }
    
    /**
     * Actualiza la instancia de Alojado guardada en json bajo suposicion de que Documento y TipoDoc son inmutables
     *
     * @param alojado actualizado
     * 
     */
    @Override
    public void actualizarAlojado(AlojadoDTO alojado){
        String documento = alojado.getNroDoc();
        TipoDoc tipo = alojado.getTipoDoc();
        AlojadoDTO remover = this.buscarPorDNI(documento, tipo);
        eliminarAlojado(remover);
        this.crearAlojado(alojado);
    }
    
    @Override
    public void eliminarAlojado(AlojadoDTO alojado){
        List<AlojadoDTO> listaAlojados = this.listarAlojados();
        listaAlojados.remove(alojado);
        escribirListaEnArchivo(listaAlojados);
    }
    
    /**
     * Devuelve la lista completa de alojados almacenados.
     *
     * @return una lista {@link Alojado}
     */
    @Override
    public List<AlojadoDTO> listarAlojados(){
        List<AlojadoDTO> listaAlojadosRetorno=new ArrayList<>();;
        
        try {
            listaAlojadosRetorno = manejador.listar();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return listaAlojadosRetorno;
    }
    
    /**
     * Busca algun alojado que coincida con el parametro
     * Se puede analizar optimizacion con hash o set ordenado.
     *
     * @param numero de documento y tipo
     * @return una {@link Alojado}
     */
    @Override
    public AlojadoDTO buscarPorDNI(String documento, TipoDoc tipo){
        List<AlojadoDTO> listaAlojados = listarAlojados();
        for(AlojadoDTO alojadoPersistente: listaAlojados){
            String documentoInstancia = alojadoPersistente.getNroDoc();
            TipoDoc tipoDocumentoInstancia = alojadoPersistente.getTipoDoc();
            if(documentoInstancia.equals(documento) && tipoDocumentoInstancia.equals(tipo)){
                return alojadoPersistente;
            }
        }
        return null;
    }
}
