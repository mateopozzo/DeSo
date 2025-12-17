"use client";

import {useRouter} from "next/navigation";
import {useState} from "react";
import {buscarReservas, cancelarReserva, ReservaGrillaDTO} from "@/services/reserva.service";

export default function CancelarReserva() {
    const router = useRouter();
    const [error, setError] = useState<string | null>(null);
    const [busquedaRealizada, setBusquedaRealizada] = useState(false);
    const [loading, setLoading] = useState(false);

    const form1_limpio = {
        apellido: "",
        nombre: "",
    };

    const [formData, setFormData] = useState(form1_limpio);
    const [resultados, setResultados] = useState<ReservaGrillaDTO[]>([]);

    const [seleccionadas, setSeleccionadas] = useState<Map<number, ReservaGrillaDTO>>(new Map());

    const listaSeleccionadas = Array.from(seleccionadas.values());

    const toggleSeleccion = (reserva: ReservaGrillaDTO) => {
        const nuevoMapa = new Map(seleccionadas);
        if (nuevoMapa.has(reserva.idReserva)) {
            nuevoMapa.delete(reserva.idReserva);
        } else {
            nuevoMapa.set(reserva.idReserva, reserva);
        }
        setSeleccionadas(nuevoMapa);
    };

    const cancelarCasoUso = () => {
        if (confirm("¿Desea cancelar todo el proceso?")) {
            router.push("/home");
        }
    };

    const handleCancelarMasivo = async () => {
        if(!confirm(`¿Eliminar ${seleccionadas.size} reservas?`)) return;

        setLoading(true);
        const ids = Array.from(seleccionadas.keys()); // Obtenemos las claves (IDs)

        for (const id of ids) {
            await cancelarReserva(id);
        }

        setResultados(prev => prev.filter(r => !seleccionadas.has(r.idReserva)));


        setSeleccionadas(new Map());
        setLoading(false);
    };

    const handleChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
    ) => {
        setFormData({...formData, [e.target.name]: e.target.value});
    };

    const handleSearch = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setBusquedaRealizada(false);
        setError(null);

        const campos_ob = [{ key: "apellido", etiq: "Apellido" }];
        const campos_faltantes = campos_ob.filter((campo) => {
            const valor = formData[campo.key as keyof typeof formData];
            return !valor || valor.toString().trim() === "";
        });

        if (campos_faltantes.length > 0) {
            const lista = campos_faltantes.map((c) => c.etiq).join(", ");
            setError(`Los siguientes campos son obligatorios para buscar: ${lista}`);
            setLoading(false);
            return;
        }

        try {
            const data = await buscarReservas({
                apellido: formData.apellido,
                nombre: formData.nombre
            });
            setResultados(data);
            setBusquedaRealizada(true);
        } catch (e) {
            setError("Error al conectar con el servidor");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="dark:bg-gray-950 dark:text-white p-5 min-h-screen">

            <div className="flex flex-col md:flex-row justify-between items-center mb-8 border-b dark:border-gray-800 pb-4 gap-4">
                <h1 className="text-[#141414] dark:text-white mb-8 text-5xl font-bold pb-2 border-b dark:border-gray-800">
                    Buscar Reservas
                </h1>
                <button
                    type="button"
                    onClick={cancelarCasoUso}
                    className="cursor-pointer px-8 py-2 rounded-xl font-bold transition duration-300 dark:border dark:border-white dark:text-white bg-[#f5f7fa] dark:bg-gray-950 dark:hover:border-[#b92716] text-[#1a252f] border border-[#1a252f] hover:bg-[#b92716] hover:text-white hover:border-[#b92716]"
                >
                    Cancelar Proceso
                </button>
            </div>

            <div className="border border-gray-200 p-5 rounded-xl bg-[#f5f7fa] dark:bg-gray-950 shadow-sm mb-6">
                <form onSubmit={handleSearch} className="flex flex-col lg:flex-row gap-4">
                    <div className="flex-1 grid grid-cols-1 md:grid-cols-3 gap-4">
                        <div className="flex flex-col">
                            <label className="mb-1 text-sm font-bold text-gray-700 dark:text-gray-200">
                                Apellido <span className="text-red-500">*</span>
                            </label>
                            <input
                                name="apellido"
                                placeholder="Ingrese apellido"
                                value={formData.apellido}
                                onChange={handleChange}
                                className={`border p-2.5 rounded-lg w-full text-sm bg-white dark:bg-gray-950 text-gray-900 dark:text-white h-[42px] ${
                                    error && formData.apellido === "" ? "border-red-500" : "border-gray-300 dark:border-gray-700"
                                }`}
                            />
                        </div>

                        <div className="flex flex-col">
                            <label className="mb-1 text-sm font-bold text-gray-700 dark:text-gray-200">
                                Nombre
                            </label>
                            <input
                                name="nombre"
                                placeholder="Ingrese nombre"
                                value={formData.nombre}
                                onChange={handleChange}
                                className="border border-gray-300 dark:border-gray-700 p-2.5 rounded-lg w-full text-sm bg-white dark:bg-gray-950 text-gray-900 dark:text-white h-[42px]"
                            />
                        </div>
                    </div>

                    <div className="lg:w-24 flex items-end">
                        <button
                            type="submit"
                            disabled={loading}
                            className="w-full h-full bg-green-400/70 text-white p-4 rounded-xl hover:bg-green-500 transition-colors disabled:opacity-50 flex items-center justify-center min-h-[50px]"
                        >
                            {loading ? "..." : <img src="search.svg" alt="Buscar" width={30} className="dark:invert" />}
                        </button>
                    </div>
                </form>
            </div>

            {listaSeleccionadas.length > 0 && (
                <div className="mb-8 animate-fade-in">
                    <div className="flex justify-between items-end mb-4">
                        <h2 className="text-2xl font-bold dark:text-white text-blue-600">
                            Selección ({listaSeleccionadas.length})
                        </h2>
                        <button
                            onClick={handleCancelarMasivo}
                            className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-lg font-bold transition-colors shadow-md"
                        >
                            Cancelar Reservaci{seleccionadas.size >1 ? "ones": "ón "} ({seleccionadas.size})
                        </button>
                    </div>

                    <div className="p-4 bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-800 rounded-xl">
                        <div className="flex flex-wrap gap-2">
                            {listaSeleccionadas.map((item) => (
                                <div
                                    key={item.idReserva}
                                    className="flex items-center gap-2 bg-white dark:bg-gray-800 border border-blue-200 dark:border-blue-700 px-3 py-1.5 rounded-lg shadow-sm"
                                >
                                    <span className="text-xs font-mono text-gray-500">#{item.idReserva}</span>
                                    <span className="text-sm font-medium text-gray-800 dark:text-gray-200">
                                        {item.apellido} ({item.habitaciones.map(h => `Hab ${h.numeroHabitacion}`).join(", ")})
                                    </span>
                                    <button
                                        onClick={() => toggleSeleccion(item)}
                                        className="ml-1 text-gray-400 hover:text-red-500 transition-colors"
                                        title="Desmarcar"
                                    >
                                        ✕
                                    </button>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            )}

            {busquedaRealizada && (
                <div className="mt-8 animate-fade-in">
                    <h2 className="text-2xl font-bold dark:text-white mb-4">Resultados de Búsqueda</h2>

                    {resultados.length === 0 ? (
                        <p className="text-gray-500 dark:text-gray-400">No se encontraron reservas.</p>
                    ) : (
                        <div className="overflow-x-auto rounded-xl border border-gray-200 dark:border-gray-800 shadow-sm">
                            <table className="w-full text-sm text-left text-gray-500 dark:text-gray-400">
                                <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-900 dark:text-gray-300">
                                <tr>
                                    <th scope="col" className="p-4 w-4">#</th>
                                    <th scope="col" className="px-6 py-3">ID</th>
                                    <th scope="col" className="px-6 py-3">Huésped</th>
                                    <th scope="col" className="px-6 py-3">Habitación</th>
                                    <th scope="col" className="px-6 py-3">Desde</th>
                                    <th scope="col" className="px-6 py-3">Hasta</th>
                                </tr>
                                </thead>
                                <tbody>
                                {resultados.map((reserva) => {
                                    const isSelected = seleccionadas.has(reserva.idReserva);
                                    return (
                                        <tr
                                            key={reserva.idReserva}
                                            className={`border-b dark:border-gray-800 transition-colors ${
                                                isSelected
                                                    ? "bg-blue-50 dark:bg-blue-900/30"
                                                    : "bg-white dark:bg-gray-950 hover:bg-gray-50 dark:hover:bg-gray-900"
                                            }`}
                                        >
                                            <td className="w-4 p-4">
                                                <input
                                                    type="checkbox"
                                                    className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500"
                                                    checked={isSelected}
                                                    // Pasamos la reserva ENTERA
                                                    onChange={() => toggleSeleccion(reserva)}
                                                />
                                            </td>
                                            <td className="px-6 py-4 font-medium">#{reserva.idReserva}</td>
                                            <td className="px-6 py-4">{reserva.apellido}, {reserva.nombre}</td>
                                            <td className="px-6 py-4">
                                                <div className="flex flex-col gap-1">
                                                    {/* Usamos .habitaciones y sus propiedades correctas */}
                                                    {reserva.habitaciones.map((h, index) => (
                                                        <span key={index} className="block">
                                                            Hab {h.numeroHabitacion} ({h.tipoHabitacion})
                                                        </span>
                                                    ))}
                                                </div>
                                            </td>
                                            <td className="px-6 py-4 text-green-600">{reserva.fechaInicio}</td>
                                            <td className="px-6 py-4 text-red-600">{reserva.fechaFin}</td>
                                        </tr>
                                    );
                                })}
                                </tbody>
                            </table>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
}