package ddb.deso.contabilidad;

import ddb.deso.alojamiento.DatosResidencia;

public class ResponsablePago {
    private String razonSocial;
    private int cuit;
    private DatosResidencia direccion;
    private int telefono;

    public ResponsablePago(String razonSocial, int cuit, DatosResidencia direccion, int telefono) {
        this.razonSocial = razonSocial;
        this.cuit = cuit;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public String getRazonSocial() {
        return razonSocial;
    }
    public int getCuit() {
        return cuit;
    }
    public DatosResidencia getDireccion() {
        return direccion;
    }
    public int getTelefono() {
        return telefono;
    }

}