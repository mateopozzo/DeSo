/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.alojamiento;

/**
 *
 * @author mat
 */
public class DatosResidencia {
    private String calle;
    private String depto;
    private String localidad;
    private String prov;
    private String pais;
    private int nro_calle;
    private long piso;
    private String cod_post;

    public DatosResidencia(String calle, String depto, String localidad, String prov, String pais, int nro_calle, long piso, String cod_post) {
        this.calle = calle;
        this.depto = depto;
        this.localidad = localidad;
        this.prov = prov;
        this.pais = pais;
        this.nro_calle = nro_calle;
        this.piso = piso;
        this.cod_post = cod_post;
    }
    
    
}
