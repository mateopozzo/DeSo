import { CriteriosBusq, ResultadoBusq } from "./busqueda.service";

export interface EstadiaDTO {
  idHabitacion: number;
  fechaInicio: string;
  fechaFin: string;
  encargado: CriteriosBusq;
  listaInvitados: CriteriosBusq[];
}

const API_URL = "http://localhost:8080/api";

export async function buscarAlojados(
  criterios: CriteriosBusq
): Promise<ResultadoBusq[]> {
  const params = new URLSearchParams();
  if (criterios.apellido) params.append("apellido", criterios.apellido);
  if (criterios.nombre) params.append("nombre", criterios.nombre);
  if (criterios.tipoDoc) params.append("tipoDoc", criterios.tipoDoc);
  if (criterios.nroDoc) params.append("nroDoc", criterios.nroDoc);

  const res = await fetch(`${API_URL}/buscar-alojados?${params}`, {
    method: "GET",
    headers: { "Content-Type": "application/json" },
  });

  if (!res.ok) throw new Error("Error buscando alojados");
  return await res.json();
}

export async function crearEstadia(estadia: EstadiaDTO) {
  const res = await fetch(`${API_URL}/ocupar-habitacion`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(estadia),
  });

  if (!res.ok) {
    const errorMsg = await res.text();
    throw new Error(`Error backend (Hab ${estadia.idHabitacion}): ${errorMsg}`);
  }
  return res.status;
}
