import Link from "next/link";

interface ActionCardProps {
  href: string;
  iconSrc: string;
  gestor: string;
  title: string;
  desc: string;
}

export default function ActionCard({
  href,
  iconSrc,
  gestor,
  title,
  desc,
}: ActionCardProps) {
  return (
    <Link
      href={href}
      className="h-full w-full group mx-auto block max-w-xs space-y-3 bg-white dark:bg-gray-950 border-2 dark:border-gray-500 border-gray-800 rounded-2xl p-4 hover:border-sky-500"
    >
      <div className="flex gap-2 justify-center">
        <img src={iconSrc} alt={gestor} width="30px" className="dark:invert" />
        <div className="text-lg font-semibold text-gray-900 dark:group-hover:text-white dark:text-white">
          {title}
        </div>
      </div>
      <p className="text-sm text-gray-500 dark:group-hover:text-white dark:text-gray-400">
        {desc}
      </p>
    </Link>
  );
}
