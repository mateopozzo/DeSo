/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;
import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import java.util.List;

/**
 *
 * @author mat
 */
public interface AlojadoDAO {
    public void crearAlojado(AlojadoDTO alojado);
    public void actualizarAlojado(AlojadoDTO alojado);
    public void eliminarAlojado(AlojadoDTO alojado);
    public List<AlojadoDTO> listarAlojados();
    public AlojadoDTO buscarPorDNI(String documento, TipoDoc tipo);
}
