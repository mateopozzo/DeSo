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
import ddb.deso.service.login.Usuario;

/**
 * Implementación de {@link UsuarioDAO} que utiliza un archivo JSON
 * como fuente de datos para la autenticación de usuarios.
 * <p>
 * La información de los usuarios se encuentra en el archivo
 * {@code data/usuarios.json} y se mapea mediante un DTO interno.
 * </p>
 * <p>
 * Utiliza la librería {@link Gson} para serializar y deserializar los objetos.
 * </p>
 */
public class UsuarioJsonDAO implements UsuarioDAO {

    private static final Path RUTA = Path.of("")
        .toAbsolutePath().resolve("DeSo").resolve("data").resolve("usuarios.json");
    private final Gson gson = new Gson();

    /**
     * Busca un usuario en el archivo JSON por nombre, sin distinguir mayúsculas ni minúsculas.
     *
     * @param nombre nombre del usuario a buscar.
     * @return un {@link Optional} con el {@link Usuario} encontrado o vacío si no existe.
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

    /**
     * Lee y deserializa la lista de usuarios desde el archivo JSON.
     *
     * @return una lista de {@link UsuarioDTO}, o una lista vacía si no se puede leer el archivo.
     */
   private List<UsuarioDTO> leerDtos() {
        try (BufferedReader reader = Files.newBufferedReader(RUTA, StandardCharsets.UTF_8)) {
            Type listType = new TypeToken<List<UsuarioDTO>>() {}.getType();
            List<UsuarioDTO> lista = gson.fromJson(reader, listType);
            return (lista == null) ? Collections.emptyList() : lista;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
}


    /**
     * DTO interno utilizado únicamente para mapear la estructura del archivo JSON.
     */
    private static class UsuarioDTO {
        String nombre;
        String contrasenia;
        int permisos;
    }
}