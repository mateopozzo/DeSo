/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.almacenamiento.JSON;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.alojamiento.CriteriosBusq;


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
    private final ManejadorJson manejador;

    public AlojadoDAOJSON() {
        this.manejador = new ManejadorJson(Path.of(RUTA_ARCHIVO_JSON_ALOJADOS), AlojadoDTO.class);
    }
    
    /**
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
    
    /**
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
        System.out.println(RUTA_ARCHIVO_JSON_ALOJADOS);
        eliminarAlojado(alojadoPrev);
        this.crearAlojado(alojadoNuevo);
    }
    
    @Override
    public void eliminarAlojado(AlojadoDTO alojado){
        List<AlojadoDTO> listaAlojados = this.listarAlojados();
        listaAlojados.remove(alojado);
        escribirListaEnArchivo(listaAlojados);
    }
    
    /**
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

    /**
    Busco todos los alojados con listarAlojados y los guardo en una lista de DTO
    Inicializo otra lista vacía de DTO llamada encontrados
    Por cada alojado, llamo a cumpleCriterio. Si es verdad, lo agrego a encontrados
    */

    @Override
    public List<AlojadoDTO> buscarHuespedDAO (CriteriosBusq criterios_busq){
        // Tengo la lista de todos los alojados del archivo Alojado.JSON
        List<AlojadoDTO> lista_alojados = listarAlojados();

        return lista_alojados.stream()
                .filter(un_alojado -> cumpleCriterio(un_alojado, criterios_busq))
                .collect(Collectors.toList());
    }

    /**
    @param alojado_DTO: Instancia de AlojadoDTO
    @param criterio: Criterio de búsqueda donde todos son opcionales, y si no fueron definidos son null
    Se llama a normalizar para evitar problemas por tildes

    Si el criterio fue definido, pero no coinciden los atributos: Devuelve false
    Si el criterio fue definido y coincide: Devuelve true
    Si el criterio no fue definido (no_es_vacio==false), no se evalúa la segunda condición
    */

    private boolean cumpleCriterio (AlojadoDTO alojado_DTO, CriteriosBusq criterio) {
        // atributo_b: Atributos obtenidos del criterio de búsqueda
        // atributo_h: Atributos del huésped propiamente dicho

        String apellido_crudo = criterio.getApellido();
        String nombre_crudo = criterio.getNombre();
        String apellido_b=null, nombre_b=null;


        if(no_es_vacio(apellido_crudo)) apellido_b = normalizar(apellido_crudo);
        if(no_es_vacio(nombre_crudo)) nombre_b = normalizar(nombre_crudo);
        TipoDoc tipoDoc_b = criterio.getTipoDoc();
        String nroDoc_b = criterio.getNroDoc();

        // para mi (mateo) seria robusto validar que todas las strings no sean vacias
        String apellido = alojado_DTO.getApellido();
        String nombre = alojado_DTO.getNombre();

        String apellido_h = normalizar(apellido);
        String nombre_h = normalizar(nombre);
        TipoDoc tipoDoc_h = alojado_DTO.getTipoDoc();
        String nroDoc_h = alojado_DTO.getNroDoc();

        if (no_es_vacio(apellido_b) && !apellido_h.equalsIgnoreCase(apellido_b)) {
            return false;
        }
        if (no_es_vacio(nombre_b) && !nombre_h.equalsIgnoreCase(nombre_b)) {
            System.out.println(nombre_b + nombre_h);
            return false;
        }
        if (tipoDoc_b != null && !tipoDoc_h.equals(tipoDoc_b)) {
            return false;
        }
        return !(no_es_vacio(nroDoc_b) && !nroDoc_h.equals(nroDoc_b));
    }

    /**
    @param cadena: String que necesito normalizar
    @return una cadena normalizada sin tildes

    FUENTE: https://stackoverflow.com/questions/4122170/java-change-%C3%A1%C3%A9%C5%91%C5%B1%C3%BA-to-aeouu
    */

    private String normalizar (String cadena){
        return Normalizer.normalize(cadena, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    private boolean no_es_vacio (String contenido){
        boolean flag = (contenido==null || contenido.isEmpty());
        return !flag;
    }

    /**
     * Busca y retorna un alojado que coincida con el número y tipo de documento especificados
     *
     * @param documento El **número de documento** del alojado a buscar (ej: "40123456").
     * @param tipo El **tipo de documento** del alojado a buscar (ej: {@code TipoDoc.DNI}).
     * @return Un objeto {@link AlojadoDTO} si se encuentra la coincidencia, o {@code null} en caso contrario.
     */
    @Override
    public AlojadoDTO buscarPorDNI(String documento, TipoDoc tipo){
        List<AlojadoDTO> listaAlojados = listarAlojados();
        return listaAlojados.stream()
                .filter(a -> a.getNroDoc().equals(documento) && a.getTipoDoc().equals(tipo))
                .findFirst() // Devuelve un Optional<AlojadoDTO>
                .orElse(null); // Si no se encuentra devuelve null
    }
}
