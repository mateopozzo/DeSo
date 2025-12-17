"use client";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import {
  AlojadoDTO,
  buscarAlojados,
  PersonaJuridica,
} from "@/services/facturar.service";
import GrillaAlojados, { ResponsablePago } from "@/components/grilla_alojados";
import { pedirHabs, Habitacion } from "../../../services/habitaciones.service";
import GrillaItemsFactura from "@/components/grilla_factura";

export default function Facturar() {
  const router = useRouter();
  const [error, setError] = useState<string | null>(null);
  const [id_hab, setIdHab] = useState("");
  const [hora_checkout, setHora] = useState("");
  const [listaAlojados, setListaAlojados] = useState<AlojadoDTO[]>([]);
  const [listaHabitaciones, setListaHabitaciones] = useState<Habitacion[]>([]);
  const [responsableSeleccionado, setResponsableSeleccionado] = useState<
    AlojadoDTO | PersonaJuridica | null
  >(null);

  const [paso, setPaso] = useState<
    "BUSCAR" | "GRILLA" | "COBRANDO" | "GUARDANDO" | "EXITO"
  >("BUSCAR");

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

  const checkOut = async () => {
    setError(null);
    if (!id_hab || !hora_checkout) {
      setError("Debe completar el número de habitación y la hora de salida.");
      return;
    }
    console.log("Haciendo el checkout");

    const nroHabInput = Number(id_hab);
    // llamada a back para saber quiénes ocupan la hab. resp es una lista de alojadoDTO
    const habitacionEncontrada = listaHabitaciones.find(
      (h) => h.nroHab === nroHabInput
    );

    if (!habitacionEncontrada) {
      setError("El número de habitación es incorrecto o no existe.");
      return;
    }

    if (listaHabitaciones.length == 0) {
      return;
    }
    try {
      const resp = await buscarAlojados(id_hab, hora_checkout);
      // if id hab existe pero lista retorna vacía, entonces no esta ocupada throw error hab no ocupada

      if (!resp || resp.length === 0) {
        setError("La habitación no está ocupada actualmente.");
        return;
      }
      console.log(`Alojados: ${resp}`);

      setListaAlojados(resp);
      setPaso("GRILLA");
    } catch (e) {
      console.error(e);
      setError("Error al traer los alojados.");
    }
  };

  const handleSeleccionResponsable = (alojado: ResponsablePago) => {
    console.log("Responsable seleccionado:", alojado);
    setResponsableSeleccionado(alojado);
    setError(null);
  };

  const avanzarAFacturacion = () => {
    if (!responsableSeleccionado) {
      setError("Debe seleccionar un responsable para continuar.");
      return;
    }

    // if (esMenor(responsableSeleccionado.fechaNacimiento)) {
    //    setError("La persona seleccionada es menor de edad. Por favor elija otra.");
    //    return;
    // }

    setPaso("COBRANDO");
  };

  return (
    <div className="p-6 min-h-screen bg-[#f5f7fa] dark:bg-gray-950 dark:text-white ">
      <h1 className="text-5xl font-bold text-gray-800 dark:text-white mb-2">
        {paso === "BUSCAR" && "Facturar: Check-out"}
        {paso === "GRILLA" && "Responsable de pago"}
        {paso === "COBRANDO" && "Items de la factura"}
        {paso === "GUARDANDO" && "Guardando..."}
        {paso === "EXITO" && "Factura creada con éxito"}
      </h1>
      <p className="dark:text-white mb-8">
        {paso === "BUSCAR" &&
          "Ingrese el número de habitación y el horario de salida para comenzar con la facturación."}
        {paso === "GRILLA" &&
          "Seleccione un responsable de pago entre los huéspedes."}
        {paso === "COBRANDO" &&
          "Seleccione los items que quiera colocar en la factura."}
        {paso === "GUARDANDO" && "Generando la factura. Aguarde."}
        {paso === "EXITO" &&
          "La factura se descargará en breve con el formato elegido."}
      </p>
      {error && (
        <div className="bg-red-600 text-white p-4 rounded-xl mb-6 font-bold shadow-md">
          {error}
        </div>
      )}
      {paso === "BUSCAR" && (
        <div className="flex flex-col">
          <div
            id="caja-campos"
            className="flex flex-row gap-4 bg-[#f5f7fa] dark:bg-gray-950 rounded-2xl shadow-sm mb-8"
          >
            <div>
              <label className="block text-md font-bold mb-2">
                Nro. de habitación
              </label>
              <input
                type="number"
                value={id_hab}
                onChange={(e) => setIdHab(e.target.value)}
                required
                className="px-4 py-2 rounded-xl border border-gray-300 dark:border-gray-700 bg-[#f5f7fa] dark:bg-gray-950 focus:ring focus:ring-blue-400 outline-none transition"
              />
            </div>
            <div>
              <label className="block text-md font-bold mb-2 ">
                Horario de salida
              </label>
              <input
                type="time"
                value={hora_checkout}
                onChange={(e) => setHora(e.target.value)}
                required
                className="px-4 py-2 rounded-xl border border-gray-300 dark:border-gray-700 bg-[#f5f7fa] dark:bg-gray-950 focus:ring focus:ring-blue-400 outline-none transition"
              />
            </div>
          </div>

          <div id="caja-botones" className="flex flex-row gap-4">
            <button
              onClick={() => router.push("/home")}
              className="cursor-pointer px-8 py-2 rounded-xl font-bold transition duration-300 dark:border dark:border-white dark:text-white bg-[#f5f7fa] dark:bg-gray-950  text-[#1a252f] border border-[#1a252f] hover:bg-[#b92716] dark:hover:border-[#b92716] hover:text-white hover:border-[#b92716]"
            >
              Cancelar
            </button>
            <button
              onClick={checkOut}
              className="cursor-pointer px-8 py-2 rounded-xl font-bold bg-[#52a173] text-white hover:bg-[#10b655] shadow-lg hover:shadow-xl transition transform active:scale-95"
            >
              Buscar
            </button>
          </div>
        </div>
      )}

      {paso === "GRILLA" && listaAlojados.length > 0 ? (
        <div className="mt-4">
          <GrillaAlojados
            idHab={id_hab}
            horaCheckout={hora_checkout}
            alojadosDTO={listaAlojados}
            onSeleccionarResponsable={handleSeleccionResponsable}
            onAvanzar={avanzarAFacturacion}
          />
        </div>
      ) : (
        paso === "GRILLA" && (
          <p className="text-gray-500">
            No se encontraron huéspedes para esta habitación.
          </p>
        )
      )}

      {paso === "COBRANDO" && (
        <div className="mt-4">
          <GrillaItemsFactura
            responsableNombre={
              responsableSeleccionado
                ? "razonSoc" in responsableSeleccionado
                  ? responsableSeleccionado.razonSoc
                  : `${responsableSeleccionado.apellido}, ${responsableSeleccionado.nombre}`
                : "Cliente de Prueba"
            }
            onConfirmar={() => {
              console.log("Simulando guardado...");
              setPaso("GUARDANDO");
              setTimeout(() => setPaso("EXITO"), 2000); // Simula delay de red
            }}
            onCancelar={() => setPaso("GRILLA")}
          />
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
            onClick={() => router.push("/home")}
            className="bg-gray-9500 dark:text-white dark:border dark:border-white text-black px-6 py-2 font-semibold rounded-xl hover:bg-green-600 hover:border-green-600"
          >
            Volver al inicio
          </button>
        </div>
      )}
    </div>
  );
}
