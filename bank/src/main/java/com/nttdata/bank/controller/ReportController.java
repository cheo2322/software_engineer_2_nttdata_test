package com.nttdata.bank.controller;

import com.nttdata.bank.dto.BankResponse;
import com.nttdata.bank.dto.ReportResponseDto;
import com.nttdata.bank.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank/v1/reports")
public class ReportController {

  private final ReportService reportService;

  public ReportController(ReportService reportService) {
    this.reportService = reportService;
  }

  @GetMapping
  public ResponseEntity<BankResponse<ReportResponseDto>> getReport(
    @RequestParam String initialDate,
    @RequestParam String finalDate,
    @RequestParam String clientIdentification
  ) {
    return ResponseEntity.ok(
      new BankResponse<>(
        "000",
        "Report generated successfully",
        reportService.getReport(initialDate, finalDate, clientIdentification)
      )
    );
  }

  @GetMapping("/pdf")
  public ResponseEntity<BankResponse<String>> getReportPdf(
    @RequestParam String initialDate,
    @RequestParam String finalDate,
    @RequestParam String clientIdentification
  ) {
    return ResponseEntity.ok(
      new BankResponse<>(
        "000",
        "Report PDF generated successfully",
        reportService.getReportPdf(initialDate, finalDate, clientIdentification)
      )
    );
  }
}
