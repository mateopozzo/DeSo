"use client";

import { useState, useEffect, FormEvent, Suspense } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import {
  obtenerAtributosHuesped,
  actualizarAlojado,
  HuespedDTO,
} from "@/services/huespedes.service";

// (lo copie igual que en Alta)
interface FormDataState {
  apellido: string;
  nombre: string;
  nacionalidad: string;
  fechaNacimiento: string;
  tipo_documento: string;
  numeroDocumento: string;
  telefono: string;
  email: string;
  calle: string;
  numeroCalle: string;
  piso: string;
  codPostal: string;
  paisResidencia: string;
  provincia: string;
  localidad: string;
  ocupacion: string;
  cuit: string;
  iva: string;
}

const form_limpio: FormDataState = {
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

function GestionarHuespedContent() {
  const router = useRouter();
  const searchParams = useSearchParams();

  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  // Datos actuales del formulario
  const [formData, setFormData] = useState<FormDataState>(form_limpio);

  // Datos originales traídos de la BD (para enviar en el campo 'pre' del DTO)
  const [originalDto, setOriginalDto] = useState<HuespedDTO | null>(null);

  useEffect(() => {
    const cargarDatos = async () => {
      const nroDoc = searchParams.get("nroDoc");
      const tipoDoc = searchParams.get("tipoDoc");

      if (!nroDoc || !tipoDoc) {
        router.push("/buscar-huesped");
        return;
      }

      try {
        const response = await obtenerAtributosHuesped({
          nroDoc,
          tipoDoc,
        });

        if (response.ok) {
          const dto: HuespedDTO = await response.json();
          setOriginalDto(dto); // copia para el backend
          setFormData(mapDtoToForm(dto)); // para que el frontend cambie
        } else {
          alert("No se encontró el huesped.");
          router.push("/buscar-huesped");
        }
      } catch (err) {
        console.error(err);
        alert("Error de conexión (ponele)");
      } finally {
        setLoading(false);
      }
    };

    cargarDatos();
  }, [searchParams, router]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    const finalValue =
      e.target.type === "text" || e.target.tagName === "SELECT"
        ? value.toUpperCase()
        : value;

    setFormData({ ...formData, [name]: finalValue });
  };

  const handleChangeFono = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value.replace(/(?!^\+)\D/g, ""),
    });
  };

  // Actualizar los datos del alojado
  const enviarDatos = async (forzar: boolean = false) => {
    if (!originalDto) return;

    // Conversion dto
    const postDto: HuespedDTO = mapFormToDto(formData);

    try {
      const response = await actualizarAlojado(originalDto, postDto, forzar);

      // duplicado
      if (response.status === 409) {
        const aceptarIgualmente = confirm(
          `¡CUIDADO! El tipo y número de documento ya existen en el sistema.\n\nACEPTAR IGUALMENTE: Guardar cambios de todas formas.\nCANCELAR: Corregir los datos.`
        );

        if (aceptarIgualmente) {
          // sobreescribe la entidad previa
          await enviarDatos(true);
        } else {
          //  corrige datos
          const inputDoc = document.querySelector<HTMLInputElement>(
            'input[name="numeroDocumento"]'
          );
          const selectDoc = document.querySelector<HTMLSelectElement>(
            'select[name="tipo_documento"]'
          );

          if (formData.tipo_documento !== originalDto.tipoDoc && selectDoc)
            selectDoc.focus();
          else if (inputDoc) inputDoc.focus();
        }
        return;
      }

      if (!response.ok) {
        alert("Error al actualizar. Código: " + response.status);
        return;
      }

      alert("La operación ha culminado con éxito.");
      router.push("/home");
    } catch (err) {
      alert("Error al conectarse al servidor");
      console.error(err);
    }
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);

    // verificacion de campos obligatorios (Reutilizadas de alta)
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

    const campos_faltantes = campos_ob.filter((campo) => {
      const valor = formData[campo.key as keyof FormDataState];
      return !valor || valor.toString().trim() === "";
    });

    if (campos_faltantes.length > 0) {
      const lista = campos_faltantes.map((c) => c.etiq).join(", ");
      setError(`Los campos: ${lista} no pueden estar vacíos`);
      return;
    }

    const soloLetrasRegex = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/;

    const camposSoloLetras = [
      { key: "apellido", etiq: "apellido" },
      { key: "nombre", etiq: "nombre" },
      { key: "nacionalidad", etiq: "nacionalidad" },
      { key: "calle", etiq: "calle" },
      { key: "paisResidencia", etiq: "país" },
      { key: "provincia", etiq: "provincia" },
      { key: "localidad", etiq: "localidad" },
      { key: "ocupacion", etiq: "ocupación" },
    ];

    let errores_regex: string[] = [];
    let primera_key_error: string | null = null;

    for (const campo of camposSoloLetras) {
      const valor = formData[campo.key as keyof typeof formData];
      // test true si cumple el regex
      if (valor && !soloLetrasRegex.test(valor.toString())) {
        errores_regex.push(campo.etiq);
        // si es el primer error lo guardo para hacer focus
        if (!primera_key_error) {
          primera_key_error = campo.key;
        }
      }
    }

    if (errores_regex.length > 0) {
      const lista = errores_regex.join(", ");
      setError(`Los siguientes campos solo permiten letras: ${lista}.`);

      if (primera_key_error) {
        const inputElement = document.querySelector<HTMLInputElement>(
          `input[name="${primera_key_error}"]`
        );
        if (inputElement) inputElement.focus();
      }
      return;
    }

    await enviarDatos(false);
  };

  // cancelar general de todo el flujo
  const handleCancelar = () => {
    const confirmar = confirm("¿Desea cancelar la modificación del huesped?");
    if (confirmar) {
      router.back();
    }
  };

  // redireccion al dado de baja
  const handleBorrar = () => {
    // pasa un criterio busq
    if (originalDto) {
      const params = new URLSearchParams();
      params.append("nroDoc", originalDto.nroDoc);
      params.append("tipoDoc", originalDto.tipoDoc);
      params.append("nombre", originalDto.nombre);
      params.append("apellid", originalDto.apellido);
      router.push(`/baja-huesped?${params.toString()}`);
    }
  };

  if (loading) {
    return (
      <div className="p-10 text-xl font-bold dark:text-white">
        Cargando datos del huésped...
      </div>
    );
  }

  return (
    <div className="dark:bg-gray-950 dark:text-white">
      <h1 className="text-[#141414] dark:text-white mb-8 text-5xl font-bold pb-2">
        Modificar huésped
      </h1>

      {error && (
        <div className="bg-[#914d45] text-white p-3 mb-4 rounded-lg font-semibold">
          {error}
        </div>
      )}

      <form
        onSubmit={handleSubmit}
        className="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-6 dark:bg-gray-950 dark:text-white"
      >
        {/* --- FILA 0 - NOMBRE Y APELLIDO --- */}
        <InputGroup
          label="Nombre"
          name="nombre"
          value={formData.nombre}
          onChange={handleChange}
          maxLength={255}
        />
        <InputGroup
          label="Apellido"
          name="apellido"
          value={formData.apellido}
          maxLength={255}
          onChange={handleChange}
        />

        {/* --- FILA 1: DNI  | (NADA) --- */}
        <div className="flex flex-col dark:text-white">
          <label className="mb-2 font-semibold text-[#141414] dark:text-white">
            Documento
          </label>
          <div className="flex gap-2">
            <select
              name="tipo_documento"
              value={formData.tipo_documento}
              onChange={handleChange}
              className="w-1/3 p-2.5 border border-[#ddd] rounded-xl bg-[#f5f7fa] dark:bg-gray-950 dark:text-white"
            >
              <option value="DNI">DNI</option>
              <option value="LC">LC</option>
              <option value="LE">LE</option>
              <option value="PASAPORTE">Pasaporte</option>
            </select>
            <input
              type="text"
              maxLength={50}
              name="numeroDocumento"
              value={formData.numeroDocumento}
              onChange={handleChange}
              className="w-2/3 p-2.5 border border-[#ddd] rounded-xl dark:bg-gray-950 dark:text-white"
            />
          </div>
        </div>

        {/* huequito */}
        <div className="hidden md:block"></div>

        {/* --- FILA 2: CUIT | Razon social --- */}
        <InputGroup
          label="CUIT"
          name="cuit"
          value={formData.cuit}
          onChange={handleChange}
          maxLength={11}
        />

        <div className="flex flex-col">
          <label className="mb-2 font-semibold text-[#141414] dark:text-white">
            Posición frente al IVA
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
            <option value="Excento">Exento</option>
          </select>
        </div>

        {/* --- FILA 3: FECHA NACIMIENTO | NACIONALIDAD --- */}
        <div className="flex flex-col">
          <label className="mb-2 font-semibold text-[#141414] dark:bg-gray-950 dark:text-white">
            Fecha de nacimiento
          </label>
          <input
            type="date"
            name="fechaNacimiento"
            value={formData.fechaNacimiento}
            onChange={handleChange}
            className="p-2.5 border border-[#ddd] rounded-xl dark:bg-gray-950 dark:text-white"
          />
        </div>

        <InputGroup
          label="Nacionalidad"
          name="nacionalidad"
          maxLength={50}
          value={formData.nacionalidad}
          onChange={handleChange}
        />

        {/* --- FILA 4: BLOQUES DE UBICACIÓN (Izquierda: Ubicación General / Derecha: Calle) --- */}

        {/* IZQUIERDA: UBICACIÓN (País, Prov-Loc, CP) */}
        <div className="flex flex-col gap-4 p-4 border border-gray-200 dark:border-gray-800 rounded-xl">
          <h3 className="font-bold text-gray-500 text-sm uppercase">
            Domicilio (Ubicación)
          </h3>

          {/* 1. País */}
          <InputGroup
            label="País"
            name="paisResidencia"
            maxLength={100}
            value={formData.paisResidencia}
            onChange={handleChange}
          />

          {/* 2. Prov - Localidad */}
          <div className="grid grid-cols-2 gap-2">
            <InputGroup
              label="Provincia"
              name="provincia"
              value={formData.provincia}
              onChange={handleChange}
              maxLength={100}
            />
            <InputGroup
              label="Localidad"
              name="localidad"
              value={formData.localidad}
              maxLength={100}
              onChange={handleChange}
            />
          </div>

          {/* 3. CP */}
          <InputGroup
            label="Cód. Postal"
            name="codPostal"
            value={formData.codPostal}
            maxLength={100}
            onChange={handleChange}
          />
        </div>

        {/* DERECHA: CALLE (Calle, Numero, Piso) */}
        <div className="flex flex-col gap-4 p-4 border border-gray-200 dark:border-gray-800 rounded-xl">
          <h3 className="font-bold text-gray-500 text-sm uppercase">
            Domicilio (Calle)
          </h3>

          {/* 1. Calle */}
          <InputGroup
            label="Calle"
            name="calle"
            value={formData.calle}
            onChange={handleChange}
            maxLength={100}
          />

          {/* 2. Número */}
          <InputGroup
            label="Número"
            name="numeroCalle"
            value={formData.numeroCalle}
            onChange={handleChange}
            type="number"
            maxLength={100}
          />

          {/* 3. Piso */}
          <InputGroup
            label="Piso"
            name="piso"
            value={formData.piso}
            onChange={handleChange}
            type="number"
            maxLength={100}
          />
        </div>

        {/* --- FILA 5: EMAIL | TELEFONO --- */}
        <InputGroup
          label="Email"
          name="email"
          value={formData.email}
          onChange={handleChange}
          maxLength={100}
          type="email"
        />
        <InputGroup
          label="Teléfono"
          name="telefono"
          value={formData.telefono}
          onChange={handleChangeFono}
          maxLength={100}
          type="tel"
        />

        {/* --- FILA 6: OCUPACION | (NADA) --- */}
        <InputGroup
          label="Ocupación"
          name="ocupacion"
          value={formData.ocupacion}
          onChange={handleChange}
          maxLength={100}
        />
        <div className="hidden md:block"></div>

        <div className="flex flex-col md:flex-row justify-between items-center gap-4 pt-8 col-span-1 md:col-span-2 mt-4 border-t border-gray-200 dark:border-gray-800">
          <button
            type="button"
            onClick={handleBorrar}
            className="w-full md:w-auto px-8 py-2 rounded-xl font-bold transition duration-300 border border-[#ca695e] text-[#ca695e] hover:bg-[#ca695e] hover:text-white"
          >
            Eliminar huésped
          </button>
          <div className="flex gap-4 w-full md:w-auto justify-end">
            <button
              type="button"
              onClick={() => router.back()}
              className="px-8 py-2 rounded-xl font-bold transition duration-300 dark:border dark:border-white dark:text-white dark:bg-gray-950 border border-[#1a252f] hover:bg-[#b92716] hover:border-[#b92716] hover:text-white"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="px-8 py-2 rounded-xl font-bold transition duration-300 bg-[#52a173] text-white hover:bg-[#10b655]"
            >
              Guardar cambios
            </button>
          </div>
        </div>
      </form>
    </div>
  );
}

