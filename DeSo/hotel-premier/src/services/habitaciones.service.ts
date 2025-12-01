export interface DisponibilidadDTO {
  idHabitacion: number;
  tipoHab: string;
  fechaDesde: string;
  fechaHasta: string;
  estado: string;
}

export async function buscarEstadoHabitaciones(
  fechaInicio: string,
  fechaFin: string
): Promise<DisponibilidadDTO[]> {
  const params = new URLSearchParams({
    desde: fechaInicio,
    hasta: fechaFin,
  });

  try {
    const response = await fetch(
      `http://localhost:8080/api/habitaciones-disponibilidad${params}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        cache: "no-store",
      }
    );

    if (!response.ok) {
      alert("Error buscar habitaciones disponibles");
    }

    return await response.json();
  } catch (error) {
    console.error(error);
    return [];
  }
}
