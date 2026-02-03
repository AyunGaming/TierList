package fr.esgi.tier_list.application.view;

import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.*;
import fr.esgi.tier_list.application.storage.StockageService;
import fr.esgi.tier_list.domain.Company;
import fr.esgi.tier_list.domain.port.ExportSyntheseTierListsUseCase.CompanySynthesis;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExportSyntheseTierListsPdfView extends AbstractPdfView {

    private final StockageService stockageService;

    public ExportSyntheseTierListsPdfView(StockageService stockageService) {
        this.stockageService = stockageService;
    }

    @Override
    protected void buildPdfDocument(Map<String, Object> model, com.lowagie.text.Document document, com.lowagie.text.pdf.PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        @SuppressWarnings("unchecked")
        Map<String, CompanySynthesis> companySynthesis = (Map<String, CompanySynthesis>) model.get("companySynthesis");

        log.info("Building PDF with {} companies", companySynthesis != null ? companySynthesis.size() : 0);

        Paragraph title = new Paragraph("Synthèse des Tier Lists", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        if (companySynthesis == null || companySynthesis.isEmpty()) {
            Paragraph noData = new Paragraph("Aucune donnée disponible", FontFactory.getFont(FontFactory.HELVETICA, 14));
            noData.setAlignment(Element.ALIGN_CENTER);
            document.add(noData);
            return;
        }

        Paragraph subtitle = new Paragraph(
            "Nombre d'apparitions de chaque entreprise dans l'ensemble des tier lists, et répartition par tiers",
            FontFactory.getFont(FontFactory.HELVETICA, 12)
        );
        subtitle.setAlignment(Element.ALIGN_CENTER);
        document.add(subtitle);
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2f, 3f, 1.5f, 3f});

        table.addCell(createHeaderCell("Logo"));
        table.addCell(createHeaderCell("Entreprise"));
        table.addCell(createHeaderCell("Occurrences"));
        table.addCell(createHeaderCell("Tiers"));

        for (Map.Entry<String, CompanySynthesis> entry : companySynthesis.entrySet()) {
            CompanySynthesis synthesis = entry.getValue();

            PdfPCell logoCell = new PdfPCell();
            logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            logoCell.setPadding(5);

            if (synthesis.logoUrl() != null) {
                logoCell.addElement(new Phrase(synthesis.logoUrl(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
            } else {
                logoCell.addElement(new Phrase("-", FontFactory.getFont(FontFactory.HELVETICA, 8)));
            }
            table.addCell(logoCell);

            table.addCell(createContentCell(synthesis.companyName()));

            PdfPCell countCell = new PdfPCell(new Phrase(
                String.valueOf(synthesis.count()),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)
            ));
            countCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            countCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            countCell.setPadding(10);
            table.addCell(countCell);

            PdfPCell tiersCell = new PdfPCell(new Phrase(
                buildTiersSummary(synthesis.tiersCount()),
                FontFactory.getFont(FontFactory.HELVETICA, 10)
            ));
            tiersCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tiersCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tiersCell.setPadding(8);
            table.addCell(tiersCell);
        }

        document.add(table);
    }

    private PdfPCell createHeaderCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new Color(220, 220, 220));
        cell.setPadding(8);
        return cell;
    }

    private PdfPCell createContentCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA)));
        cell.setPadding(10);
        return cell;
    }

    private String buildTiersSummary(Map<String, Integer> tiersCount) {
        if (tiersCount == null || tiersCount.isEmpty()) {
            return "-";
        }

        StringBuilder sb = new StringBuilder();
        tiersCount.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    if (!sb.isEmpty()) {
                        sb.append(", ");
                    }
                    sb.append(entry.getKey())
                      .append(" : ")
                      .append(entry.getValue());
                });
        return sb.toString();
    }
}
