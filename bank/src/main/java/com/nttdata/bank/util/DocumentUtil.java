package com.nttdata.bank.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.nttdata.bank.dto.Report;
import com.nttdata.bank.dto.ReportResponseDto;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class DocumentUtil {

  private DocumentUtil() {

  }

  public static String generateReportPdfBase64(ReportResponseDto reportResponse) {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    PdfWriter writer = new PdfWriter(stream);
    PdfDocument pdf = new PdfDocument(writer);
    Document document = new Document(pdf);

    float[] columnWidths = {100F, 100F, 100F, 80F, 100F, 60F, 100F, 120F};
    Table table = new Table(columnWidths);

    // Headers
    table.addHeaderCell(new Cell().add(new Paragraph("Date")));
    table.addHeaderCell(new Cell().add(new Paragraph("Client")));
    table.addHeaderCell(new Cell().add(new Paragraph("Account")));
    table.addHeaderCell(new Cell().add(new Paragraph("Type")));
    table.addHeaderCell(new Cell().add(new Paragraph("Initial Balance")));
    table.addHeaderCell(new Cell().add(new Paragraph("Status")));
    table.addHeaderCell(new Cell().add(new Paragraph("Movement")));
    table.addHeaderCell(new Cell().add(new Paragraph("Available Balance")));

    // Data rows
    for (Report report : reportResponse.reports()) {
      table.addCell(new Cell().add(new Paragraph(report.date())));
      table.addCell(new Cell().add(new Paragraph(report.client())));
      table.addCell(new Cell().add(new Paragraph(report.accountNumber())));
      table.addCell(new Cell().add(new Paragraph(report.type())));
      table.addCell(new Cell().add(new Paragraph(String.valueOf(report.initialBalance()))));
      table.addCell(new Cell().add(new Paragraph(String.valueOf(report.status()))));
      table.addCell(new Cell().add(new Paragraph(String.valueOf(report.movement()))));
      table.addCell(new Cell().add(new Paragraph(String.valueOf(report.availableBalance()))));
    }

    document.add(table);
    document.close();

    return Base64.getEncoder().encodeToString(stream.toByteArray());
  }
}
