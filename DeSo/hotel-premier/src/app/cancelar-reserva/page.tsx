// componente server != componente client, lo necesito para usar state
"use client";

import {useRouter} from "next/navigation";
import {FormEvent, useState} from "react";
import {cancelarReserva, ReservaDTO} from "@/services/reserva.service";
import {crearHuesped} from "@/services/huespedes.service";
import {Input} from "postcss";

export default function CancelarReserva() {
    const router = useRouter();
    const [error, setError] = useState<string | null>(null);

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


    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();
        setError(null);

        const campos_ob= [
            {key: "apellido", etiq: "apellido"},
            {key: "nombre", etiq: "nombre"},
            {key: "nroHab", etiq: "nroHab"},
            {key: "tipoHab", etiq: "tipoHab"},
            {key: "desde", etiq: "desde"},
            {key: "hasta", etiq: "hata"},
        ];

        const campos_obligatorios = campos_ob.filter((campo) => {
            // porque si no al typescript no le gusta
            const valor = formData[campo.key as keyof typeof formData];
            return !valor || valor.toString().trim() === "";
        });

        if (campos_obligatorios.length > 0) {
            // junto todos los campos con un map donde hago join entre c/u y una coma
            const lista = campos_obligatorios.map((c) => c.etiq).join(", ");
            setError(`Los campos: ${lista} no pueden estar vacíos`);
            return;
        }

        //Esto no esta terminado
        // await enviarDatos(false);
    };

    // en el return mandamos el html que teniamos antes
    return(
        <div className="dark:bg-gray-950 dark:text-white">
            <h1 className="text-[#141414] dark:text-white  mb-8 text-5xl font-bold pb-2">
                Dar alta de huésped
            </h1>

            {/* INICIO DEL FORM */}
            {error && (
                <div className="bg-[#914d45] text-white p-2 mb-4 rounded font-semibold">
                    {error}
                </div>
            )}

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
                    maxLength={50}
                    type="text"
                />
                <InputGroup
                    label="Nombre"
                    name="nombre"
                    value={formData.nombre}
                    onChange={handleChange}
                    placeholder="Doe"
                    maxLength={50}
                    type="text"
                />
                <InputGroup
                    label="nroHab"
                    name="nroHab"
                    value={formData.nroHab}
                    onChange={handleChange}
                    placeholder="Doe"
                    maxLength={50}
                    type="number"
                />
                <InputGroup
                    label="tipoHab"
                    name="tipoHab"
                    value={formData.tipoHab}
                    onChange={handleChange}
                    placeholder="Doe"
                    maxLength={50}
                    type="text"
                />
                <InputGroup
                    label="desde"
                    name="desde"
                    value={formData.desde}
                    onChange={handleChange}
                    placeholder="Doe"
                    maxLength={50}
                    type="date"
                />
                <InputGroup
                    label="hasta"
                    name="hasta"
                    value={formData.hasta}
                    onChange={handleChange}
                    placeholder="Doe"
                    maxLength={50}
                    type="date"
                />
            </form>
        </div>
    );


}


const InputGroup =({
                       label,
                       name,
                       value,
                       onChange,
                       type = "text",
                       placeholder = "",
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
            className="p-2.5 border border-[#ddd] rounded-xl dark:bg-gray-950 dark:text-white focus:outline-none focus:border-[#9ca9be] focus:ring-2 focus:ring-[#4a6491]/20"
        />
    </div>
);

