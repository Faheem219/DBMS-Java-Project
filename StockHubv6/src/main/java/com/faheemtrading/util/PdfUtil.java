// src/main/java/com/faheemtrading/util/PdfUtil.java
package com.faheemtrading.util;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.util.List;

/** Simple table‑like PDF export (A4 portrait). */
public final class PdfUtil {
    private PdfUtil(){}
    public static void exportTable(File out, String title, List<String[]> rows) throws IOException {
        try(PDDocument doc=new PDDocument()){
            PDPage page=new PDPage(PDRectangle.A4);
            doc.addPage(page);

            try(PDPageContentStream cs=new PDPageContentStream(doc,page)){
                float margin=50, y=page.getMediaBox().getHeight()-margin;
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD,16);
                cs.newLineAtOffset(margin,y);
                cs.showText(title); cs.endText();

                y-=30; cs.setFont(PDType1Font.HELVETICA,12);
                for(String[] r:rows){
                    cs.beginText();
                    cs.newLineAtOffset(margin,y);
                    cs.showText(String.join("   |   ",r));
                    cs.endText();
                    y-=18;
                    if(y<margin){            // new page
                        cs.close();
                        page=new PDPage(PDRectangle.A4);
                        doc.addPage(page);
                        y=page.getMediaBox().getHeight()-margin;
                        cs.close();           // reopen later if more rows remain
                    }
                }
            }
            doc.save(out);
        }
    }
}
