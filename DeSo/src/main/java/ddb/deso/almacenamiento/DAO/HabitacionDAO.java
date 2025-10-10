/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.TipoHab;
import ddb.deso.habitaciones.Habitacion;
import java.util.ArrayList;

/**
 *
 * @author mat
 */
public interface HabitacionDAO {
    void crearHabitacion(Habitacion habitacion);
    void actualizarHabitacion(Habitacion habitacion);
    void eliminarHabitacion(Habitacion habitacion);
    ArrayList<Habitacion> listar();
    ArrayList<Habitacion> listarPorTipo(TipoHab tipoHabitacion);
    Habitacion buscarPorNumero(int numero);
}
