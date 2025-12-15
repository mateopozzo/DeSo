const PUERTO = "http://localhost:8080/api";

export interface FacturaDTO {
  hola: string;
}

export async function crearFactura(factura: FacturaDTO) {
  console.log("Enviando datos de factura al back");
  try {
    const response = await fetch(`${PUERTO}/facturacion`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(factura),
    });
    console.log("Vuelta de back factura");

    return response.status;
  } catch (error) {
    console.log("Error creando factura" + error);
    throw error;
  }
}
