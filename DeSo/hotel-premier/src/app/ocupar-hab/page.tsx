"use client";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import GrillaHabitaciones from "@/components/estado_hab";
import BuscadorPersona from "@/components/buscador_persona";
import {
  pedirHabs,
  buscarEstadoHabitaciones,
  Habitacion,
  DisponibilidadDTO,
} from "../../services/habitaciones.service";
import Grilla from "../../components/grilla";

import { busqueda, ResultadoBusq } from "@/services/busqueda.service";
import {
  buscarAlojados,
  crearEstadia,
  EstadiaDTO,
} from "@/services/estadia.service";

interface ReservaCola {
  idhab: number;
  fecha_inicio: string;
  fecha_fin: string;
}

export default function OcuparHabPage() {
  const router = useRouter();

  // ESTADOS DEL FLUJO
  const [paso, setPaso] = useState<"GRILLA" | "CARGA" | "GUARDANDO" | "EXITO">(
    "GRILLA"
  );
  const [error, setError] = useState<string | null>(null);

  // DATOS
  const [fecha_inicio, setDesde] = useState("");
  const [fecha_fin, setHasta] = useState("");
  const [estados, setEstados] = useState<DisponibilidadDTO[]>([]);
  const [listaHabitaciones, setListaHabitaciones] = useState<Habitacion[]>([]);

  const [seleccionConfirmada, setSeleccionConfirmada] = useState<
    { idhab: number | string; fecha: string }[]
  >([]);

  // CARGA SECUENCIAL
  const [colaReservas, setColaReservas] = useState<ReservaCola[]>([]);
  const [indiceActual, setIndiceActual] = useState(0);
  const [estadiasListas, setEstadiasListas] = useState<EstadiaDTO[]>([]);

  // DATOS HABITACIÓN ACTUAL
  const [encargadoActual, setEncargadoActual] = useState<ResultadoBusq | null>(
    null
  );
  const [invitadosActuales, setInvitadosActuales] = useState<ResultadoBusq[]>(
    []
  );

  // EFECTO DE CARGA INICIAL
  useEffect(() => {
    const cargarHabitaciones = async () => {
      try {
        const data = await pedirHabs();
        setListaHabitaciones(data.sort((a, b) => a.nroHab - b.nroHab));
      } catch (err) {
        console.error("Error cargando habitaciones:", err);
        setError("No se pudieron cargar las habitaciones.");
      }
    };
    cargarHabitaciones();
  }, []);

  const cancelarCasoUso = () => {
    if (confirm("¿Seguro que desea cancelar todo el proceso?")) {
      router.push("/");
    }
  };

  const resetearFormulario = () => {
    setPaso("GRILLA");
    setEstados([]);
    setSeleccionConfirmada([]);
    setColaReservas([]);
    setIndiceActual(0);
    setEstadiasListas([]);
    setEncargadoActual(null);
    setInvitadosActuales([]);
    setError(null);
  };

  const buscarDisponibilidad = async () => {
    if (!fecha_inicio || !fecha_fin || fecha_inicio > fecha_fin) {
      setError("Fechas inválidas o incompletas.");
      return;
    }
    setError(null);
    try {
      const resp = await buscarEstadoHabitaciones(fecha_inicio, fecha_fin);
      setEstados(resp);
    } catch (e) {
      console.error(e);
      setError("Error al traer estados de habitaciones.");
    }
  };

  const iniciarCargaHuespedes = () => {
    if (seleccionConfirmada.length === 0) {
      setError("Debe seleccionar y confirmar días en la grilla.");
      return;
    }

    const grupos = new Map<number, string[]>();
    seleccionConfirmada.forEach((sel) => {
      const id = Number(sel.idhab);
      const fechas = grupos.get(id) || [];
      fechas.push(sel.fecha);
      grupos.set(id, fechas);
    });

    const colaProcesada: ReservaCola[] = [];
    grupos.forEach((fechas, id) => {
      fechas.sort();
      colaProcesada.push({
        idhab: id,
        fecha_inicio: fechas[0],
        fecha_fin: fechas[fechas.length - 1],
      });
    });

    setColaReservas(colaProcesada);
    setPaso("CARGA");
    setIndiceActual(0);
    setError(null);
  };

  const confirmarHabitacionYContinuar = () => {
    if (!encargadoActual) {
      alert("Falta asignar el encargado.");
      return;
    }
    const reserva = colaReservas[indiceActual];
    const nuevaEstadia: EstadiaDTO = {
      idHabitacion: reserva.idhab,
      fechaInicio: reserva.fecha_inicio,
      fechaFin: reserva.fecha_fin,
      encargado: { ...encargadoActual },
      listaInvitados: invitadosActuales.map((inv) => ({ ...inv })),
    };

    const nuevasListas = [...estadiasListas, nuevaEstadia];
    setEstadiasListas(nuevasListas);
    setEncargadoActual(null);
    setInvitadosActuales([]);

    if (indiceActual < colaReservas.length - 1) {
      setIndiceActual(indiceActual + 1);
    } else {
      persistirTodo(nuevasListas);
    }
  };

  const persistirTodo = async (listaFinal: EstadiaDTO[]) => {
    setPaso("GUARDANDO");
    try {
      for (const estadia of listaFinal) {
        await crearEstadia(estadia);
      }
      setPaso("EXITO");
    } catch (err: any) {
      console.error(err);
      setError("Error guardando reservas: " + err.message);
      setPaso("CARGA");
    }
  };

  const reservaActual = colaReservas[indiceActual];

  return (
    <div className="p-6 min-h-screen bg-[#f5f7fa] dark:bg-gray-950 dark:text-white ">
      <h1 className=" dark:text-white text-5xl font-bold pb-2">
        Ocupar habitación
      </h1>
      <p className="dark:text-white mb-8">
        Indique las fechas deseadas para verificar disponibilidad de
        habitaciones
      </p>
      <div className="flex justify-between items-center mb-4">
        <h1 className="text-2xl font-bold text-gray-800 dark:text-white">
          {paso === "GRILLA" && "Verificar disponibilidad de habitaciones"}
          {paso === "CARGA" && "Asignación de huéspedes"}
          {paso === "GUARDANDO" && "Guardando..."}
          {paso === "EXITO" && "Check in exitoso"}
        </h1>
        {paso !== "EXITO" && paso !== "GUARDANDO" && (
          <button
            onClick={cancelarCasoUso}
            className="cursor-pointer px-8 py-2 rounded-xl font-bold transition duration-300 dark:border dark:border-white dark:text-white bg-[#f5f7fa] dark:bg-gray-950 dark:hover:border-[#b92716] text-[#1a252f] border border-[#1a252f] hover:bg-[#b92716] hover:text-white hover:border-[#b92716]"
          >
            Cancelar
          </button>
        )}
      </div>

      {error && (
        <div className="bg-red-700 text-white p-4 mb-4 rounded shadow-sm font-bold">
          {error}
        </div>
      )}

      {paso === "GRILLA" && (
        <div className="animate-fade-in space-y-6">
          <div className="flex flex-wrap gap-4 items-end  p-6 rounded-xl">
            <div>
              <label className="block text-md font-bold mb-2">Desde</label>
              <input
                type="date"
                value={fecha_inicio}
                onChange={(e) => setDesde(e.target.value)}
                className="px-8 py-2 rounded-xl text-black bg-[#f5f7fa] dark:bg-gray-950 dark:text-white border border-black dark:border-white"
              />
            </div>

            <div>
              <label className="block text-md font-bold mb-2">Hasta</label>
              <input
                type="date"
                value={fecha_fin}
                onChange={(e) => setHasta(e.target.value)}
                className="px-8 py-2 rounded-xl text-black bg-[#f5f7fa] dark:bg-gray-950 dark:text-white border border-black dark:border-white"
              />
            </div>
            <button
              onClick={buscarDisponibilidad}
              className="px-8 py-2 rounded-xl font-bold transition duration-300 text-white bg-[#52a173] hover:bg-[#10b655] cursor-pointer"
            >
              Ver disponibilidad
            </button>
          </div>

          {estados.length > 0 && listaHabitaciones.length > 0 && (
            <>
              <div className="border rounded-xl overflow-hidden shadow-sm">
                <GrillaHabitaciones
                  desde={fecha_inicio}
                  hasta={fecha_fin}
                  habitaciones={listaHabitaciones}
                  estados={estados}
                  seleccionDias={setSeleccionConfirmada}
                />
              </div>
              <div className="flex justify-end pt-4">
                <button
                  onClick={iniciarCargaHuespedes}
                  disabled={seleccionConfirmada.length === 0}
                  className={`px-8 py-3 rounded-xl font-bold text-lg shadow-lg transition-all ${
                    seleccionConfirmada.length > 0
                      ? "bg-green-600 text-white hover:bg-green-700"
                      : "bg-gray-300 text-gray-500 cursor-not-allowed"
                  }`}
                >
                  Continuar (
                  {new Set(seleccionConfirmada.map((s) => s.idhab)).size} habs)
                  →
                </button>
              </div>
            </>
          )}
        </div>
      )}

      {paso === "CARGA" && reservaActual && (
        <div className="max-w-6xl mx-auto grid grid-cols-1 xl:grid-cols-3 gap-8">
          <div className="xl:col-span-2 space-y-8">
            <div className="bg-[#f5f7fa] dark:bg-gray-950 p-6 rounded-xl border border-gray-200 shadow-sm">
              <h2 className="text-xl font-bold mb-4 text-blue-600">
                Seleccionar encargado
              </h2>
              {!encargadoActual ? (
                <BuscadorPersona
                  titulo="Buscar Responsable"
                  onBuscar={busqueda}
                  onSeleccionar={setEncargadoActual}
                />
              ) : (
                <div className="flex justify-between items-center bg-blue-50 p-4 rounded-lg text-black">
                  <div>
                    <p className="font-bold">
                      {encargadoActual.apellido}, {encargadoActual.nombre}
                    </p>
                    <p className="text-sm">{encargadoActual.nroDoc}</p>
                  </div>
                  <button
                    onClick={() => setEncargadoActual(null)}
                    className="text-red-500 font-bold"
                  >
                    Cambiar
                  </button>
                </div>
              )}
            </div>
            <div
              className={`bg-[#f5f7fa] dark:bg-gray-950 p-6 rounded-xl border border-gray-200 shadow-sm ${
                !encargadoActual ? "opacity-50 pointer-events-none" : ""
              }`}
            >
              <h2 className="text-xl font-bold mb-4 text-purple-600">
                2. Ocupantes adicionales
              </h2>
              <BuscadorPersona
                titulo="Buscar Acompañante"
                onBuscar={buscarAlojados}
                onSeleccionar={(p) =>
                  setInvitadosActuales([...invitadosActuales, p])
                }
                excluidos={invitadosActuales}
              />
            </div>
          </div>
          <div className="xl:col-span-1">
            <div className="sticky top-6 bg-[#f5f7fa] dark:bg-gray-950 border rounded-xl p-6 shadow-xl">
              <h3 className="text-2xl font-bold mb-2">
                Habitación {reservaActual.idhab}
              </h3>
              <p className="text-gray-500 mb-4">
                {reservaActual.fecha_inicio} ➔ {reservaActual.fecha_fin}
              </p>

              <div className="mb-4 space-y-2">
                <p className="font-bold text-sm uppercase">Ocupantes:</p>
                {encargadoActual && (
                  <div className="text-blue-600 font-bold">
                    ★ {encargadoActual.apellido} (Resp)
                  </div>
                )}
                {invitadosActuales.map((inv, i) => (
                  <div
                    key={i}
                    className="flex justify-between text-sm bg-gray-50 p-2 rounded text-black"
                  >
                    {inv.apellido}{" "}
                    <button
                      onClick={() =>
                        setInvitadosActuales(
                          invitadosActuales.filter((_, idx) => idx !== i)
                        )
                      }
                      className="text-red-500"
                    >
                      ✕
                    </button>
                  </div>
                ))}
              </div>

              <button
                onClick={confirmarHabitacionYContinuar}
                disabled={!encargadoActual}
                className={`w-full py-4 rounded-xl font-bold text-white ${
                  encargadoActual
                    ? "bg-green-600 hover:bg-green-700"
                    : "bg-gray-400"
                }`}
              >
                {indiceActual < colaReservas.length - 1
                  ? "Siguiente →"
                  : "Finalizar"}
              </button>
            </div>
          </div>
        </div>
      )}

      {/* VISTAS FINALES */}
      {paso === "GUARDANDO" && (
        <div className="fixed inset-0 bg-white/90 flex items-center justify-center z-50">
          <h2 className="text-2xl font-bold text-black">Guardando...</h2>
        </div>
      )}
      {paso === "EXITO" && (
        <div className="text-center py-20">
          <h2 className="text-4xl font-bold text-green-600 mb-4">¡Éxito!</h2>
          <button
            onClick={() => router.push("/")}
            className="bg-gray-800 text-white px-6 py-3 rounded-xl"
          >
            Volver al inicio
          </button>
        </div>
      )}
    </div>
  );
}
