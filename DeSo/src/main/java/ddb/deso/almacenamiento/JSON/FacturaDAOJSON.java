/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.almacenamiento.JSON;

import ddb.deso.negocio.TipoFactura;
import ddb.deso.almacenamiento.DAO.FacturaDAO;
import ddb.deso.almacenamiento.DTO.FacturaDTO;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author mat
 */
public class FacturaDAOJSON implements FacturaDAO {
    private final static String RUTA_ARCHIVO_JSON_FACTURA = Paths.get("").toAbsolutePath().resolve("data").resolve("Factura.json").toString();
    private final ManejadorJson manejador;
    
    public FacturaDAOJSON(){
        this.manejador = new ManejadorJson(Path.of(RUTA_ARCHIVO_JSON_FACTURA),FacturaDTO.class);
    }
    private void escribirListaEnArchivo(List<FacturaDTO> listaFactura){
        try {
            manejador.escribir(listaFactura);
        } catch (IOException ex) {
            ex.printStackTrace();
        } 
    }
    @Override
    public void crearFactura(FacturaDTO factura){
        List<FacturaDTO> listaFacturas = listarFacturas();
        listaFacturas.add(factura);
        escribirListaEnArchivo(listaFacturas);     
    }
    @Override
    public void actualizarFactura(FacturaDTO factura){
        var listaFacturas = listarFacturas();
        Iterator i = listaFacturas.iterator();
        int nroFactura=factura.getNum_factura();
        TipoFactura tipoFactura = factura.getTipo_factura();
        FacturaDTO remover = buscarPorNumero(nroFactura, tipoFactura);
        eliminarFactura(remover);
        crearFactura(factura);
    }
    @Override
    public void eliminarFactura(FacturaDTO fatura){
        var listaFacturas = listarFacturas();
        listaFacturas.remove(fatura);
    }
    @Override
    public List<FacturaDTO> listarFacturas(){   
        List<FacturaDTO> listaFacturas = new ArrayList<>();
        try{
            listaFacturas=manejador.listar();
        } catch(Exception e) {
            e.printStackTrace(); //Sujeto a cambios (excepcion propia)
        }
        return listaFacturas;
    }
    @Override
    public FacturaDTO buscarPorNumero(int nroFactura, TipoFactura tipo){
        List<FacturaDTO> listaFacturas = listarFacturas();
        for(var fatura : listaFacturas ){
            if(fatura.getNum_factura() == nroFactura){
                if (fatura.getTipo_factura().equals(tipo)) {
                    return fatura;
                }
            }
        }
        return null;
    }
}
