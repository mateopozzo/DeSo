/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.TipoServicio;
import ddb.deso.habitaciones.Servicio;
import java.util.ArrayList;

/**
 *
 * @author mat
 */
public interface ServicioDAO {
    void crearServicio(Servicio servicio);
    void crearServicio(TipoServicio tipoServicio);
    void actualizarServicio(Servicio servicio);
    void eliminarServicio(Servicio servicio);
    ArrayList<Servicio> listar();
}
