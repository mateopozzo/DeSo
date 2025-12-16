"use client";

import React, { Suspense, useEffect, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { eliminarHuesped, HuespedDTO } from "@/services/huespedes.service";

function BajaHuespedContent() {
    const router = useRouter();
    const searchParams = useSearchParams();

    const [huesped, setHuesped] = useState<Partial<HuespedDTO> | null>(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const nroDoc = searchParams.get("nroDoc");
        const tipoDoc = searchParams.get("tipoDoc");
        const nombre = searchParams.get("nombre");
        const apellido = searchParams.get("apellido"); // Nota: corregí 'apellid' a 'apellido' asumiendo consistencia, verifica tu push

        if (!nroDoc || !tipoDoc) {
            alert("Faltan datos para identificar al huésped.");
            router.push("/buscar-huesped");
            return;
        }

        setHuesped({
            nroDoc,
            tipoDoc,
            nombre: nombre || "",
            apellido: apellido || "",
            nacionalidad: "", fechanac: "", telefono: "", email: "",
            calle: "", nroCalle: "", piso: "", codPost: "", pais: "",
            prov: "", localidad: "", ocupacion: "", cuit: "", posicionIva: ""
        });
    }, [searchParams, router]);

    const handleEliminar = async () => {
        if (!huesped) return;

        //cartel de confirmación
        const confirmacion = window.confirm(
            `Los datos del huésped ${huesped.nombre} ${huesped.apellido}, ${huesped.tipoDoc} y ${huesped.nroDoc} serán eliminados del sistema.\n\n¿Desea continuar?`
        );

        if (!confirmacion) {
            // CU Flujo Alternativo 3.A
            return;
        }

        setLoading(true);

        try {
            const response = await eliminarHuesped(huesped as HuespedDTO);

            if (response.ok) {
                const resultadoTexto = await response.text();

                const resultadoLimpio = resultadoTexto.replace(/"/g, "");

                if (resultadoLimpio === "DADO_DE_BAJA") {
                    // exito
                    alert(
                        `Los datos del huésped ${huesped.nombre} ${huesped.apellido}, ${huesped.tipoDoc} y ${huesped.nroDoc} han sido eliminados del sistema.\nPRESIONE CUALQUIER TECLA PARA CONTINUAR...`
                    );
                    router.push("/home");
                } else if (resultadoLimpio === "OPERACION_PROHIBIDA") {
                    // historial
                    alert(
                        `El huésped no puede ser eliminado pues se ha alojado en el Hotel en alguna oportunidad.\nPRESIONE CUALQUIER TECLA PARA CONTINUAR...`
                    );
                    router.push("/home");
                } else {
                    // Caso borde: respuesta inesperada del Enum
                    alert("Respuesta inesperada del servidor: " + resultadoLimpio);
                    router.push("/home");
                }

            } else {
                // Errores HTTP (500, 400, etc)
                alert("Ocurrió un error al intentar eliminar. Código: " + response.status);
            }
        } catch (error) {
            console.error(error);
            alert("Error de conexión con el servidor.");
        } finally {
            setLoading(false);
        }
    };

    const handleCancelar = () => {
        router.back();
    };

    if (!huesped) return <div className="p-10 dark:text-white">Cargando información...</div>;

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 dark:bg-gray-950 p-4">
            <div className="max-w-2xl w-full bg-white dark:bg-gray-900 rounded-xl shadow-lg p-8 border border-gray-200 dark:border-gray-800">

                <h1 className="text-3xl font-bold text-[#ca695e] mb-6 text-center">
                    Dar de baja Huésped
                </h1>

                <div className="bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 rounded-lg p-6 mb-8 text-center">
                    <p className="text-lg text-gray-800 dark:text-gray-200 mb-2">
                        Está a punto de eliminar al siguiente huésped:
                    </p>
                    <div className="text-xl font-bold text-[#141414] dark:text-white my-4">
                        {huesped.nombre} {huesped.apellido}
                    </div>
                    <p className="text-gray-600 dark:text-gray-400">
                        {huesped.tipoDoc}: {huesped.nroDoc}
                    </p>
                </div>

                <div className="flex flex-col md:flex-row gap-4 justify-center mt-4">
                    <button
                        onClick={handleCancelar}
                        disabled={loading}
                        className="px-8 py-3 rounded-xl font-bold transition duration-300 border border-gray-300 dark:border-gray-600 text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800"
                    >
                        Cancelar
                    </button>

                    <button
                        onClick={handleEliminar}
                        disabled={loading}
                        className="px-8 py-3 rounded-xl font-bold transition duration-300 bg-[#ca695e] text-white hover:bg-[#a84f45] shadow-md hover:shadow-lg"
                    >
                        {loading ? "Procesando..." : "ELIMINAR"}
                    </button>
                </div>

            </div>
        </div>
    );
}

export default function BajaHuespedPage() {
    return (
        <Suspense fallback={<div className="p-10 dark:text-white">Cargando...</div>}>
            <BajaHuespedContent />
        </Suspense>
    );
}