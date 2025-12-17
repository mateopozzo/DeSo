"use client";
import { useState, useMemo, useEffect } from "react";

export interface ServicioDTO {
  idServicio: number;
  tipoServicio: string;
  precio: number;
}

export interface DetalleFacturaDTO {
  idEstadia: number;
  descripcionHabitacion: string;
  costoEstadia: number;
  listaServicios: ServicioDTO[];
  montoTotal: number;
  tipoSugerido: "A" | "B";
}

// testing BORRAR PLIS
const DATOS_MOCK: DetalleFacturaDTO = {
  idEstadia: 129,
  descripcionHabitacion: "Habitación 129 - Single",
  costoEstadia: 85000.0,
  listaServicios: [
    { idServicio: 1, tipoServicio: "Bar", precio: 4500.0 },
    { idServicio: 2, tipoServicio: "Lavandería", precio: 2200.0 },
    { idServicio: 3, tipoServicio: "Sauna", precio: 6000.0 },
  ],
  montoTotal: 97700.0,
  tipoSugerido: "A",
};

interface Props {
  detalle?: DetalleFacturaDTO;
  responsableNombre?: string;
  onConfirmar?: () => void;
  onCancelar?: () => void;
}

export default function GrillaItemsFactura({
  // cambiar datos mock por los datos del back
  detalle = DATOS_MOCK,
  responsableNombre = "Ejemplo ejemplo",
  onConfirmar,
  onCancelar,
}: Props) {
  const [seleccionados, setSeleccionados] = useState<Set<string | number>>(
    new Set()
  );

  useEffect(() => {
    const inicial = new Set<string | number>();
    inicial.add("ESTADIA");
    detalle.listaServicios.forEach((s) => inicial.add(s.idServicio));
    setSeleccionados(inicial);
  }, [detalle]);

  const toggleItem = (id: string | number) => {
    const nuevoSet = new Set(seleccionados);
    if (nuevoSet.has(id)) nuevoSet.delete(id);
    else nuevoSet.add(id);
    setSeleccionados(nuevoSet);
  };

  // simula back
  const calculos = useMemo(() => {
    let subtotal = 0;

    if (seleccionados.has("ESTADIA")) {
      subtotal += detalle.costoEstadia;
    }

    detalle.listaServicios.forEach((s) => {
      if (seleccionados.has(s.idServicio)) {
        subtotal += s.precio;
      }
    });

    let neto = subtotal;
    let iva = 0;

    if (detalle.tipoSugerido === "A") {
      neto = subtotal / 1.21;
      iva = subtotal - neto;
    }

    return { subtotal, neto, iva };
  }, [seleccionados, detalle]);

  const pesosARG = (amount: number) =>
    new Intl.NumberFormat("es-AR", {
      style: "currency",
      currency: "ARS",
    }).format(amount);

  return (
    <div className="w-full max-w-4xl mx-auto bg-[#f5f7fa] dark:bg-gray-900 rounded-2xl shadow-xl border border-gray-200 dark:border-gray-800 overflow-hidden animate-in fade-in zoom-in duration-300">
      {/* HEADER */}
      <div className="bg-gray-50 dark:bg-gray-950/50 p-6 border-b border-gray-200 dark:border-gray-700 flex flex-col md:flex-row justify-between items-start gap-4">
        <div>
          <div className="flex items-center gap-3">
            <span
              className={`px-3 py-1 rounded-lg text-sm font-bold border ${
                detalle.tipoSugerido === "A"
                  ? "bg-purple-100 text-purple-700 border-purple-200"
                  : "bg-blue-100 text-blue-700 border-blue-200"
              }`}
            >
              Tipo {detalle.tipoSugerido}
            </span>
          </div>
          <p className="text-gray-500 mt-1">
            Responsable:{" "}
            <span className="font-semibold text-gray-700 dark:text-gray-300">
              {responsableNombre}
            </span>
          </p>
        </div>
        <div className="text-right">
          <p className="text-xs text-gray-400 uppercase tracking-wider font-bold">
            Alojamiento
          </p>
          <p className="text-lg font-medium text-gray-800 dark:text-white">
            {detalle.descripcionHabitacion}
          </p>
        </div>
      </div>

      {/* TABLA */}
      <div className="p-6">
        <div className="overflow-hidden rounded-xl border border-gray-200 dark:border-gray-700">
          <table className="min-w-full divide-y divide-gray-200 dark:divide-gray-700">
            <thead className="bg-gray-100 dark:bg-gray-950">
              <tr>
                <th className="px-6 py-4 text-left text-xs font-bold text-gray-500 uppercase tracking-wider w-16">
                  Incluir
                </th>
                <th className="px-6 py-4 text-left text-xs font-bold text-gray-500 uppercase tracking-wider">
                  Concepto / Servicio
                </th>
                <th className="px-6 py-4 text-right text-xs font-bold text-gray-500 uppercase tracking-wider">
                  Importe
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200 dark:divide-gray-700 bg-[#f5f7fa] dark:bg-gray-900">
              {/* estadía */}
              <tr
                className={`transition-colors ${
                  seleccionados.has("ESTADIA")
                    ? "bg-[#f5f7fa] dark:bg-gray-900"
                    : "bg-gray-50 dark:bg-gray-950 opacity-60 grayscale"
                }`}
              >
                <td className="px-6 py-4">
                  <input
                    type="checkbox"
                    checked={seleccionados.has("ESTADIA")}
                    onChange={() => toggleItem("ESTADIA")}
                    className="h-5 w-5 text-green-600 rounded focus:ring-green-500 border-gray-300 cursor-pointer"
                  />
                </td>
                <td className="px-6 py-4">
                  <span className="block text-sm font-bold text-gray-900 dark:text-white">
                    Estadía
                  </span>
                  <span className="text-xs text-gray-500">
                    Cargo base de la habitación
                  </span>
                </td>
                <td className="px-6 py-4 text-right text-sm font-mono font-medium text-gray-900 dark:text-white">
                  {pesosARG(detalle.costoEstadia)}
                </td>
              </tr>

              {/* servicios */}
              {detalle.listaServicios.map((s) => (
                <tr
                  key={s.idServicio}
                  className={`transition-colors ${
                    seleccionados.has(s.idServicio)
                      ? "bg-[#f5f7fa] dark:bg-gray-900"
                      : "bg-gray-50 dark:bg-gray-950 opacity-60 grayscale"
                  }`}
                >
                  <td className="px-6 py-4">
                    <input
                      type="checkbox"
                      checked={seleccionados.has(s.idServicio)}
                      onChange={() => toggleItem(s.idServicio)}
                      className="h-5 w-5 text-green-600 rounded focus:ring-green-500 border-gray-300 cursor-pointer"
                    />
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-700 dark:text-gray-300">
                    {s.tipoServicio}
                  </td>
                  <td className="px-6 py-4 text-right text-sm font-mono text-gray-700 dark:text-gray-300">
                    {pesosARG(s.precio)}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* TOTAL */}
        <div className="flex flex-col items-end gap-2 mt-6">
          {detalle.tipoSugerido === "A" && (
            <div className="w-full max-w-xs space-y-1 text-right border-b border-gray-200 dark:border-gray-700 pb-2 mb-2">
              <div className="flex justify-between text-sm text-gray-500 dark:text-gray-400">
                <span>Neto:</span>
                <span>{pesosARG(calculos.neto)}</span>
              </div>
              <div className="flex justify-between text-sm text-gray-500 dark:text-gray-400">
                <span>IVA (21%):</span>
                <span>{pesosARG(calculos.iva)}</span>
              </div>
            </div>
          )}

          <div className="flex items-center justify-between w-full max-w-xs p-4 bg-green-50 dark:bg-green-900/20 rounded-xl border border-green-100 dark:border-green-800">
            <span className="text-lg font-bold text-green-900 dark:text-green-400">
              Total a pagar
            </span>
            <span className="text-2xl font-bold text-green-700 dark:text-green-400">
              {pesosARG(calculos.subtotal)}
            </span>
          </div>
        </div>
      </div>

      {/* BOTONES */}
      <div className="bg-gray-50 dark:bg-gray-950/50 p-6 border-t border-gray-200 dark:border-gray-700 flex justify-end gap-3">
        <button
          onClick={onCancelar}
          className="px-6 py-2.5 rounded-xl font-bold text-gray-700 dark:text-gray-300 border border-gray-300 dark:border-gray-600 hover:bg-gray-100 dark:hover:bg-gray-700 transition-all"
        >
          Volver
        </button>
        <button
          onClick={onConfirmar}
          className="px-6 py-2.5 rounded-xl font-bold text-white bg-[#52a173] hover:bg-[#10b655] shadow-lg hover:shadow-green-500/30 transition-all transform active:scale-95 flex items-center gap-2"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="h-5 w-5"
            viewBox="0 0 20 20"
            fill="currentColor"
          >
            <path
              fillRule="evenodd"
              d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
              clipRule="evenodd"
            />
          </svg>
          Generar factura
        </button>
      </div>
    </div>
  );
}
