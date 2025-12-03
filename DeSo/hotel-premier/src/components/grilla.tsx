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

type Seleccion = {
  idHabitacion: number;
  fecha_inicio_sel: string;
  fecha_fin_sel: string | null;
};

export default function Grilla({
  fecha_inicio,
  fecha_fin,
  habitaciones,
  reservas,
}: GrillaProps) {
  const [seleccion, setSeleccion] = useState<Seleccion | null>(null);

  const obtenerEstado = (idHab: number, fecha: string) => {
    const ocupada = reservas.find(
      (res) =>
        res.idHabitacion === idHab &&
        fecha >= res.fecha_inicio &&
        fecha <= res.fecha_fin
    );

    if (ocupada) return ocupada.estado;

    if (seleccion && seleccion.idHabitacion === idHab) {
      // solo hay fecha de inicio
      if (fecha === seleccion.fecha_inicio_sel && !seleccion.fecha_fin_sel) {
        return "SELECCIONADA";
      }
      // hay rango completo
      if (
        seleccion.fecha_fin_sel &&
        fecha >= seleccion.fecha_inicio_sel &&
        fecha <= seleccion.fecha_fin_sel
      ) {
        return "SELECCIONADA";
      }
    }

    // si no está en la lista de reservas, está libre
    return "DISPONIBLE";
  };

  const manejarClick = (idHab: number, fecha: string, estadoActual: string) => {
    if (
      estadoActual === "OCUPADA" ||
      estadoActual === "RESERVADA" ||
      estadoActual === "EN MANTENIMIENTO" ||
      estadoActual === "FUERA_SERVICIO"
    ) {
      return;
    }

    setSeleccion((prev) => {
      if (
        !prev ||
        prev.idHabitacion !== idHab ||
        (prev.fecha_inicio_sel && prev.fecha_fin_sel)
      ) {
        const nuevaSeleccion = {
          idHabitacion: idHab,
          fecha_inicio_sel: fecha,
          fecha_fin_sel: null,
        };
        console.log("Nueva selección:", nuevaSeleccion);
        return nuevaSeleccion;
      }

      if (prev.idHabitacion === idHab && !prev.fecha_fin_sel) {
        let inicio = prev.fecha_inicio_sel;
        let fin = fecha;

        if (fin < inicio) {
          [inicio, fin] = [fin, inicio];
        }

        const seleccionCompleta = {
          idHabitacion: idHab,
          fecha_inicio_sel: inicio,
          fecha_fin_sel: fin,
        };
        console.log("Selección completada:", seleccionCompleta);
        return seleccionCompleta;
      }

      return prev;
    });
  };

  // paso cada estado a un color para poder pintar la grilla
  // como solo devuelve ocupadas o reservadas, todas por defecto están disponibles
  const pintarEstado = (estado: string) => {
    const estadoUpper = estado ? estado.toUpperCase() : "DISPONIBLE";

    switch (estadoUpper) {
      case "OCUPADA":
        return "dark:bg-white bg-black text-white cursor-not-allowed opacity-80";
      case "RESERVADA":
        return "bg-yellow-500 text-white cursor-pointer opacity-90";
      case "EN MANTENIMIENTO":
      case "FUERA_SERVICIO":
        return "bg-red-500 text-white cursor-not-allowed";
      case "SELECCIONADA":
        return "bg-blue-600 text-white font-bold cursor-pointer ring-2 ring-blue-300 z-10 scale-105 shadow-md";
      default:
        return "dark:bg-gray-950 dark:text-white bg-[#f5f7fa] dark:bg-gray-950 text-black hover:bg-green-500 cursor-pointer";
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
        <div>
          <p className="md:mr-12 text-sm italic  ">
            Para seleccionar, haga click en la fecha inicial y luego en la fecha
            final.
          </p>
          <p className="md:mr-12 text-md font-semibold ">
            Habitaciones desde {fecha_inicio} hasta {fecha_fin}
          </p>
        </div>

        <button className="cursor-pointer px-8 py-2 rounded-xl font-bold transition duration-300 bg-[#52a173] text-white hover:bg-[#10b655]">
          Confirmar
        </button>
      </div>
      <div>
        <div className="flex gap-3 text-sm">
          <div className="flex items-center gap-1">
            <div className="w-3 h-3 bg-blue-600 rounded-sm"></div> Tu selección
          </div>
          <div className="flex items-center gap-1">
            <div className="w-3 h-3 bg-black dark:bg-white rounded-sm"></div>{" "}
            Ocupada
          </div>
          <div className="flex items-center gap-1">
            <div className="w-3 h-3 bg-yellow-500 rounded-sm"></div> Reservada
          </div>
          <div className="flex items-center gap-1">
            <div className="w-3 h-3 border border-gray-400 bg-[#f5f7fa] dark:bg-gray-950 rounded-sm"></div>{" "}
            Libre
          </div>
        </div>
      </div>

      <div className="bg-[#f5f7fa] dark:bg-gray-950 overflow-auto max-h-[60vh] relative">
        <table className="w-full  bg-[#f5f7fa] dark:bg-gray-950 border dark:border-white border-gray-950">
          <thead>
            <tr>
              <th className="p-2  border dark:border-white border-gray-950 dark:text-white sticky left-0 top-0 z-30 min-w-[100px] text-left">
                Fecha
              </th>
              {habitaciones.map((h) => (
                <th
                  key={h.nroHab}
                  className="p-2 min-w-20 bg-[#f5f7fa] dark:bg-gray-950 sticky top-0 z-20  border dark:border-white border-gray-950"
                >
                  <div className="flex flex-col items-center">
                    <span className="font-bold">Hab {h.nroHab}</span>
                    <span className="text-[10px] font-normal text-gray-500">
                      {h.tipo_hab}
                    </span>
                  </div>
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {fechas.map((fecha) => (
              <tr
                key={fecha}
                className="hover:bg-gray-50 dark:hover:bg-gray-950 transition-colors"
              >
                <td className="p-2 border font-bold dark:text-white sticky left-0 z-10">
                  {fecha}
                </td>
                {habitaciones.map((hab) => {
                  const estado = obtenerEstado(hab.nroHab, fecha);

                  return (
                    <td
                      key={`${hab.nroHab}-${fecha}`}
                      className={`p-2 border-b border-gray-100 dark:border-gray-800 text-center text-sm font-bold transition-all duration-100 dark:text-black ${pintarEstado(
                        estado
                      )}`}
                      title={`Hab: ${hab.nroHab} - ${estado}`}
                      onClick={() => manejarClick(hab.nroHab, fecha, estado)}
                    >
                      {estado !== "DISPONIBLE" &&
                        estado !== "SELECCIONADA" &&
                        estado.substring(0, 1)}
                      {estado === "SELECCIONADA" && "✓"}
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
