import {
  DetalleFacturaDTO,
  GenerarFacturaRequestDTO,
} from "@/types/facturacion";

import {
  CriteriosBusq,
  PersonaJuridica,
  EstadiaDTO,
  AlojadoDTO,
} from "@/types/facturacion";

const PUERTO = "http://localhost:8080/api";

// ESTADIA EXISTE?
export async function verificarEstadia(
  id_hab: string,
  hora_checkout: string
): Promise<EstadiaDTO | null> {
  if (!id_hab) return null;

  const params = new URLSearchParams({
    horaSalida: hora_checkout || "",
  });
  console.log("Verificando en back que exista la estadía...");

  try {
    const response = await fetch(
      `${PUERTO}/facturacion/habitacion/${id_hab}/verificar-estadia?${params.toString()}`,
      {
        method: "GET",
        headers: { "Content-Type": "application/json" },
        cache: "no-store",
      }
    );
    if (response.status === 404) {
      console.log("Estadía no encontrada");
      return null;
    }

    if (!response.ok) {
      throw new Error("Error del servidor: " + response.status);
    }

    console.log("Se encontró la estadía. Se devuelve: " + response.json());

    return await response.json();
  } catch (error) {
    console.error("Error buscando estadía: " + error);
    return null;
  }
}

// OCUPANTES ESTADÍA
export async function buscarAlojados(
  id_estadia: string
): Promise<CriteriosBusq[]> {
  const params = new URLSearchParams({
    idEstadia: id_estadia.toString(),
  });
  console.log("Pidiendo ocupantes en el back...");
  try {
    const response = await fetch(
      `${PUERTO}/buscar-huespedes-de-estadia?${params.toString()}`,
      {
        method: "GET",
        headers: { "Content-Type": "application/json" },
        cache: "no-store",
      }
    );

    if (!response.ok) {
      console.log("Ocupantes vacío");
      return [];
    }
    console.log("OCUPANTES: " + response.json());
    return await response.json();
  } catch (error) {
    console.error("Error buscando alojados: " + error);
    return [];
  }
}

export async function obtenerDatosHuesped(
  nroDoc: string,
  tipoDoc: string
): Promise<AlojadoDTO> {
  const params = new URLSearchParams({
    nroDoc: nroDoc,
    tipoDocStr: tipoDoc,
  });

  try {
    const response = await fetch(
      `${PUERTO}/obtener-atributos-huesped?${params.toString()}`,
      {
        method: "GET",
        headers: { "Content-Type": "application/json" },
        cache: "no-store",
      }
    );

    if (!response.ok) throw new Error("Error obteniendo datos del huésped");
    return await response.json();
  } catch (error) {
    console.error(error);
    throw error;
  }
}

// QUE PAGUE PERSONA JURIDICA
export async function buscarTercero(cuit: string): Promise<PersonaJuridica> {
  const params = new URLSearchParams({ cuit: cuit });
  console.log("Pidiendo razón social al back para CUIT:", cuit);
  try {
    const response = await fetch(
      `${PUERTO}/buscar-tercero?${params.toString()}`,
      {
        method: "GET",
        headers: { "Content-Type": "application/json" },
        cache: "no-store",
      }
    );

    if (!response.ok) throw new Error("No se encontró la empresa");

    return await response.json();
  } catch (er) {
    throw er;
  }
}

// ITEMS FACTURA GRILLA
export async function obtenerDetalleFacturacion(
  nroHab: string,
  horaSalida: string
): Promise<DetalleFacturaDTO> {
  const params = new URLSearchParams({
    horaSalida: horaSalida,
  });

  try {
    const response = await fetch(
      `${PUERTO}/facturacion/habitacion/${nroHab}/detalle?${params.toString()}`,
      {
        method: "GET",
        headers: { "Content-Type": "application/json" },
        cache: "no-store",
      }
    );

    if (!response.ok) throw new Error("Error obteniendo el detalle");
    return await response.json();
  } catch (error) {
    console.error(error);
    throw error;
  }
}

// PEDIR FACTURA
export async function generarFacturaFinal(request: GenerarFacturaRequestDTO) {
  try {
    const response = await fetch(`${PUERTO}/facturacion/generar`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(request),
    });

    if (!response.ok) throw new Error("Error generando factura");
    return await response.json();
  } catch (error) {
    throw error;
  }
}
