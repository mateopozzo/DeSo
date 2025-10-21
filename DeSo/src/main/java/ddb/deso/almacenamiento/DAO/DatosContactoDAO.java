/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.TipoDoc;
import ddb.deso.alojamiento.DatosContacto;

/**
 *
 * @author mat
 */
public interface DatosContactoDAO {
    void crearDatosContacto(DatosContacto datosContacto);
    void actualizarDatosContacto(DatosContacto datosContacto);
    void eliminarDatosContacto(DatosContacto datosContacto);
    DatosContacto buscarDatosContacto(String documento, TipoDoc tipo);
}
