/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.contabilidad.Pago;
import ddb.deso.contabilidad.ResponsablePago;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author mat
 */
public interface PagoDAO {
    void crearPago(Pago pago);
    void actualizarPago(Pago pago);
    void eliminarPago(Pago pago);
    ArrayList<Pago> listar();
    ArrayList<Pago> listarPorFecha(LocalDate fecha);
    ArrayList<Pago> listarPorResponsable(ResponsablePago responsablePago);
    ArrayList<Pago> listarPorFechayResponsbale(LocalDate fecha, ResponsablePago responsablePago);
}
