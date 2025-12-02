"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import GrillaHabitaciones from "@/components/estado_hab";

import {
  buscarEstadoHabitaciones,
  DisponibilidadDTO,
} from "@/services/habitaciones.service";

export default function OcuparHab() {
  const [error, setError] = useState<string | null>(null);
  const [desde, setDesde] = useState("");
  const [hasta, setHasta] = useState("");
  const [estados, setEstados] = useState<DisponibilidadDTO[]>([]);
  const [seleccion, setSeleccion] = useState<
    { idhab: number | string; fecha: string }[]
  >([]);

  const buscarEstado = async () => {
    if (!desde || !hasta) {
      setError("Es obligatorio ingresar ambas fechas.");
      return;
    }

    if (desde > hasta) {
      setError("La fecha 'desde' no puede ser posterior a la fecha 'hasta'.");
      return;
    }

    const resp = await buscarEstadoHabitaciones(desde, hasta);
    if (resp.length === 0) {
      console.log("No hay habitaciones disponibles en esas fechas");
      console.log("FIN CASO DE USO");
    }
    setEstados(resp);
  };

  return (
    <div className="dark:bg-gray-950 dark:text-white">
      <h1 className="text-[#141414] dark:text-white  mb-4 text-5xl font-bold pb-2">
        Ocupar habitación
      </h1>
      <div className="flex flex-col w-sm gap-4 mb-4">
        <label className="text-lg">Desde fecha: </label>
        <input
          type="date"
          className="border p-2 rounded-xl"
          value={desde}
          onChange={(e) => setDesde(e.target.value)}
        />
        <label className="text-lg">Hasta fecha: </label>
        <input
          type="date"
          className="border p-2 rounded-xl"
          value={hasta}
          onChange={(e) => setHasta(e.target.value)}
        />

        <button
          onClick={buscarEstado}
          className="cursor-pointer px-8 py-2 rounded-xl font-bold transition duration-300 bg-[#52a173] text-white hover:bg-[#10b655]"
        >
          Buscar
        </button>
      </div>
      {estados.length > 0 && (
        <GrillaHabitaciones
          idHab={estados.map((x) => ({
            id: String(x.idHabitacion),
            name: x.tipoHab,
          }))}
          desde={desde}
          hasta={hasta}
          estados={estados.map((x) => ({
            idhab: x.idHabitacion,
            fecha: x.fechaDesde,
            estado: x.estado as any,
          }))}
          seleccionDias={(sel) => setSeleccion(sel)}
        />
      )}
    </div>
  );
}
