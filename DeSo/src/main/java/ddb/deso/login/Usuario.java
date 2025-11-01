package ddb.deso.login;

/**
 * Representa a un **usuario** del sistema.
 * <p>
 * Contiene las credenciales básicas (nombre y contraseña) y el nivel de acceso
 * (permisos) necesario para operar dentro de la aplicación.
 * </p>
 */
public class Usuario {
    private String nombre;
    private String contrasenia; // texto plano
    private int permisos;

    public Usuario(String nombre, String contrasenia, int permisos) {
        this.nombre = nombre;
        this.contrasenia = contrasenia;
        this.permisos = permisos;
    }

    public String getNombre() { return nombre; }
    public int getPermisos() { return permisos; }

    /**
     * Verifica si la contraseña ingresada coincide con la contraseña registrada en este objeto.
     *
     * @param textoPlano Contraseña ingresada por el usuario en texto plano.
     * @return {@code true} si la contraseña coincide exactamente, {@code false} en caso contrario.
     */
   public boolean coincidePasswordCon(String textoPlano) {
    return this.contrasenia.equals(textoPlano);
    }
    
}
