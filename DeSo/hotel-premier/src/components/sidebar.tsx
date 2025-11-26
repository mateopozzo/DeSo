import Link from "next/link";

export default function sidebar() {
  return (
    <aside className="w-[100px] hidden h-full lg:flex flex-col items-center py-8  bg-[#f5f7fa] dark:bg-gray-950 ">
      <ul className="flex flex-col gap-8 items-center w-full">
        <li>
          <Link href="/page.tsx">
            <img
              src="/ICON-LOGO-LIGHT.svg"
              alt="Logo Hotel Premier"
              width={60}
              height={60}
              className="dark:hidden block"
            />
            <img
              src="/ICON-LOGO-DARK.svg"
              alt="Logo Hotel Premier"
              width={60}
              height={60}
              className="hidden dark:block"
            />
          </Link>
        </li>
        <li className="cursor-pointer hover:opacity-80 transition-opacity">
          <img
            src="/hab.svg"
            alt="Habitaciones"
            width={40}
            height={40}
            className="dark:invert"
          />
        </li>
        <li className="cursor-pointer hover:opacity-80 transition-opacity">
          <img
            src="/reserva.svg"
            alt="Reservas"
            width={40}
            height={40}
            className="dark:invert"
          />
        </li>
        <li className="cursor-pointer hover:opacity-80 transition-opacity">
          <img
            src="/fac.svg"
            alt="Facturación"
            width={50}
            height={50}
            className="dark:invert"
          />
        </li>
      </ul>
    </aside>
  );
}
