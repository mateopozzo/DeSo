"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { login } from "@/services/auth.service";

export default function LoginPage() {
  const router = useRouter();

  const [nombre, setNombre] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      await login(nombre, password);

      // Éxito: pasar a "pantalla principal"
      router.push("/home");
    } catch (err: any) {
      // Flujo alternativo CU01: mostrar error y blanquear campos
      setError(err?.message ?? "El usuario o la contraseña no son válidos");
      setPassword("");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="min-h-[calc(100vh-2rem)] flex items-center justify-center">
      <div className="w-full max-w-md rounded-2xl border border-gray-200 dark:border-gray-800 bg-white dark:bg-gray-950 p-6 shadow-sm">
        <div className="flex justify-center items-center mb-4">
          <img
            src="/ICON-LOGO-LIGHT.svg"
            alt="Logo"
            width={80}
            height={80}
            className="dark:hidden block"
          />
          <img
            src="/ICON-LOGO-DARK.svg"
            alt="Logo"
            width={80}
            height={80}
            className="hidden dark:block"
          />
        </div>
        <div className="flex flex-row justify-between">
          <h1 className="text-3xl font-semibold mb-2">Hotel Premier</h1>
          <img
            src="/log-in.svg"
            alt="Cerrar sesión"
            width={40}
            height={40}
            className="dark:invert"
          />
        </div>

        <p className="text-sm opacity-80 mb-6">
          Ingresá con tu usuario y contraseña
        </p>

        {error && (
          <div className="mb-4 rounded-xl border border-red-300 bg-red-50 p-3 text-sm text-red-700 dark:bg-red-950/30 dark:text-red-200 dark:border-red-900">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="flex flex-col">
            <label className="mb-2 font-medium">Nombre de usuario</label>
            <input
              value={nombre}
              onChange={(e) => setNombre(e.target.value)}
              className="p-2.5 border border-gray-300 rounded-xl dark:bg-gray-950"
              placeholder="Ej: conserje"
              autoComplete="username"
            />
          </div>

          <div className="flex flex-col">
            <label className="mb-2 font-medium">Contraseña</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="p-2.5 border border-gray-300 rounded-xl dark:bg-gray-950"
              placeholder="********"
              autoComplete="current-password"
            />
          </div>
          <div className="flex flex-col justify-center items-center">
            <button
              type="submit"
              disabled={loading}
              className="px-8 py-2 justify-center rounded-xl font-bold transition duration-300 text-white bg-[#52a173] hover:bg-[#10b655] cursor-pointer"
            >
              {loading ? "Ingresando..." : "Ingresar"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
