package com.nttdata.bank.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nttdata.bank.dto.Report;
import com.nttdata.bank.dto.ReportResponseDto;
import com.nttdata.bank.entity.Client;
import com.nttdata.bank.exception.EntityNotFoundException;
import com.nttdata.bank.service.ReportService;
import java.sql.Timestamp;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

@WebMvcTest(ReportController.class)
@AutoConfigureRestTestClient
class ReportControllerTest {

  @Autowired
  private RestTestClient restTestClient;

  @MockitoBean
  private ReportService reportService;

  @Test
  void shouldGenerateReportSuccessfully() {
    Report report = new Report(
      Timestamp.valueOf("2026-02-10 12:53:00").toString(),
      "Test",
      "100200",
      "CREDIT",
      500.0,
      true,
      50.0,
      150.0
    );
    ReportResponseDto responseDto = new ReportResponseDto(List.of(report));

    when(reportService.getReport(anyString(), anyString(), anyString()))
      .thenReturn(responseDto);

    restTestClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/bank/v1/reports")
        .queryParam("initialDate", "2026-02-01")
        .queryParam("finalDate", "2026-02-12")
        .queryParam("clientIdentification", "1234567890")
        .build())
      .exchange()
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$.code").isEqualTo("000")
      .jsonPath("$.message").isEqualTo("Report generated successfully")
      .jsonPath("$.data.reports[0].accountNumber").isEqualTo("100200")
      .jsonPath("$.data.reports[0].client").isEqualTo("Test")
      .jsonPath("$.data.reports[0].availableBalance").isEqualTo(150.0);

    verify(reportService).getReport("2026-02-01", "2026-02-12", "1234567890");
  }

  @Test
  void shouldHandleEntityNotFoundException_whenGenerateReport() {
    doThrow(new EntityNotFoundException(Client.class, "identification", "9999999999"))
      .when(reportService).getReport("2026-02-01", "2026-02-12", "9999999999");

    restTestClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/bank/v1/reports")
        .queryParam("initialDate", "2026-02-01")
        .queryParam("finalDate", "2026-02-12")
        .queryParam("clientIdentification", "9999999999")
        .build())
      .exchange()
      .expectStatus().isNotFound()
      .expectBody()
      .jsonPath("$.message").isEqualTo("Client with identification: 9999999999, not found");
  }

  @Test
  void shouldGenerateReportPdfSuccessfully() {
    String pdfBase64 = "JVBERi0xLjQKJc...";

    when(reportService.getReportPdf("2026-02-01", "2026-02-12", "1234567890"))
      .thenReturn(pdfBase64);

    restTestClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/bank/v1/reports/pdf")
        .queryParam("initialDate", "2026-02-01")
        .queryParam("finalDate", "2026-02-12")
        .queryParam("clientIdentification", "1234567890")
        .build())
      .exchange()
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$.message").isEqualTo("Report PDF generated successfully")
      .jsonPath("$.data").isEqualTo(pdfBase64);

    verify(reportService).getReportPdf("2026-02-01", "2026-02-12", "1234567890");
  }

  @Test
  void shouldHandleEntityNotFoundException_whenGenerateReportPdf() {
    doThrow(new EntityNotFoundException(Client.class, "identification", "9999999999"))
      .when(reportService).getReportPdf("2026-02-01", "2026-02-12", "9999999999");

    restTestClient.get()
      .uri(uriBuilder -> uriBuilder
        .path("/bank/v1/reports/pdf")
        .queryParam("initialDate", "2026-02-01")
        .queryParam("finalDate", "2026-02-12")
        .queryParam("clientIdentification", "9999999999")
        .build())
      .exchange()
      .expectStatus().isNotFound()
      .expectBody()
      .jsonPath("$.message").isEqualTo("Client with identification: 9999999999, not found");
  }
}