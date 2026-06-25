package com.qoobot.openaccounting.report.service.impl;

import com.qoobot.openaccounting.ledger.entity.GlAccount;
import com.qoobot.openaccounting.ledger.entity.GlBalance;
import com.qoobot.openaccounting.ledger.mapper.GlAccountMapper;
import com.qoobot.openaccounting.ledger.mapper.GlBalanceMapper;
import com.qoobot.openaccounting.report.dto.ReportGenerateRequest;
import com.qoobot.openaccounting.report.service.ReportService;
import com.qoobot.openaccounting.report.vo.ReportVO;
import com.qoobot.openaccounting.system.entity.SysCompany;
import com.qoobot.openaccounting.system.mapper.SysCompanyMapper;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 报表Service实现
 *
 * @author openaccounting
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final GlBalanceMapper balanceMapper;
    private final GlAccountMapper accountMapper;
    private final SysCompanyMapper companyMapper;

    @Override
    public ReportVO generateBalanceSheet(ReportGenerateRequest request) {
        // 获取公司信息
        SysCompany company = companyMapper.selectById(request.getCompanyId());

        ReportVO report = new ReportVO();
        report.setReportType("balance_sheet");
        report.setReportTypeName("资产负债表");
        report.setCompanyId(request.getCompanyId());
        report.setCompanyName(company != null ? company.getCompanyName() : "");
        report.setAccountingYear(request.getAccountingYear());
        report.setAccountingPeriod(request.getAccountingPeriod());
        report.setReportDate(request.getReportDate() != null ? request.getReportDate() : LocalDate.now());

        // 构建资产负债表项目
        List<ReportVO.ReportItemVO> items = new ArrayList<>();

        // 流动资产
        items.addAll(buildAssetsItems(request));
        // 流动负债
        items.addAll(buildLiabilitiesItems(request));
        // 所有者权益
        items.addAll(buildEquityItems(request));

        report.setItems(items);
        return report;
    }

    @Override
    public ReportVO generateIncomeStatement(ReportGenerateRequest request) {
        // 获取公司信息
        SysCompany company = companyMapper.selectById(request.getCompanyId());

        ReportVO report = new ReportVO();
        report.setReportType("income_statement");
        report.setReportTypeName("利润表");
        report.setCompanyId(request.getCompanyId());
        report.setCompanyName(company != null ? company.getCompanyName() : "");
        report.setAccountingYear(request.getAccountingYear());
        report.setAccountingPeriod(request.getAccountingPeriod());
        report.setReportDate(request.getReportDate() != null ? request.getReportDate() : LocalDate.now());

        // 构建利润表项目
        List<ReportVO.ReportItemVO> items = buildIncomeStatementItems(request);
        report.setItems(items);

        return report;
    }

    @Override
    public ReportVO generateCashFlowStatement(ReportGenerateRequest request) {
        // 获取公司信息
        SysCompany company = companyMapper.selectById(request.getCompanyId());

        ReportVO report = new ReportVO();
        report.setReportType("cash_flow");
        report.setReportTypeName("现金流量表");
        report.setCompanyId(request.getCompanyId());
        report.setCompanyName(company != null ? company.getCompanyName() : "");
        report.setAccountingYear(request.getAccountingYear());
        report.setAccountingPeriod(request.getAccountingPeriod());
        report.setReportDate(request.getReportDate() != null ? request.getReportDate() : LocalDate.now());

        // 构建现金流量表项目
        List<ReportVO.ReportItemVO> items = buildCashFlowItems(request);
        report.setItems(items);

        return report;
    }

    @Override
    public void exportToExcel(ReportGenerateRequest request, OutputStream outputStream) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("报表");

            // 创建标题行
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(getReportTypeName(request.getReportType()));

            // 创建表头
            Row headerRow = sheet.createRow(2);
            headerRow.createCell(0).setCellValue("项目");
            headerRow.createCell(1).setCellValue("行次");
            headerRow.createCell(2).setCellValue("期末余额");
            headerRow.createCell(3).setCellValue("期初余额");

            // 生成报表数据
            ReportVO report = switch (request.getReportType()) {
                case "balance_sheet" -> generateBalanceSheet(request);
                case "income_statement" -> generateIncomeStatement(request);
                case "cash_flow" -> generateCashFlowStatement(request);
                default -> generateBalanceSheet(request);
            };

            // 填充数据
            if (report.getItems() != null) {
                int rowNum = 3;
                for (ReportVO.ReportItemVO item : report.getItems()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(item.getItemName());
                    row.createCell(1).setCellValue(item.getLineNo() != null ? item.getLineNo() : 0);
                    if (item.getEndingBalance() != null) {
                        row.createCell(2).setCellValue(item.getEndingBalance().doubleValue());
                    }
                    if (item.getBeginningBalance() != null) {
                        row.createCell(3).setCellValue(item.getBeginningBalance().doubleValue());
                    }
                }
            }

            // 自动调整列宽
            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("导出Excel失败", e);
        }
    }

    /**
     * 构建资产类项目
     */
    private List<ReportVO.ReportItemVO> buildAssetsItems(ReportGenerateRequest request) {
        List<ReportVO.ReportItemVO> items = new ArrayList<>();
        List<GlBalance> assetBalances = getAccountBalancesByType(request, "assets");

        // 货币资金 (1001, 1002)
        BigDecimal cashBalance = getAccountBalanceByCode(assetBalances, "1001", "1002");
        items.add(createItem("1001", "货币资金", 1, cashBalance, cashBalance, 1));

        // 应收账款
        BigDecimal arBalance = getAccountBalanceByCode(assetBalances, "1122");
        items.add(createItem("1122", "应收账款", 4, arBalance, arBalance, 1));

        // 存货 (1403, 1405)
        BigDecimal inventoryBalance = getAccountBalanceByCode(assetBalances, "1403", "1405");
        items.add(createItem("1401", "存货", 9, inventoryBalance, inventoryBalance, 1));

        // 流动资产合计
        BigDecimal currentAssetsTotal = items.stream()
                .filter(item -> item.getLineNo() < 20)
                .map(ReportVO.ReportItemVO::getEndingBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        items.add(createItem("1100", "流动资产合计", 20, currentAssetsTotal, currentAssetsTotal, 20));

        // 固定资产 (1601)
        BigDecimal fixedAssetsBalance = getAccountBalanceByCode(assetBalances, "1601");
        items.add(createItem("1601", "固定资产", 24, fixedAssetsBalance, fixedAssetsBalance, 1));

        // 资产总计
        BigDecimal totalAssets = currentAssetsTotal.add(fixedAssetsBalance);
        items.add(createItem("0000", "资产总计", 39, totalAssets, totalAssets, 30));

        return items;
    }

    /**
     * 构建负债类项目
     */
    private List<ReportVO.ReportItemVO> buildLiabilitiesItems(ReportGenerateRequest request) {
        List<ReportVO.ReportItemVO> items = new ArrayList<>();
        List<GlBalance> liabilityBalances = getAccountBalancesByType(request, "liabilities");

        // 短期借款 (2001)
        BigDecimal shortTermLoanBalance = getAccountBalanceByCode(liabilityBalances, "2001");
        items.add(createItem("2001", "短期借款", 41, shortTermLoanBalance, shortTermLoanBalance, 1));

        // 应付账款 (2202)
        BigDecimal apBalance = getAccountBalanceByCode(liabilityBalances, "2202");
        items.add(createItem("2202", "应付账款", 44, apBalance, apBalance, 1));

        // 流动负债合计
        BigDecimal currentLiabilitiesTotal = items.stream()
                .map(ReportVO.ReportItemVO::getEndingBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        items.add(createItem("2100", "流动负债合计", 55, currentLiabilitiesTotal, currentLiabilitiesTotal, 20));

        // 负债合计
        items.add(createItem("2000", "负债合计", 66, currentLiabilitiesTotal, currentLiabilitiesTotal, 30));

        return items;
    }

    /**
     * 构建所有者权益类项目
     */
    private List<ReportVO.ReportItemVO> buildEquityItems(ReportGenerateRequest request) {
        List<ReportVO.ReportItemVO> items = new ArrayList<>();
        List<GlBalance> equityBalances = getAccountBalancesByType(request, "equity");

        // 实收资本 (3001)
        BigDecimal capitalBalance = getAccountBalanceByCode(equityBalances, "3001");
        items.add(createItem("3001", "实收资本", 67, capitalBalance, capitalBalance, 1));

        // 未分配利润
        BigDecimal retainedEarningsBalance = getAccountBalanceByCode(equityBalances, "3104");
        items.add(createItem("3104", "未分配利润", 69, retainedEarningsBalance, retainedEarningsBalance, 1));

        // 所有者权益合计
        BigDecimal equityTotal = items.stream()
                .map(ReportVO.ReportItemVO::getEndingBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        items.add(createItem("3000", "所有者权益合计", 76, equityTotal, equityTotal, 30));

        // 负债和所有者权益总计
        BigDecimal total = items.get(items.size() - 1).getEndingBalance()
                .add(getAccountBalanceByCode(getAccountBalancesByType(request, "liabilities"), "2202"));
        items.add(createItem("0001", "负债和所有者权益总计", 78, total, total, 40));

        return items;
    }

    /**
     * 构建利润表项目
     */
    private List<ReportVO.ReportItemVO> buildIncomeStatementItems(ReportGenerateRequest request) {
        List<ReportVO.ReportItemVO> items = new ArrayList<>();
        List<GlBalance> revenueBalances = getAccountBalancesByType(request, "revenue");
        List<GlBalance> expenseBalances = getAccountBalancesByType(request, "expense");

        // 营业收入 (6001)
        BigDecimal operatingRevenue = getAccountBalanceByCode(revenueBalances, "6001");
        items.add(createItem("6001", "一、营业收入", 1, operatingRevenue, operatingRevenue, 1));

        // 营业成本 (6401)
        BigDecimal operatingCost = getAccountBalanceByCode(expenseBalances, "6401");
        items.add(createItem("6401", "减：营业成本", 2, operatingCost, operatingCost, 1));

        // 销售费用 (6601)
        BigDecimal sellingExpense = getAccountBalanceByCode(expenseBalances, "6601");
        items.add(createItem("6601", "销售费用", 3, sellingExpense, sellingExpense, 1));

        // 管理费用 (6602)
        BigDecimal administrativeExpense = getAccountBalanceByCode(expenseBalances, "6602");
        items.add(createItem("6602", "管理费用", 4, administrativeExpense, administrativeExpense, 1));

        // 营业利润
        BigDecimal operatingProfit = operatingRevenue.subtract(operatingCost)
                .subtract(sellingExpense).subtract(administrativeExpense);
        items.add(createItem("1000", "二、营业利润", 10, operatingProfit, operatingProfit, 20));

        // 净利润
        items.add(createItem("2000", "三、净利润", 20, operatingProfit, operatingProfit, 30));

        return items;
    }

    /**
     * 构建现金流量表项目
     */
    private List<ReportVO.ReportItemVO> buildCashFlowItems(ReportGenerateRequest request) {
        List<ReportVO.ReportItemVO> items = new ArrayList<>();

        // 经营活动现金流量
        items.add(createItem("1001", "一、经营活动产生的现金流量", 1, BigDecimal.ZERO, BigDecimal.ZERO, 1));
        items.add(createItem("1002", "销售商品、提供劳务收到的现金", 2, BigDecimal.ZERO, BigDecimal.ZERO, 2));
        items.add(createItem("1099", "经营活动现金流入小计", 9, BigDecimal.ZERO, BigDecimal.ZERO, 20));

        // 投资活动现金流量
        items.add(createItem("2001", "二、投资活动产生的现金流量", 10, BigDecimal.ZERO, BigDecimal.ZERO, 1));

        // 筹资活动现金流量
        items.add(createItem("3001", "三、筹资活动产生的现金流量", 20, BigDecimal.ZERO, BigDecimal.ZERO, 1));

        // 现金及现金等价物净增加额
        items.add(createItem("4000", "现金及现金等价物净增加额", 30, BigDecimal.ZERO, BigDecimal.ZERO, 30));

        return items;
    }

    /**
     * 获取指定类型的科目余额
     */
    private List<GlBalance> getAccountBalancesByType(ReportGenerateRequest request, String accountType) {
        // TODO: 从数据库查询实际余额数据
        return new ArrayList<>();
    }

    /**
     * 根据科目编码获取余额
     */
    private BigDecimal getAccountBalanceByCode(List<GlBalance> balances, String... codes) {
        // TODO: 根据科目编码汇总余额
        return BigDecimal.ZERO;
    }

    /**
     * 创建报表项目
     */
    private ReportVO.ReportItemVO createItem(String code, String name, int lineNo,
                                              BigDecimal endingBalance, BigDecimal beginningBalance, int level) {
        ReportVO.ReportItemVO item = new ReportVO.ReportItemVO();
        item.setItemCode(code);
        item.setItemName(name);
        item.setLineNo(lineNo);
        item.setLevel(level);
        item.setEndingBalance(endingBalance != null ? endingBalance : BigDecimal.ZERO);
        item.setBeginningBalance(beginningBalance != null ? beginningBalance : BigDecimal.ZERO);
        item.setIsSummary(level >= 20);
        return item;
    }

    /**
     * 获取报表类型名称
     */
    private String getReportTypeName(String reportType) {
        return switch (reportType) {
            case "balance_sheet" -> "资产负债表";
            case "income_statement" -> "利润表";
            case "cash_flow" -> "现金流量表";
            default -> "报表";
        };
    }
}
