// cada service necesita: objeto que se va a intercambiar + llamda al back
// del manejo de response se encarga cada página
export interface HuespedDTO {
  apellido: string;
  nombre: string;
  nacionalidad: string;
  fechanac: string;
  tipoDoc: string;
  nroDoc: string;
  telefono: string;
  email: string;
  calle: string;
  nroCalle: string;
  piso: string;
  codPost: string;
  pais: string;
  prov: string;
  localidad: string;
  ocupacion: string;
  cuit: string;
  posicionIva: string;
}

export async function crearHuesped(data: HuespedDTO, forzar: boolean = false) {
  const baseUrl = "http://localhost:8080/api/huesped";
  const url = forzar ? `${baseUrl}?force=true` : baseUrl;

  const response = await fetch(url, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });

  return response;
}
