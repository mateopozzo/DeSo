package ddb.deso.service.estrategias;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import ddb.deso.almacenamiento.DTO.FacturaDTO;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;

@NoArgsConstructor
public class GuardarFacturaJSON implements EstrategiaGuardadoFactura {

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

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
