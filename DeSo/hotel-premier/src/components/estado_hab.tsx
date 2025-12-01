"use client";
import { useState } from "react";
import {
  buscarEstadoHabitaciones,
  DisponibilidadDTO,
} from "../services/habitaciones.service";

export interface grillaHab {
  habitacionId: number;
  fechaInicio: string;
  fechaFin: string;
}

export default async function EstadoHabitaciones() {
  const [error, setError] = useState<string | null>(null);

  const [fechaDesde, setFechaDesde] = useState("");
  const [fechaHasta, setFechaHasta] = useState("");
  // meto las habs que recibo de back y con > las meto en un nuevo arreglo
  const [habitaciones, setHabitaciones] = useState<DisponibilidadDTO[]>([]);
  const [loading, setLoading] = useState(false);

  const handleBuscar = async () => {
    if (!fechaDesde || !fechaHasta) {
      setError(`El formato de fecha no es válido.`);
      return;
    }

    if (fechaDesde > fechaHasta) {
      setError(
        `La fecha "desde" ${fechaDesde} no puede ser mayor a la fecha "hasta" ${fechaHasta}.`
      );
      return;
    }

    // me deja que no se cambie el estado mientras se este llamado al back
    setLoading(true);
    const datos = await buscarEstadoHabitaciones(fechaDesde, fechaHasta);
    setHabitaciones(datos);
    // vuelvo a dejarlo habilitado para que cambie
    setLoading(false);
  };

  return (
    <div className="flex flex-col gap-6 p-4 rounded-xl  dark:bg-gray-950 dark:text-white">
      <div className="flex gap-4 items-end">
        <div>
          {error && (
            <div className="bg-[#914d45] text-white p-2 mb-4 rounded font-semibold">
              {error}
            </div>
          )}
          <label className="block text-sm mb-1 dark:text-gray-300">
            Desde fecha
          </label>
          <input
            type="date"
            value={fechaDesde}
            onChange={(e) => setFechaDesde(e.target.value)}
            className="border p-2 rounded dark:bg-gray-800 dark:text-white"
          />
        </div>
        <div>
          <label className="block text-sm mb-1 dark:text-gray-300">
            Hasta fecha
          </label>
          <input
            type="date"
            value={fechaHasta}
            onChange={(e) => setFechaHasta(e.target.value)}
            className="border p-2 rounded dark:bg-gray-800 dark:text-white"
          />
        </div>
        <button
          onClick={handleBuscar}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
        >
          Buscar
        </button>
      </div>

      <div className="border rounded-lg p-4 min-h-[300px] bg-gray-50 dark:bg-gray-800">
        <p className="text-gray-500 mb-4"></p>
      </div>
    </div>
  );
}
