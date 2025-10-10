/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.TipoDoc;

/**
 *
 * @author mat
 */
public interface DatosCheckIn {
    void crearDatosResidencia(CheckIn datosCheckIn);
    void actualizarDatosResidenciado(CheckIn datosCheckIn);
    void eliminarDatosResidencia(CheckIn datosCheckIn);
    DatosCheckIn buscarDatosResidencia(String documento, TipoDoc tipo);
}
