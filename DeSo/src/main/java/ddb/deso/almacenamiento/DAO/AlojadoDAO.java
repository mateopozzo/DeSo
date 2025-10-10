/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;
import ddb.deso.alojamiento.Alojado;
import ddb.deso.TipoDoc;
import java.util.List;

/**
 *
 * @author mat
 */
public interface AlojadoDAO {
    void crearAlojado(Alojado alojado);
    void actualizarAlojado(Alojado alojado);
    void eliminarAlojado(Alojado alojado);
    List<Alojado> listarAlojados();
    Alojado buscarPorDNI(String documento, TipoDoc tipo);
}
