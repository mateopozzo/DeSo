export default function footer() {
  return (
    <footer className="flex flex-col items-center justify-center text-[#141414] text-md bg-[#f5f7fa] dark:bg-gray-950 dark:text-white mt-32 pb-4">
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
      <span className="text-center">© 2025 Hotel Premier</span>
      <span className="text-center italic">Discipulos de Bjarne</span>
      <span className="text-center">Universidad Tecnológica Nacional FRSF</span>
    </footer>
  );
}
