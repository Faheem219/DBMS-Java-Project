package com.faheemtrading.util;

import com.faheemtrading.model.Portfolio;
import java.util.List;

public final class ReportUtil {
    private ReportUtil(){}
    public static void portfolioPdf(String file, Portfolio p, List<String[]> stocks) throws Exception{
        List<String> headers = List.of("Stock ID","Qty","Purchase ₹","Current ₹");
        PdfUtil.table(file, "Portfolio Report – "+p.getName(), headers,
                stocks.stream().map(List::of).toList());
    }
}
