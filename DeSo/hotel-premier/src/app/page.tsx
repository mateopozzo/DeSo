import Link from "next/link";
import Card from "../components/card";

export default function Home() {
  return (
    <div className="flex flex-col items-center text-center h-full">
      <img
        src="/ICON-LOGO-LIGHT.svg"
        alt="Logo Hotel Premier"
        width={200}
        height={200}
        className="dark:hidden block"
      />
      <img
        src="/ICON-LOGO-DARK.svg"
        alt="Logo Hotel Premier"
        width={200}
        height={200}
        className="hidden dark:block"
      />
      <h1 className="text-5xl font-bold dark:text-white text-[#141414] mb-2">
        Hotel Premier
      </h1>
      <h3 className="text-xl dark:text-white text-[#141414] mb-8">
        Sistema de gestión hotelera
      </h3>

      <div className="flex flex-col items-start gap-4 md:grid lg:grid-cols-2 xl:grid-cols-3">
        <Card
          href="/alta-huesped"
          iconSrc="/hab.svg"
          gestor="Habitaciones"
          title="Dar alta de huésped"
          desc="Ingresa un nuevo huésped en el sistema"
        />

        <Card
          href="/"
          iconSrc="/reserva.svg"
          gestor="Reservas"
          title="Reservar habitación"
          desc="Genera una reserva para un huésped"
        />
        <Card
          href="/"
          iconSrc="/fac.svg"
          gestor="Facturación"
          title="Generar factura"
          desc="Genera una nueva factura"
        />
        <Card
          href="/"
          iconSrc="/hab.svg"
          gestor="Habitaciones"
          title="Dar de baja huésped"
          desc="Elimina un huésped existente en el sistema"
        />

        <Card
          href="/"
          iconSrc="/reserva.svg"
          gestor="Reservas"
          title="Cancelar reserva"
          desc="Cancela una reserva existente"
        />
        <Card
          href="/"
          iconSrc="/fac.svg"
          gestor="Facturación"
          title="Ingresar pago"
          desc="Ingresa el pago de una factura"
        />
        <Card
          href="/ocupar-hab"
          iconSrc="/hab.svg"
          gestor="Habitaciones"
          title="Ocupar habitación"
          desc="Ingresa un huésped a su habitación"
        />
      </div>
    </div>
  );
}
