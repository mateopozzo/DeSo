"use client";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import BuscadorPersona from "@/components/buscador_persona";
import {
  pedirHabs,
  buscarEstadoHabitaciones,
  Habitacion,
  DisponibilidadDTO,
  DatosRes,
  verificarRes,
} from "../../../services/habitaciones.service";
import Grilla from "../../../components/grilla";

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

  const [fecha_inicio, setDesde] = useState("");
  const [fecha_fin, setHasta] = useState("");
  const [estados, setEstados] = useState<DisponibilidadDTO[]>([]);
  const [listaHabitaciones, setListaHabitaciones] = useState<Habitacion[]>([]);
  const [busquedaRealizada, setBusquedaRealizada] = useState(false);
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

  const fecha_hoy = new Date();
  const mes = String(fecha_hoy.getMonth() + 1).padStart(2, "0");
  const dia = String(fecha_hoy.getDate()).padStart(2, "0");

  const hoy_fecha = `${fecha_hoy.getFullYear()}-${mes}-${dia}`;

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
    if (confirm("¿Desea cancelar todo el proceso?")) {
      router.push("/");
    }
  };

  const buscarDisponibilidad = async () => {
    if (!fecha_inicio || !fecha_fin) {
      setError("Por favor, ingrese fechas válidas.");
      return;
    }

    if (fecha_inicio > fecha_fin) {
      setError("La fecha inicial no puede ser mayor a la final");
      return;
    }

    if (fecha_inicio < hoy_fecha) {
      setError("Las fechas no pueden ser anteriores al día actual");
      return;
    }

    console.log("Buscando disponibilidad habitaciones");

    setError(null);
    try {
      setBusquedaRealizada(true);

      const resp = await buscarEstadoHabitaciones(fecha_inicio, fecha_fin);
      setEstados(resp);
      console.log("Fin busqueda de habitaciones", resp);
    } catch (e) {
      console.error(e);
      setError("Error al traer estados de habitaciones.");
    }
  };

  // recibe el seleccionado directamente desde el componente Grilla
  const procesarSeleccionGrilla = async (datos: {
    idHabitacion: number;
    fechaInicio: string;
    fechaFin: string;
    estado: string;
  }) => {
    console.log("Selección recibida de la grilla:", datos);

    if (datos.estado === "RESERVADA") {
      const datosResOc = await verificarRes(
        datos.idHabitacion,
        datos.fechaInicio,
        datos.fechaFin
      );

      if (datosResOc.length > 0) {
        const reservaEncontrada = datosResOc[0];
        console.log("Reserva encontrada:", reservaEncontrada);
        const ocuparRes = confirm(
          `La reserva seleccionada está a nombre de ${reservaEncontrada.apellido} ${reservaEncontrada.nombre}. \n\n ¿Desea ocupar la reserva?`
        );

        if (!ocuparRes) {
          return;
        }
      }
      if (datosResOc.length == 0) {
        console.log("Celda reservada pero no hay reserva en back");
      }
    }

    const nuevaReserva: ReservaCola = {
      idhab: datos.idHabitacion,
      fecha_inicio: datos.fechaInicio,
      fecha_fin: datos.fechaFin,
    };

    setColaReservas([nuevaReserva]);
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
      <hr className="mb-8 dark:text-white/50 text-black/50" />

      <div className="flex justify-between items-center mb-4">
        <h1 className="text-2xl font-bold text-gray-800 dark:text-white">
          {paso === "GRILLA" && "Verificar disponibilidad de habitaciones"}
          {paso === "CARGA" && "Asignación de huéspedes"}
          {paso === "GUARDANDO" && "Guardando..."}
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
        <div className="bg-red-600 text-white p-4 rounded-xl mb-6 font-bold shadow-md">
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
                className="px-4 py-2 rounded-xl border border-gray-300 dark:border-gray-700 bg-[#f5f7fa] dark:bg-gray-950 focus:ring focus:ring-blue-400 outline-none transition"
              />
            </div>

            <div>
              <label className="block text-md font-bold mb-2">Hasta</label>
              <input
                type="date"
                value={fecha_fin}
                onChange={(e) => setHasta(e.target.value)}
                className="px-4 py-2 rounded-xl border border-gray-300 dark:border-gray-700 bg-[#f5f7fa] dark:bg-gray-950 focus:ring focus:ring-blue-400 outline-none transition"
              />
            </div>
            <button
              onClick={buscarDisponibilidad}
              className="px-8 py-2 rounded-xl font-bold transition duration-300 text-white bg-[#52a173] hover:bg-[#10b655] cursor-pointer"
            >
              Ver disponibilidad
            </button>
          </div>

          {busquedaRealizada && listaHabitaciones.length > 0 ? (
            <div className="mt-4">
              <Grilla
                fecha_inicio={fecha_inicio}
                fecha_fin={fecha_fin}
                habitaciones={listaHabitaciones}
                reservas={estados}
                onConfirmarSeleccion={procesarSeleccionGrilla}
                casoDeUso="OCUPAR"
              />
            </div>
          ) : (
            busquedaRealizada && (
              <p className="text-gray-500">
                No se encontraron habitaciones cargadas en el sistema.
              </p>
            )
          )}
        </div>
      )}

      {paso === "CARGA" && reservaActual && (
        <div className="max-w-6xl mx-auto grid grid-cols-1 xl:grid-cols-3 gap-8">
          <div className="xl:col-span-2 space-y-8">
            <div className="bg-[#f5f7fa] dark:bg-gray-950 p-6 rounded-xl border border-gray-200 shadow-sm">
              <h2 className="text-xl font-bold mb-4 dark:text-white text-black">
                Seleccionar encargado de habitación
              </h2>
              {!encargadoActual ? (
                <BuscadorPersona
                  titulo="Buscar alojados"
                  onBuscar={busqueda}
                  onSeleccionar={setEncargadoActual}
                />
              ) : (
                <div className="flex justify-between items-center dark:text-white  bg-green-400/50 p-4 rounded-lg text-black">
                  <div>
                    <p className="font-bold">
                      {encargadoActual.apellido}, {encargadoActual.nombre}
                    </p>
                    <p className="text-sm">{encargadoActual.nroDoc}</p>
                  </div>
                  <button
                    onClick={() => setEncargadoActual(null)}
                    className="cursor-pointer px-8 py-2 rounded-xl font-bold transition duration-300 border-2 dark:border-white dark:text-white bg-green-400/20 dark:hover:border-[#b92716] text-[#1a252f] border-[#1a252f] hover:bg-[#b92716] hover:text-white hover:border-[#b92716]"
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
              <h2 className="text-xl font-bold mb-4 dark:text-white text-black">
                Ocupantes adicionales
              </h2>
              <BuscadorPersona
                titulo="Buscar alojados"
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
                    ★ {encargadoActual.apellido} (Responsable de hab.)
                  </div>
                )}
                {invitadosActuales.map((inv, i) => (
                  <div
                    key={i}
                    className="flex justify-between text-sm bg-gray-50 p-2 rounded text-black"
                  >
                    {inv.apellido} {inv.nombre}{" "}
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

      {paso === "GUARDANDO" && (
        <div className="fixed inset-0 bg-white/50 dark:bg-white/20 flex items-center justify-center z-50">
          <h2 className="text-2xl font-bold text-black">Guardando...</h2>
        </div>
      )}
      {paso === "EXITO" && (
        <div className="text-center py-20 flex flex-col justify-center items-center">
          <img src="success.svg" alt="" width={90} className="dark:invert" />
          <h2 className="text-3xl font-bold text-green-500 mb-4">
            Check-in exitoso
          </h2>
          <button
            onClick={() => router.push("/")}
            className="bg-gray-9500 dark:text-white dark:border dark:border-white text-black px-6 py-2 font-semibold rounded-xl hover:bg-green-600 hover:border-green-600"
          >
            Volver al inicio
          </button>
        </div>
      )}
    </div>
  );
}
