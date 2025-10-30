/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.almacenamiento.JSON;
import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import java.util.ArrayList;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.alojamiento.CriteriosBusq;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


/**
 Implementa la interfaz DAO para:
 <ul>
    <li>Creación</li>
    <li>Modificación</li>
    <li>Eliminación</li>
    <li>Listado</li>
 </ul>
 de la clase abstracta Alojado en el sistema usando libreria GSON.
 @author mat
 */

public class AlojadoDAOJSON implements AlojadoDAO {
    // Ruta del archivo que contiene los datos se guarda en la carpeta data en el directorio raiz del proyecto
    private final static String RUTA_ARCHIVO_JSON_ALOJADOS = Paths.get("").toAbsolutePath().resolve("DeSo").resolve("data").resolve("Alojado.json").toString();
    private ManejadorJson manejador;

    public AlojadoDAOJSON() {
        this.manejador = new ManejadorJson(Path.of(RUTA_ARCHIVO_JSON_ALOJADOS), AlojadoDTO.class);
        //System.out.println(RUTA_ARCHIVO_JSON_ALOJADOS);
    }
    
    /*
     Escribe lista completa en JSON
     listaAlojados es una lista de entidades de Alojado a persistir
     */

    private void escribirListaEnArchivo(List<AlojadoDTO> listaAlojados){
        try {
            manejador.escribir(listaAlojados);
        } catch (IOException ex) {
            ex.printStackTrace();
        } 
    }
    
    /*
     Crea un nuevo registro de alojado y lo guarda en el archivo JSON
     Alojado es el objeto a guardar
     */

    @Override
    public void crearAlojado(AlojadoDTO alojado){
        List<AlojadoDTO> listaAlojados = listarAlojados();
        listaAlojados.add(alojado);
        escribirListaEnArchivo(listaAlojados);
    }

    // REVISAR ESTO --------------
    // Actualiza la instancia de alojado guardada en JSON

    @Override
    public void actualizarAlojado(AlojadoDTO alojadoPrev, AlojadoDTO alojadoNuevo){
        eliminarAlojado(alojadoPrev);
        this.crearAlojado(alojadoNuevo);
    }
    
    @Override
    public void eliminarAlojado(AlojadoDTO alojado){
        List<AlojadoDTO> listaAlojados = this.listarAlojados();
        listaAlojados.remove(alojado);
        escribirListaEnArchivo(listaAlojados);
    }
    
    /*
     Devuelve la lista completa de alojados almacenados.
     @return una lista {@link Alojado}
     */

    @Override
    public List<AlojadoDTO> listarAlojados(){
        List<AlojadoDTO> listaAlojadosRetorno=new ArrayList<>();
        
        try {
            listaAlojadosRetorno = manejador.listar();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return listaAlojadosRetorno;
    }

    /*
    Busco todos los alojados con listarAlojados y los guardo en una lista de DTO
    Inicializo otra lista vacía de DTO llamada encontrados
    Por cada alojado, llamo a cumpleCriterio. Si es verdad, lo agrego a encontrados
    */

    @Override
    public List<AlojadoDTO> buscarHuespedDAO (CriteriosBusq criterios_busq){
        // Tengo la lista de todos los alojados del archivo Alojado.JSON
        List<AlojadoDTO> lista_alojados = listarAlojados();
        List<AlojadoDTO> encontrados = new ArrayList<>();

        for(AlojadoDTO un_alojado: lista_alojados) {
            if (cumpleCriterio(un_alojado, criterios_busq)){
                encontrados.add(un_alojado);
            }
        }
        return encontrados;
    }

    /*
    Recibo una instancia de AlojadoDTO y un criterio de búsqueda
    Si el criterio fue definido, pero no coinciden los atributos, retorno falso
    Si el criterio fue definido y coincide, no entra a ningún if y devuelve true
    Si el criterio no fue definido, no se evalúa la segunda condición
    */

    private boolean cumpleCriterio (AlojadoDTO alojado_DTO, CriteriosBusq criterio) {
        // Criterios de búsqueda que pueden o no estar vacíos -> Hechos con clase plantilla CriteriosBusq
        String apellido_b = criterio.getApellido();
        String nombres_b = criterio.getNombre();
        TipoDoc tipoDoc_b = criterio.getTipoDoc();
        String nroDoc_b = criterio.getNroDoc();
        

        String apellido_h = alojado_DTO.getApellido();
        String nombre_h = alojado_DTO.getNombre();
        TipoDoc tipoDoc_h = alojado_DTO.getTipoDoc();
        String nroDoc_h = alojado_DTO.getNroDoc();

        if (no_es_vacio(apellido_b) && !apellido_h.equalsIgnoreCase(apellido_b)) {
            return false;
        }
        if (no_es_vacio(nombres_b) && !nombre_h.equalsIgnoreCase(nombres_b)) {
            return false;
        }
        if (no_es_vacio(tipoDoc_b.toString()) && !tipoDoc_h.equals(tipoDoc_b)) {
            return false;
        }
        if (no_es_vacio(nroDoc_b) && !nroDoc_h.equals(nroDoc_b)) {
            return false;
        }

        return true;
    }

    private boolean no_es_vacio (String contenido){
        boolean flag = (contenido==null || contenido.isEmpty());
        return !flag;
    }
    
    /*
     Busca algún alojado que coincida con el parámetro
     Se puede analizar optimización con hash o set ordenado.

     @param número de documento y tipo
     @return una {@link Alojado}
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
