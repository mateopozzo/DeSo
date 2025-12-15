package ddb.deso.negocio.login;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa un usuario del sistema.
 *
 * <p>Entidad persistente (JPA) utilizada para el CU01 (Autenticar Usuario).
 * Contiene nombre, contraseña (según implementación actual) y nivel de permisos.</p>
 *
 * <p><b>Nota:</b> En esta entrega se asume que los usuarios ya están cargados en la base de datos.
 * No se implementa alta/modificación de usuarios.</p>
 */
@Entity
@Table(name = "usuarios")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Usuario {

    /**
     * Identificador interno del usuario (PK).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario (único).
     */
    @Column(nullable = false, unique = true, length = 60)
    private String nombre;

    /**
     * Contraseña del usuario.
     *
     * <p>En la implementación actual se mantiene como texto plano para ser consistente con la entrega anterior.
     * Si se decide hashear contraseñas (recomendado), este atributo debería almacenar el hash.</p>
     */
    @Column(nullable = false, length = 100)
    private String contrasenia;

    /**
     * Nivel de permisos/rol del usuario dentro del sistema.
     */
    @Column(nullable = false)
    private int permisos;

    /**
     * Verifica si la contraseña ingresada coincide con la registrada.
     *
     * @param textoPlano contraseña ingresada por el usuario.
     * @return {@code true} si coincide, {@code false} caso contrario.
     */
    public boolean coincidePasswordCon(String textoPlano) {
        return this.contrasenia != null && this.contrasenia.equals(textoPlano);
    }
}
