// componentes server != a componente client, lo necesito para usar hooks
"use client";

import { useState, FormEvent } from "react";
import { useRouter } from "next/navigation";

export default function AltaHuesped() {
  const router = useRouter();
  const [error, setError] = useState<string | null>(null);

  const form_limpio = {
    apellido: "",
    nombre: "",
    nacionalidad: "",
    fechaNacimiento: "",
    tipo_documento: "DNI",
    numeroDocumento: "",
    telefono: "",
    email: "",
    calle: "",
    numeroCalle: "",
    piso: "",
    codPostal: "",
    paisResidencia: "",
    provincia: "",
    localidad: "",
    ocupacion: "",
    cuit: "",
    iva: "Consumidor final",
  };

  // usestate para poder ver los cambios dinámicos del formulario
  const [formData, setFormData] = useState(form_limpio);

  // cuando se ingrese algo en el form, se actualiza formData
  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  // cuando se presione enviar, se verifican campos, se forma objeto, se envia a back, se procesa response
  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);

    if (!formData.apellido || !formData.nombre || !formData.numeroDocumento) {
      setError("Los campos obligatorios no pueden estar vacíos.");
      return;
    }

    const dataDTO = {
      apellido: formData.apellido,
      nombre: formData.nombre,
      nacionalidad: formData.nacionalidad,
      fechanac: formData.fechaNacimiento,
      tipo_doc: formData.tipo_documento,
      nro_doc: formData.numeroDocumento,
      telefono: formData.telefono,
      email: formData.email,
      calle: formData.calle,
      nro_calle: formData.numeroCalle,
      piso: formData.piso,
      codPost: formData.codPostal,
      pais: formData.paisResidencia,
      prov: formData.provincia,
      localidad: formData.localidad,
      ocupacion: formData.ocupacion,
      cuit: formData.cuit,
      posicionIva: formData.iva,
    };

    // enviar post a back
    try {
      const response = await fetch("http://localhost:8000/api/huesped", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(dataDTO),
      });

      if (!response.ok)
        throw new Error(`Error en el servidor: Código ${response.status}`);

      const confirmar = confirm(
        `El huésped ${formData.nombre} ${formData.apellido} ha sido cargado satisfactoriamente en el sistema. \n\n¿Desea cargar otro?`
      );

      if (confirmar) {
        setFormData(form_limpio);
      } else {
        router.push("/");
      }
    } catch (err) {
      alert("Error al enviar los datos");
      console.error(err);
    }
  };

  // en el return mandamos el html que teniamos antes
  return (
    <div className="dark:bg-gray-950 dark:text-white">
      <h1 className="text-[#141414] dark:text-white  mb-8 text-5xl font-bold pb-2">
        Dar de alta un huésped
      </h1>

      {/* INICIO DEL FORM */}
      {error && (
        <div className="bg-[#b67c75] text-white p-2 mb-5 rounded font-semibold">
          {error}
        </div>
      )}

      <form
        onSubmit={handleSubmit}
        className="grid grid-cols-1 md:grid-cols-4 gap-5 dark:bg-gray-950 dark:text-white"
      >
        <InputGroup
          label="Apellido"
          name="apellido"
          value={formData.apellido}
          onChange={handleChange}
          placeholder="Doe"
        />
        <InputGroup
          label="Nombre"
          name="nombre"
          value={formData.nombre}
          onChange={handleChange}
          placeholder="John"
          type="text"
        />
        <InputGroup
          label="Nacionalidad"
          name="nacionalidad"
          value={formData.nacionalidad}
          onChange={handleChange}
          placeholder="Argentina"
          type="text"
        />

        <div className="flex flex-col">
          <label className="mb-2 font-semibold text-[#141414] dark:bg-gray-950 dark:text-white">
            Fecha de nacimiento
          </label>
          <input
            type="date"
            name="fechaNacimiento"
            value={formData.fechaNacimiento}
            onChange={handleChange}
            className="p-2.5 border border-[#ddd] rounded-xl  dark:text-white focus:outline-none focus:border-[#9ca9be] focus:ring-2 focus:ring-[#4a6491]/20"
          />
        </div>

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
        />
        <InputGroup
          label="Teléfono"
          name="telefono"
          value={formData.telefono}
          onChange={handleChange}
          type="tel"
          placeholder="Cod. de área + número de teléfono"
        />
        <InputGroup
          label="Email"
          name="email"
          value={formData.email}
          onChange={handleChange}
          type="email"
          placeholder="Opcional"
        />

        <InputGroup
          label="Calle"
          name="calle"
          value={formData.calle}
          onChange={handleChange}
          placeholder="Lavaisse"
          type="text"
        />
        <InputGroup
          label="Número"
          name="numeroCalle"
          value={formData.numeroCalle}
          onChange={handleChange}
          placeholder="610"
          type="number"
        />
        <InputGroup
          label="Piso"
          name="piso"
          value={formData.piso}
          onChange={handleChange}
          placeholder="Opcional"
          type="number"
        />
        <InputGroup
          label="Código Postal"
          name="codPostal"
          value={formData.codPostal}
          onChange={handleChange}
          placeholder="3000"
          type="text"
        />

        <InputGroup
          label="País"
          name="paisResidencia"
          value={formData.paisResidencia}
          onChange={handleChange}
          placeholder="Argentina"
          type="text"
        />
        <InputGroup
          label="Provincia"
          name="provincia"
          value={formData.provincia}
          onChange={handleChange}
          placeholder="Santa Fe"
          type="text"
        />
        <InputGroup
          label="Localidad"
          name="localidad"
          value={formData.localidad}
          onChange={handleChange}
          placeholder="Santa Fe"
          type="text"
        />

        <InputGroup
          label="Ocupación"
          name="ocupacion"
          value={formData.ocupacion}
          onChange={handleChange}
          placeholder="Empleado"
          type="text"
        />
        <InputGroup
          label="CUIT"
          name="cuit"
          value={formData.cuit}
          onChange={handleChange}
          placeholder="Opcional. Sin guiones ni puntos"
          type="text"
        />

        <div className="flex flex-col">
          <label className="mb-2 font-semibold text-[#141414]  dark:text-white">
            Condición IVA
          </label>
          <select
            name="iva"
            value={formData.iva}
            onChange={handleChange}
            className="p-2.5 border border-[#ddd] rounded-xl bg-[#f5f7fa] dark:bg-gray-950 dark:text-white"
          >
            <option value="Consumidor final">Consumidor final</option>
            <option value="Responsable inscripto">Responsable inscripto</option>
            <option value="Monotributista">Monotributista</option>
            <option value="Monotributista">Excento</option>
          </select>
        </div>
        {/* FIN DEL FORM */}
      </form>
      {/* INICIO DE BOTONES */}
      <div className="flex flex-col lg:flex-row  justify-center gap-4 mt-12 pt-5">
        <button
          type="button"
          onClick={() => setFormData(form_limpio)}
          className="cursor-pointer px-8 py-2 rounded-xl font-bold transition duration-300 bg-[#ca695e] text-white hover:bg-[#b92716]"
        >
          Reiniciar
        </button>
        <button
          type="button"
          onClick={() => router.push("/")}
          className="cursor-pointer px-8 py-2 rounded-xl font-bold transition duration-300 dark:border dark:border-white dark:text-white dark:bg-gray-950 dark:hover:border-[#b92716] text-[#1a252f] border border-[#1a252f] hover:bg-[#b92716] hover:text-white hover:border-[#b92716]"
        >
          Cancelar
        </button>
        <button
          type="submit"
          className="cursor-pointer px-8 py-2 rounded-xl font-bold transition duration-300 bg-[#52a173] text-white hover:bg-[#10b655]"
        >
          Siguiente
        </button>
      </div>
      {/* FIN DE BOTONES */}
    </div>
  );
}

// hago una vez el campo y lo repito
const InputGroup = ({
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
