document
  .getElementById("alta-huesped")
  .addEventListener("submit", async (e) => {
    e.preventDefault();

    const form = e.target;

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

      return; // Detener la ejecución si hay errores
    } else {
      document.getElementById("error-campos").style.display = "none";
    }

    // Si no hay errores, construir el objeto y enviar los datos
    const data = {
      apellido: form.apellido.value,
      nombre: form.nombre.value,
      tipoDoc: form["tipo-documento"].value,
      documento: form["numero-documento"].value,
      fechaNacimiento: form["fecha-nacimiento"].value,
      calle: form.calle.value,
      numero_calle: form["numero-calle"].value,
      piso: form.piso.value || null,
      localidad: form.localidad.value,
      provincia: form.provincia.value,
      paisResidencia: form["pais-residencia"].value,
      cuit: form.cuit.value || null,
      iva: form.iva.value,
      ocupacion: form.ocupacion.value,
      telefono: form.telefono.value,
      email: form.email.value,
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

      const cargarOtro = confirm(
        "El huésped " +
          data.nombre +
          " " +
          data.apellido +
          " ha sido cargado satisfactoriamente en el sistema. \n\n¿Desea cargar otro?"
      );

      if (cargarOtro) {
        form.reset();
      } else {
        window.location.href = "home.html";
      }
    } catch (error) {
      console.error("Error:", error);
      alert("Error al enviar los datos.");
    }
  });

// Evento para los botones reiniciar y cancelar
document.getElementById("botones-op").addEventListener("click", (e) => {
  const form = document.getElementById("alta-huesped");

  if (e.target.id === "boton-cancelar") {
    if (confirm("¿Desea cancelar la modificación del huésped?")) {
      window.location.href = "home.html";
    }
  }

  if (e.target.id === "reinicio") {
    if (confirm("Se borrarán todos los datos del formulario.")) {
      document.getElementById("error-campos").style.display = "none";
      form.reset();
    }
  }
});
