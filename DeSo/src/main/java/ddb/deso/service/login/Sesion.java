package ddb.deso.service.login;

/**
 * Clase que implementa el patrón **Singleton** para gestionar el estado de la sesión
 * del usuario actualmente logueado.
 * <p>
 * Asegura que solo pueda haber una única instancia de la sesión
 * (controlada por {@code SINGLETON_INSTANCE}) y un solo usuario
 * activo (gestionado por {@code usuarioLogueado}) a la vez en la aplicación.
 * </p>
 *
 * @see Usuario
 */

public class Sesion {
    // Almacena la instancia del usuario que ha iniciado sesión.

    private static Usuario usuarioLogueado = null;

    private static Sesion SINGLETON_INSTANCE;
    private Sesion() {
    }

    /**
     * Intenta iniciar una sesión con el usuario proporcionado.
     * <p>Si el usuario es válido (no {@code null}), establece el usuario logueado
     * y, si aún no existe, crea la única instancia de {@code Sesion}.</p>
     *
     * @param u La instancia de {@link Usuario} con la que se desea iniciar sesión.
     */
    public static void iniciarSesion(Usuario u) {
        usuarioLogueado = u;

        if (usuarioLogueado != null & SINGLETON_INSTANCE==null){
            SINGLETON_INSTANCE = new Sesion();
        }
    }

    /**
     Finaliza la sesión actual.
     Establece el usuario logueado a 'null', liberando la sesión.
     */
    public static void finalizarSesion() {
        usuarioLogueado = null;
        SINGLETON_INSTANCE = null;
    }

    /**
     Obtiene el usuario que actualmente tiene la sesión iniciada.
     @return La instancia de {@link Usuario} logueado, o 'null' si no hay una sesión activa.
     */
    public static Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }
}
