package ddb.deso.contabilidad;

public class ResponsablePago {
    private String razonSocial;
    private int cuit;
    private String direccion;
    private int telefono;

    public ResponsablePago(String razonSocial, int cuit, String direccion, int telefono) {
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
    public String getDireccion() {
        return direccion;
    }
    public int getTelefono() {
        return telefono;
    }

}