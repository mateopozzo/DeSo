/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

/**
 *
 * @author mat
 */
public class DAOGENERICO {
    void crear(C c);
    void actualizar(C c);
    void eliminar(C c);
    ArrayList<C> listar();
    C buscarPorX();
}
