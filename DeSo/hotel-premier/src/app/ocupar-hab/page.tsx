"use client";
import { useState, FormEvent } from "react";
import { useRouter } from "next/navigation";

export default function AltaHuesped() {
  const router = useRouter();
  const [error, setError] = useState<string | null>(null);

  return <div>hola</div>;
}
