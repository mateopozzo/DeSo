import React, { useState, useRef } from "react";

export default function GrillaHabitaciones({
  idHab,
  desde,
  hasta,
  estados,
  seleccionDias,
}: {
  idHab: { id: number | string; name?: string }[];
  desde: string;
  hasta: string;
  estados: {
    idhab: number | string;
    fecha: string;
    estado: "DISPONIBLE" | "OCUPADA" | "EN MANTENIMIENTO";
  }[];
  seleccionDias?: (sel: { idhab: string | number; fecha: string }[]) => void;
}) {
  const generarFechas = (desde: string, hasta: string) => {
    const fechas: string[] = [];
    let actual = new Date(desde + "T00:00:00");
    const final = new Date(hasta + "T00:00:00");

    while (actual <= final) {
      const iso = actual.toISOString().slice(0, 10);
      fechas.push(iso);
      actual.setDate(actual.getDate() + 1);
    }
    return fechas;
  };

  const fechas = generarFechas(desde, hasta);

  // mapear {fecha -> {idhab -> estado}}
  const buildmapaEstadosHab = () => {
    const mapa = new Map<string, Map<string | number, string>>();
    estados.forEach((s) => {
      if (!mapa.has(s.fecha)) mapa.set(s.fecha, new Map());
      mapa.get(s.fecha)!.set(s.idhab, s.estado);
    });
    return mapa;
  };

  const mapaEstadosHab = buildmapaEstadosHab();

  const stateToColor = (state: string) => {
    if (state === "DISPONIBLE") return "bg-green-300 hover:bg-green-400";
    if (state === "OCUPADA") return "bg-red-400";
    if (state === "EN MANTENIMIENTO") return "bg-yellow-300";
    return "bg-gray-200";
  };

  //useState<Map<string, Set<number | string>>>(new Map())
  const [seleccionado, setSeleccionado] = useState<
    Map<string, Set<number | string>>
  >(new Map());

  const isseleccionado = (idhab: number | string, fecha: string) => {
    return seleccionado.get(fecha)?.has(idhab) ?? false;
  };

  const toggleCell = (
    idhab: number | string,
    fecha: string,
    force?: "add" | "remove"
  ) => {
    setSeleccionado((prev) => {
      const siguiente = new Map(prev);
      const fila = new Set(siguiente.get(fecha) ?? []);

      fila.add(idhab);

      if (fila.size) siguiente.set(fecha, fila);
      else siguiente.delete(fecha);

      return siguiente;
    });
  };

  // useRef para manejar mouse events y que sea draggeable
  const mouseDownRef = useRef(false);
  const modeRef = useRef<"add" | "remove" | null>(null);
  const anchorRef = useRef<{ idhab: string | number; date: string } | null>(
    null
  );

  const onMouseDownCell = (idhab: string | number, date: string) => {
    mouseDownRef.current = true;

    const already = isseleccionado(idhab, date);
    modeRef.current = already ? "remove" : "add";
    anchorRef.current = { idhab, date };

    toggleCell(idhab, date, modeRef.current);
  };

  const onMouseEnterCell = (idhab: string | number, date: string) => {
    if (!mouseDownRef.current || !anchorRef.current) return;

    const anchor = anchorRef.current;

    const fila0 = idHab.findIndex((r) => r.id === anchor.idhab);
    const fila1 = idHab.findIndex((r) => r.id === idhab);
    const col0 = fechas.indexOf(anchor.date);
    const col1 = fechas.indexOf(date);

    const minR = Math.min(fila0, fila1);
    const maxR = Math.max(fila0, fila1);
    const minD = Math.min(col0, col1);
    const maxD = Math.max(col0, col1);

    setSeleccionado(() => {
      const siguiente = new Map();

      for (let di = minD; di <= maxD; di++) {
        const dd = fechas[di];
        const fila = new Set<string | number>();

        for (let ri = minR; ri <= maxR; ri++) {
          const rr = idHab[ri].id;
          if (modeRef.current === "add") fila.add(rr);
        }

        if (fila.size) siguiente.set(dd, fila);
      }

      return siguiente;
    });
  };

  const onMouseUp = () => {
    mouseDownRef.current = false;
    modeRef.current = null;
    anchorRef.current = null;
  };

  const getSeleccionFechas = () => {
    const fechas: { idhab: string | number; fecha: string }[] = [];
    seleccionado.forEach((setidHab, fecha) => {
      setidHab.forEach((idhab) => fechas.push({ idhab, fecha }));
    });
    return fechas;
  };

  const confirm = () => {
    seleccionDias?.(getSeleccionFechas());
  };

  return (
    <div className="w-full" onMouseUp={onMouseUp} onMouseLeave={onMouseUp}>
      <div className="flex justify-between items-center mb-2">
        <div className="text-xs">
          Rango {desde} → {hasta}
        </div>

        <button
          onClick={confirm}
          className="px-3 py-1 bg-blue-600 text-white rounded"
        >
          Confirmar ({getSeleccionFechas().length})
        </button>
      </div>

      <div className="overflow-auto border">
        {/* head */}
        <div className="grid grid-cols-[150px_repeat(auto-fit,minmax(120px,1fr))]">
          <div className="sticky left-0 bg-white border-r px-2 py-2 text-sm">
            Fecha / Hab
          </div>

          {idHab.map((hab) => (
            <div
              key={hab.id}
              className="min-w-[120px] px-2 py-2 border-l text-center text-xs"
            >
              {hab.name ?? `#${hab.id}`}
            </div>
          ))}
        </div>

        {/* fila */}
        {fechas.map((fecha) => (
          <div
            key={fecha}
            className="grid grid-cols-[150px_repeat(auto-fit,minmax(120px,1fr))]"
          >
            {/* fecha fija */}
            <div className="sticky left-0 bg-white border-r border-t px-2 py-2 text-xs">
              {fecha}
            </div>

            {/* celdas */}
            {idHab.map((hab) => {
              const estadoHab =
                mapaEstadosHab.get(fecha)?.get(hab.id) ?? "DISPONIBLE";

              const base = stateToColor(estadoHab);
              const sel = isseleccionado(hab.id, fecha)
                ? "ring-4 ring-blue-400"
                : "";

              const disabled = estadoHab === "OCUPADA";

              return (
                <div
                  key={hab.id + "" + fecha}
                  className={`min-w-[120px] h-10 border-t border-l flex items-center justify-center text-[10px] cursor-pointer select-none ${base} ${sel} ${
                    disabled ? "opacity-50 cursor-not-allowed" : ""
                  }`}
                  onMouseDown={() =>
                    !disabled && onMouseDownCell(hab.id, fecha)
                  }
                  onMouseEnter={() =>
                    !disabled && onMouseEnterCell(hab.id, fecha)
                  }
                >
                  {estadoHab}
                </div>
              );
            })}
          </div>
        ))}
      </div>
    </div>
  );
}
