import { CriteriosBusq, ResultadoBusq } from "./busqueda.service";

// Esta interfaz debe ser EXACTA al EstadiaDTO.java
export interface EstadiaDTO {
    idHabitacion: number;
    fechaInicio: string; // Java LocalDate acepta string "YYYY-MM-DD"
    fechaFin: string;
    encargado: CriteriosBusq; // Reutilizamos CriteriosBusq porque tiene los datos necesarios (nombre, apellido, doc)
    listaInvitados: CriteriosBusq[];
}

const API_URL = "http://localhost:8080/api";

// Función para buscar invitados (usa el endpoint de alojados)
export async function buscarAlojados(criterios: CriteriosBusq): Promise<ResultadoBusq[]> {
    const params = new URLSearchParams();
    // Validamos que los campos no sean undefined antes de agregarlos
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

// Persistir la estadía (Crear Reserva/Ocupación)
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