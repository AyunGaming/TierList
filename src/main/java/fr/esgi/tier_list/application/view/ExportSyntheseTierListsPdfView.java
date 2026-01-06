package fr.esgi.tier_list.application.view;

import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.*;
import fr.esgi.tier_list.application.storage.StockageService;
import fr.esgi.tier_list.domain.Company;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

public class ExportSyntheseTierListsPdfView extends AbstractPdfView {

    private final StockageService stockageService;

    public ExportSyntheseTierListsPdfView(StockageService stockageService) {
        this.stockageService = stockageService;
    }

    @Override
    protected void buildPdfDocument(Map<String, Object> model, com.lowagie.text.Document document, com.lowagie.text.pdf.PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {

        @SuppressWarnings("unchecked")
        Map<String, List<Company>> tierList =
                (Map<String, List<Company>>) model.get("tierList");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        document.open();
        document.newPage();
        Paragraph title = new Paragraph(
                "Synthèse de la Tier List",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)
        );
        document.add(title);
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f, 5f});

        table.addCell(createHeaderCell("Tier"));
        table.addCell(createHeaderCell("Entreprises"));

        for(Map.Entry<String, List<Company>> entry: tierList.entrySet()){
            table.addCell(createTierCell(entry.getKey()));

            String companiesText = entry.getValue().isEmpty()
                    ? "-"
                    : entry.getValue().stream()
                    .map(Company::getName)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("-");

            table.addCell(createContentCell(companiesText));
        }

        document.add(table);
        document.close();

        byte[] pdfBytes = baos.toByteArray();

        // ☁️ Upload MinIO
        stockageService.uploadPdf(
                "tierlist-" + System.currentTimeMillis() + ".pdf",
                new ByteArrayInputStream(pdfBytes)
        );

        // 🌐 Envoi au navigateur
        response.setContentType("application/pdf");
        response.setContentLength(pdfBytes.length);
        response.getOutputStream().write(pdfBytes);
        response.flushBuffer();
    }

    private PdfPCell createHeaderCell(String text) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD))
        );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new Color(220, 220, 220));
        cell.setPadding(8);
        return cell;
    }

    private PdfPCell createTierCell(String tier) {
        PdfPCell cell = new PdfPCell(
                new Phrase(tier, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14))
        );
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(10);
        return cell;
    }

    private PdfPCell createContentCell(String text) {
        PdfPCell cell = new PdfPCell(
                new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA))
        );
        cell.setPadding(10);
        return cell;
    }
}
