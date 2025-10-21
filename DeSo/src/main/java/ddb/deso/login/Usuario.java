package ddb.deso.login;

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

    // Comportamiento intrínseco: verificar password
   public boolean coincidePasswordCon(String textoPlano) {
    return this.contrasenia.equals(textoPlano);
    }
    
}
