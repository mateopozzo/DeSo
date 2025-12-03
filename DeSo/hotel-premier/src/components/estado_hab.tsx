"use client";
import React, { useState, useMemo } from "react";
import { DisponibilidadDTO, Habitacion } from "@/services/habitaciones.service";

interface GrillaProps {
  desde: string; // ANTES: fecha_inicio
  hasta: string; // ANTES: fecha_fin
  habitaciones: Habitacion[];
  estados: DisponibilidadDTO[]; // Usamos 'estados' para coincidir con lo que envia la pagina
  seleccionDias: (sel: { idhab: string | number; fecha: string }[]) => void;
}

export default function GrillaHabitaciones({
                                             desde,
                                             hasta,
                                             habitaciones,
                                             estados,
                                             seleccionDias,
                                           }: GrillaProps) {

  // LOGICA RANGOS LOCAL: { idHabitacion: { inicio: "2023-01-01", fin: "2023-01-05" } }
  const [rangosSeleccionados, setRangosSeleccionados] = useState<
      Record<number, { inicio: string; fin: string | null }>
  >({});

  // 1. GENERAR FECHAS
  const fechas = useMemo(() => {
    const arr: string[] = [];
    if (!desde || !hasta) return arr;

    let actual = new Date(desde + "T00:00:00");
    const final = new Date(hasta + "T00:00:00");

    while (actual <= final) {
      arr.push(actual.toISOString().split("T")[0]);
      actual.setDate(actual.getDate() + 1);
    }
    return arr;
  }, [desde, hasta]);

  // 2. VERIFICAR SI ESTÁ OCUPADA EN BACKEND
  const obtenerEstadoBackend = (idHab: number, fecha: string) => {
    const ocupada = estados.find(
        (res) =>
            res.idHabitacion === idHab &&
            fecha >= res.fecha_inicio &&
            fecha <= res.fecha_fin
    );
    return ocupada ? ocupada.estado : "DISPONIBLE";
  };

  // 3. MANEJAR CLICKS (Lógica de Rangos: Click 1 = Inicio, Click 2 = Fin)
  const handleCellClick = (idHab: number, fecha: string, estadoActual: string) => {
    if (estadoActual !== "DISPONIBLE") return; // No dejar seleccionar ocupadas

    setRangosSeleccionados((prev) => {
      const nuevo = { ...prev };
      const rangoActual = nuevo[idHab];

      // CASO 1: No hay nada seleccionado para esta habitación -> Nuevo Inicio
      if (!rangoActual) {
        nuevo[idHab] = { inicio: fecha, fin: null };
      }
      // CASO 2: Ya hay inicio, falta fin -> Definir Rango
      else if (rangoActual.inicio && !rangoActual.fin) {
        if (fecha < rangoActual.inicio) {
          // Si clickeo una fecha ANTERIOR al inicio, esa se vuelve el nuevo inicio
          nuevo[idHab] = { inicio: fecha, fin: null };
        } else {
          // Si es posterior, cerramos el rango
          nuevo[idHab] = { inicio: rangoActual.inicio, fin: fecha };
        }
      }
      // CASO 3: Ya hay rango completo -> Resetear y empezar uno nuevo
      else {
        nuevo[idHab] = { inicio: fecha, fin: null };
      }

      // Notificar al padre
      actualizarPadre(nuevo);
      return nuevo;
    });
  };

  const actualizarPadre = (mapaRangos: Record<number, { inicio: string; fin: string | null }>) => {
    const seleccionPlana: { idhab: number; fecha: string }[] = [];

    Object.entries(mapaRangos).forEach(([idStr, rango]) => {
      const id = Number(idStr);
      if (rango.inicio) {
        const finCalculo = rango.fin ? rango.fin : rango.inicio;

        let cursor = new Date(rango.inicio + "T00:00:00");
        const limite = new Date(finCalculo + "T00:00:00");

        while (cursor <= limite) {
          seleccionPlana.push({
            idhab: id,
            fecha: cursor.toISOString().split("T")[0]
          });
          cursor.setDate(cursor.getDate() + 1);
        }
      }
    });
    seleccionDias(seleccionPlana);
  };

  // 4. ESTILOS DE CELDA
  const getClaseCelda = (idHab: number, fecha: string, estadoBackend: string) => {
    // A. Prioridad Backend
    if (estadoBackend === "OCUPADA") return "bg-black text-white dark:bg-white dark:text-black cursor-not-allowed opacity-50";
    if (estadoBackend === "RESERVADA") return "bg-yellow-500 text-white cursor-not-allowed";
    if (estadoBackend === "EN MANTENIMIENTO") return "bg-red-400 text-white cursor-not-allowed";

    // B. Prioridad Selección Local
    const rango = rangosSeleccionados[idHab];
    if (rango) {
      if (fecha === rango.inicio || fecha === rango.fin) {
        return "bg-blue-600 text-white font-bold ring-2 ring-blue-300 z-10"; // Puntas del rango
      }
      if (rango.fin && fecha > rango.inicio && fecha < rango.fin) {
        return "bg-blue-200 text-blue-800 dark:bg-blue-900 dark:text-blue-200"; // Medio del rango
      }
    }

    // C. Defecto
    return "bg-white text-black hover:bg-green-200 dark:bg-gray-950 dark:text-white cursor-pointer transition-colors";
  };

  return (
      <div className="flex flex-col mb-2 bg-[#f5f7fa] dark:bg-gray-950 p-4 rounded-xl">
        <div className="mb-4">
          <p className="text-sm italic text-gray-500">
            Click en una fecha para <span className="font-bold text-blue-600">INICIAR</span> rango.
            Click en otra posterior para <span className="font-bold text-blue-600">CERRAR</span> rango.
          </p>
        </div>

        <div className="overflow-auto max-h-[60vh] relative border rounded-lg shadow-inner dark:border-gray-800">
          <table className="w-full border-collapse">
            <thead>
            <tr>
              <th className="p-3 bg-white dark:bg-gray-900 border-b dark:border-gray-800 sticky left-0 top-0 z-30 min-w-[100px] text-left shadow-sm">
                Fecha
              </th>
              {habitaciones.map((h) => (
                  <th key={h.nroHab} className="p-2 min-w-24 bg-[#f5f7fa] dark:bg-gray-900 sticky top-0 z-20 border-b dark:border-gray-800">
                    <div className="flex flex-col items-center">
                      <span className="font-bold text-gray-800 dark:text-gray-200">{h.nroHab}</span>
                      <span className="text-[10px] uppercase text-gray-500">{h.tipo_hab}</span>
                    </div>
                  </th>
              ))}
            </tr>
            </thead>
            <tbody>
            {fechas.map((fecha) => (
                <tr key={fecha} className="border-b dark:border-gray-800">
                  <td className="p-2 bg-white dark:bg-gray-900 font-mono text-sm sticky left-0 z-10 font-bold text-gray-700 dark:text-gray-300 border-r dark:border-gray-800">
                    {fecha}
                  </td>
                  {habitaciones.map((hab) => {
                    const estadoBack = obtenerEstadoBackend(hab.nroHab, fecha);
                    const clase = getClaseCelda(hab.nroHab, fecha, estadoBack);

                    return (
                        <td
                            key={`${hab.nroHab}-${fecha}`}
                            className={`p-2 border-l border-r dark:border-gray-800 text-center text-xs select-none ${clase}`}
                            onClick={() => handleCellClick(hab.nroHab, fecha, estadoBack)}
                        >
                          {estadoBack === "DISPONIBLE" ? "" : estadoBack.charAt(0)}
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