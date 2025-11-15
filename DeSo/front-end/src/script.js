document
  .getElementById("alta-huesped")
  .addEventListener("submit", function (e) {
    e.preventDefault();

    const form = e.target;

    const letras = /^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$/;
    const numeros = /^[0-9]+$/;
    const formato_email = /^[A-Za-z0-9._%+-]+@[A-Za-z]+\.(com)$/;

    const apellido = form.apellido.value.trim();
    const nombre = form.nombre.value.trim();
    const localidad = form.localidad.value.trim();
    const provincia = form.provincia.value.trim();
    const paisResidencia = form["pais-residencia"].value.trim();
    const ocupacion = form.ocupacion.value.trim();
    const cuit = form.cuit.value.trim();
    const email = form.email.value.trim();

    if (!letras.test(apellido)) {
      alert("El apellido solo puede contener letras y espacios.");
      return;
    }

    if (!letras.test(nombre)) {
      alert("El nombre solo puede contener letras y espacios.");
      return;
    }

    if (!letras.test(localidad)) {
      alert("La localidad solo puede contener letras y espacios.");
      return;
    }

    if (!letras.test(provincia)) {
      alert("La provincia solo puede contener letras y espacios.");
      return;
    }

    if (!letras.test(paisResidencia)) {
      alert("El país solo puede contener letras y espacios.");
      return;
    }

    if (!letras.test(ocupacion)) {
      alert("La ocupación solo puede contener letras y espacios.");
      return;
    }

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
      tipoDocumento: form["tipo-documento"].value,
      numeroDocumento: form["numero-documento"].value,
      fechaNacimiento: form["fecha-nacimiento"].value,
      calle: form.calle.value,
      numeroCalle: form["numero-calle"].value,
      piso: form.piso.value || null,
      localidad,
      provincia,
      paisResidencia,
      nacionalidad: form.nacionalidad.value,
      cuit: cuit || null,
      iva: form.iva.value,
      ocupacion,
      telefono: form.telefono.value,
      email,
    };

    // enviar al back

    fetch("http://localhost:8080/api/huespedes", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    })
      .then((res) => res.text())
      .then((msg) => alert(msg))
      .catch((err) => {
        console.error(err);
        alert("Error al enviar los datos.");
      });
  });
