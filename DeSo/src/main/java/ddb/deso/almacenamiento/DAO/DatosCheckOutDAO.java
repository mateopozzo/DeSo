/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.TipoDoc;
import ddb.deso.alojamiento.DatosCheckOut;
import java.util.List;

/**
 *
 * @author mat
 */
public interface DatosCheckOutDAO {
    void crearDatosCheckOut(DatosCheckOut datosCheckOut);
    void actualizarDatosCheckOut(DatosCheckOut datosCheckOut);
    void eliminarDatosCheckOut(DatosCheckOut datosCheckOut);
    List<DatosCheckOut> listarDatosCheckOut(String documento, TipoDoc tipo);
}
