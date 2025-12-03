"use client";
import { useState } from "react";
import Link from "next/link";
import Hamburguesa from "@/components/hamburguesa";
export default function navbar() {
  const [isHamburguesaOpen, setIsHamburguesaOpen] = useState(false);

  return (
    <>
      <nav className="h-20 w-full flex flex-row items-center justify-between px-6 bg-white dark:bg-gray-950  z-20 sticky top-0">
        <div className="flex items-center gap-4">
          <button
            onClick={() => setIsHamburguesaOpen(true)}
            className="lg:hidden p-2 -ml-2 rounded-md hover:bg-gray-100 dark:hover:bg-gray-800"
            aria-label="Abrir menú"
          >
            <svg
              className="w-8 h-8 text-gray-700 dark:text-white"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M4 6h16M4 12h16M4 18h16"
              ></path>
            </svg>
          </button>

          <Link href="/" className="lg:hidden">
            <img
              src="/ICON-LOGO-LIGHT.svg"
              alt="Logo"
              width={40}
              height={40}
              className="dark:hidden block"
            />
            <img
              src="/ICON-LOGO-DARK.svg"
              alt="Logo"
              width={40}
              height={40}
              className="hidden dark:block"
            />
          </Link>
        </div>

        <div className="flex items-center gap-4">
          <span className="text-[#141414] dark:text-white font-semibold text-lg hidden sm:block">
            Hola, Aldo
          </span>
          <Link href="/">
            <img
              src="/log-out.svg"
              alt="Cerrar sesión"
              width={24}
              height={24}
              className="dark:invert cursor-pointer hover:opacity-70 transition-opacity"
            />
          </Link>
        </div>
      </nav>

      <Hamburguesa
        isOpen={isHamburguesaOpen}
        onClose={() => setIsHamburguesaOpen(false)}
      />
    </>
  );
}
