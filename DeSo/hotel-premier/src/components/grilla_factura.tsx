import React, { useState, useMemo, useEffect } from "react";
import { DetalleFacturaDTO } from "@/types/facturacion";

interface Props {
  detalle: DetalleFacturaDTO;
  responsableNombre: string;
  onConfirmar: (payload: {
    cobrarEstadia: boolean;
    idsServicios: number[];
    strat: string;
  }) => void;
  onCancelar: () => void;
}

export default function GrillaItemsFactura({
  detalle,
  responsableNombre,
  onConfirmar,
  onCancelar,
}: Props) {
  const [cobrarEstadia, setCobrarEstadia] = useState(true);

  const [serviciosSeleccionados, setServiciosSeleccionados] = useState<
    number[]
  >(detalle?.consumos?.map((s) => s.idServicio) || []);

  const [formatoDescarga, setFormatoDescarga] = useState<string>("pdf");

  // estado local para el tipo de factura seleccionado (no mutamos la prop)
  const [tipoFac, setTipoFac] = useState<string>(
    detalle.tipoFacturaSugerida || "B"
  );
  const [facturaDistinta, setFacturaDistinta] = useState<boolean>(false);

  const toggleServicio = (id: number) => {
    setServiciosSeleccionados((prev) =>
      prev.includes(id) ? prev.filter((i) => i !== id) : [...prev, id]
    );
  };

  const totales = useMemo(() => {
    const totalEstadia = cobrarEstadia ? detalle.costoEstadia : 0;

    const totalServicios = detalle.consumos
      .filter((s) => serviciosSeleccionados.includes(s.idServicio))
      .reduce((acc, s) => acc + s.precio * (s.cantidad || 1), 0);

    let subtotal = totalEstadia + totalServicios;
    let iva = 0;
    let total = subtotal;

    // solo calcular si es A (usando el tipo seleccionado)
    if (tipoFac === "A") {
      iva = subtotal * 0.21;
      total = subtotal + iva;
    } else {
      // en B el precio es final
      iva = 0;
      total = subtotal;
    }

    return { subtotal, iva, total };
  }, [cobrarEstadia, serviciosSeleccionados, detalle, tipoFac]);

  const cambiarTipo = (nuevoTipo: string) => {
    setTipoFac(nuevoTipo);
    if (nuevoTipo === detalle.tipoFacturaSugerida) {
      setFacturaDistinta(false);
      return;
    }

    switch (nuevoTipo) {
      case "A":
        break;
      case "B":
        break;
      case "C":
        break;
      case "E":
        break;
      default:
        setTipoFac("B");
        break;
    }

    setFacturaDistinta(true);
  };
  return (
    <div className="bg-white dark:bg-gray-950 p-6 rounded-xl shadow-lg border border-gray-200 dark:border-gray-700 mb-8">
      <div className="flex flex-row justify-between mb-8">
        <div className="flex flex-col">
          <h2 className="text-xl dark:text-white text-black font-semibold">
            Seleccione el tipo de factura a generar
          </h2>
          <p className="text-sm text-gray-500">
            Le recomendamos utilizar el tipo de factura sugerido, ya que este se
            basa en la condición tributaria del destinatario.
          </p>
        </div>
        <div className="flex flex-col">
          <label className="mb-2 text-gray-700 dark:text-gray-400 uppercase text-xs">
            Tipo de factura
          </label>
          <select
            name="tipoFac"
            value={tipoFac}
            onChange={(e) => cambiarTipo(e.target.value)}
            className="py-1 px-2 text-center border border-blue-600 rounded-lg bg-[#f5f7fa] dark:bg-gray-950 dark:text-white"
          >
            <option value="B">B</option>
            <option value="A">A</option>
            <option value="C">C</option>
            <option value="E">E</option>
          </select>
        </div>
      </div>

      <div className="flex justify-between items-center mb-6 border-b pb-4 border-gray-200 dark:border-gray-700">
        <div>
          <h3 className="text-xl font-bold text-gray-800 dark:text-white">
            Detalle de consumos
          </h3>
          <p className="text-sm text-gray-500">
            Facturar a:{" "}
            <span className="font-semibold text-blue-600">
              {responsableNombre}
            </span>
          </p>
        </div>
        <div className="text-right flex flex-col">
          <div>
            <span className="text-xs uppercase text-gray-400">
              Tipo sugerido
            </span>
            <div
              className={`mb-4 text-2xl font-bold border-2 px-3 rounded inline-block ml-2 ${
                facturaDistinta
                  ? "text-gray-400 dark:text-gray-600 border-gray-300 dark:border-gray-600"
                  : "text-blue-500 border-blue-500"
              }`}
            >
              {detalle.tipoFacturaSugerida}
            </div>

            {facturaDistinta && (
              <div className="text-right">
                <span className="text-xs uppercase text-gray-400">
                  Tipo seleccionado
                </span>
                <div className="text-2xl font-bold text-green-400 border-2 border-green-400 px-3 rounded inline-block ml-2">
                  {tipoFac}
                </div>
              </div>
            )}
          </div>
        </div>
      </div>

      <div className="overflow-hidden rounded-lg border border-gray-200 dark:border-gray-700 mb-6">
        <table className="w-full text-left text-sm">
          <thead className="bg-gray-100 dark:bg-gray-900 text-gray-600 dark:text-gray-200 uppercase">
            <tr>
              <th className="p-3 text-center w-12">#</th>
              <th className="p-3">Concepto</th>
              <th className="p-3 text-right">Monto</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-200 dark:divide-gray-700">
            <tr
              className={`cursor-pointer transition hover:bg-blue-50 dark:hover:bg-gray-700 ${
                cobrarEstadia ? "bg-blue-50/50 dark:bg-gray-900/50" : ""
              }`}
              onClick={() => setCobrarEstadia(!cobrarEstadia)}
            >
              <td className="p-3 text-center">
                <input
                  type="checkbox"
                  checked={cobrarEstadia}
                  readOnly
                  className="rounded text-blue-600 pointer-events-none"
                />
              </td>
              <td className="p-3 font-medium text-gray-800 dark:text-white">
                Alojamiento - {detalle.habitacion}
              </td>
              <td className="p-3 text-right font-semibold text-gray-700 dark:text-gray-300">
                ${detalle.costoEstadia.toFixed(2)}
              </td>
            </tr>

            {detalle.consumos.map((serv) => (
              <tr
                key={serv.idServicio}
                className={`cursor-pointer transition hover:bg-blue-50 dark:hover:bg-gray-700 ${
                  serviciosSeleccionados.includes(serv.idServicio)
                    ? "bg-blue-50/50 dark:bg-gray-900/50"
                    : ""
                }`}
                onClick={() => toggleServicio(serv.idServicio)}
              >
                <td className="p-3 text-center">
                  <input
                    type="checkbox"
                    checked={serviciosSeleccionados.includes(serv.idServicio)}
                    readOnly
                    className="rounded text-blue-600 pointer-events-none"
                  />
                </td>
                <td className="p-3 text-gray-600 dark:text-gray-300">
                  {serv.descripcion || serv.tipoServicio}
                </td>
                <td className="p-3 text-right text-gray-600 dark:text-gray-300">
                  ${serv.precio.toFixed(2)}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="flex flex-col items-end gap-2 text-right">
        <div className="text-gray-500 dark:text-gray-400">
          Subtotal: ${totales.subtotal.toFixed(2)}
        </div>
        <div className="text-gray-500 dark:text-gray-400">
          IVA (21%): ${totales.iva.toFixed(2)}
        </div>
        <div className="text-2xl font-bold text-gray-800 dark:text-white border-t border-gray-300 dark:border-gray-600 pt-2 w-48">
          ${totales.total.toFixed(2)}
        </div>
      </div>

      <div className="flex justify-between gap-4 mt-6">
        <div className="flex flex-col items-center justify-center">
          <label className="text-sm text-gray-500 mb-2">
            Formato de descarga
          </label>
          <select
            value={formatoDescarga}
            onChange={(e) => setFormatoDescarga(e.target.value)}
            className="px-6 py-2 border border-gray-300 rounded-lg text-sm bg-white dark:bg-gray-950 dark:border-gray-600 focus:ring-2 focus:ring-blue-500 outline-none"
          >
            <option value="pdf">PDF</option>
            <option value="json">JSON</option>
          </select>
        </div>
        <div className="flex flex-row items-end gap-4">
          <button
            onClick={onCancelar}
            className="px-6 py-2 text-gray-600 dark:text-white font-semibold hover:bg-red-500 rounded-lg border border-gray-300 transition hover:border-red-500"
          >
            Volver
          </button>
          <button
            disabled={!cobrarEstadia && serviciosSeleccionados.length === 0}
            onClick={() =>
              onConfirmar({
                cobrarEstadia,
                idsServicios: serviciosSeleccionados,
                strat: formatoDescarga,
              })
            }
            className="px-6 py-2 border border-green-600 bg-green-600 text-white font-bold rounded-lg hover:bg-green-700 shadow-lg transition"
          >
            Confirmar factura
          </button>
        </div>
      </div>
    </div>
  );
}
