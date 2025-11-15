document
  .getElementById("alta-huesped")
  .addEventListener("submit", async (e) => {
    e.preventDefault();

    const form = e.target;

    const numeros = /^[0-9]+$/;
    const formato_email = /^[A-Za-z0-9._%+-]+@[A-Za-z]+\.(com)$/;

    const camposObligatorios = [
      { id: "apellido", nombre: "Apellido" },
      { id: "nombre", nombre: "Nombre" },
      { id: "numero-documento", nombre: "Número de documento" },
      { id: "fecha-nacimiento", nombre: "Fecha de nacimiento" },
      { id: "calle", nombre: "Calle" },
      { id: "numero-calle", nombre: "Número" },
      { id: "localidad", nombre: "Localidad" },
      { id: "provincia", nombre: "Provincia" },
      { id: "pais-residencia", nombre: "País de residencia" },
      { id: "nacionalidad", nombre: "Nacionalidad" },
      { id: "iva", nombre: "IVA" },
      { id: "ocupacion", nombre: "Ocupación" },
      { id: "telefono", nombre: "Teléfono" },
      { id: "email", nombre: "Correo electrónico" },
    ];

    const faltantes = [];

    camposObligatorios.forEach((campo) => {
      const valor = form[campo.id].value.trim();
      if (!valor) faltantes.push(campo.nombre.toLowerCase());
    });

    if (faltantes.length > 0) {
      const mensajeError =
        "Los campos " + faltantes.join(", ") + " no pueden estar vacíos.";

      let msg_error_campo = document.getElementById("error-campos");
      msg_error_campo.innerText = mensajeError;
      msg_error_campo.style.display = "block";

      faltantes.forEach((campo) => {
        const campo_input = document.getElementById(campo.id);
        campo_input.classList.add("input-error");
      });
    } else {
      msg_error_campo.style.display = "none";
    }

    return;
  });

// si no es vacío que solo tenga num
if (cuit !== "" && !numeros.test(cuit)) {
  alert("El CUIT solo puede contener números.");
  return;
}

if (!formato_email.test(email)) {
  alert("El correo debe tener un formato válido: ejemplo@algo.com");
  return;
}

// si no tiene errores no retorna y arma el objeto
const data = {
  apellido,
  nombre,
  tipoDoc,
  documento,
  fechaNacimiento,
  calle,
  numero_calle,
  piso: form.piso.value || null,
  localidad,
  provincia,
  paisResidencia,
  cuit: cuit || null,
  iva,
  ocupacion,
  telefono,
  email,
};

try {
  const response = await fetch("http://localhost:8080/api/huespedes", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    alert("Error al guardar el huésped.");
    return;
  }

  const msg = await response.text();
  console.log("Éxito (server): ", msg);

  // confirm me da opción de confirmar o cancelar en lugar de alert que solo se puede dar aceptar
  const cargarOtro = confirm(
    "El huésped " +
      nombre +
      " " +
      apellido +
      "ha sido cargado satisfactoriamente en el sistema. \n\n¿Desea cargar otro?"
  );

  // si cargarotro true limpio el form else fin
  if (cargarOtro) {
    form.reset();
  } else {
    alert("<Fin de caso de uso>");
  }
} catch (error) {
  console.error("Error:", error);
  alert("Error al enviar los datos.");
}
