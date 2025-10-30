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
 *
 * @author mat
 */
public class ManejadorJson<T> {
    // quiero probar algo
    private Gson gson = new Gson();
    // private Gson gson = new GsonBuilder().setPrettyPrinting().create();

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
    
    public void escribir(List<T> items) throws IOException {
        try (FileWriter escritor = new FileWriter(path.toFile())) {
            gson.toJson(items, escritor);
            escritor.flush();
            escritor.close();
        }
    }
    
}
