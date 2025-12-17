/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.negocio.TipoDoc;
import ddb.deso.negocio.alojamiento.DatosContacto;

/**
 *
 * @author mat
 */
public interface DatosContactoDAO {
    void crear(DatosContacto datosContacto);
    void actualizar(DatosContacto datosContacto);
    void eliminar(DatosContacto datosContacto);
    DatosContacto buscarDatosContacto(String documento, TipoDoc tipo);
}
