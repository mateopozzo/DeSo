/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.service.contabilidad.Pago;
import ddb.deso.service.contabilidad.ResponsablePago;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author mat
 */
public interface PagoDAO {
    void crearPago(Pago pago);
    void actualizarPago(Pago pago);
    void eliminarPago(Pago pago);
    List<Pago> listar();
    List<Pago> listarPorFecha(LocalDate fecha);
    List<Pago> listarPorResponsable(ResponsablePago responsablePago);
    List<Pago> listarPorFechayResponsbale(LocalDate fecha, ResponsablePago responsablePago);
}
