/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.service.alojamiento.DatosCheckIn;

import java.util.List;

/**
 *
 * @author mat
 */
public interface DatosCheckInDAO {
    void crearDatosCheckIn(DatosCheckIn datosCheckIn);
    void actualizarDatosCheckIn(DatosCheckIn datosCheckInPre, DatosCheckIn datosCheckIn);
    void eliminarDatosCheckIn(DatosCheckIn datosCheckIn);
    List<DatosCheckIn> listarDatosCheckIn();
}
