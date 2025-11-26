import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import Navbar from "@/components/navbar";
import Sidebar from "@/components/sidebar";
import Footer from "@/components/footer";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Hotel Premier",
  description: "Sistema de gestión",
  authors: [{ name: "Discipulos de Bjarne" }],
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="es">
      <body
        className={`${inter.className} h-screen flex overflow-hidden bg-[#f5f7fa] dark:bg-gray-950 text-[#333] dark:text-white`}
      >
        <Sidebar />
        <div className="flex-1 flex flex-col h-full relative">
          <Navbar />
          <main className="flex-1 overflow-y-auto p-8 bg-[#f5f7fa] dark:bg-gray-950">
            <div className="max-w-7xl mx-auto w-full">{children}</div>

            <div className="mt-10">
              <Footer />
            </div>
          </main>
        </div>
      </body>
    </html>
  );
}
