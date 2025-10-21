/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.alojamiento.Huesped;
import ddb.deso.habitaciones.Estadia;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author mat
 */
public interface EstadiaDAO {
    void crear(Estadia estadia);
    void actualizar(Estadia estadia);
    void eliminar(Estadia estadia);
    List<Estadia> listar();
    
    //  devuelve todas las estadias activas durante la fecha
    List<Estadia> listarPorFecha(LocalDate fecha);   
    
    List<Estadia> listarPorHuesped(Huesped huesped);
    
    //  devuelve todas las estadias del huesped que ocupo durante la fecha
    List<Estadia> listarPorFechayHuesped(LocalDate fecha, Huesped huesped);
}
