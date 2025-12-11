/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.service.TipoHab;
import ddb.deso.service.habitaciones.Habitacion;
import java.util.List;

/**
 * Contrato de acceso a datos para la entidad {@link Habitacion}.
 *
 * @author mat
 */
public interface HabitacionDAO {

    /**
     * Persiste una nueva habitación.
     * @param habitacion Entidad a crear.
     */
    void crearHabitacion(Habitacion habitacion);

    /**
     * Actualiza los datos de una habitación existente.
     * @param habitacion Entidad con datos actualizados.
     */
    void actualizarHabitacion(Habitacion habitacion);

    /**
     * Da de baja una habitación del inventario.
     * @param habitacion Entidad a eliminar.
     */
    void eliminarHabitacion(Habitacion habitacion);

    /**
     * Recupera el inventario completo de habitaciones.
     * @return Lista de habitaciones.
     */
    List<Habitacion> listar();

    /**
     * Filtra habitaciones por su categoría o configuración.
     * @param tipoHabitacion Enum {@link TipoHab} con la categoría buscada.
     * @return Lista de habitaciones coincidentes.
     */
    List<Habitacion> listarPorTipo(TipoHab tipoHabitacion);

    /**
     * Busca una habitación por su número identificador.
     * @param numero Identificador numérico de la habitación.
     * @return Entidad encontrada o {@code null}.
     */
    Habitacion buscarPorNumero(Long numero);
}
