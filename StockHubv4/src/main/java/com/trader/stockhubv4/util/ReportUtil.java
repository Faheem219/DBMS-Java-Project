package com.trader.stockhubv4.util;

import com.trader.stockhubv4.model.Portfolio;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Stream;

/**
 * Utility to export portfolio lists to CSV or PDF.
 * Requires iText (com.itextpdf:itextpdf) on classpath.
 */
public class ReportUtil {

    /** Simple CSV export */
    public static void exportPortfoliosToCSV(List<Portfolio> list, File file) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(file.toPath())) {
            w.write("ID,Name,CreationDate,TotalValue");
            w.newLine();
            for (Portfolio p : list) {
                w.write(String.format("%d,%s,%s,%.2f",
                        p.getPortfolioId(),
                        p.getPortfolioName(),
                        p.getCreationDate(),
                        p.getTotalValue()));
                w.newLine();
            }
        }
    }

    /** PDF export using iText */
    public static void exportPortfoliosToPDF(List<Portfolio> list, File file) throws Exception {
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(file));
        doc.open();

        Font hFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph title = new Paragraph("Portfolio Report", hFont);
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);
        doc.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setHeaderRows(1);
        Stream.of("ID","Name","Creation Date","Total Value")
                .forEach(h -> {
                    PdfPCell cell = new PdfPCell(new Phrase(h, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    table.addCell(cell);
                });

        for (Portfolio p : list) {
            table.addCell(String.valueOf(p.getPortfolioId()));
            table.addCell(p.getPortfolioName());
            table.addCell(p.getCreationDate().toString());
            table.addCell(String.format("%.2f", p.getTotalValue()));
        }

        doc.add(table);
        doc.close();
    }
}
