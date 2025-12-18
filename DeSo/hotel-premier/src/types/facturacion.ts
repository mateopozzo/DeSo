// servicio DTO
export interface ServicioDTO {
  idServicio: number;
  tipoServicio: string;
  precio: number;
  descripcion?: string;
  cantidad?: number;
}

// DetalleFacturaDTO
export interface DetalleFacturaDTO {
  idEstadia: number;
  habitacion: string;
  costoEstadia: number;
  consumos: ServicioDTO[];
  montoTotal: number;
  tipoFacturaSugerida: "A" | "B" | "C" | "E";
}

// POST a generar GenerarFacturaRequestDTO
export interface GenerarFacturaRequestDTO {
  idEstadia: number;
  tipoFactura: string;
  // aca depende de si es alojado o persona juridica
  destinatario: string;
  cobrarAlojamiento: boolean;
  idsConsumosAIncluir: number[];
  responsableId?: string | number;
  responsableTipo?: "HUESPED" | "TERCERO";
}

export interface CriteriosBusq {
  nombre: string;
  apellido: string;
  tipoDoc: string;
  nroDoc: string;
  cuit?: string;
}

export interface AlojadoDTO extends CriteriosBusq {
  fechanac: string;
  cuit?: string;
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

export interface EstadiaDTO {
  idEstadia: number;
  idHabitacion: number;
  idReserva: number;
  fechaInicio: string;
  fechaFin: string;
  encargado: CriteriosBusq;
  listaInvitados: CriteriosBusq[];
}
