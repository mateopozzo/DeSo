import {HuespedDTO} from "@/services/huespedes.service";

export interface ReservaDTO {
    apellido: string,
    nombre: string,
    nroHab: string,
    tipoHab: string,
    desde: string,
    hasta: string,
}

export async function cancelarReserva(data: ReservaDTO, forzar: boolean = false) {
    console.log(data);
    const baseUrl = "http://localhost:8080/api/reserva";
    const url = forzar ? `${baseUrl}?force=true` : baseUrl;

    const response = await fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
    });

    return response;
}