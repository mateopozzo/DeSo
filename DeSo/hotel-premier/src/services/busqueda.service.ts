import {DisponibilidadDTO} from "@/services/habitaciones.service";

export interface CriteriosBusq {
  apellido?: string;
  nombre?: string;
  tipoDoc?: string;
  nroDoc?: string;
}

export interface ResultadoBusq {
  apellido: string;
  nombre: string;
  tipoDoc: string;
  nroDoc: string;
}

export async function busqueda(criterios: CriteriosBusq): Promise<ResultadoBusq[]> {
  const params = new URLSearchParams();
  if (criterios.apellido) params.append("apellido", criterios.apellido);
  if (criterios.nombre) params.append("nombre", criterios.nombre);
  if (criterios.tipoDoc) params.append("tipoDoc", criterios.tipoDoc);
  if (criterios.nroDoc) params.append("nroDoc", criterios.nroDoc);

  const res = await fetch(
    `http://localhost:8080/api/buscar-huesped?${params}`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    }
  );

  if (!res.ok) {
    throw new Error("Error al buscar huéspedes");
  }

  return await res.json();
}
