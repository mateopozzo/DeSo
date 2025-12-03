// componente server != componente client, lo necesito para usar state
"use client";

import { useState, FormEvent } from "react";
import { useRouter } from "next/navigation";
import { crearHuesped, HuespedDTO } from "@/services/huespedes.service";

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

  const enviarDatos = async (forzar: boolean = false) => {
    const dataDTO: HuespedDTO = {
      apellido: formData.apellido,
      nombre: formData.nombre,
      nacionalidad: formData.nacionalidad,
      fechanac: formData.fechaNacimiento,
      tipoDoc: formData.tipo_documento,
      nroDoc: formData.numeroDocumento,
      telefono: formData.telefono,
      email: formData.email,
      calle: formData.calle,
      nroCalle: formData.numeroCalle,
      piso: formData.piso,
      codPost: formData.codPostal,
      pais: formData.paisResidencia,
      prov: formData.provincia,
      localidad: formData.localidad,
      ocupacion: formData.ocupacion,
      cuit: formData.cuit,
      posicionIva: formData.iva,
    };

    try {
      const response = await crearHuesped(dataDTO, forzar);

      if (response.status === 409) {
        const sobreescribir = confirm(
          `¡CUIDADO! El documento ${formData.tipo_documento} ${formData.numeroDocumento} ya existe.\n\nACEPTAR: Sobreescribir los datos viejos con estos nuevos.\nCANCELAR: Corregir el número de documento.`
        );

        if (sobreescribir) {
          enviarDatos(true);
        } else {
          const inputDoc = document.querySelector<HTMLInputElement>(
            'input[name="numeroDocumento"]'
          );
          if (inputDoc) inputDoc.focus();
        }
        return;
      }

      if (!response.ok) {
        alert("Error. Código: " + response.status);
        return;
      }
      const confirmar = confirm(
        `El huésped ${formData.nombre} ${formData.apellido} ha sido satisfactoriamente cargado al sistema.\n\n¿Desea cargar otro?`
      );

      if (confirmar) {
        setFormData(form_limpio);
        document
          .querySelector<HTMLInputElement>('input[name="apellido"]')
          ?.focus();
      } else {
        router.push("/");
      }
    } catch (err) {
      alert("Error al conectarse al servidor");
      console.error(err);
    }
  };

  // cuando se presione enviar, se verifican campos, se forma objeto, se envia a back, se procesa response
  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);

    const campos_ob = [
      { key: "apellido", etiq: "apellido" },
      { key: "nombre", etiq: "nombre" },
      { key: "nacionalidad", etiq: "nacionalidad" },
      { key: "fechaNacimiento", etiq: "fecha de nacimiento" },
      { key: "tipo_documento", etiq: "tipo de doc." },
      { key: "numeroDocumento", etiq: "nro. de doc." },
      { key: "calle", etiq: "calle" },
      { key: "numeroCalle", etiq: "nro. de calle" },
      { key: "codPostal", etiq: "cód. postal" },
      { key: "paisResidencia", etiq: "país" },
      { key: "provincia", etiq: "provincia" },
      { key: "ocupacion", etiq: "ocupación" },
      { key: "iva", etiq: "posición frente al IVA" },
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

    await enviarDatos(false);
  };

  // en el return mandamos el html que teniamos antes
  return (
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
          placeholder="John"
          type="text"
          maxLength={50}
        />
        <InputGroup
          label="Nacionalidad"
          name="nacionalidad"
          value={formData.nacionalidad}
          onChange={handleChange}
          placeholder="Argentina"
          type="text"
          maxLength={50}
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
          maxLength={20}
        />
        <InputGroup
          label="Teléfono"
          name="telefono"
          value={formData.telefono}
          onChange={handleChange}
          type="tel"
          placeholder="Cod. de área + número de teléfono"
          maxLength={20}
        />
        <InputGroup
          label="Email"
          name="email"
          value={formData.email}
          onChange={handleChange}
          type="email"
          placeholder="Opcional"
          maxLength={50}
        />

        <InputGroup
          label="Calle"
          name="calle"
          value={formData.calle}
          onChange={handleChange}
          placeholder="Lavaisse"
          type="text"
          maxLength={30}
        />
        <InputGroup
          label="Número"
          name="numeroCalle"
          value={formData.numeroCalle}
          onChange={handleChange}
          placeholder="610"
          type="number"
          maxLength={20}
        />
        <InputGroup
          label="Piso"
          name="piso"
          value={formData.piso}
          onChange={handleChange}
          placeholder="Opcional"
          type="number"
          maxLength={10}
        />
        <InputGroup
          label="Código Postal"
          name="codPostal"
          value={formData.codPostal}
          onChange={handleChange}
          placeholder="3000"
          type="text"
          maxLength={20}
        />

        <InputGroup
          label="País"
          name="paisResidencia"
          value={formData.paisResidencia}
          onChange={handleChange}
          placeholder="Argentina"
          type="text"
          maxLength={30}
        />
        <InputGroup
          label="Provincia"
          name="provincia"
          value={formData.provincia}
          onChange={handleChange}
          placeholder="Santa Fe"
          type="text"
          maxLength={30}
        />
        <InputGroup
          label="Localidad"
          name="localidad"
          value={formData.localidad}
          onChange={handleChange}
          placeholder="Santa Fe"
          type="text"
          maxLength={30}
        />

        <InputGroup
          label="Ocupación"
          name="ocupacion"
          value={formData.ocupacion}
          onChange={handleChange}
          placeholder="Empleado"
          type="text"
          maxLength={30}
        />
        <InputGroup
          label="CUIT"
          name="cuit"
          value={formData.cuit}
          onChange={handleChange}
          placeholder="Opcional. Sin guiones ni puntos"
          type="text"
          maxLength={11}
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
        {/* INICIO DE BOTONES */}
        <div className="flex flex-col lg:flex-row  justify-center gap-4 pt-5 col-span-4">
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

        {/* FIN DEL FORM */}
      </form>
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
