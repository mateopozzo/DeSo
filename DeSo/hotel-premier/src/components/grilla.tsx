"use client";
import React, { useState } from "react";
import {
  Habitacion,
  DisponibilidadDTO,
} from "../services/habitaciones.service";

export type DatosSeleccion = {
  idHabitacion: number;
  fechaInicio: string;
  fechaFin: string;
};

interface GrillaProps {
  fecha_inicio: string;
  fecha_fin: string;
  habitaciones: Habitacion[];
  reservas: DisponibilidadDTO[];
  onConfirmarSeleccion?: (datos: DatosSeleccion) => void;
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
  onConfirmarSeleccion,
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
      if (fecha === seleccion.fecha_inicio_sel && !seleccion.fecha_fin_sel) {
        return "SELECCIONADA";
      }
      if (
        seleccion.fecha_fin_sel &&
        fecha >= seleccion.fecha_inicio_sel &&
        fecha <= seleccion.fecha_fin_sel
      ) {
        return "SELECCIONADA";
      }
    }
    return "DISPONIBLE";
  };

  const manejarClick = (idHab: number, fecha: string, estadoActual: string) => {
    if (estadoActual === "OCUPADA" || estadoActual === "EN MANTENIMIENTO") {
      return;
    }

    setSeleccion((prev) => {
      if (
        !prev ||
        prev.idHabitacion !== idHab ||
        (prev.fecha_inicio_sel && prev.fecha_fin_sel)
      ) {
        return {
          idHabitacion: idHab,
          fecha_inicio_sel: fecha,
          fecha_fin_sel: null,
        };
      }

      if (prev.idHabitacion === idHab && !prev.fecha_fin_sel) {
        let inicio = prev.fecha_inicio_sel;
        let fin = fecha;
        if (fin < inicio) {
          [inicio, fin] = [fin, inicio];
        }
        return {
          idHabitacion: idHab,
          fecha_inicio_sel: inicio,
          fecha_fin_sel: fin,
        };
      }
      return prev;
    });
  };

  const confirmar = () => {
    if (!seleccion || !seleccion.fecha_fin_sel) {
      alert("Por favor selecciona una fecha de inicio y una de fin.");
      return;
    }

    if (onConfirmarSeleccion) {
      onConfirmarSeleccion({
        idHabitacion: seleccion.idHabitacion,
        fechaInicio: seleccion.fecha_inicio_sel,
        fechaFin: seleccion.fecha_fin_sel,
      });
    }
  };

  const pintarEstado = (estado: string) => {
    const estadoUpper = estado ? estado.toUpperCase() : "DISPONIBLE";
    switch (estadoUpper) {
      case "MANTENIMIENTO":
        return "bg-red-500 text-white cursor-not-allowed";
      case "SELECCIONADA":
        return "bg-blue-600 text-white font-bold cursor-pointer ring-2 ring-blue-300 z-10 scale-105 shadow-md";
      case "OCUPADA":
        return "dark:bg-white dark:text-black bg-black text-white cursor-not-allowed opacity-80";
      case "RESERVADA":
        return "bg-yellow-500 text-white cursor-not-allowed opacity-80";
      default:
        return "dark:bg-gray-950 dark:text-white bg-[#f5f7fa] dark:bg-gray-950 text-black hover:bg-green-500 cursor-pointer";
    }
  };

  const generarFechas = () => {
    const fechasArr: string[] = [];
    if (!fecha_inicio || !fecha_fin) return [];

    const [anioI, mesI, diaI] = fecha_inicio.split("-").map(Number);
    const [anioF, mesF, diaF] = fecha_fin.split("-").map(Number);

    let actual = new Date(anioI, mesI - 1, diaI);
    const final = new Date(anioF, mesF - 1, diaF);

    while (actual <= final) {
      fechasArr.push(actual.toISOString().split("T")[0]);
      actual.setDate(actual.getDate() + 1);
    }
    return fechasArr;
  };

  const listaFechas = generarFechas();

  return (
    <div className="flex flex-col justify-between items-center mb-2 bg-[#f5f7fa] dark:bg-gray-950 p-4 rounded-lg">
      <div className="flex flex-col items-center justify-between w-full md:flex-row mb-4 gap-4">
        <div>
          <p className="text-sm italic text-gray-500 dark:text-gray-400">
            Haga click en fecha inicio y luego en fecha fin.
          </p>
          <div className="flex gap-3 text-xs mt-2">
            <div className="flex items-center gap-1">
              <div className="w-3 h-3 bg-blue-600 rounded-sm"></div> Tu
              selección
            </div>
            <div className="flex items-center gap-1">
              <div className="w-3 h-3 bg-black dark:bg-white rounded-sm"></div>{" "}
              Ocupada
            </div>
            <div className="flex items-center gap-1">
              <div className="w-3 h-3 bg-yellow-500 rounded-sm"></div> Reservada
            </div>
          </div>
        </div>

        {seleccion?.fecha_fin_sel && onConfirmarSeleccion && (
          <button
            onClick={confirmar}
            className="cursor-pointer px-8 py-2 rounded-xl font-bold transition duration-300 bg-[#52a173] text-white hover:bg-[#10b655] shadow-lg animate-in fade-in slide-in-from-right-5"
          >
            Confirmar selección
          </button>
        )}
      </div>

      <div className="bg-[#f5f7fa] dark:bg-gray-950 overflow-auto max-h-[60vh] w-full relative border rounded shadow-inner">
        <table className="w-full border-collapse">
          <thead>
            <tr>
              <th className="p-2 border dark:border-gray-700 dark:text-white sticky left-0 top-0 z-30 min-w-[100px] text-left bg-[#f5f7fa] dark:bg-gray-950">
                Fecha
              </th>
              {habitaciones.map((h) => (
                <th
                  key={h.nroHab}
                  className="p-2 min-w-20 sticky top-0 z-20 border dark:border-gray-700 bg-[#f5f7fa] dark:bg-gray-950"
                >
                  <div className="flex flex-col items-center">
                    <span className="font-bold">Hab {h.nroHab}</span>
                    <span className="text-[10px] text-gray-500">
                      {h.tipo_hab}
                    </span>
                  </div>
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {listaFechas.map((fecha) => (
              <tr
                key={fecha}
                className="hover:bg-gray-100 dark:hover:bg-gray-900"
              >
                <td className="p-2 border dark:border-gray-700 font-bold dark:text-white sticky left-0 z-10 bg-[#f5f7fa] dark:bg-gray-950">
                  {fecha}
                </td>
                {habitaciones.map((hab) => {
                  const estado = obtenerEstado(hab.nroHab, fecha);
                  return (
                    <td
                      key={`${hab.nroHab}-${fecha}`}
                      className={`p-2 border dark:border-gray-800 text-center text-sm font-bold transition-all duration-100 ${pintarEstado(
                        estado
                      )}`}
                      title={`Hab: ${hab.nroHab} - ${estado}`}
                      onClick={() => manejarClick(hab.nroHab, fecha, estado)}
                    >
                      {estado === "SELECCIONADA" && "✓"}
                      {estado !== "DISPONIBLE" &&
                        estado !== "SELECCIONADA" &&
                        estado.charAt(0)}
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
