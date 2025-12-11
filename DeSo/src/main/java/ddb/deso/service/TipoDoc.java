/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package ddb.deso.service;
/**
 * Enumeración que define los **tipos de documento de identificación**
 * aceptados por el sistema para la identificación de los alojados.
 *
 * @author mat
 */
public enum TipoDoc {
    /** Documento Nacional de Identidad (DNI). */
    DNI,
    /** Libreta de Enrolamiento (LE). */
    LE,
    /** Libreta Cívica (LC). */
    LC,
    /** Pasaporte. */
    PASAPORTE,
    /** Otros tipos de documentos no especificados. */
    OTRO
}