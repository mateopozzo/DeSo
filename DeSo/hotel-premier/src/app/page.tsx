import Link from "next/link";

export default function Home() {
  return (
    <div className="flex flex-col items-center justify-center text-center h-full">
      <img
        src="/ICON-LOGO-LIGHT.svg"
        alt="Logo Hotel Premier"
        width={300}
        height={300}
        className="dark:hidden block"
      />
      <img
        src="/ICON-LOGO-DARK.svg"
        alt="Logo Hotel Premier"
        width={300}
        height={300}
        className="hidden dark:block"
      />
      <h1 className="text-5xl font-bold dark:text-white text-[#141414] mb-2">
        Hotel Premier
      </h1>
      <h3 className="text-2xl dark:text-white text-[#141414] mb-8">
        Sistema de gestión hotelera
      </h3>
      <p className="text-xl dark:text-white text-[#141414] mb-8">
        Disfruta de nuestro hotel falso en Santa Fe con vista a la laguna
        Setúbal
      </p>

      <Link href="/alta-huesped">
        <button className="px-8 py-3 rounded-2xl font-bold transition duration-300 dark:bg-gray-950 dark:border dark:border-white dark:hover:border-[#10b655] border-gray-950 bg-[#f5f7fa] text-white hover:bg-[#10b655]">
          Ir a dar de alta huésped (CU09)
        </button>
      </Link>
    </div>
  );
}
