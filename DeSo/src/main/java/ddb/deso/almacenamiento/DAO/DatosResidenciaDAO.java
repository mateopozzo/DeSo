/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.TipoDoc;
import ddb.deso.alojamiento.DatosResidencia;

/**
 *
 * @author mat
 */
public interface DatosResidenciaDAO {
    void crearDatosResidencia(DatosResidencia datosResidencia);
    void actualizarDatosResidenciado(DatosResidencia datosResidencia);
    void eliminarDatosResidencia(DatosResidencia datosResidencia);
    DatosResidencia buscarDatosResidencia(String documento, TipoDoc tipo);
}
