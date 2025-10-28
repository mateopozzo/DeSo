package ddb.deso.login;

/*
 Representa a un usuario del sistema, con nombre, contraseña y nivel de permisos.
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

    /*
     Verifica si la contraseña ingresada coincide con la registrada.

     @param textoPlano Contraseña ingresada en texto plano.
     @return {@code true} si coincide, {@code false} en caso contrario.
     */
   public boolean coincidePasswordCon(String textoPlano) {
    return this.contrasenia.equals(textoPlano);
    }
    
}
