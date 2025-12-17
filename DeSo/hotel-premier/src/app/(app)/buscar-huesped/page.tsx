"use client";
import React, { useState, FormEvent } from "react";
import { useRouter } from "next/navigation";
import {
  busqueda,
  CriteriosBusq,
  ResultadoBusq,
} from "@/services/busqueda.service";

export default function buscarHuesped() {
  const router = useRouter();

  const form_limpio = {
    apellido: "",
    nombre: "",
    tipo_documento: "",
    numeroDocumento: "",
  };

  // inicializar el form
  const [formData, setFormData] = useState(form_limpio);
  const [resultados, setResultados] = useState<ResultadoBusq[]>([]);
  // si ya busque con estos criterios que no siga buscando y pueda sobrecargar con requests
  const [busquedaRealizada, setBusquedaRealizada] = useState(false);
  // para mostrar que estoy esperando datos del back y no que se colgó
  const [loading, setLoading] = useState(false);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const enviarDatos = async () => {
    const dataDTO: CriteriosBusq = {
      apellido: formData.apellido,
      nombre: formData.nombre,
      tipoDoc: formData.tipo_documento,
      nroDoc: formData.numeroDocumento,
    };

    setLoading(true);
    // si ya busque cosas antes las borro
    setResultados([]);

    try {
      const datos = await busqueda(dataDTO);

      setResultados(datos);
      setBusquedaRealizada(true);
      setLoading(false);

      //para que la redireccion no sea automatica y el usuario tenga control
      // if (datos.length === 0) {
      //   router.push("/alta-huesped");
      // }
    } catch (err) {
      alert("Error al conectarse al servidor");
      setLoading(false);
      console.error(err);
    }
  };

  // cuando se presione enviar, se forma objeto, se envia a back, se procesa response
  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    await enviarDatos();
  };

  const handleSeleccionar = (huesped: ResultadoBusq) => {
    router.push(`/gestionar-huesped/?tipoDoc=${huesped.tipoDoc}&nroDoc=${huesped.nroDoc}&nombre=${huesped.nombre}&apellido=${huesped.apellido}`);
  };

  return (
    <div className="dark:bg-gray-950 dark:text-white">
      <h1 className="text-[#141414] dark:text-white  mb-8 text-5xl font-bold pb-2">
        Buscar huésped
      </h1>

      <form
        onSubmit={handleSubmit}
        className="flex flex-col md:grid md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-5 dark:bg-gray-950 dark:text-white"
      >
        <InputGroup
          label="Apellido"
          name="apellido"
          value={formData.apellido}
          onChange={handleChange}
          placeholder="Doe"
          maxLength="50"
          type="text"
        />
        <InputGroup
          label="Nombre"
          name="nombre"
          value={formData.nombre}
          onChange={handleChange}
          placeholder="John"
          type="text"
          maxLength="50"
        />
        <div className="flex flex-col  dark:text-white">
          <label className="mb-2 font-semibold text-[#141414]  dark:text-white">
            Tipo de documento
          </label>
          <select
            name="tipo_documento"
            value={formData.tipo_documento}
            onChange={handleChange}
            className="p-2.5 border border-[#ddd] rounded-xl bg-[#f5f7fa] dark:bg-gray-950 dark:text-white"
          >
            <option value=" "> </option>
            <option value="DNI">DNI</option>
            <option value="LC">LC</option>
            <option value="LE">LE</option>
            <option value="PASAPORTE">Pasaporte</option>
            <option value="OTRO">Otro</option>
          </select>
        </div>

        <InputGroup
          label="Nro. de documento"
          name="numeroDocumento"
          value={formData.numeroDocumento}
          onChange={handleChange}
          placeholder="Sin guiones ni puntos"
          type="text"
          maxLength="20"
        />
        <div className="flex flex-col lg:flex-row  justify-center gap-4 mt-8 col-span-4">
          <button
            type="button"
            onClick={() => router.push("/home")}
            className="cursor-pointer px-8 py-2 rounded-xl font-bold transition duration-300 dark:border dark:border-white dark:text-white dark:bg-gray-950 dark:hover:border-[#b92716] text-[#1a252f] border border-[#1a252f] hover:bg-[#b92716] hover:text-white hover:border-[#b92716]"
          >
            Cancelar
          </button>
          <button
            type="submit"
            disabled={loading}
            className="cursor-pointer px-8 py-2 rounded-xl font-bold transition duration-300 bg-[#52a173] text-white hover:bg-[#10b655]"
          >
            {loading ? "Buscando huéspedes" : "Buscar"}
          </button>
        </div>
      </form>

      {busquedaRealizada && (
          <div className="w-full mt-8 animate-fade-in-up">
            <h2 className="text-2xl font-bold mb-4 border-b pb-2 border-gray-200 dark:border-gray-800">
              Resultados de la búsqueda
            </h2>

            {resultados.length > 0 ? (
                <div className="rounded-xl border border-gray-200 dark:border-gray-800 shadow-sm overflow-hidden bg-white dark:bg-gray-950">
                  <div className="overflow-y-auto max-h-[600px]">
                    <table className="w-full text-left text-sm border-collapse">
                      <thead className="bg-gray-100 dark:bg-gray-900 text-gray-600 dark:text-gray-300 uppercase font-semibold sticky top-0 z-10 shadow-sm">
                      <tr>
                        <th className="px-6 py-4 bg-gray-100 dark:bg-gray-900">Apellido</th>
                        <th className="px-6 py-4 bg-gray-100 dark:bg-gray-900">Nombre</th>
                        <th className="px-6 py-4 bg-gray-100 dark:bg-gray-900">Tipo de documento</th>
                        <th className="px-6 py-4 bg-gray-100 dark:bg-gray-900">Nro. de documento</th>
                        <th className="px-6 py-4 bg-gray-100 dark:bg-gray-900 text-center">Acciones</th>
                      </tr>
                      </thead>
                      <tbody className="divide-y divide-gray-200 dark:divide-gray-800">
                      {resultados.map((huesped, index) => (
                          <tr key={index} className="hover:bg-gray-50 dark:hover:bg-gray-900 transition-colors group">
                            <td className="px-6 py-4 font-medium text-gray-900 dark:text-white">{huesped.apellido}</td>
                            <td className="px-6 py-4 font-medium text-gray-900 dark:text-white">{huesped.nombre}</td>
                            <td className="px-6 py-4 text-gray-500 dark:text-gray-400">
                    <span className="bg-gray-100 dark:bg-gray-800 px-2 py-1 rounded text-xs font-bold">
                      {huesped.tipoDoc}
                    </span>
                            </td>
                            <td className="px-6 py-4 text-gray-500 dark:text-gray-400 font-mono">{huesped.nroDoc}</td>
                            <td className="px-6 py-4 text-center">
                              <button
                                  onClick={() => handleSeleccionar(huesped)}
                                  className="text-blue-600 dark:text-blue-400 hover:text-blue-800 dark:hover:text-blue-300 font-medium hover:underline px-3 py-1 rounded hover:bg-blue-50 dark:hover:bg-blue-900/30 transition-colors"
                              >
                                Seleccionar
                              </button>
                            </td>
                          </tr>
                      ))}
                      </tbody>
                    </table>
                  </div>
                </div>
            ) : (
                <div className="flex flex-col items-center justify-center p-10 border-2 border-dashed border-gray-300 dark:border-gray-700 rounded-xl bg-gray-50 dark:bg-gray-900/50">
                  <p className="text-lg text-gray-600 dark:text-gray-400 mb-4 text-center">
                    No se encontró ningún huésped con esos datos. <br />
                    ¿Deseas intentar una nueva búsqueda o registrar uno nuevo?
                  </p>
                  <button
                      onClick={() => router.push("/alta-huesped")}
                      className="cursor-pointer px-6 py-3 rounded-xl font-bold transition duration-300 bg-[#52a173] text-white hover:bg-[#10b655] shadow-lg"
                  >
                    + Crear nuevo huésped
                  </button>
                </div>
            )}
          </div>
      )}
      {/* FIN RESULTADOS */}
    </div>
  );
}

const InputGroup = ({
                      label,
                      name,
                      value,
                      onChange,
                      type = "text",
                      placeholder = "",
                      maxLength = "255",
                    }: any) => (
    <div className="flex flex-col dark:bg-gray-950 dark:text-white">
      <label className="mb-2 font-semibold text-[#141414] dark:bg-gray-950 dark:text-white">
        {label}
      </label>
      <input
          type={type}
          name={name}
          value={value}
          onChange={onChange}
          placeholder={placeholder}
          maxLength={maxLength}
          className="p-2.5 border border-[#ddd] rounded-xl dark:bg-gray-950 dark:text-white focus:outline-none focus:border-[#9ca9be] focus:ring-2 focus:ring-[#4a6491]/20"
      />
    </div>
);
