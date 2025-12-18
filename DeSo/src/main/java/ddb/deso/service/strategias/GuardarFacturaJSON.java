package ddb.deso.service.strategias;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ddb.deso.almacenamiento.DTO.FacturaDTO;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@NoArgsConstructor
public class GuardarFacturaJSON implements EstrategiaGuardadoFactura {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public byte[] guardarFactura(FacturaDTO factura) {
        try {
            String json = mapper.writeValueAsString(factura);
            return json.getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error generando JSON", e);
        }
    }
}
