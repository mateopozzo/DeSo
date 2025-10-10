/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.TipoDoc;
import java.util.List;

/**
 *
 * @author mat
 */
public interface DatosCheckIn {
    void crearDatosCheckIn(DatosCheckIn datosCheckIn);
    void actualizarDatosCheckIn(DatosCheckIn datosCheckIn);
    void eliminarDatosCheckIn(DatosCheckIn datosCheckIn);
    List<DatosCheckIn> listarDatosCheckIn(String documento, TipoDoc tipo);
}
