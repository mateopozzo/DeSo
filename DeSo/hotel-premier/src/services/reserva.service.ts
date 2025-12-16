
// DTO para los filtros de búsqueda (lo que envías al backend)
export interface CriterioBusquedaDTO {
    apellido: string;
    nombre?: string;
}

// DTO para mostrar en la grilla (lo que recibes del backend: ReservaGrillaDTO)
export interface ReservaGrillaDTO {
    idReserva: number;
    apellido: string;
    nombre: string;
    fechaInicio: string;
    fechaFin: string;
    listaIDHabitaciones: HabitacionesReservaDTO []
}

export interface HabitacionesReservaDTO{
    numero: number;
    tipo: string
}

const BASE_URL = "http://localhost:8080/api";

export async function buscarReservas(criterio: CriterioBusquedaDTO): Promise<ReservaGrillaDTO[]> {
    const response = await fetch(`${BASE_URL}/reservas/buscar`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(criterio),
    });

    console.log(response);
    if (!response.ok) {
        // Manejo básico de error
        throw new Error("Error al buscar reservas");
    }

    return await response.json();
}

export async function cancelarReserva(idReserva: number): Promise<boolean> {
    const response = await fetch(`${BASE_URL}/reservas/${idReserva}`, {
        method: "DELETE",
        headers: { "Content-Type": "application/json" }
    });

    // 204 No Content significa éxito en el delete
    return response.status === 204;
}