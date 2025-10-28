/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.DTO.DatosCheckInDTO;

import java.util.List;

/**
 *
 * @author mat
 */
public interface DatosCheckInDAO {
    void crearDatosCheckIn(DatosCheckInDTO datosCheckIn);
    void actualizarDatosCheckIn(DatosCheckInDTO datosCheckInPre, DatosCheckInDTO datosCheckIn);
    void eliminarDatosCheckIn(DatosCheckInDTO datosCheckIn);
    List<DatosCheckInDTO> listarDatosCheckIn();
}
