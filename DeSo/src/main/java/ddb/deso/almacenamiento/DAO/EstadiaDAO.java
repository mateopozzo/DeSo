/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.negocio.alojamiento.Huesped;
import ddb.deso.negocio.habitaciones.Estadia;
import java.time.LocalDate;
import java.util.List;

/**
 * Contrato de persistencia para la entidad {@link Estadia}.
 * Provee operaciones CRUD y consultas especializadas para filtrar ocupaciones
 * por rangos temporales y asociación con huéspedes.
 *
 * @author mat
 */
public interface EstadiaDAO {
    /**
     * Persiste una nueva instancia de Estadia en el repositorio.
     *
     * @param estadia Entidad con los datos de la estadía a registrar.
     */
    void crear(Estadia estadia);

    /**
     * Actualiza la información de una estadía existente.
     *
     * @param estadia Entidad con los datos modificados a persistir.
     */
    void actualizar(Estadia estadia);

    /**
     * Elimina el registro físico o lógico de la estadía.
     *
     * @param estadia Entidad a eliminar del repositorio.
     */
    void eliminar(Estadia estadia);

    /**
     * Recupera la totalidad de las estadías registradas en el sistema.
     *
     * @return Lista completa de entidades {@link Estadia}.
     */
    List<Estadia> listar();

    /**
     * Recupera las estadías que se encuentren activas o vigentes dentro del rango de fechas proporcionado.
     * Se considera coincidencia si hay solapamiento temporal.
     *
     * @param fechaInicio Fecha de inicio del intervalo de búsqueda.
     * @param fechaFin Fecha de fin del intervalo de búsqueda.
     * @return Lista de estadías activas durante el periodo especificado.
     */
    List<Estadia> listarPorFecha(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Obtiene el historial completo de estadías asociadas a un huésped específico.
     *
     * @param huesped Entidad {@link Huesped} sobre la cual filtrar.
     * @return Lista de estadías vinculadas al huésped.
     */
    List<Estadia> listarPorHuesped(Huesped huesped);

    /**
     * Consulta las estadías de un huésped específico que estuvieron activas en una fecha puntual.
     * Útil para validar ubicación del huésped en un momento dado.
     *
     * @param fecha Fecha específica a consultar.
     * @param huesped Entidad {@link Huesped} a verificar.
     * @return Lista de estadías del huésped vigentes en la fecha indicada.
     */
    List<Estadia> listarPorFechayHuesped(LocalDate fecha, Huesped huesped);

    Estadia read(long id);
}
