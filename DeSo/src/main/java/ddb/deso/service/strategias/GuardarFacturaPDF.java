package ddb.deso.service.strategias;

import ddb.deso.almacenamiento.DTO.FacturaDTO;
import lombok.NoArgsConstructor;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@NoArgsConstructor
public class GuardarFacturaPDF implements EstrategiaGuardadoFactura {

    /**
     * @param factura
     */
    @Override
    public void guardarFactura(FacturaDTO factura) {

        Document document = new Document();

        try {
            Files.createDirectories(Paths.get("data/factura"));
            String nombreArchivo = "data/factura/factura_" + factura.getNum_factura() + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));

            document.open();
            document.add(new Paragraph("FACTURA - HOTEL DESO"));
            document.add(new Paragraph("------------------------------------------------"));

            document.add(new Paragraph("Fecha: " + factura.getFecha_factura()));
            document.add(new Paragraph("Número de Factura: " + factura.getNum_factura()));
            document.add(new Paragraph("Tipo: " + factura.getTipo_factura()));
            document.add(new Paragraph("Cliente (Destinatario): " + factura.getDestinatario()));

            document.add(new Paragraph("------------------------------------------------"));

            document.add(new Paragraph("Importe Neto: $" + factura.getImporte_neto()));
            document.add(new Paragraph("Importe IVA: $" + factura.getImporte_iva()));
            document.add(new Paragraph("IMPORTE TOTAL: $" + factura.getImporte_total()));

            System.out.println("PDF generado exitosamente: " + nombreArchivo);

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error al generar el PDF: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }
}
