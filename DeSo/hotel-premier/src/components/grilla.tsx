"use client";
import React, { useState } from "react";
import {
  Habitacion,
  pedirHabs,
  buscarEstadoHabitaciones,
  DisponibilidadDTO,
} from "../services/habitaciones.service";

interface GrillaProps {
  fecha_inicio: string;
  fecha_fin: string;
  habitaciones: Habitacion[];
  reservas: DisponibilidadDTO[];
}

export default function Grilla({
  fecha_inicio,
  fecha_fin,
  habitaciones,
  reservas,
}: GrillaProps) {
  const obtenerEstado = (idHab: number, fecha: string) => {
    const ocupada = reservas.find(
      (res) =>
        res.idHabitacion === idHab &&
        fecha >= res.fecha_inicio &&
        fecha <= res.fecha_fin
    );

    if (ocupada) return ocupada.estado;
    // si no está en la lista de reservas, está libre
    return "DISPONIBLE";
  };

  // paso cada estado a un color para poder pintar la grilla
  // como solo devuelve ocupadas o reservadas, todas por defecto están disponibles
  const pintarEstado = (estado: string) => {
    if (estado === "OCUPADA") {
      return "dark:text-gray-950 dark:bg-white bg-black text-white hover:bg-gray-400";
    } else if (estado === "RESERVADA") {
      return "bg-yellow-500 text-white hover:bg-green-400";
    } else if (estado === "EN MANTENIMIENTO") {
      return "bg-red-400";
    } else {
      return "dark:bg-gray-950 dark:text-white bg-white text-black hover:bg-green-400";
    }
  };

  // como solo tengo inicio y fin, tengo que generar un array con el intermedio
  const generarFechas = (fecha_inicio: string, fecha_fin: string) => {
    console.log("Generando fechas");
    const fechas: string[] = [];

    let actual = new Date(fecha_inicio + "T00:00:00");
    const final = new Date(fecha_fin + "T00:00:00");

    while (actual <= final) {
      fechas.push(actual.toISOString().split("T")[0]);
      actual.setDate(actual.getDate() + 1);
    }

    return fechas;
  };

  const fechas = generarFechas(fecha_inicio, fecha_fin);
  console.log(`Fechas generadas: ${fechas}`);

  return (
    <div className="flex flex-col justify-between items-center mb-2 bg-[#f5f7fa] dark:bg-gray-950">
      <div className="flex flex-col items-center justify-center md:flex-row mb-8">
        <p className="md:mr-12 text-md font-semibold ">
          Habitaciones desde {fecha_inicio} hasta {fecha_fin}
        </p>
        <button className="cursor-pointer px-8 py-2 rounded-xl font-bold transition duration-300 bg-[#52a173] text-white hover:bg-[#10b655]">
          Confirmar
        </button>
      </div>

      <div className="bg-[#f5f7fa] dark:bg-gray-950 overflow-auto">
        <table className="w-full border-collapse bg-[#f5f7fa] dark:bg-gray-950">
          <thead>
            <tr>
              <th className="p-2 border dark:text-white">Fecha</th>
              {habitaciones.map((h) => (
                <th key={h.nroHab} className="p-2 border">
                  Hab {h.nroHab}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {fechas.map((fecha) => (
              <tr key={fecha}>
                <td className="p-2 border font-bold dark:text-white">
                  {fecha}
                </td>
                {habitaciones.map((hab) => {
                  const estado = obtenerEstado(hab.nroHab, fecha);

                  return (
                    <td
                      key={`${hab.nroHab}-${fecha}`}
                      className={`p-2 border text-center ${pintarEstado(
                        estado
                      )}`}
                      onClick={() =>
                        console.log(`Click en ${hab.nroHab} el ${fecha}`)
                      }
                    >
                      {estado === "DISPONIBLE" ? "" : estado.charAt(0)}
                    </td>
                  );
                })}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
