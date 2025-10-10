/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.TipoDoc;
import ddb.deso.alojamiento.DatosAlojado;

/**
 *
 * @author mat
 */
public interface DatosAlojadoDAO {
    void crearDatosAlojado(DatosAlojado datosAlojado);
    void actualizarDatosAlojado(DatosAlojado datosAlojado);
    void eliminarDatosAlojado(DatosAlojado datosAlojado);
    DatosAlojado buscarDatosAlojado(String documento, TipoDoc tipo);
}
