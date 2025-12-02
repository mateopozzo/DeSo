export interface DisponibilidadDTO {
  idHabitacion: number;
  tipoHab: string;
  fechaDesde: string;
  fechaHasta: string;
  estado: string;
}

export interface RequestReserva {
  reservaDTO: {
    fecha_inicio: string;
    fecha_fin: string;
    nombre: string;
    apellido: string;
    telefono: string;
  };
  listaIDHabitaciones: string[];
}

const PUERTO = "http://localhost:8080/api/";

export async function buscarEstadoHabitaciones(
  fechaInicio: string,
  fechaFin: string
  // promise como en javascript, es lo mismo que await
): Promise<DisponibilidadDTO[]> {
  const params = new URLSearchParams({
    desde: fechaInicio,
    hasta: fechaFin,
  });

  try {
    const response = await fetch(
      `${PUERTO}/habitaciones-disponibilidad?` + params.toString(),
      {
        method: "GET",
        headers: { "Content-Type": "application/json" },
        // para que no se quede pegado con datos viejos
        cache: "no-store",
      }
    );

    return await response.json();
  } catch (error) {
    console.error("Error buscando estados: " + error);
    return [];
  }
}

export async function crearReserva(reserva: RequestReserva) {
  try {
    const response = await fetch(`${PUERTO}/reservas`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(reserva),
    });

    return await response.status;
  } catch (error) {
    console.error("Error creando reserva: " + error);
    throw error;
  }
}
