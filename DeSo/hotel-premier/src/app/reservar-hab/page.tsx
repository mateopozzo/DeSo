"use client";
import { useState, FormEvent } from "react";
import { useRouter } from "next/navigation";
import EstadoHabitaciones from "@/components/estado_hab";

export default function ReservarHab() {
  const router = useRouter();
  const [error, setError] = useState<string | null>(null);

  return (
    <div className="dark:bg-gray-950 dark:text-white">
      <h1 className="text-[#141414] dark:text-white  mb-8 text-5xl font-bold pb-2">
        Reservar habitación
      </h1>
    </div>
  );
}
