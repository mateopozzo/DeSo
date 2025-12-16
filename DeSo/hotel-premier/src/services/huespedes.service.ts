
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

export interface ActualizarAlojadoDTO{
  pre : HuespedDTO;
  post : HuespedDTO;
}

import {CriteriosBusq} from "@/services/busqueda.service";

export async function crearHuesped(data: HuespedDTO, forzar: boolean = false) {
  console.log(data);
  const baseUrl = "http://localhost:8080/api/huesped";
  const url = forzar ? `${baseUrl}?force=true` : baseUrl;

  const response = await fetch(url, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });

  return response;
}

export async function obtenerAtributosHuesped(criterio: CriteriosBusq) {

  const urlBase = `http://localhost:8080/api/obtener-atributos-huesped`;
  const url = urlBase + `?nroDoc=${criterio.nroDoc}&tipoDocStr=${criterio.tipoDoc}`;

  const response = await fetch(url, {
    method : "GET",
  });

  return response;
}

export async function actualizarAlojado(pre: HuespedDTO, post: HuespedDTO, force : boolean){

  const url = `http://localhost:8080/api/actualizar-alojado${force ? '?force=true' : '?force=false'}`;

  const body:ActualizarAlojadoDTO = {pre:pre, post:post};

  console.log(JSON.stringify(body));

  const response = await fetch (url, {
    method : "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body)
  });

  return response;
}

export async function eliminarHuesped(data: HuespedDTO){
  const url = `http://localhost:8080/api/eliminar-huesped`;

  const response = await fetch(url, {
    method: "DELETE",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });

  return response;
}
