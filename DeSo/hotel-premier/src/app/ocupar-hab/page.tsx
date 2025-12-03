"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import GrillaHabitaciones from "@/components/estado_hab";
import BuscadorPersona from "@/components/buscador_persona";
import { buscarEstadoHabitaciones, DisponibilidadDTO } from "@/services/habitaciones.service";
import { busqueda, ResultadoBusq } from "@/services/busqueda.service";
import { buscarAlojados, crearEstadia, EstadiaDTO } from "@/services/estadia.service";

interface ReservaCola {
  idhab: number;
  fechaInicio: string;
  fechaFin: string;
}

export default function OcuparHabPage() {
  const router = useRouter();

  // ESTADOS DEL FLUJO
  const [paso, setPaso] = useState<"GRILLA" | "CARGA" | "GUARDANDO" | "EXITO">("GRILLA");
  const [error, setError] = useState<string | null>(null);

  // DATOS
  const [desde, setDesde] = useState("");
  const [hasta, setHasta] = useState("");
  const [estados, setEstados] = useState<DisponibilidadDTO[]>([]);
  const [seleccionConfirmada, setSeleccionConfirmada] = useState<{ idhab: number | string; fecha: string }[]>([]);

  // CARGA SECUENCIAL
  const [colaReservas, setColaReservas] = useState<ReservaCola[]>([]);
  const [indiceActual, setIndiceActual] = useState(0);
  const [estadiasListas, setEstadiasListas] = useState<EstadiaDTO[]>([]);

  // DATOS HABITACIÓN ACTUAL
  const [encargadoActual, setEncargadoActual] = useState<ResultadoBusq | null>(null);
  const [invitadosActuales, setInvitadosActuales] = useState<ResultadoBusq[]>([]);

  // --- ACCIONES GENERALES ---

  const cancelarCasoUso = () => {
    if (confirm("¿Seguro que desea cancelar todo el proceso? Se perderán los datos no guardados.")) {
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

  // --- LÓGICA DE NEGOCIO ---

  const buscarDisponibilidad = async () => {
    if (!desde || !hasta || desde > hasta) {
      setError("Fechas inválidas o incompletas."); return;
    }
    setError(null);
    try {
      const resp = await buscarEstadoHabitaciones(desde, hasta);
      setEstados(resp);
    } catch (e) {
      console.error(e);
      setError("Error al traer estados de habitaciones.");
    }
  };

  const iniciarCargaHuespedes = () => {
    if (seleccionConfirmada.length === 0) {
      setError("Debe seleccionar y confirmar días en la grilla."); return;
    }
    // Agrupar selección
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
        fechaInicio: fechas[0],
        fechaFin: fechas[fechas.length - 1],
      });
    });

    setColaReservas(colaProcesada);
    setPaso("CARGA");
    setIndiceActual(0);
    setError(null);
  };

  const confirmarHabitacionYContinuar = () => {
    if (!encargadoActual) {
      alert("Falta asignar el Encargado (Responsable de pago) de la habitación.");
      return;
    }

    const reserva = colaReservas[indiceActual];

    // LOGICA CORREGIDA:
    // El encargado va en 'encargado'.
    // Los 'invitadosActuales' son los ocupantes.
    // NO mezclamos al encargado en la lista de invitados automáticamente.
    const nuevaEstadia: EstadiaDTO = {
      idHabitacion: reserva.idhab,
      fechaInicio: reserva.fechaInicio,
      fechaFin: reserva.fechaFin,
      encargado: { ...encargadoActual },
      listaInvitados: invitadosActuales.map(inv => ({ ...inv }))
    };

    const nuevasListas = [...estadiasListas, nuevaEstadia];
    setEstadiasListas(nuevasListas);

    // Limpieza para la siguiente habitación
    setEncargadoActual(null);
    setInvitadosActuales([]);

    // Loop: ¿Quedan habitaciones?
    if (indiceActual < colaReservas.length - 1) {
      setIndiceActual(indiceActual + 1); // Siguiente habitación
    } else {
      persistirTodo(nuevasListas); // Fin del flujo de carga -> Guardar
    }
  };

  const persistirTodo = async (listaFinal: EstadiaDTO[]) => {
    setPaso("GUARDANDO");
    try {
      for (const estadia of listaFinal) {
        await crearEstadia(estadia);
      }
      // AL FINALIZAR CORRECTAMENTE:
      setPaso("EXITO");
    } catch (err: any) {
      console.error(err);
      setError("Error guardando reservas: " + err.message);
      setPaso("CARGA"); // Volver atrás si falla
    }
  };

  const reservaActual = colaReservas[indiceActual];

  return (
      <div className="p-6 min-h-screen dark:bg-gray-950 dark:text-white font-sans">

        {/* HEADER CON BOTÓN CANCELAR */}
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-3xl font-bold text-gray-800 dark:text-white">
            {paso === "GRILLA" && "Paso 1: Disponibilidad"}
            {paso === "CARGA" && "Paso 2: Asignación de Ocupantes"}
            {paso === "GUARDANDO" && "Procesando..."}
            {paso === "EXITO" && "Operación Exitosa"}
          </h1>
          {paso !== "EXITO" && paso !== "GUARDANDO" && (
              <button
                  onClick={cancelarCasoUso}
                  className="px-4 py-2 border border-red-300 text-red-600 rounded-lg hover:bg-red-50 dark:hover:bg-red-900/20 dark:border-red-800 transition-colors"
              >
                Cancelar Operación
              </button>
          )}
        </div>

        {error && (
            <div className="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-4 rounded shadow-sm">
              <p className="font-bold">Error</p>
              <p>{error}</p>
            </div>
        )}

        {/* --- VISTA 1: GRILLA --- */}
        {paso === "GRILLA" && (
            <div className="animate-fade-in space-y-6">
              <div className="flex flex-wrap gap-4 items-end bg-gray-50 dark:bg-gray-900 p-6 rounded-xl shadow-sm border border-gray-200 dark:border-gray-800">
                <div>
                  <label className="block text-sm font-bold mb-2 text-gray-700 dark:text-gray-300">Desde</label>
                  <input
                      type="date" value={desde} onChange={e => setDesde(e.target.value)}
                      className="border border-gray-300 p-2.5 rounded-lg bg-white text-gray-900 focus:ring-2 focus:ring-blue-500 outline-none"
                  />
                </div>
                <div>
                  <label className="block text-sm font-bold mb-2 text-gray-700 dark:text-gray-300">Hasta</label>
                  <input
                      type="date" value={hasta} onChange={e => setHasta(e.target.value)}
                      className="border border-gray-300 p-2.5 rounded-lg bg-white text-gray-900 focus:ring-2 focus:ring-blue-500 outline-none"
                  />
                </div>
                <button
                    onClick={buscarDisponibilidad}
                    className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2.5 rounded-lg font-bold shadow"
                >
                  Ver Disponibilidad
                </button>
              </div>

              {estados.length > 0 && (
                  <>
                    <div className="border rounded-xl overflow-hidden shadow-sm">
                      <GrillaHabitaciones
                          desde={desde} hasta={hasta}
                          estados={estados.map(x => ({ idhab: x.idHabitacion, fecha: x.fechaDesde, estado: x.estado as any }))}
                          seleccionDias={setSeleccionConfirmada}
                      />
                    </div>
                    <div className="flex justify-end pt-4">
                      <button
                          onClick={iniciarCargaHuespedes}
                          disabled={seleccionConfirmada.length === 0}
                          className={`px-8 py-3 rounded-xl font-bold text-lg shadow-lg transition-all ${
                              seleccionConfirmada.length > 0 ? "bg-green-600 text-white hover:bg-green-700 hover:-translate-y-1" : "bg-gray-300 text-gray-500 cursor-not-allowed"
                          }`}
                      >
                        Continuar con la Carga ({new Set(seleccionConfirmada.map(s => s.idhab)).size} habs) →
                      </button>
                    </div>
                  </>
              )}
            </div>
        )}

        {/* --- VISTA 2: CARGA LOOP --- */}
        {paso === "CARGA" && reservaActual && (
            <div className="max-w-6xl mx-auto animate-fade-in-up grid grid-cols-1 xl:grid-cols-3 gap-8">

              {/* Panel Izquierdo */}
              <div className="xl:col-span-2 space-y-8">
                {/* 1. ENCARGADO */}
                <div className="bg-white dark:bg-gray-900 p-6 rounded-xl border border-gray-200 dark:border-gray-800 shadow-sm">
                  <h2 className="text-xl font-bold mb-4 text-blue-600 flex items-center gap-2">
                    <span className="bg-blue-100 text-blue-800 w-8 h-8 flex items-center justify-center rounded-full text-sm">1</span>
                    Seleccionar Encargado (Responsable)
                  </h2>

                  {!encargadoActual ? (
                      <BuscadorPersona
                          titulo="Buscar Persona Responsable"
                          onBuscar={busqueda}
                          onSeleccionar={setEncargadoActual}
                      />
                  ) : (
                      <div className="flex justify-between items-center bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-800 p-4 rounded-lg">
                        <div>
                          <p className="font-bold text-lg">{encargadoActual.apellido}, {encargadoActual.nombre}</p>
                          <p className="text-gray-600 dark:text-gray-400">{encargadoActual.tipoDoc} {encargadoActual.nroDoc}</p>
                        </div>
                        <button onClick={() => setEncargadoActual(null)} className="text-red-500 underline font-semibold text-sm">Cambiar</button>
                      </div>
                  )}
                </div>

                {/* 2. OCUPANTES */}
                <div className={`bg-white dark:bg-gray-900 p-6 rounded-xl border border-gray-200 dark:border-gray-800 shadow-sm transition-opacity ${!encargadoActual ? 'opacity-50 pointer-events-none' : ''}`}>
                  <h2 className="text-xl font-bold mb-4 text-purple-600 flex items-center gap-2">
                    <span className="bg-purple-100 text-purple-800 w-8 h-8 flex items-center justify-center rounded-full text-sm">2</span>
                    Agregar Ocupantes de la Habitación
                  </h2>
                  <p className="text-sm text-gray-500 mb-4">
                    Busque y seleccione a las personas que se alojarán en esta habitación.
                    <br/>(Nota: Si el Encargado también se aloja, debe agregarlo aquí también).
                  </p>

                  <BuscadorPersona
                      titulo="Buscar Ocupante"
                      onBuscar={buscarAlojados}
                      onSeleccionar={(p) => setInvitadosActuales([...invitadosActuales, p])}
                      // Excluimos a los que YA están en la lista de invitados para no duplicar
                      excluidos={invitadosActuales}
                  />
                </div>
              </div>

              {/* Panel Derecho Sticky: Resumen */}
              <div className="xl:col-span-1">
                <div className="sticky top-6 bg-white dark:bg-gray-900 border border-gray-200 dark:border-gray-800 rounded-xl p-6 shadow-xl">
                  <div className="mb-6 border-b pb-4">
                    <div className="flex justify-between items-center mb-2">
                      <span className="text-sm font-bold text-gray-500 uppercase">Editando</span>
                      <span className="bg-gray-100 dark:bg-gray-800 text-xs px-2 py-1 rounded-full font-bold">
                                {indiceActual + 1} / {colaReservas.length}
                            </span>
                    </div>
                    <h3 className="text-3xl font-bold text-gray-800 dark:text-white">Habitación {reservaActual.idhab}</h3>
                    <p className="text-gray-500 mt-1 text-sm">
                      {reservaActual.fechaInicio} <span className="text-gray-300">➔</span> {reservaActual.fechaFin}
                    </p>
                  </div>

                  <div className="space-y-4 mb-8">
                    <div>
                      <p className="text-xs font-bold text-gray-400 uppercase mb-1">Responsable</p>
                      {encargadoActual ? (
                          <div className="p-2 bg-blue-50 dark:bg-blue-900/10 rounded border border-blue-100 dark:border-blue-900 text-sm font-medium text-blue-800 dark:text-blue-200">
                            {encargadoActual.apellido}, {encargadoActual.nombre}
                          </div>
                      ) : (
                          <div className="text-sm text-red-400 italic">Selección obligatoria</div>
                      )}
                    </div>

                    <div>
                      <p className="text-xs font-bold text-gray-400 uppercase mb-1">
                        Ocupantes ({invitadosActuales.length})
                      </p>
                      {invitadosActuales.length === 0 ? (
                          <p className="text-sm text-gray-400 italic p-2 border border-dashed rounded text-center">Sin ocupantes cargados</p>
                      ) : (
                          <ul className="space-y-2">
                            {invitadosActuales.map((inv, idx) => (
                                <li key={idx} className="flex justify-between items-center text-sm p-2 bg-gray-50 dark:bg-gray-800 rounded border border-gray-100 dark:border-gray-700">
                                  <span>{inv.apellido}, {inv.nombre}</span>
                                  <button onClick={() => setInvitadosActuales(invitadosActuales.filter((_, i) => i !== idx))} className="text-red-500 hover:text-red-700 px-2 font-bold">✕</button>
                                </li>
                            ))}
                          </ul>
                      )}
                    </div>
                  </div>

                  <button
                      onClick={confirmarHabitacionYContinuar}
                      disabled={!encargadoActual}
                      className={`w-full py-4 rounded-xl font-bold text-white shadow-md transition-all ${
                          encargadoActual
                              ? "bg-green-600 hover:bg-green-700 hover:-translate-y-1"
                              : "bg-gray-300 dark:bg-gray-700 cursor-not-allowed"
                      }`}
                  >
                    {indiceActual < colaReservas.length - 1 ? "Siguiente Habitación →" : "Finalizar y Guardar Todo 💾"}
                  </button>
                </div>
              </div>
            </div>
        )}

        {/* --- VISTA 3: PANTALLA CARGANDO --- */}
        {paso === "GUARDANDO" && (
            <div className="fixed inset-0 bg-white/90 dark:bg-black/90 flex flex-col items-center justify-center z-50">
              <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-b-4 border-blue-600 mb-6"></div>
              <h2 className="text-2xl font-bold">Guardando Reservas...</h2>
            </div>
        )}

        {/* --- VISTA 4: EXITO (MENÚ FINAL) --- */}
        {paso === "EXITO" && (
            <div className="flex flex-col items-center justify-center py-20 animate-fade-in-up">
              <div className="bg-green-100 text-green-600 w-24 h-24 rounded-full flex items-center justify-center text-5xl mb-6 shadow-sm">
                ✓
              </div>
              <h2 className="text-4xl font-bold text-gray-800 dark:text-white mb-2">¡Carga Exitosa!</h2>
              <p className="text-gray-500 text-lg mb-10">Todas las habitaciones y sus ocupantes han sido registrados correctamente.</p>

              <div className="grid grid-cols-1 md:grid-cols-3 gap-4 w-full max-w-4xl">
                {/* OPCIÓN 1: SALIR */}
                <button
                    onClick={() => router.push("/")}
                    className="p-6 border-2 border-gray-200 dark:border-gray-800 rounded-2xl hover:border-red-400 hover:bg-red-50 dark:hover:bg-red-900/10 transition-all group"
                >
                  <span className="block text-3xl mb-2 group-hover:scale-110 transition-transform">🚪</span>
                  <span className="font-bold text-gray-700 dark:text-gray-200">Salir</span>
                  <p className="text-sm text-gray-400 mt-2">Volver al inicio del sistema</p>
                </button>

                {/* OPCIÓN 2: SEGUIR CARGANDO (Reinicia para misma tarea) */}
                <button
                    onClick={resetearFormulario}
                    className="p-6 border-2 border-gray-200 dark:border-gray-800 rounded-2xl hover:border-blue-400 hover:bg-blue-50 dark:hover:bg-blue-900/10 transition-all group"
                >
                  <span className="block text-3xl mb-2 group-hover:scale-110 transition-transform">↺</span>
                  <span className="font-bold text-gray-700 dark:text-gray-200">Seguir Cargando</span>
                  <p className="text-sm text-gray-400 mt-2">Volver a procesar ocupantes (Reinicia)</p>
                </button>

                {/* OPCIÓN 3: CARGAR OTRA (Reinicia Todo) */}
                <button
                    onClick={resetearFormulario}
                    className="p-6 border-2 border-gray-200 dark:border-gray-800 rounded-2xl hover:border-green-400 hover:bg-green-50 dark:hover:bg-green-900/10 transition-all group"
                >
                  <span className="block text-3xl mb-2 group-hover:scale-110 transition-transform">📅</span>
                  <span className="font-bold text-gray-700 dark:text-gray-200">Cargar Otra Habitación</span>
                  <p className="text-sm text-gray-400 mt-2">Nueva fecha y habitaciones</p>
                </button>
              </div>
            </div>
        )}
      </div>
  );
}