export default function GestionarHuesped() {
  return (
    <Suspense fallback={<div className="p-10 text-white">Cargando...</div>}>
      <GestionarHuespedContent />
    </Suspense>
  );
}
// copiado de alta
const InputGroup = ({
  label,
  name,
  value,
  onChange,
  type = "text",
  placeholder = "",
  maxLength,
}: any) => (
  <div className="flex flex-col w-full dark:bg-gray-950 dark:text-white">
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
      className="w-full p-2.5 border border-[#ddd] rounded-xl dark:bg-gray-950 dark:text-white focus:outline-none focus:border-[#9ca9be] focus:ring-2 focus:ring-[#4a6491]/20"
    />
  </div>
);

function mapDtoToForm(dto: HuespedDTO): FormDataState {
  return {
    apellido: dto.apellido || "",
    nombre: dto.nombre || "",
    nacionalidad: dto.nacionalidad || "",
    fechaNacimiento: dto.fechanac || "",
    tipo_documento: dto.tipoDoc || "DNI",
    numeroDocumento: dto.nroDoc || "",
    telefono: dto.telefono || "",
    email: dto.email || "",
    calle: dto.calle || "",
    numeroCalle: dto.nroCalle || "",
    piso: dto.piso || "",
    codPostal: dto.codPost || "",
    paisResidencia: dto.pais || "",
    provincia: dto.prov || "",
    localidad: dto.localidad || "",
    ocupacion: dto.ocupacion || "",
    cuit: dto.cuit || "",
    iva: dto.posicionIva || "Consumidor final",
  };
}

function mapFormToDto(form: FormDataState): HuespedDTO {
  return {
    apellido: form.apellido,
    nombre: form.nombre,
    nacionalidad: form.nacionalidad,
    fechanac: form.fechaNacimiento,
    tipoDoc: form.tipo_documento,
    nroDoc: form.numeroDocumento,
    telefono: form.telefono,
    email: form.email,
    calle: form.calle,
    nroCalle: form.numeroCalle,
    piso: form.piso,
    codPost: form.codPostal,
    pais: form.paisResidencia,
    prov: form.provincia,
    localidad: form.localidad,
    ocupacion: form.ocupacion,
    cuit: form.cuit,
    posicionIva: form.iva,
  };
}
