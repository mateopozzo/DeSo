/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.service.habitaciones.Reserva;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author mat
 */
public interface ReservaDAO {
    void crearReserva(Reserva reserva);
    void actualizar(Reserva reserva);
    void eliminar(Reserva reserva);
    List<Reserva> listar();
    Reserva buscarPorID(Long ID);
    
    //  devuelve las instancias de reserva que ocurren durante la fecha
    List<Reserva> listarPorFecha(LocalDate fechaInicio, LocalDate fechaFin);

}
