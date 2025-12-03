"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import {
  pedirHabs,
  buscarEstadoHabitaciones,
  Habitacion,
  DisponibilidadDTO,
} from "../../services/habitaciones.service";
import Grilla from "../../components/grilla";

export default function ReservarHab() {
  const router = useRouter();
  const [nombre, setNombre] = useState("");
  const [apellido, setApellido] = useState("");
  const [telefono, setTelefono] = useState("");
  const [fecha_inicio, setDesde] = useState("");
  const [fecha_fin, setHasta] = useState("");

  const [habitaciones, setHabitaciones] = useState<Habitacion[]>([]);
  const [reservas, setReservas] = useState<DisponibilidadDTO[]>([]);

  const [cargando, setCargando] = useState(false);
  const [busquedaRealizada, setBusquedaRealizada] = useState(false);

  const handleBuscar = async () => {
    console.log("Botón presionado. Fechas:", fecha_inicio, fecha_fin);

    if (!fecha_inicio || !fecha_fin) {
      alert("Por favor selecciona ambas fechas");
      return;
    }

    try {
      setCargando(true);
      setBusquedaRealizada(true);

      console.log("Pidiendo datos al back...");

      const [habsData, reservasData] = await Promise.all([
        pedirHabs(),
        buscarEstadoHabitaciones(fecha_inicio, fecha_fin),
      ]);

      console.log("Datos recibidos:", {
        habs: habsData.length,
        reservas: reservasData.length,
      });

      setHabitaciones(habsData);
      setReservas(reservasData);
    } catch (error) {
      console.error("ERROR en la búsqueda:", error);
      alert("Ocurrió un error al buscar disponibilidad.");
    } finally {
      console.log("Fin de carga");
      setCargando(false);
    }
  };

  const cancelarCasoUso = () => {
    if (confirm("¿Seguro que desea cancelar todo el proceso?")) {
      router.push("/");
    }
  };

  return (
    <div className="dark:bg-gray-950 dark:text-white bg-[#f5f7fa] min-h-screen p-8">
      <h1 className="text-[#141414] dark:text-white  text-5xl font-bold pb-2">
        Reservar habitación
      </h1>
      <p className="dark:text-white mb-8">
        Indique las fechas deseadas para verificar disponibilidad de
        habitaciones
      </p>

      <div className="flex flex-col w-sm mb-4">
        <label className="text-lg">Desde: </label>
        <input
          type="date"
          className="border p-2 rounded-xl text-black dark:text-white mb-2"
          value={fecha_inicio}
          onChange={(e) => setDesde(e.target.value)}
        />
        <label className="text-lg">Hasta: </label>
        <input
          type="date"
          className="border p-2 rounded-xl text-black dark:text-white mb-2"
          value={fecha_fin}
          onChange={(e) => setHasta(e.target.value)}
        />

        <button
          onClick={handleBuscar}
          disabled={cargando}
          className={`px-8 py-2 mt-4 rounded-xl font-bold transition duration-300 text-white 
            ${
              cargando
                ? "bg-gray-400 cursor-not-allowed"
                : "bg-[#52a173] hover:bg-[#10b655] cursor-pointer"
            }`}
        >
          {cargando ? "Buscando..." : "Buscar"}
        </button>
        <button
          onClick={cancelarCasoUso}
          className="cursor-pointer mt-4 px-8 py-2 rounded-xl font-bold transition duration-300 dark:border dark:border-white dark:text-white dark:bg-gray-950 dark:hover:border-[#b92716] text-[#1a252f] border border-[#1a252f] hover:bg-[#b92716] hover:text-white hover:border-[#b92716]"
        >
          Cancelar
        </button>
      </div>

      {busquedaRealizada && (
        <div className="mt-4 border-t pt-4">
          {cargando ? (
            <p className="text-xl animate-pulse">
              Consultando disponibilidad...
            </p>
          ) : habitaciones.length > 0 ? (
            <Grilla
              fecha_inicio={fecha_inicio}
              fecha_fin={fecha_fin}
              habitaciones={habitaciones}
              reservas={reservas}
            />
          ) : (
            <div className="p-4 text-white bg-red-400 rounded">
              No se encontraron habitaciones o hubo un error de conexión.
            </div>
          )}
        </div>
      )}

      {/* inicio datos huesped reserva */}
      <div className="mt-6">
        <h2 className="text-2xl font-bold">Reserva a nombre de</h2>

        <div className="mt-3 flex flex-col gap-2 w-80">
          <input
            type="text"
            placeholder="Apellido"
            value={apellido}
            onChange={(e) => setApellido(e.target.value)}
            className="p-2.5 border border-[#ddd] rounded-xl dark:bg-gray-950 dark:text-white focus:outline-none focus:border-[#9ca9be] focus:ring-2 focus:ring-[#4a6491]/20"
            maxLength={20}
          />
          <input
            type="text"
            placeholder="Nombre"
            value={nombre}
            onChange={(e) => setNombre(e.target.value)}
            className="p-2.5 border border-[#ddd] rounded-xl dark:bg-gray-950 dark:text-white focus:outline-none focus:border-[#9ca9be] focus:ring-2 focus:ring-[#4a6491]/20"
            maxLength={20}
          />
          <input
            type="tel"
            placeholder="Teléfono"
            value={telefono}
            onChange={(e) => setTelefono(e.target.value)}
            className="p-2.5 border border-[#ddd] rounded-xl dark:bg-gray-950 dark:text-white focus:outline-none focus:border-[#9ca9be] focus:ring-2 focus:ring-[#4a6491]/20"
            maxLength={25}
          />

          <button className="cursor-pointer px-8 py-2 rounded-xl font-bold transition duration-300 bg-[#52a173] text-white hover:bg-[#10b655]">
            Confirmar reserva
          </button>
        </div>
      </div>
      {/* fin datos huesped reserva */}
    </div>
  );
}
