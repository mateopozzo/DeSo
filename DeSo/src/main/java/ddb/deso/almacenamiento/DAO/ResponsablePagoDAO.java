/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.negocio.contabilidad.ResponsablePago;
import java.util.List;

/**
 * Interfaz de Acceso a Datos (DAO) para la entidad ResponsablePago.
 * Proporciona los métodos necesarios para la gestión de los responsables
 * de pago en el sistema.
 * * @author mat
 */
public interface ResponsablePagoDAO {

    /**
     * Registra un nuevo responsable de pago en la base de datos.
     * @param responsablePago El objeto con la información a persistir.
     */
    void crear(ResponsablePago responsablePago);

    /**
     * Actualiza la información de un responsable de pago existente.
     * @param responsablePago El objeto con los datos actualizados.
     */
    void actualizar(ResponsablePago responsablePago);

    /**
     * Elimina un responsable de pago del sistema.
     * @param responsablePago El objeto que se desea dar de baja.
     */
    void eliminar(ResponsablePago responsablePago);

    /**
     * Obtiene una lista completa de todos los responsables de pago registrados.
     * @return Una {@link List} de {@link ResponsablePago}.
     */
    List<ResponsablePago> listar();

    /**
     * Busca un responsable de pago utilizando su CUIT.
     * @param CUIT Identificador tributario (Código Único de Identificación Tributaria).
     * @return El {@link ResponsablePago} asociado, o {@code null} si no se encuentra.
     */
    ResponsablePago buscarPorCUIT(String CUIT);

    /**
     * Busca un responsable de pago por su ID (CUIT).
     * @param id El CUIT del responsable (clave primaria).
     * @return La entidad encontrada o null.
     */
    ResponsablePago read(Long id);
}
