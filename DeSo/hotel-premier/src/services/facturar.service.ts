const PUERTO = "http://localhost:8080/api";

export interface AlojadoDTO {
  nombre: string;
  apellido: string;
  tipoDoc: string;
  nroDoc: string;
}

export interface FacturaDTO {
  valor: number;
  servicios: string[];
  monto: number;
  tipoFac: string;
}

export interface PersonaJuridica {
  razonSoc: string;
  cuit: string;
}

// llamada a back para preguntar quienes ocuparon la habitacion
export async function buscarAlojados(
  nro_hab: string,
  hora_checkout: string
): Promise<AlojadoDTO[]> {
  const params = new URLSearchParams({
    nroHab: nro_hab,
    hora: hora_checkout,
  });

  try {
    const response = await fetch(
      `${PUERTO}/buscar-alojados?${params.toString()}`,
      {
        method: "GET",
        headers: { "Content-Type": "application/json" },
        cache: "no-store",
      }
    );

    return await response.json();
  } catch (error) {
    console.error("Error buscando estados: " + error);
    return [];
  }
}

export async function buscarTercero(cuit: string): Promise<PersonaJuridica> {
  console.log("Pidiendo razón social al back");
  try {
    const response = await fetch(`${PUERTO}/buscar-tercero`, {
      method: "GET",
      headers: { "Content-Type": "application/json" },
      cache: "no-store",
    });

    return await response.json();
  } catch (er) {
    throw er;
  }
}

export async function crearFactura(factura: FacturaDTO) {
  console.log("Enviando datos de factura al back");
  try {
    const response = await fetch(`${PUERTO}/facturacion`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(factura),
    });
    console.log("Vuelta de back factura");

    return response.status;
  } catch (error) {
    console.log("Error creando factura" + error);
    throw error;
  }
}
