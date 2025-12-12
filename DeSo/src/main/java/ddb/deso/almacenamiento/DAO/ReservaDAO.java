/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.negocio.habitaciones.Reserva;
import java.time.LocalDate;
import java.util.List;

import ddb.deso.service.habitaciones.Reserva;

/**
 * Contrato de acceso a datos para la entidad {@link Reserva}.
 *
 * @author mat
 */
public interface ReservaDAO {
    /**
     * Persiste una nueva reserva en el repositorio.
     * @param reserva Entidad a persistir.
     */
    void crearReserva(Reserva reserva);

    /**
     * Sincroniza los cambios de una reserva existente.
     * @param reserva Entidad con estado modificado.
     */
    void actualizar(Reserva reserva);

    List<Reserva> buscarPorApellidoNombre(String apellido, String nombre);

    /**
     * Elimina el registro de la reserva.
     * @param reserva Entidad a eliminar.
     */
    void eliminar(Reserva reserva);

    /**
     * Recupera la totalidad de reservas registradas.
     * @return Lista completa de entidades.
     */
    List<Reserva> listar();

    /**
     * Busca una reserva por su identificador único.
     * @param ID Clave primaria.
     * @return La entidad encontrada o {@code null}.
     */
    Reserva buscarPorID(Long ID);

    /**
     * Recupera reservas cuyo periodo de vigencia se solapa con el rango proporcionado.
     *
     * @param fechaInicio Inicio del intervalo de búsqueda.
     * @param fechaFin Fin del intervalo de búsqueda.
     * @return Lista de reservas con intersección temporal.
     */
    List<Reserva> listarPorFecha(LocalDate fechaInicio, LocalDate fechaFin);

}
