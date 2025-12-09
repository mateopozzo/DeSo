export interface DisponibilidadDTO {
  idHabitacion: number;
  idReserva: number;
  tipoHab: string;
  fecha_inicio: string;
  fecha_fin: string;
  estado: string;
}

export interface RequestReserva {
  reservaDTO: {
    fecha_inicio: string;
    fecha_fin: string;
    nombre: string;
    apellido: string;
    telefono: string;
    estado: "Reservada";
  };
  listaIDHabitaciones: string[];
}

export interface Habitacion {
  nroHab: number;
  tipo_hab: string;
  estado_hab: string;
}

export interface DatosRes {
  fecha_inicio: string;
  fecha_fin: string;
  nombre: string;
  apellido: string;
  telefono: string;
  estado: string;
}

const PUERTO = "http://localhost:8080/api";

export async function pedirHabs(): Promise<Habitacion[]> {
  try {
    const response = await fetch(`${PUERTO}/habitacion`, {
      method: "GET",
      headers: { "Content-Type": "application/json" },
      cache: "no-store",
    });

    return await response.json();
  } catch (error) {
    console.error("Error pidiendo habitaciones: " + error);
    return [];
  }
}

export async function buscarEstadoHabitaciones(
  fechaInicio: string,
  fechaFin: string
): Promise<DisponibilidadDTO[]> {
  const params = new URLSearchParams({
    fecha_inicio: fechaInicio,
    fecha_fin: fechaFin,
  });

  try {
    const response = await fetch(
      // va con ? porque si no piensa que es una url de verdad y no que le estoy dando parametroa
      `${PUERTO}/habitaciones-disponibilidad?${params.toString()}`,
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
  console.log("Enviando reserva al back");
  try {
    const response = await fetch(`${PUERTO}/reserva`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(reserva),
    });
    console.log("Vuelta de back");

    return response.status;
  } catch (error) {
    console.log("Error captado en service - Error guardando reserva");
    console.error("Error creando reserva: " + error);
    throw error;
  }
}

export async function verificarRes(
  idHab: number,
  fechaInicio: string,
  fechaFin: string
): Promise<DatosRes[]> {
  const consulta = [
    {
      idHabitacion: idHab,
      fechaInicio: fechaInicio,
      fechaFin: fechaFin,
    },
  ];

  try {
    console.log(
      "Pidiendo verificar reserva a back con payload:",
      JSON.stringify(consulta)
    );

    const response = await fetch(`${PUERTO}/obtener-reservas`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(consulta),
      cache: "no-store",
    });

    if (!response.ok) {
      throw new Error(`Error al volver back: ${response.status}`);
    }

    return await response.json();
  } catch (error) {
    console.error("Error verificando reservas: " + error);
    return [];
  }
}
