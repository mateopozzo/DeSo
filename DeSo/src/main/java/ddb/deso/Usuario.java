/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso;

/**
 *
 * @author mat
 */
public class Usuario {
    private String nombre;
    private String contrasena;
    private int permisos;

    public Usuario(String nombre, String contrasena, int permisos) {
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.permisos = permisos;
    }

    public Usuario() {
    }

    public String getNombre() {
        return nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public int getPermisos() {
        return permisos;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setPermisos(int permisos) {
        this.permisos = permisos;
    }
     
}
