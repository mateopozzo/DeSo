"use client";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { CriteriosBusq, AlojadoDTO } from "@/types/facturacion";
import {
  buscarAlojados,
  obtenerDetalleFacturacion,
  generarFacturaFinal,
  verificarEstadia,
  obtenerDatosHuesped,
  descargarFacturaPDF,
} from "@/services/facturar.service";
import { DetalleFacturaDTO } from "@/types/facturacion";
import GrillaAlojados, { ResponsablePago } from "@/components/grilla_alojados";
import { pedirHabs, Habitacion } from "../../../services/habitaciones.service";
import GrillaItemsFactura from "@/components/grilla_factura";

// revisar de donde sacar que es mayor
const esMayorDeEdad = (fechaNacStr: string): boolean => {
  if (!fechaNacStr) return false;
  const nacimiento = new Date(fechaNacStr);
  const hoy = new Date();
  let edad = hoy.getFullYear() - nacimiento.getFullYear();
  const m = hoy.getMonth() - nacimiento.getMonth();
  if (m < 0 || (m === 0 && hoy.getDate() < nacimiento.getDate())) {
    edad--;
  }
  return edad >= 18;
};

export default function Facturar() {
  const router = useRouter();
  const [error, setError] = useState<string | null>(null);
  const [id_hab, setIdHab] = useState("");
  const [id_est, setIdEst] = useState("");
  const [hora_checkout, setHora] = useState("");

  const [listaHabitaciones, setListaHabitaciones] = useState<Habitacion[]>([]);
  const [listaAlojados, setListaAlojados] = useState<CriteriosBusq[]>([]);
  const [facturaGenerada, setFacturaGenerada] = useState<any>(null);
  const [responsableSeleccionado, setResponsableSeleccionado] = useState<
    ResponsablePago | AlojadoDTO | null
  >(null);
  const [detalleFactura, setDetalleFactura] =
    useState<DetalleFacturaDTO | null>(null);

  let estadia_existe = false;

  const [paso, setPaso] = useState<
    | "BUSCAR"
    | "GRILLA"
    | "CARGANDO_CONSUMOS"
    | "COBRANDO"
    | "GUARDANDO"
    | "EXITO"
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

    console.log("Buscando habitaciones existentes");

    const nroHabInput = Number(id_hab);
    // llamada a back para saber quiénes ocupan la hab. resp es una lista de CriteriosBusq
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

    // LLAMADA A BACK: verificar estadia. devuelve ESTADIA DTO
    console.log("Verificando que exista la estadía");
    try {
      const response = await verificarEstadia(id_hab, hora_checkout);
      if (!response) {
        setError("La habitación no está ocupada actualmente.");
        return;
      }

      if (!response) {
        estadia_existe = false;
      } else {
        estadia_existe = true;
        console.log(`Response ${response}`);

        const idEstadia = response.idEstadia.toString();
        setIdEst(idEstadia);

        const resp = await buscarAlojados(idEstadia);
        if (!resp || resp.length === 0) {
          setError("La habitación no está ocupada actualmente.");
          return;
        }
        console.log(`Alojados: ${resp}`);

        setListaAlojados(resp);
        setPaso("GRILLA");
      }
    } catch (err) {
      console.error(err);
      setError("Error al traer los alojados.");
    }
  };

  const cargarConsumos = async () => {
    setPaso("CARGANDO_CONSUMOS");
    try {
      const detalle = await obtenerDetalleFacturacion(id_hab, hora_checkout);
      setDetalleFactura(detalle);
      setPaso("COBRANDO");
    } catch (err) {
      console.error(err);
      setError(
        "Error al obtener los consumos de la habitación. Intente nuevamente."
      );
      setPaso("GRILLA");
    }
  };

  const handleSeleccionResponsable = (
    alojado: ResponsablePago | AlojadoDTO
  ) => {
    console.log("Responsable seleccionado:", alojado);
    setResponsableSeleccionado(alojado);
    setError(null);
  };

  const avanzarAFacturacion = async () => {
    if (!responsableSeleccionado) {
      setError("Debe seleccionar un responsable.");
      return;
    }

    if ("razonSoc" in responsableSeleccionado) {
      setPaso("CARGANDO_CONSUMOS");
      cargarConsumos();
      return;
    }

    try {
      const huespedCompleto = await obtenerDatosHuesped(
        responsableSeleccionado.nroDoc,
        responsableSeleccionado.tipoDoc || "DNI"
      );

      if (!esMayorDeEdad(huespedCompleto.fechanac)) {
        setError(
          `El huésped ${responsableSeleccionado.nombre} no es mayor de edad. Por favor, seleccione otro.`
        );
        return;
      }

      setResponsableSeleccionado(huespedCompleto);

      cargarConsumos();
    } catch (err) {
      console.error(err);
      setError("Error al verificar los datos del huésped");
    }
  };

  const confirmarFactura = async (datosCobro: {
    cobrarEstadia: boolean;
    idsServicios: number[];
  }) => {
    if (!detalleFactura || !responsableSeleccionado) return;

    if (!responsableSeleccionado.cuit) {
      setError(
        "El responsable seleccionado no tiene CUIT cargado en el sistema."
      );
      return;
    }
    setPaso("GUARDANDO");

    const destinatarioNombre =
      "razonSoc" in responsableSeleccionado
        ? responsableSeleccionado.razonSoc
        : `${responsableSeleccionado.apellido}, ${responsableSeleccionado.nombre}`;

    const payload = {
      idEstadia: detalleFactura.idEstadia,
      tipoFactura: detalleFactura.tipoFacturaSugerida,
      destinatario: destinatarioNombre,
      cobrarAlojamiento: datosCobro.cobrarEstadia,
      idsConsumosAIncluir: datosCobro.idsServicios,
      responsableTipo:
        "razonSoc" in responsableSeleccionado ? "TERCERO" : "HUESPED",
      responsableId: responsableSeleccionado.cuit,
    };

    console.log(payload);

    try {
      const factura = await generarFacturaFinal(payload as any);
      setFacturaGenerada(factura);
      await descargarPDF(factura);
      setPaso("EXITO");
    } catch (err) {
      setError("Error al generar la factura.");
      setPaso("COBRANDO");
    }
  };

  const descargarPDF = async (factura: any) => {
    const blob = await descargarFacturaPDF(factura);

    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = `factura_${factura.num_factura}.pdf`;
    document.body.appendChild(a);
    a.click();
    a.remove();
    window.URL.revokeObjectURL(url);
  };

  return (
    <div className="p-6 min-h-screen bg-[#f5f7fa] dark:bg-gray-950 dark:text-white ">
      <h1 className="text-5xl font-bold text-gray-800 dark:text-white mb-2">
        {paso === "BUSCAR" && "Facturar: Check-out"}
        {paso === "GRILLA" && "Responsable de pago"}
        {paso === "CARGANDO_CONSUMOS" && "Obteniendo consumos..."}
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
      {/* errores */}
      {error && (
        <div className="bg-red-600 text-white p-4 rounded-xl mb-6 font-bold shadow-md">
          {error}
        </div>
      )}
      {/* buscar */}
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
      {/* seleccionar resp */}
      {paso === "GRILLA" && (
        <GrillaAlojados
          idHab={id_hab}
          horaCheckout={hora_checkout}
          alojadosDTO={listaAlojados}
          onSeleccionarResponsable={handleSeleccionResponsable}
          onAvanzar={avanzarAFacturacion}
        />
      )}
      {/* esperar detalles */}
      {paso === "CARGANDO_CONSUMOS" && (
        <div className="text-center py-10">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Calculando estadía. Aguarde</p>
        </div>
      )}

      {paso === "COBRANDO" && detalleFactura && responsableSeleccionado && (
        <GrillaItemsFactura
          detalle={detalleFactura}
          responsableNombre={
            "razonSoc" in responsableSeleccionado
              ? responsableSeleccionado.razonSoc
              : `${responsableSeleccionado.nombre} ${responsableSeleccionado.apellido}`
          }
          onConfirmar={confirmarFactura}
          onCancelar={() => setPaso("GRILLA")}
        />
      )}
      {paso === "GUARDANDO" && (
        <div className="fixed inset-0 bg-white/50 dark:bg-white/20 dark:text-white flex items-center justify-center z-50">
          <h2 className="text-5xl font-bold text-black dark:text-white">
            Guardando...
          </h2>
        </div>
      )}
      {/* PASO 4: EXITO */}
      {paso === "EXITO" && (
        <div className="text-center py-20">
          <h2 className="text-3xl text-green-600 font-bold mb-4">
            Operación Exitosa
          </h2>
          <button
            onClick={() => router.push("/home")}
            className="bg-blue-600 text-white px-6 py-2 rounded"
          >
            Volver al inicio
          </button>
        </div>
      )}
    </div>
  );
}
