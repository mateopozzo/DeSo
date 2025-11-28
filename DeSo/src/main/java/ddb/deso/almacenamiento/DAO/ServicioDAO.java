/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.TipoServicio;
import ddb.deso.habitaciones.Servicio;
import java.util.List;

/**
 *
 * @author mat
 */
public interface ServicioDAO {
    void crear(Servicio servicio);
    void crear(TipoServicio tipoServicio);
    void actualizar(Servicio servicio);
    void eliminar(Servicio servicio);
    List<Servicio> listar();
}
