/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.contabilidad.ResponsablePago;
import java.util.ArrayList;

/**
 *
 * @author mat
 */
public interface ResponsablePagoDAO {
    void crearResponsablePago(ResponsablePago responsablePago);
    void actualizarAlojado(ResponsablePago responsablePago);
    void eliminarAlojado(ResponsablePago responsablePago);
    ArrayList<ResponsablePago> listarResponsablePago();
    ResponsablePago buscarPorCUIT(String CUIT);
}
