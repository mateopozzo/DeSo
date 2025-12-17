import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Hotel Premier",
  description: "Sistema de gestión",
  authors: [{ name: "Discipulos de Bjarne" }],
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="es">
      <body
        className={`${inter.className} h-screen bg-[#f5f7fa] dark:bg-gray-950 text-[#333] dark:text-white`}
      >
        {children}
      </body>
    </html>
  );
}