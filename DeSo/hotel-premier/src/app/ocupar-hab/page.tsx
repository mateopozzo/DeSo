"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import GrillaHabitaciones from "@/components/estado_hab";

export default function AltaHuesped() {
  const router = useRouter();
  const [error, setError] = useState<string | null>(null);

  return <p>holi</p>;
}
