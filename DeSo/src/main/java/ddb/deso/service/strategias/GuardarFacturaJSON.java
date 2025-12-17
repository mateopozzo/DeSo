package ddb.deso.service.strategias;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ddb.deso.almacenamiento.DTO.FacturaDTO;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EmptyStackException;

@NoArgsConstructor
public class GuardarFacturaJSON implements EstrategiaGuardadoFactura {
    /**
     * @param factura
     */
    @Override
    public void guardarFactura(FacturaDTO factura) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            mapper.registerModule(new JavaTimeModule());

            Files.createDirectories(Paths.get("data/factura"));
            String nombreArchivo = "data/factura/factura_" + factura.getNum_factura() + ".json";

            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(nombreArchivo), factura);

            System.out.println("Factura guardada exitosamente en JSON: " + nombreArchivo);

        } catch (IOException e) {
            System.err.println("Error al guardar la factura JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
