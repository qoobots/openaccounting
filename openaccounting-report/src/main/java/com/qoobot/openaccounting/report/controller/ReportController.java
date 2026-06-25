package com.qoobot.openaccounting.report.controller;

import com.qoobot.openaccounting.common.result.Result;
import com.qoobot.openaccounting.report.dto.ReportGenerateRequest;
import com.qoobot.openaccounting.report.service.ReportService;
import com.qoobot.openaccounting.report.vo.ReportVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 报表Controller
 *
 * @author openaccounting
 */
@Tag(name = "财务报表", description = "财务报表相关接口")
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "生成资产负债表")
    @PostMapping("/balance-sheet")
    public Result<ReportVO> generateBalanceSheet(@Valid @RequestBody ReportGenerateRequest request) {
        ReportVO report = reportService.generateBalanceSheet(request);
        return Result.success(report);
    }

    @Operation(summary = "生成利润表")
    @PostMapping("/income-statement")
    public Result<ReportVO> generateIncomeStatement(@Valid @RequestBody ReportGenerateRequest request) {
        ReportVO report = reportService.generateIncomeStatement(request);
        return Result.success(report);
    }

    @Operation(summary = "生成现金流量表")
    @PostMapping("/cash-flow-statement")
    public Result<ReportVO> generateCashFlowStatement(@Valid @RequestBody ReportGenerateRequest request) {
        ReportVO report = reportService.generateCashFlowStatement(request);
        return Result.success(report);
    }

    @Operation(summary = "导出报表到Excel")
    @PostMapping("/export")
    public ResponseEntity<byte[]> exportToExcel(@Valid @RequestBody ReportGenerateRequest request) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            reportService.exportToExcel(request, outputStream);

            String fileName = getFileName(request.getReportType());
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", encodedFileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 获取文件名
     */
    private String getFileName(String reportType) {
        String name = switch (reportType) {
            case "balance_sheet" -> "资产负债表";
            case "income_statement" -> "利润表";
            case "cash_flow" -> "现金流量表";
            default -> "报表";
        };
        return name + ".xlsx";
    }
}
