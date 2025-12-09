/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.service.TipoDoc;
import ddb.deso.service.alojamiento.DatosResidencia;

/**
 *
 * @author mat
 */
public interface DatosResidenciaDAO {
    void crear(DatosResidencia datosResidencia);
    void actualizar(DatosResidencia datosResidencia);
    void eliminar(DatosResidencia datosResidencia);
    DatosResidencia buscar(String documento, TipoDoc tipo);
}
