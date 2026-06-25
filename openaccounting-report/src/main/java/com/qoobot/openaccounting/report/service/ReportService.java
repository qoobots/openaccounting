package com.qoobot.openaccounting.report.service;

import com.qoobot.openaccounting.report.dto.ReportGenerateRequest;
import com.qoobot.openaccounting.report.vo.ReportVO;

import java.io.OutputStream;

/**
 * 报表Service
 *
 * @author openaccounting
 */
public interface ReportService {

    /**
     * 生成资产负债表
     *
     * @param request 请求
     * @return 报表数据
     */
    ReportVO generateBalanceSheet(ReportGenerateRequest request);

    /**
     * 生成利润表
     *
     * @param request 请求
     * @return 报表数据
     */
    ReportVO generateIncomeStatement(ReportGenerateRequest request);

    /**
     * 生成现金流量表
     *
     * @param request 请求
     * @return 报表数据
     */
    ReportVO generateCashFlowStatement(ReportGenerateRequest request);

    /**
     * 导出报表到Excel
     *
     * @param request 请求
     * @param outputStream 输出流
     */
    void exportToExcel(ReportGenerateRequest request, OutputStream outputStream);
}
