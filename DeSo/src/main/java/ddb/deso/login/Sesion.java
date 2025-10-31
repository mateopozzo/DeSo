package ddb.deso.login;

/*
 Clase Sesion

 Implementa el patrón Singleton asegura que solo pueda haber
 una **sesión abierta** de un usuario a la vez en la aplicación, controlada mediante
 variables y métodos estáticos.

 Esta clase gestiona el estado del usuario actualmente logueado.
 */

public class Sesion {
    // Almacena la instancia del usuario que ha iniciado sesión.

    private static Usuario usuarioLogueado = null;

    private static Sesion SINGLETON_INSTANCE;
    private Sesion() {
    }

    /*
     Intenta iniciar una sesión con el usuario proporcionado.
     @param u La instancia de {@link Usuario} con la que se desea iniciar sesión.
     */

    public static void iniciarSesion(Usuario u) {
        usuarioLogueado = u;

        if (usuarioLogueado != null & SINGLETON_INSTANCE==null){
            SINGLETON_INSTANCE = new Sesion();
        }
    }

    /*
     Finaliza la sesión actual.
     Establece el usuario logueado a 'null', liberando la sesión.
     */

    public static void finalizarSesion() {
        usuarioLogueado = null;
        SINGLETON_INSTANCE = null;
    }

    /*
     Obtiene el usuario que actualmente tiene la sesión iniciada.
     @return La instancia de {@link Usuario} logueado, o 'null' si no hay una sesión activa.
     */
    
    public static Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }
}
