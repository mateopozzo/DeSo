"use client";
import { useEffect } from "react";
import Link from "next/link";

interface HamburguesaProps {
  isOpen: boolean;
  onClose: () => void;
}

export default function hamburguesa({ isOpen, onClose }: HamburguesaProps) {
  useEffect(() => {
    if (isOpen) {
      document.body.style.overflow = "hidden";
    } else {
      document.body.style.overflow = "unset";
    }
    return () => {
      document.body.style.overflow = "unset";
    };
  }, [isOpen]);

  return (
    <div
      className={`fixed inset-0 z-50 flex lg:hidden transition-all duration-300 ${
        isOpen ? "opacity-100 visible" : "opacity-0 invisible"
      }`}
    >
      <div
        className={`fixed inset-0 bg-black/50 backdrop-blur-sm transition-opacity duration-300 ${
          isOpen ? "opacity-100" : "opacity-0"
        }`}
        onClick={onClose}
      />

      <aside
        className={`relative w-72 h-full bg-white dark:bg-gray-900 shadow-2xl flex flex-col transition-transform duration-300 ease-in-out ${
          isOpen ? "translate-x-0" : "-translate-x-full"
        }`}
      >
        <div className="flex items-center justify-between p-6 border-b border-gray-100 dark:border-gray-800">
          <div onClick={onClose} className="shrink-0">
            <Link href="/">
              <img
                src="/ICON-LOGO-LIGHT.svg"
                alt="Logo"
                width={45}
                height={45}
                className="dark:hidden block"
              />
              <img
                src="/ICON-LOGO-DARK.svg"
                alt="Logo"
                width={45}
                height={45}
                className="hidden dark:block"
              />
            </Link>
          </div>

          <button
            onClick={onClose}
            className="p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-800 text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-white transition-colors"
            aria-label="Cerrar menú"
          >
            <svg
              width="20"
              height="20"
              viewBox="0 0 20 20"
              xmlns="http://www.w3.org/2000/svg"
            >
              <line
                x1="0"
                y1="20"
                x2="20"
                y2="0"
                strokeWidth="2"
                stroke="currentColor"
              />
              <line
                x1="0"
                y1="0"
                x2="20"
                y2="20"
                strokeWidth="2"
                stroke="currentColor"
              />
            </svg>
          </button>
        </div>

        <div className="flex-1 overflow-y-auto py-6 px-4">
          <ul className="flex flex-col gap-2">
            <MenuLink
              href="/habitaciones"
              icon="/hab.svg"
              label="Habitaciones"
              onClose={onClose}
            />
            <MenuLink
              href="/reservas"
              icon="/reserva.svg"
              label="Reservas"
              onClose={onClose}
            />
            <MenuLink
              href="/facturacion"
              icon="/fac.svg"
              label="Facturación"
              onClose={onClose}
            />
          </ul>
        </div>

        {/* Footer del menú */}
        <div className="p-6 border-t border-gray-100 dark:border-gray-800 bg-gray-50 dark:bg-gray-900/50">
          <p className="text-xs text-gray-400 text-center uppercase tracking-wider font-semibold">
            Hotel Premier © 2025
          </p>
        </div>
      </aside>
    </div>
  );
}

function MenuLink({
  href,
  icon,
  label,
  onClose,
}: {
  href: string;
  icon: string;
  label: string;
  onClose: () => void;
}) {
  return (
    <li>
      <Link
        href={href}
        onClick={onClose}
        className="flex items-center gap-4 px-4 py-3 rounded-xl hover:bg-gray-100 dark:hover:bg-gray-800 transition-all group active:scale-95 duration-200"
      >
        <div className="p-2 bg-gray-100 dark:bg-gray-800 rounded-lg group-hover:bg-white dark:group-hover:bg-gray-700 transition-colors">
          <img
            src={icon}
            alt={label}
            width={24}
            height={24}
            className="dark:invert opacity-70 group-hover:opacity-100 transition-opacity"
          />
        </div>
        <span className="font-medium text-gray-600 dark:text-gray-300 group-hover:text-gray-900 dark:group-hover:text-white transition-colors">
          {label}
        </span>
      </Link>
    </li>
  );
}
