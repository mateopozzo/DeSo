document
  .getElementById("alta-huesped")
  .addEventListener("submit", async (e) => {
    e.preventDefault();

    const form = e.target;

    const data = {
      apellido: form.apellido.value,
      nombre: form.nombre.value,
      tipoDocumento: form["tipo-documento"].value,
      numeroDocumento: form["numero-documento"].value,
      fechaNacimiento: form["fecha-nacimiento"].value,
      calle: form.calle.value,
      numeroCalle: form["numero-calle"].value,
      piso: form.piso.value || null,
      localidad: form.localidad.value,
      provincia: form.provincia.value,
      paisResidencia: form["pais-residencia"].value,
      nacionalidad: form.nacionalidad.value,
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

      const result = await response.text();
      alert(result);

      form.reset();
    } catch (error) {
      console.error("Error:", error);
      alert("Ocurrió un error al guardar el huésped.");
    }
  });
