/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.almacenamiento.JSON;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mat
 */
public class ManejadorJson<T> {
    private Gson gson = new Gson();
    private Path path;
    private Class<T> clase;
    
    public ManejadorJson(Path path, Class<T> clase) {
        this.path = path;
        this.clase = clase;
    }
    
    public ManejadorJson(Gson gson, Path path, Class<T> clase) {
        this.gson = gson;
        this.path = path;
        this.clase = clase;
    }
    
    public List<T> listar() throws IOException {
        File archivo = path.toFile();
        if(!archivo.exists()){
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
    
    public void escribir(List<T> items) throws IOException {
        System.out.println("Escribiendo JSON en: " + path.toAbsolutePath());
        System.out.println("Directorio padre: " + path.getParent());
        try (FileWriter escritor = new FileWriter(path.toFile())) {
            gson.toJson(items, escritor);
            escritor.flush();
            escritor.close();
        }
    }
    
}
