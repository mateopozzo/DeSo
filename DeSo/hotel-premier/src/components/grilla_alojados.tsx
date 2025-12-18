"use client";
import { useState } from "react";
import {
  CriteriosBusq,
  PersonaJuridica,
  EstadiaDTO,
} from "@/types/facturacion";

import { buscarTercero } from "@/services/facturar.service";

export type ResponsablePago = CriteriosBusq | PersonaJuridica;

interface AlojadosProps {
  idHab: string;
  horaCheckout: string;
  alojadosDTO: CriteriosBusq[];
  onSeleccionarResponsable?: (responsable: ResponsablePago) => void;
  onAvanzar?: () => void;
}

export default function GrillaAlojados({
  idHab,
  alojadosDTO,
  onSeleccionarResponsable,
  onAvanzar,
}: AlojadosProps) {
  const [seleccionadoIndex, setSeleccionadoIndex] = useState<number | null>(
    null
  );
  const [modoTercero, setModoTercero] = useState(false);
  const [cuit, setCuit] = useState("");
  const [terceroEncontrado, setTerceroEncontrado] =
    useState<PersonaJuridica | null>(null);
  const [errorTercero, setErrorTercero] = useState<string | null>(null);
  const [buscando, setBuscando] = useState(false);
  const [mensajeNoCUIT, setNoCUIT] = useState("");

  const handleSeleccionHuesped = (index: number, alojado: CriteriosBusq) => {
    setSeleccionadoIndex(index);
    setModoTercero(false);
    setTerceroEncontrado(null);

    if (onSeleccionarResponsable) {
      onSeleccionarResponsable(alojado);
    }
  };

  const activarModoTercero = () => {
    setModoTercero(true);
    setSeleccionadoIndex(null);
    if (onSeleccionarResponsable) {
    }
  };

  const buscarCuit = async () => {
    console.log("cuit: " + cuit);
    if (!cuit) return;
    setBuscando(true);
    setErrorTercero(null);
    setTerceroEncontrado(null);

    try {
      const data = await buscarTercero(cuit);
      if (data) {
        setTerceroEncontrado(data);
        if (data.razonSoc.length == 0) {
          setNoCUIT("No se encontró una razón social para este CUIT.");
        }
      }
    } catch (err) {
      console.error(err);
      setErrorTercero("Error al buscar el tercero.");
    } finally {
      setBuscando(false);
    }
  };

  const confirmarTercero = () => {
    console.log("Tercero encontrado: " + terceroEncontrado);
    if (terceroEncontrado && onSeleccionarResponsable && onAvanzar) {
      onSeleccionarResponsable(terceroEncontrado);
      onAvanzar();
    }
  };

  return (
    <div className="bg-white dark:bg-gray-950 p-6 rounded-2xl shadow-lg border border-gray-200 dark:border-gray-800">
      <h3 className="text-xl font-bold mb-2 text-gray-800 dark:text-white">
        Huéspedes en habitación {idHab}
      </h3>
      <p className="text-sm text-gray-500 mb-4">
        Seleccione el responsable de pago. Este debe ser mayor de edad.
      </p>

      <div className="overflow-x-auto rounded-xl overflow-y-auto max-h-96 border border-gray-300 dark:border-gray-950">
        <table className="min-w-full divide-y divide-gray-300 dark:divide-gray-700">
          <thead className="bg-gray-100 dark:bg-gray-950 sticky top-0 z-10">
            <tr>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                Seleccionar
              </th>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                Apellido
              </th>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                Nombre
              </th>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                Tipo Doc.
              </th>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                Nro. Doc.
              </th>
            </tr>
          </thead>
          <tbody className="bg-white dark:bg-gray-950 divide-y divide-gray-200 dark:divide-gray-800">
            {alojadosDTO.map((alojado, index) => (
              <tr
                key={index}
                className={`cursor-pointer transition-colors hover:bg-blue-50 dark:hover:bg-blue-900/20 ${
                  seleccionadoIndex === index
                    ? "bg-blue-100 dark:bg-blue-900/40"
                    : ""
                }`}
                onClick={() => handleSeleccionHuesped(index, alojado)}
              >
                <td className="px-6 py-4 whitespace-nowrap">
                  <input
                    type="radio"
                    name="responsable"
                    checked={seleccionadoIndex === index}
                    onChange={() => handleSeleccionHuesped(index, alojado)}
                    className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300"
                  />
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900 dark:text-white">
                  {alojado.apellido}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-300">
                  {alojado.nombre}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-300">
                  {alojado.tipoDoc || "DNI"}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-300">
                  {alojado.nroDoc}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {!modoTercero && (
        <div className="flex justify-between items-center animate-fade-in-up">
          <button
            onClick={activarModoTercero}
            className="dark:text-white font-semibold text-black border dark:border-white rounded-2xl px-6 py-2 m-4"
          >
            Facturar a un tercero
          </button>

          <button
            disabled={seleccionadoIndex === null}
            onClick={onAvanzar}
            className={`px-6 py-2 rounded-xl font-bold shadow-md transition-all m-4 ${
              seleccionadoIndex === null
                ? "bg-gray-300 text-gray-500 cursor-not-allowed"
                : "bg-[#52a173] text-white hover:bg-[#10b655] hover:shadow-xl transform active:scale-95"
            }`}
          >
            Siguiente
          </button>
        </div>
      )}

      {modoTercero && (
        <div className="mt-8 bg-gray-50 dark:bg-gray-950 p-8 rounded-4xl border-2 border-blue-900 animate-fade-in">
          <div className="flex justify-between mb-4">
            <h4 className="font-bold text-2xl text-gray-700 dark:text-gray-200">
              Facturar a un tercero
            </h4>
            <button
              onClick={() => setModoTercero(false)}
              className="text-sm font-semibold px-6 py-2 rounded-xl border dark:border-white dark:text-white hover:bg-red-500 hover:border-red-500"
            >
              Cancelar
            </button>
          </div>

          <div className="flex flex-col gap-4">
            <div className="flex flex-row gap-2">
              <input
                type="text"
                placeholder="Ingrese CUIT sin guiones ni puntos"
                value={cuit}
                onChange={(e) => setCuit(e.target.value)}
                className="flex-1 px-4 py-2 rounded-xl border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-950 focus:ring focus:ring-blue-400 outline-none"
              />
              <button
                onClick={buscarCuit}
                disabled={buscando || !cuit}
                className="px-4 py-2 rounded-xl font-bold bg-blue-600 text-white hover:bg-blue-700 disabled:opacity-50"
              >
                {buscando ? "Buscando..." : "Buscar"}
              </button>
            </div>

            {errorTercero && (
              <p className="text-red-500 text-sm font-semibold">
                {errorTercero}
              </p>
            )}

            {terceroEncontrado && terceroEncontrado.razonSoc.length > 0 ? (
              <div className="bg-green-50 dark:bg-green-900/20 p-3 rounded-lg border border-green-200 dark:border-green-800 flex flex-col md:flex-row justify-between items-center gap-4">
                <div>
                  <p className="text-xs text-gray-500 dark:text-gray-400">
                    Razón social:
                  </p>
                  <p className="font-bold text-lg text-green-700 dark:text-green-400">
                    {terceroEncontrado.razonSoc}
                  </p>
                  <p className="text-xs text-gray-500">
                    CUIT: {terceroEncontrado.cuit}
                  </p>
                </div>
                <button
                  onClick={confirmarTercero}
                  className="px-6 py-2 rounded-xl cursor-pointer font-bold shadow-md bg-[#52a173] text-white hover:bg-[#10b655] hover:shadow-xl transform active:scale-95"
                >
                  Confirmar
                </button>
              </div>
            ) : (
              <div>
                <p className="text-xs text-gray-500 dark:text-gray-400">
                  {mensajeNoCUIT}
                </p>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
