package com.faheemtrading.util;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.UnitValue;

import java.util.List;

public final class PdfUtil {
    private PdfUtil(){}
    public static void table(String file, String title, List<String> headers, List<List<String>> rows) throws Exception{
        PdfDocument pdf = new PdfDocument(new PdfWriter(file));
        Document doc = new Document(pdf);
        doc.add(new Paragraph(title).setBold().setFontSize(14));
        Table table = new Table(UnitValue.createPercentArray(headers.size())).useAllAvailableWidth();
        headers.forEach(h -> table.addHeaderCell(new Cell().add(new Paragraph(h).setBold())));
        for (List<String> row : rows)
            row.forEach(val -> table.addCell(new Cell().add(new Paragraph(val))));
        doc.add(table);
        doc.close();
    }
}
