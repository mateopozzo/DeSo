"use client";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import {
  pedirHabs,
  buscarEstadoHabitaciones,
  crearReserva,
  Habitacion,
  DisponibilidadDTO,
  RequestReserva,
} from "../../../services/habitaciones.service";
import Grilla, {
  DatosSeleccion,
} from "../../../components/grilla_habitaciones";

export default function ReservarHab() {
  const router = useRouter();

  const [paso, setPaso] = useState<
    "GRILLA" | "FORMULARIO" | "GUARDANDO" | "EXITO"
  >("GRILLA");
  const [error, setError] = useState<string | null>(null);

  const [fecha_inicio_busq, setDesdeBusq] = useState("");
  const [fecha_fin_busq, setHastaBusq] = useState("");
  const [listaHabitaciones, setListaHabitaciones] = useState<Habitacion[]>([]);
  const [estadosReservas, setEstadosReservas] = useState<DisponibilidadDTO[]>(
    []
  );
  const [busquedaRealizada, setBusquedaRealizada] = useState(false);

  const [seleccion, setSeleccion] = useState<{
    idHabitacion: number;
    fechaInicio: string;
    fechaFin: string;
  } | null>(null);

  const [nombre, setNombre] = useState("");
  const [apellido, setApellido] = useState("");
  const [telefono, setTelefono] = useState("");

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
        setError("Error al conectar con el servidor de habitaciones.");
      }
    };
    cargarHabitaciones();
  }, []);

  const buscarDisponibilidad = async () => {
    if (!fecha_inicio_busq || !fecha_fin_busq) {
      setError("Por favor, ingrese fechas válidas.");
      return;
    }

    if (fecha_inicio_busq > fecha_fin_busq) {
      setError("La fecha inicial no puede ser mayor a la final");
      return;
    }

    if (fecha_inicio_busq < hoy_fecha) {
      setError("Las fechas no pueden ser anteriores al día actual");
      return;
    }

    setError(null);
    try {
      setBusquedaRealizada(true);
      const resp = await buscarEstadoHabitaciones(
        fecha_inicio_busq,
        fecha_fin_busq
      );
      setEstadosReservas(resp);
    } catch (e) {
      console.error(e);
      setError("Ocurrió un error al buscar la disponibilidad.");
    }
  };

  const procesarSeleccionGrilla = (datos: DatosSeleccion) => {
    console.log("Habitación seleccionada:", datos);
    setSeleccion(datos);
    setPaso("FORMULARIO");
    setError(null);
  };

  const confirmarReservaFinal = async () => {
    if (!nombre.trim() || !apellido.trim() || !telefono.trim()) {
      alert("Por favor complete nombre, apellido y teléfono.");
      return;
    }
    if (!seleccion) return;

    setPaso("GUARDANDO");

    try {
      const payload: RequestReserva = {
        reservaDTO: {
          fecha_inicio: seleccion.fechaInicio,
          fecha_fin: seleccion.fechaFin,
          nombre: nombre,
          apellido: apellido,
          telefono: telefono,
          estado: "Reservada",
        },
        listaIDHabitaciones: [String(seleccion.idHabitacion)],
      };

      console.log("Enviando payload:", JSON.stringify(payload));

      const status = await crearReserva(payload);

      if (status === 201 || status === 200) {
        setPaso("EXITO");
      } else {
        setError(`El servidor respondió con error: ${status}`);
        setPaso("FORMULARIO");
      }
    } catch (err) {
      console.error(err);
      setError("Error de conexión al crear la reserva.");
      setPaso("FORMULARIO");
    }
  };

  const cancelar = () => {
    setNombre("");
    setApellido("");
    setTelefono("");
    setSeleccion(null);
    setPaso("GRILLA");
  };

  const cancelarCasoUso = () => {
    if (confirm("¿Desea cancelar todo el proceso?")) {
      router.push("/home");
    }
  };

  return (
    <div className="p-6 min-h-screen bg-[#f5f7fa] dark:bg-gray-950 dark:text-white">
      <h1 className=" dark:text-white text-5xl font-bold pb-2">
        Reservar habitación
      </h1>
      <p className="dark:text-white mb-8">
        Indique las fechas deseadas para verificar disponibilidad de
        habitaciones
      </p>
      <hr className="mb-8 dark:text-white/50 text-black/50" />
      <div className="flex justify-between items-center mb-4">
        <h1 className="text-2xl font-bold text-gray-800 dark:text-white">
          {paso === "GRILLA" && "Verificar disponibilidad de habitaciones"}
          {paso === "FORMULARIO" && "Ingresar los datos del huésped"}
          {paso === "GUARDANDO" && "Guardando..."}
          {paso === "EXITO" && "Reserva exitosa"}
        </h1>
        <button
          onClick={() => router.push("/home")}
          className="cursor-pointer px-8 py-2 rounded-xl font-bold transition duration-300 dark:border dark:border-white dark:text-white bg-[#f5f7fa] dark:bg-gray-950  text-[#1a252f] border border-[#1a252f] hover:bg-[#b92716] dark:hover:border-[#b92716] hover:text-white hover:border-[#b92716]"
        >
          Cancelar
        </button>
      </div>

      {error && (
        <div className="bg-red-600 text-white p-4 rounded-xl mb-6 font-bold shadow-md">
          {error}
        </div>
      )}
      {paso === "GRILLA" && (
        <div className="space-y-6 animate-fade-in">
          <div className="flex flex-wrap gap-4 items-end bg-[#f5f7fa] dark:bg-gray-950 p-6 rounded-2xl shadow-sm ">
            <div>
              <label className="block text-md font-bold mb-2">Desde</label>
              <input
                type="date"
                value={fecha_inicio_busq}
                onChange={(e) => setDesdeBusq(e.target.value)}
                className="px-4 py-2 rounded-xl border border-gray-300 dark:border-gray-700 bg-[#f5f7fa] dark:bg-gray-950 focus:ring focus:ring-blue-400 outline-none transition"
              />
            </div>
            <div>
              <label className="block text-md font-bold mb-2">Hasta</label>
              <input
                type="date"
                value={fecha_fin_busq}
                onChange={(e) => setHastaBusq(e.target.value)}
                className="px-4 py-2 rounded-xl border border-gray-300 dark:border-gray-700 bg-[#f5f7fa] dark:bg-gray-950 focus:ring focus:ring-blue-400 outline-none transition"
              />
            </div>
            <button
              onClick={buscarDisponibilidad}
              className="px-8 py-2 rounded-xl font-bold bg-[#52a173] text-white hover:bg-[#10b655] shadow-lg hover:shadow-xl transition transform active:scale-95"
            >
              Ver disponibilidad
            </button>
          </div>

          {busquedaRealizada && listaHabitaciones.length > 0 ? (
            <div className="mt-6 shadow-xl rounded-2xl overflow-hidden border border-gray-200 dark:border-gray-800">
              <Grilla
                fecha_inicio={fecha_inicio_busq}
                fecha_fin={fecha_fin_busq}
                habitaciones={listaHabitaciones}
                reservas={estadosReservas}
                onConfirmarSeleccion={procesarSeleccionGrilla}
                casoDeUso="RESERVAR"
              />
            </div>
          ) : (
            busquedaRealizada && (
              <div className="text-center py-10 text-gray-400 italic bg-[#f5f7fa] dark:bg-gray-950 rounded-xl border border-dashed">
                No se encontraron habitaciones disponibles para mostrar.
              </div>
            )
          )}
        </div>
      )}

      {paso === "FORMULARIO" && seleccion && (
        <div className="max-w-2xl mx-auto animate-fade-in mt-8">
          <div className="bg-[#f5f7fa] dark:bg-gray-950 border-2 dark:border-gray-700 border-gray-300 rounded-3xl shadow-2xl overflow-hidden">
            <div className="bg-[#f5f7fa] dark:bg-gray-950 p-6 text-white">
              <h2 className="text-2xl font-bold dark:text-white text-black">
                Completar datos
              </h2>
              <p className="opacity-90 mt-2 text-sm flex gap-4">
                <span className="dark:bg-blue-400/70 bg-blue-700/80 px-2 py-1 rounded">
                  Habitación <strong>{seleccion.idHabitacion}</strong>
                </span>
                <span className="dark:bg-blue-400/70 bg-blue-700/80 px-2 py-1 rounded">
                  Del <strong>{seleccion.fechaInicio}</strong> al{" "}
                  <strong>{seleccion.fechaFin}</strong>
                </span>
              </p>
            </div>

            <div className="p-8 space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label className="block text-sm font-bold mb-2 text-gray-700 dark:text-gray-300">
                    Nombre
                  </label>
                  <input
                    type="text"
                    placeholder="Ej: Juan"
                    value={nombre}
                    onChange={(e) => setNombre(e.target.value)}
                    pattern="[a-z][A-Z]"
                    className="w-full px-4 py-3 rounded-xl border border-gray-300 dark:border-gray-700 bg-[#f5f7fa] dark:bg-gray-950 focus:ring-2 focus:ring-[#52a173] outline-none transition"
                  />
                </div>
                <div>
                  <label className="block text-sm font-bold mb-2 text-gray-700 dark:text-gray-300">
                    Apellido
                  </label>
                  <input
                    type="text"
                    placeholder="Ej: Pérez"
                    value={apellido}
                    onChange={(e) => setApellido(e.target.value)}
                    className="w-full px-4 py-3 rounded-xl border border-gray-300 dark:border-gray-700 bg-[#f5f7fa] dark:bg-gray-950 focus:ring-2 focus:ring-[#52a173] outline-none transition"
                  />
                </div>
              </div>

              <div>
                <label className="block text-sm font-bold mb-2 text-gray-700 dark:text-gray-300">
                  Teléfono
                </label>
                <input
                  type="tel"
                  placeholder="Ej: 5491112345678"
                  value={telefono}
                  onChange={(e) => setTelefono(e.target.value)}
                  className="w-full px-4 py-3 rounded-xl border border-gray-300 dark:border-gray-700 bg-[#f5f7fa] dark:bg-gray-950 focus:ring-2 focus:ring-[#52a173] outline-none transition"
                />
              </div>

              <div className="pt-6 flex gap-4">
                <button
                  onClick={cancelar}
                  className="hover:bg-[#b92716] border dark:hover:border-[#b92716] hover:text-white hover:border-[#b92716] flex-1 py-3 rounded-xl font-bold  text-gray-600 dark:text-gray-300  transition"
                >
                  Volver
                </button>
                <button
                  onClick={confirmarReservaFinal}
                  className="flex-2 py-3 rounded-xl font-bold bg-[#52a173] text-white hover:bg-[#10b655] shadow-lg hover:shadow-green-500/30 transition transform active:scale-95"
                >
                  Confirmar reserva
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {paso === "GUARDANDO" && (
        <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50 animate-fade-in">
          <div className="bg-[#f5f7fa] dark:bg-gray-950 p-8 rounded-2xl shadow-2xl flex flex-col items-center">
            <div className="animate-spin rounded-full h-12 w-12 border-4 border-[#52a173] border-t-transparent mb-4"></div>
            <h2 className="text-xl font-bold text-gray-800 dark:text-white">
              Procesando reserva...
            </h2>
          </div>
        </div>
      )}

      {paso === "EXITO" && (
        <div className="flex flex-col items-center justify-center py-20 animate-scale-in">
          <div className="bg-green-100 text-green-600 p-6 rounded-full text-6xl mb-6 shadow-inner">
            ✓
          </div>
          <h2 className="text-4xl font-bold text-gray-800 dark:text-white mb-4">
            Reserva creada con éxito
          </h2>
          <div className="flex gap-4">
            <button
              onClick={() => {
                setPaso("GRILLA");
                setNombre("");
                setApellido("");
                setTelefono("");
                setSeleccion(null);
                buscarDisponibilidad();
              }}
              className="px-8 py-3 rounded-xl font-bold bg-[#f5f7fa] dark:bg-gray-950 text-white hover:bg-black shadow-lg transition"
            >
              Hacer otra reserva
            </button>
            <button
              onClick={() => router.push("/home")}
              className="px-8 py-3 rounded-xl font-bold border border-gray-300 hover:bg-gray-100 dark:border-gray-700 dark:hover:bg-gray-800 transition"
            >
              Ir al inicio
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
