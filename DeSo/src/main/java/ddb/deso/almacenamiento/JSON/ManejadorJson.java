/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.almacenamiento.JSON;

import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase genérica para la **persistencia de colecciones de objetos** ({@code List<T>})
 * utilizando el formato JSON.
 * <p>
 * Actúa como una capa de abstracción para la lectura y escritura de un array JSON
 * completo desde y hacia un archivo, utilizando la librería **Gson**.
 * </p>
 *
 * @param <T> El tipo de objeto (DTO o Entidad) que se serializará/deserializará (uso de Generics).
 * @author mat
 */
public class ManejadorJson<T> {
    private Gson gson = new Gson();

    private Path path;
    private Class<T> clase;
    /**
     * Constructor que inicializa el manejador con la ruta del archivo y la clase de los ítems.
     * Utiliza una instancia de {@code Gson} por defecto.
     *
     * @param path La {@link Path} al archivo JSON donde se persistirán los datos.
     * @param clase El objeto {@link Class} que representa el tipo {@code T} a manipular (necesario para la deserialización genérica).
     */
    public ManejadorJson(Path path, Class<T> clase) {
        this.path = path;
        this.clase = clase;
    }
    /**
     * Constructor que permite inyectar una instancia de {@code Gson} personalizada.
     *
     * @param gson Instancia personalizada de {@link Gson} para manejar serialización/deserialización.
     * @param path La {@link Path} al archivo JSON.
     * @param clase El objeto {@link Class} que representa el tipo {@code T}.
     */
    public ManejadorJson(Gson gson, Path path, Class<T> clase) {
        this.gson = gson;
        this.path = path;
        this.clase = clase;
    }

    /**
     * Lee y deserializa el contenido completo del archivo JSON a una lista de objetos de tipo {@code T}.
     *
     * @return Una {@code List<T>} con los ítems leídos. Retorna una lista vacía si el archivo no existe o está vacío.
     * @throws IOException Si ocurre un error de lectura o al acceder al archivo.
     */
    public List<T> listar() throws IOException {
        File archivo = path.toFile();
        if(!archivo.exists()){
            return new ArrayList<T>();
        }
        if(archivo.length() == 0){
            return new ArrayList<T>();
        }
        try (FileReader lector = new FileReader(path.toFile())) {
            JsonArray jsonArray = JsonParser.parseReader(lector).getAsJsonArray();
            List<T> listaDto = new ArrayList<>();
            for (JsonElement e : jsonArray)
                listaDto.add(gson.fromJson(e, clase));
            return listaDto;
        }
    }

    /**
     * Serializa la lista de ítems a formato JSON y los escribe en el archivo configurado,
     * sobrescribiendo el contenido existente.
     *
     * @param items La {@code List<T>} de objetos a persistir.
     * @throws IOException Si ocurre un error de escritura o al acceder al archivo.
     */
    public void escribir(List<T> items) throws IOException {
        try (FileWriter escritor = new FileWriter(path.toFile())) {
            gson.toJson(items, escritor);
            escritor.flush();
        }
    }
    
}
