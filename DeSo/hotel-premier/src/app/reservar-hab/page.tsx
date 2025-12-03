"use client";
import { useState } from "react";
import {
  pedirHabs,
  buscarEstadoHabitaciones,
  Habitacion,
  DisponibilidadDTO,
} from "../../services/habitaciones.service";
import Grilla from "../../components/grilla";

export default function ReservarHab() {
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
    console.log("1. Botón presionado. Fechas:", fecha_inicio, fecha_fin);

    if (!fecha_inicio || !fecha_fin) {
      alert("Por favor selecciona ambas fechas");
      return;
    }

    try {
      setCargando(true);
      setBusquedaRealizada(true);

      console.log("2. Iniciando peticiones al back...");

      const [habsData, reservasData] = await Promise.all([
        pedirHabs(),
        buscarEstadoHabitaciones(fecha_inicio, fecha_fin),
      ]);

      console.log("3. Datos recibidos:", {
        habs: habsData.length,
        reservas: reservasData.length,
      });

      setHabitaciones(habsData);
      setReservas(reservasData);
    } catch (error) {
      console.error("ERROR en la búsqueda:", error);
      alert("Ocurrió un error al buscar disponibilidad.");
    } finally {
      console.log("4. Finalizando carga");
      setCargando(false);
    }
  };

  return (
    <div className="dark:bg-gray-950 dark:text-white bg-[#f5f7fa] min-h-screen p-8">
      <h1 className="text-[#141414] dark:text-white mb-4 text-5xl font-bold pb-2">
        Reservar habitación
      </h1>

      <div className="flex flex-col w-sm gap-4 mb-4">
        <label className="text-lg">Desde fecha: </label>
        <input
          type="date"
          className="border p-2 rounded-xl text-black dark:text-white"
          value={fecha_inicio}
          onChange={(e) => setDesde(e.target.value)}
        />
        <label className="text-lg">Hasta fecha: </label>
        <input
          type="date"
          className="border p-2 rounded-xl text-black dark:text-white"
          value={fecha_fin}
          onChange={(e) => setHasta(e.target.value)}
        />

        <button
          onClick={handleBuscar}
          disabled={cargando}
          className={`px-8 py-2 rounded-xl font-bold transition duration-300 text-white 
            ${
              cargando
                ? "bg-gray-400 cursor-not-allowed"
                : "bg-[#52a173] hover:bg-[#10b655] cursor-pointer"
            }`}
        >
          {cargando ? "Buscando..." : "Buscar"}
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
