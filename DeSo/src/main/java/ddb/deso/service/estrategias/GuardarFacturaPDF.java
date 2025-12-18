package ddb.deso.service.estrategias;

import ddb.deso.almacenamiento.DTO.FacturaDTO;
import lombok.NoArgsConstructor;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;

@NoArgsConstructor
public class GuardarFacturaPDF implements EstrategiaGuardadoFactura {

    /**
     * @param factura
     */
    @Override
    public byte[] guardarFactura(FacturaDTO factura) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, out);


            document.open();
            document.add(new Paragraph("FACTURA - HOTEL PREMIER"));
            document.add(new Paragraph("Lavaisse 610 - S3004EWB Santa Fe"));
            document.add(new Paragraph("------------------------------------------------"));

            document.add(new Paragraph("Fecha: " + factura.getFecha_factura()));
            document.add(new Paragraph("Número de factura: " + factura.getNum_factura()));
            document.add(new Paragraph("Tipo: " + factura.getTipo_factura()));
            document.add(new Paragraph("Cliente: " + factura.getDestinatario()));

            document.add(new Paragraph("------------------------------------------------"));

            document.add(new Paragraph("Importe Neto: $" + factura.getImporte_neto()));
            document.add(new Paragraph("Importe IVA: $" + factura.getImporte_iva()));
            document.add(new Paragraph("IMPORTE TOTAL: $" + factura.getImporte_total()));
            document.close();
            System.out.println("Factura creada con éxito");
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
        }
    }
}
