// componente server != componente client, lo necesito para usar state
"use client";

import {useRouter} from "next/navigation";
import {FormEvent, useState} from "react";
import {cancelarReserva, ReservaDTO} from "@/services/reserva.service";

export default function CancelarReserva() {
    const router = useRouter();
    const [error, setError] = useState<string | null>(null);
    const [busquedaRealizada, setBusquedaRealizada] = useState(false);
    const [loading, setLoading] = useState(false);

    const form1_limpio = {
        apellido: "",
        nombre: "",
        nroHab: "",
        tipoHab: "",
        desde: "",
        hasta: "",
    };

    // usestate para poder ver los cambios dinámicos del formulario
    const [formData, setFormData] = useState(form1_limpio);

    // cuando se ingrese algo en el form, se actualiza formData
    const handleChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
    ) => {
        setFormData({...formData, [e.target.name]: e.target.value});
    };


    //Falta toda la logica de conexcion con el back
    const enviarDatos = async (forzar: boolean = false) => {
        const dataDTO: ReservaDTO = {
            apellido: formData.apellido,
            nombre: formData.nombre,
            nroHab: formData.nroHab,
            tipoHab: formData.tipoHab,
            desde: formData.desde,
            hasta: formData.hasta,
        };

    };

    const handleSearch = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setBusquedaRealizada(false);
        setError(null);

        // 1. VALIDACIÓN
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

        // 2. LLAMADA A TU FUNCIÓN enviarDatos
    };


    // en el return mandamos el html que teniamos antes
    return (
        <div className="dark:bg-gray-950 dark:text-white p-5">
            <h1 className="text-[#141414] dark:text-white mb-8 text-5xl font-bold pb-2 border-b dark:border-gray-800">
                Buscar Reservas
            </h1>

            <div className="border border-gray-200 p-5 rounded-xl bg-[#f5f7fa] dark:bg-gray-950 shadow-sm mb-6">
                <form
                    onSubmit={handleSearch}
                    className="flex flex-col lg:flex-row gap-4"
                >
                    {/* --- PARTE IZQUIERDA: INPUTS (Grid 3x2) --- */}
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

                        <div className="flex flex-col">
                            <label className="mb-1 text-sm font-bold text-gray-700 dark:text-gray-200">
                                Fecha Desde
                            </label>
                            <input
                                name="desde"
                                type="date"
                                value={formData.desde}
                                onChange={handleChange}
                                className="border border-gray-300 dark:border-gray-700 p-2.5 rounded-lg w-full text-sm bg-white dark:bg-gray-950 text-gray-900 dark:text-white h-[42px]"
                            />
                        </div>

                        <div className="flex flex-col">
                            <label className="mb-1 text-sm font-bold text-gray-700 dark:text-gray-200">
                                Nro Habitación
                            </label>
                            <input
                                name="nroHab"
                                placeholder="Ej: 101"
                                value={formData.nroHab}
                                onChange={handleChange}
                                type="number"
                                className="border border-gray-300 dark:border-gray-700 p-2.5 rounded-lg w-full text-sm bg-white dark:bg-gray-950 text-gray-900 dark:text-white h-[42px]"
                            />
                        </div>

                        <div className="flex flex-col">
                            <label className="mb-1 text-sm font-bold text-gray-700 dark:text-gray-200">
                                Tipo Habitación
                            </label>
                            <input
                                name="tipoHab"
                                placeholder="Ej: Simple, Doble"
                                value={formData.tipoHab}
                                onChange={handleChange}
                                className="border border-gray-300 dark:border-gray-700 p-2.5 rounded-lg w-full text-sm bg-white dark:bg-gray-950 text-gray-900 dark:text-white h-[42px]"
                            />
                        </div>

                        <div className="flex flex-col">
                            <label className="mb-1 text-sm font-bold text-gray-700 dark:text-gray-200">
                                Fecha Hasta
                            </label>
                            <input
                                name="hasta"
                                type="date"
                                value={formData.hasta}
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

            /* Continua la busqueda de reserva */

        </div>
    );
}


