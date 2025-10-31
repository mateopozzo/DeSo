package ddb.deso.almacenamiento.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ddb.deso.almacenamiento.DAO.UsuarioDAO;
import ddb.deso.login.Usuario;

public class UsuarioJsonDAO implements UsuarioDAO {
    private static final Path RUTA = Path.of("")
        .toAbsolutePath().resolve("DeSo").resolve("data").resolve("usuarios.json");
    private final Gson gson = new Gson();

    /*
    Metodo temporal para depurar la ruta real
    private void debugRuta() {
        System.out.println("[DEBUG] user.dir = " + System.getProperty("user.dir"));
        System.out.println("[DEBUG] JSON esperado en: " + RUTA.toString());
        System.out.println("[DEBUG] exists=" + Files.exists(RUTA));
    }
    */

    @Override
    public Optional<Usuario> buscarPorNombre(String nombre) {
        String buscado = (nombre == null) ? "" : nombre.trim();

        return leerDtos().stream()
                .filter(Objects::nonNull)
                .filter(dto -> dto.nombre != null)
                .filter(dto -> dto.nombre.trim().equalsIgnoreCase(buscado))
                .findFirst()
                .map(dto -> new Usuario(dto.nombre, dto.contrasenia, dto.permisos));
    }

   private List<UsuarioDTO> leerDtos() {
        //debugRuta(); Metodo temporal para depurar la ruta real

        try (BufferedReader reader = Files.newBufferedReader(RUTA, StandardCharsets.UTF_8)) {
            Type listType = new TypeToken<List<UsuarioDTO>>() {}.getType();
            List<UsuarioDTO> lista = gson.fromJson(reader, listType);
            return (lista == null) ? Collections.emptyList() : lista;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
}


    // DTO interno mapeado al JSON
    private static class UsuarioDTO {
        String nombre;
        String contrasenia;
        int permisos;
    }
}