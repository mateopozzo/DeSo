export interface LoginResponseDTO {
  id: number;
  nombre: string;
  permisos: number;
}

const API_URL = "http://localhost:8080/api";

export async function login(
  nombre: string,
  password: string
): Promise<LoginResponseDTO> {
  const res = await fetch(`${API_URL}/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ nombre, password }),
  }).catch(() => {
    throw new Error(
      "Error al conectarse con el servidor. Verifique su conexión."
    );
  });

  // Si el backend devuelve 401 o 400, mostramos el mensaje del CU
  if (!res.ok) {
    // si viene json con {message} lo tomo; si no, uso mensaje fijo
    try {
      const data = await res.json();
      throw new Error(
        data?.message ?? "El usuario o la contraseña no son válidos"
      );
    } catch {
      throw new Error("El usuario o la contraseña no son válidos");
    }
  }

  return await res.json();
}
