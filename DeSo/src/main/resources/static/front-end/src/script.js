document
    .getElementById("alta-huesped")
    .addEventListener("submit", async (e) => {
      e.preventDefault();

      const form = e.target;

      // --- 1. VALIDACIÓN DE CAMPOS VACÍOS ---
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
      ];

      const cuit = form.cuit.value.trim();
      const email = form.email.value.trim();
      console.log(cuit+" "+email);

      const faltantes = [];
      const data = {}; // Creamos el objeto data aquí

      camposObligatorios.forEach((campo) => {
        const input = form[campo.id];
        const valor = input.value.trim();
        if (!valor) {
          faltantes.push(campo.nombre.toLowerCase());
          input.classList.add("input-error"); // Marcamos el input con error
        } else {
          input.classList.remove("input-error"); // Limpiamos si está bien
          data[campo.id] = valor; // Añadimos al objeto data
        }
      });

      let msg_error_campo = document.getElementById("error-campos");
      if (faltantes.length > 0) {
        const mensajeError =
            "Los campos " + faltantes.join(", ") + " no pueden estar vacíos.";
        msg_error_campo.innerText = mensajeError;
        msg_error_campo.style.display = "block";
        return; // Si hay campos faltantes, MOSTRAMOS ERROR y SALIMOS
      } else {
        msg_error_campo.style.display = "none"; // Ocultamos si no hay errores
      }

      const dataDTO = {
        apellido: data.apellido,
        nombre: data.nombre,
        nacionalidad: data.nacionalidad,
        fechanac: data["fecha-nacimiento"],
        tipo_doc: form["tipo-documento"].value.toUpperCase(), // Aseguramos mayúsculas para el Enum
        nro_doc: data["numero-documento"],
        telefono: data.telefono,
        email: data.email,
        calle: data.calle,
        nro_calle: data["numero-calle"],
        piso: form.piso.value || null,
        cod_post: form.cod_postal.value || null,
        pais: data["pais-residencia"],
        prov: data.provincia,
        localidad: data.localidad,
        ocupacion: data.ocupacion,
        cuit: cuit || null,
        posicion_iva: data.iva,
        razon_social: null // Ajusta esto si tienes un campo para razon_social,
        depto:null
      };






      // --- 4. ENVÍO (FETCH) (AHORA DENTRO DE LA FUNCIÓN ASYNC) ---
      try {
        // CORREGÍ LA URL (puerto 8000 y /api/huesped)
        const response = await fetch("http://localhost:8000/api/huesped", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(dataDTO),
        });

        if (!response.ok) {
          alert("Error al guardar el huésped. Código: " + response.status);
          return;
        }

        const msg = await response.text();
        console.log("Éxito (server): ", msg);

        const cargarOtro = confirm(
            "El huésped " +
            dataDTO.nombre +
            " " +
            dataDTO.apellido +
            " ha sido cargado satisfactoriamente en el sistema. \n\n¿Desea cargar otro?"
        );

        if (cargarOtro) {
          form.reset();
          // Limpiamos los errores visuales
          camposObligatorios.forEach(campo => {
            form[campo.id].classList.remove("input-error");
          });
        } else {
          window.location.href = "home.html";
        }
      } catch (error) {
        console.error("Error:", error);
        alert("Error al enviar los datos.");
      }
    }); // <-- El listener del formulario AHORA CIERRA AQUÍ, al final de la lógica

// --- Listener del botón Cancelar (Este sí estaba bien) ---
document
    .getElementById("botones-op")
    .querySelector("#boton-cancelar") // Usamos querySelector para más seguridad
    .addEventListener("click", function (e) {
      const cancelado = confirm("¿Desea cancelar el alta del huésped?");

      if (cancelado) {
        window.location.href = "home.html";
      }
    });

// No había ningún '}' extra, la elimino.