package ddb.deso.almacenamiento.JPA;

import ddb.deso.almacenamiento.DAO.FacturaDAO;
import ddb.deso.almacenamiento.DTO.FacturaDTO;
import ddb.deso.negocio.contabilidad.Factura;
import ddb.deso.negocio.TipoFactura;
import ddb.deso.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FacturaDAOJPA implements FacturaDAO {

    @Autowired
    private FacturaRepository facturaRepository;

    @Override
    public void crearFactura(FacturaDTO facturaDTO) {
        // Convertir DTO a Entidad
        Factura entidad = mapearAEntidad(facturaDTO);
        facturaRepository.save(entidad);
    }

    @Override
    public void actualizarFactura(FacturaDTO facturaDTO) {
        // En JPA, save() actualiza si el ID existe.
        // Pero como el DTO a veces no trae ID, buscamos por lógica o asumimos nuevo.
        Factura entidad = mapearAEntidad(facturaDTO);
        facturaRepository.save(entidad);
    }

    @Override
    public void eliminarFactura(FacturaDTO facturaDTO) {
        // Buscamos la entidad para eliminarla
        Factura entidad = facturaRepository.buscarPorNumeroYTipo(
                facturaDTO.getNum_factura(), 
                facturaDTO.getTipo_factura()
        ).orElse(null);
        
        if (entidad != null) {
            facturaRepository.delete(entidad);
        }
    }

    @Override
    public List<FacturaDTO> listarFacturas() {
        // Obtenemos todas las entidades y las convertimos a DTOs
        return facturaRepository.findAll().stream()
                .map(FacturaDTO::new) 
                .collect(Collectors.toList());
    }

    @Override
    public FacturaDTO buscarPorNumero(int nroFactura, TipoFactura tipo) {
        return facturaRepository.buscarPorNumeroYTipo(nroFactura, tipo)
                .map(FacturaDTO::new)
                .orElse(null);
    }

    // Método auxiliar privado para mapear DTO -> Entidad
    private Factura mapearAEntidad(FacturaDTO dto) {
        Factura f = new Factura();
        f.setFecha_factura(dto.getFecha_factura());
        f.setNum_factura(dto.getNum_factura());
        f.setTipo_factura(dto.getTipo_factura());
        f.setImporte_total(dto.getImporte_total());
        f.setImporte_iva(dto.getImporte_iva());
        f.setImporte_neto(dto.getImporte_neto());
        f.setDestinatario(dto.getDestinatario());
        
        return f;
    }
}