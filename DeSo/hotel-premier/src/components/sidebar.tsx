import Link from "next/link";

interface casosDeUso {
  label: string;
  href: string;
}

interface opciones {
  id: string;
  icon: string;
  alt: string;
  width: number;
  options: casosDeUso[];
}

const MENU_ITEMS: opciones[] = [
  {
    id: "habitaciones",
    icon: "/hab.svg",
    alt: "Habitaciones",
    width: 40,
    options: [
      { label: "Dar de alta huésped", href: "/alta-huesped" },
      { label: "Ocupar habitación", href: "/ocupar-hab" },
    ],
  },
  {
    id: "reservas",
    icon: "/reserva.svg",
    alt: "Reservas",
    width: 40,
    options: [{ label: "Reservar habitación", href: "/reservar-hab" }],
  },
  {
    id: "facturacion",
    icon: "/fac.svg",
    alt: "Facturación",
    width: 50,
    options: [{ label: "Generar factura", href: "/" }],
  },
];

export default function sidebar() {
  return (
    <aside className="w-[100px] hidden h-full lg:flex flex-col items-center py-8 bg-[#f5f7fa] dark:bg-gray-950 border-r border-gray-200 dark:border-gray-800 z-50 relative">
      <ul className="flex flex-col gap-8 items-center w-full">
        <li className="mb-4">
          <Link href="/">
            <img
              src="/ICON-LOGO-LIGHT.svg"
              alt="Logo Hotel Premier"
              width={60}
              height={60}
              className="dark:hidden block hover:scale-105 transition-transform"
            />
            <img
              src="/ICON-LOGO-DARK.svg"
              alt="Logo Hotel Premier"
              width={60}
              height={60}
              className="hidden dark:block hover:scale-105 transition-transform"
            />
          </Link>
        </li>
        {/* ITEM: id, imagen del gestor (icon, alt, width, oscuro o light),  */}
        {MENU_ITEMS.map((item) => (
          <li
            key={item.id}
            className="group relative flex items-center justify-center w-full px-4"
          >
            <div className="cursor-pointer opacity-70 group-hover:opacity-100 transition-opacity duration-300">
              <img
                src={item.icon}
                alt={item.alt}
                width={item.width}
                height={item.width}
                className="dark:invert"
              />
            </div>

            <div className="absolute left-[80%] top-1/2 -translate-y-1/2 pl-4 invisible opacity-0 -translate-x-2 group-hover:visible group-hover:opacity-100 group-hover:translate-x-0 transition-all duration-300 ease-out z-50">
              <div className="bg-white dark:bg-gray-900 shadow-xl rounded-lg overflow-hidden border border-gray-100 dark:border-gray-800 min-w-[200px] p-2">
                <div className="absolute top-1/2 left-2 -translate-y-1/2 -ml-1 w-3 h-3 bg-white dark:bg-gray-900 border-l border-b border-gray-100 dark:border-gray-800 transform rotate-45"></div>

                <div className="flex flex-col gap-1 relative z-10">
                  <span className="text-xs font-semibold text-gray-400 dark:text-gray-500 uppercase px-3 py-1 mb-1 border-b border-gray-100 dark:border-gray-800">
                    {item.alt}
                  </span>
                  {item.options.map((option) => (
                    <Link
                      key={option.label}
                      href={option.href}
                      className="text-sm text-gray-700 dark:text-gray-200 hover:bg-blue-50 dark:hover:bg-blue-900/20 hover:text-blue-600 dark:hover:text-blue-400 px-3 py-2 rounded-md transition-colors whitespace-nowrap"
                    >
                      {option.label}
                    </Link>
                  ))}
                </div>
              </div>
            </div>
          </li>
        ))}
      </ul>
    </aside>
  );
}
