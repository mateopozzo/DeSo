"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import GrillaHabitaciones from "@/components/estado_hab";

import {
  buscarEstadoHabitaciones,
  crearReserva,
  DisponibilidadDTO,
} from "@/services/habitaciones.service";

export default function ReservarHab() {
  const router = useRouter();
  const [error, setError] = useState<string | null>(null);

  const [desde, setDesde] = useState("");
  const [hasta, setHasta] = useState("");
  const [estados, setEstados] = useState<DisponibilidadDTO[]>([]);
  const [seleccion, setSeleccion] = useState<
    { idhab: number | string; fecha: string }[]
  >([]);

  const [nombre, setNombre] = useState("");
  const [apellido, setApellido] = useState("");
  const [telefono, setTelefono] = useState("");

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
    setEstados(resp);
  };

  const confirmarReserva = async () => {
    if (!nombre || !apellido || !telefono) {
      alert("EDITAR ESTO: Debe completar todos los datos");
      return;
    }
    const habitaciones = Array.from(
      new Set(seleccion.map((x) => String(x.idhab)))
    );

    const datosRes = {
      reservaDTO: {
        fecha_inicio: desde,
        fecha_fin: hasta,
        nombre,
        apellido,
        telefono,
      },
      listaIDHabitaciones: habitaciones,
    };
    try {
      await crearReserva(datosRes);
      alert("Reserva creada correctamente");
    } catch (err) {
      alert("Error al crear reserva");
    }
  };

  return (
    <div className="dark:bg-gray-950 dark:text-white">
      <h1 className="text-[#141414] dark:text-white  mb-4 text-5xl font-bold pb-2">
        Reservar habitación
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
      {seleccion.length > 0 && (
        <div className="mt-6">
          <h2 className="text-2xl font-bold">Reserva a nombre de</h2>

          <div className="mt-3 flex flex-col gap-2 w-80">
            <input
              type="text"
              placeholder="Apellido"
              value={apellido}
              onChange={(e) => setApellido(e.target.value)}
              className="border px-2 py-1"
              maxLength={20}
            />
            <input
              type="text"
              placeholder="Nombre"
              value={nombre}
              onChange={(e) => setNombre(e.target.value)}
              className="border px-2 py-1"
              maxLength={20}
            />
            <input
              type="text"
              placeholder="Teléfono"
              value={telefono}
              onChange={(e) => setTelefono(e.target.value)}
              className="border px-2 py-1"
              maxLength={25}
            />

            <button
              onClick={confirmarReserva}
              className="mt-2 px-4 py-2 bg-green-600 text-white rounded"
            >
              Confirmar reserva
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
