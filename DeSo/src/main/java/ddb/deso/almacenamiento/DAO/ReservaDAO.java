/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.alojamiento.Huesped;
import ddb.deso.habitaciones.Estadia;
import ddb.deso.habitaciones.Reserva;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author mat
 */
public interface ReservaDAO {
    void crearReserva(Reserva reserva);
    void actualizar(Reserva reserva);
    void eliminar(Reserva reserva);
    ArrayList<Reserva> listar();
    
    //  Devuelve las instancias de reserva que ocurren durante la fecha
    Reserva listarPorFecha(LocalDate fecha);
    
    ArrayList<Estadia> listarPorHuesped(Huesped huesped);
    
    //  devuelve todas las estadias del huesped que ocupo durante la fecha
    ArrayList<Estadia> listarPorFechayHuesped(LocalDate fecha, Huesped huesped);
    
    
}
