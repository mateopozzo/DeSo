import React, { useState } from "react";
import { useRouter } from "next/navigation";
import { CriteriosBusq, ResultadoBusq } from "@/services/busqueda.service";

interface Props {
  titulo: string;
  onBuscar: (criterios: CriteriosBusq) => Promise<ResultadoBusq[]>;
  onSeleccionar: (persona: ResultadoBusq) => void;
  excluidos?: ResultadoBusq[];
}

export default function BuscadorPersona({
  titulo,
  onBuscar,
  onSeleccionar,
  excluidos = [],
}: Props) {
  const [form, setForm] = useState<CriteriosBusq>({
    apellido: "",
    nombre: "",
    tipoDoc: "",
    nroDoc: "",
  });
  const [resultados, setResultados] = useState<ResultadoBusq[]>([]);
  const [busquedaRealizada, setBusquedaRealizada] = useState(false);
  const [loading, setLoading] = useState(false);
  const router = useRouter();

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setBusquedaRealizada(false);
    try {
      const data = await onBuscar(form);
      setResultados(data || []);
      setBusquedaRealizada(true);
    } catch (error) {
      console.error(error);
      alert("Error al conectar con el servicio de búsqueda.");
    } finally {
      setLoading(false);
    }
  };

  const isExcluido = (p: ResultadoBusq) => {
    return excluidos.some(
      (e) => e.tipoDoc === p.tipoDoc && e.nroDoc === p.nroDoc
    );
  };

  return (
    <div className="border border-gray-200 p-5 rounded-xl bg-[#f5f7fa] dark:bg-gray-950 shadow-sm mb-6">
      <h3 className="text-lg font-bold mb-4 text-gray-800 dark:text-gray-100 border-b pb-2">
        {titulo}
      </h3>

      <form
        onSubmit={handleSearch}
        className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-[1fr_1fr_0.8fr_1fr] gap-3 mb-4"
      >
        <input
          name="apellido"
          placeholder="Apellido"
          value={form.apellido}
          onChange={handleChange}
          className="border border-gray-300 dark:border-gray-700 p-2 rounded-lg w-full text-sm bg-[#f5f7fa] dark:bg-gray-950 text-gray-900 dark:text-white"
        />
        <input
          name="nombre"
          placeholder="Nombre"
          value={form.nombre}
          onChange={handleChange}
          className="border border-gray-300 dark:border-gray-700 p-2 rounded-lg w-full text-sm bg-[#f5f7fa] dark:bg-gray-950 text-gray-900 dark:text-white"
        />

        <select
          name="tipoDoc"
          value={form.tipoDoc}
          onChange={handleChange}
          className="border border-gray-300 dark:border-gray-700 p-2 rounded-lg w-full text-sm bg-[#f5f7fa] dark:bg-gray-950 text-gray-900 dark:text-white"
        >
          <option value="">Tipo Doc</option>
          <option value="DNI">DNI</option>
          <option value="PASAPORTE">Pasaporte</option>
          <option value="LC">LC</option>
          <option value="LE">LE</option>
        </select>

        <div className="flex gap-2">
          <input
            name="nroDoc"
            placeholder="Nro Doc"
            value={form.nroDoc}
            onChange={handleChange}
            className="border border-gray-300 dark:border-gray-700 p-2 rounded-lg w-full text-sm flex-1 bg-white dark:bg-gray-950 text-gray-900 dark:text-white"
          />
          <button
            type="submit"
            disabled={loading}
            className="bg-green-400/70 text-white px-4 py-2 rounded-xl hover:bg-green-500 transition-colors disabled:opacity-50"
          >
            <img src="search.svg" alt="" width={25} className="dark:invert" />
          </button>
        </div>
      </form>

      {busquedaRealizada && (
        <div className="max-h-48 overflow-y-auto border border-gray-200 dark:border-gray-700 rounded-lg">
          {resultados.length === 0 ? (
            <div className="flex flex-col justify-center items-center">
              <p className="p-4 text-center text-gray-500 text-md">
                No se encontraron resultados.
              </p>
              <button
                type="button"
                onClick={() => window.open("/alta-huesped", "_blank")}
                className="px-8 py-2 mb-4 text-sm rounded-xl font-semibold transition duration-300 dark:text-white border dark:border-white hover:bg-[#10b655] hover:border-[#10b655] cursor-pointer"
              >
                Crear un nuevo huésped
              </button>
            </div>
          ) : (
            <table className="w-full text-sm text-left">
              <thead className="bg-gray-100 dark:bg-gray-950 text-gray-700 dark:text-gray-300 text-xs uppercase sticky top-0">
                <tr>
                  <th className="px-3 py-2">Nombre</th>
                  <th className="px-3 py-2">Documento</th>
                  <th className="px-3 py-2 text-right">Acción</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-200 dark:divide-gray-700">
                {resultados.map((r, i) => {
                  const disabled = isExcluido(r);
                  return (
                    <tr
                      key={i}
                      className={`transition-colors ${
                        disabled
                          ? "opacity-50 bg-gray-50 dark:bg-gray-950"
                          : "hover:bg-blue-50 dark:hover:bg-blue-900/20"
                      }`}
                    >
                      <td className="px-3 py-2 font-medium text-gray-900 dark:text-white">
                        {r.apellido}, {r.nombre}
                      </td>
                      <td className="px-3 py-2 text-gray-500 dark:text-gray-400">
                        {r.tipoDoc} {r.nroDoc}
                      </td>
                      <td className="px-3 py-2 text-right">
                        <button
                          type="button"
                          disabled={disabled}
                          onClick={() => {
                            onSeleccionar(r);
                            setResultados([]);
                            setForm({
                              apellido: "",
                              nombre: "",
                              tipoDoc: "",
                              nroDoc: "",
                            });
                            setBusquedaRealizada(false);
                          }}
                          className={`text-xs font-bold px-3 py-1 rounded ${
                            disabled
                              ? "text-gray-400 cursor-not-allowed"
                              : "text-white bg-green-600 hover:bg-green-700"
                          }`}
                        >
                          {disabled ? "Ya agregado" : "Seleccionar"}
                        </button>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          )}
        </div>
      )}
    </div>
  );
}
