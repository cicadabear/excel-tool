package cn.elift.exceltool;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Jack on 2018/5/2.
 */
public class Tool {
    //    private static final String FILE_NAME = "C:\\Users\\dream\\Desktop\\clean\\test\\test\\src\\main\\java\\e电梯维保结算清单（20180510期）(2).xlsx";
//    private static final String NEW_FILE_NAME = "C:\\Users\\dream\\Desktop\\clean\\test\\test\\src\\main\\java\\e电梯维保结算清单(20180510期)1.xlsx";
    public static final DecimalFormat decimalFormat = new DecimalFormat("#.00");

    public static void main(String[] args) {
        Console console = System.console();
        if (console == null) {
            System.out.println("Console is not supported");
            System.exit(1);
        }

        File curDir = new File(Tool.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
        System.out.println(curDir.getAbsolutePath());
        File originalFile = null;
        for (File file : curDir.listFiles()) {
            if (file.getName().endsWith(".xlsx") && !file.getName().startsWith("~")) {
                System.out.println(file.getName());
                if (originalFile != null) {
                    System.out.println("注意: 当前文件夹只能有一个Excel 2007 xlsx文件");
                    System.exit(1);
                    break;
                }
                originalFile = file;
            }
        }
        if (originalFile == null) {
            System.out.println("注意: 当前文件夹没有Excel 2007 xlsx文件");
            System.exit(1);
        }

        String paramGroupIndex = console.readLine("请输入按第几列分组：");
        while (!StringUtils.isNumeric(paramGroupIndex)) {
            paramGroupIndex = console.readLine("请输入数字, 按第几列分组: ");
        }
        String paramStartIndex = console.readLine("请输入正文数据开始行数：");
        while (!StringUtils.isNumeric(paramGroupIndex)) {
            paramStartIndex = console.readLine("请输入数字, 正文数据开始行数: ");
        }
        int startIndex = Integer.valueOf(paramStartIndex);
        String needTotal = console.readLine("是否需要合计(Yes/No) [No]: ");
        if (StringUtils.isEmpty(needTotal)) {
            needTotal = "No";
        }
        while (!(needTotal.equalsIgnoreCase("yes") || needTotal.equalsIgnoreCase("no"))) {
            needTotal = console.readLine("请输入Yes或No: ");
        }
        int sumColIndex = -1;
        if (needTotal.equalsIgnoreCase("yes")) {
            String paramSumColIndex = console.readLine("请输入需要合计第几列 [默认最后一列, 不用输入直接回车]: ");
            while (StringUtils.isNotEmpty(paramSumColIndex) && !StringUtils.isNumeric(paramGroupIndex)) {
                paramSumColIndex = console.readLine("请输入数字, 需要合计第几列: ");
            }
            if (StringUtils.isNotEmpty(paramSumColIndex) && StringUtils.isNumeric(paramSumColIndex)) {
                sumColIndex = Integer.valueOf(paramSumColIndex) - 1;
            }
        }
        String title = console.readLine("请输入标题: ");
        while (StringUtils.isEmpty(title)) {
            title = console.readLine("请输入标题, 标题不能为空: ");
        }
        try {
            List<List<String>> data = new ArrayList<>();
            int groupIndex = Integer.valueOf(paramGroupIndex) - 1;//以第几列分组

            FileInputStream excelFile = new FileInputStream(originalFile);
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            int i = 0;
//            String title = "";
            List<String> headers = new ArrayList<>();

            while (iterator.hasNext()) {
                i++;
//                if (i <= 4) continue;
                System.out.println(i);
                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                List<String> row = new ArrayList<>();
                for (int n = 0; n < headers.size(); n++) {
                    row.add("");
                }
                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();

//                    if (i == 1) {
//                        if (StringUtils.isNotEmpty(currentCell.getStringCellValue())) {
//                            title = currentCell.getStringCellValue();
//                        }
//                    } else
                    if (i == (startIndex - 1)) {
                        headers.add(currentCell.getStringCellValue());
                    } else if (i >= startIndex) {
                        //getCellTypeEnum shown as deprecated for version 3.15
                        //getCellTypeEnum ill be renamed to getCellType starting from version 4.0
                        if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                            System.out.print(currentCell.getStringCellValue() + "--");
                            row.set(currentCell.getColumnIndex(), currentCell.getStringCellValue());
                        } else if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            System.out.print(currentCell.getNumericCellValue() + "--");
                            row.set(currentCell.getColumnIndex(), String.valueOf(currentCell.getNumericCellValue()));
                        } else if (currentCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                            System.out.print(currentCell.getCellFormula() + "--");
                            switch(currentCell.getCachedFormulaResultType()) {
                                case Cell.CELL_TYPE_NUMERIC:
                                    System.out.println("Last evaluated as: " + currentCell.getNumericCellValue());
                                    row.set(currentCell.getColumnIndex(), String.valueOf(currentCell.getNumericCellValue()));
                                    break;
                                case Cell.CELL_TYPE_STRING:
                                    System.out.println("Last evaluated as \"" + currentCell.getRichStringCellValue() + "\"");
                                    row.set(currentCell.getColumnIndex(), currentCell.getRichStringCellValue().getString());
                                    break;
                            }
                        }
                    }
                }
                System.out.println();
                if (row.size() > 0)
                    data.add(row);
            }
            Map<String, List<List<String>>> groupData = new LinkedHashMap<>();
            data.forEach(row -> {
                String groupName = row.get(groupIndex);
                if (!groupData.containsKey(groupName)) {
                    groupData.put(groupName, new ArrayList<>());
                }
                List<List<String>> groupList = groupData.get(groupName);
                groupList.add(row);
                row.set(0, String.valueOf(groupList.size()));
            });
            //合计
            if (needTotal.equalsIgnoreCase("yes")) {
                if (sumColIndex == -1) {
                    sumColIndex = headers.size() - 1;
                }
                for (Map.Entry<String, List<List<String>>> entry : groupData.entrySet()) {
                    Double total = 0D;
                    int size = 0;
                    for (List<String> list : entry.getValue()) {
                        size = list.size();
                        total += Double.valueOf(list.get(sumColIndex));
                    }
                    List<String> lastRow = new ArrayList<>();
                    for (int n = 0; n < size; n++) {
                        if (n == 0) {
                            lastRow.add("合计");
                        } else if (n == sumColIndex) {
                            lastRow.add(decimalFormat.format(total));
                        } else {
                            lastRow.add("");
                        }
                    }
                    entry.getValue().add(lastRow);

                }
            }
            // 声明一个工作薄
            SXSSFWorkbook workbookWrite = new SXSSFWorkbook(100);
            ExportExcelXUtils exportExcelXUtils = new ExportExcelXUtils();
            for (Map.Entry<String, List<List<String>>> entry : groupData.entrySet()) {
                System.out.println(entry.getKey());
                exportExcelXUtils.exportExcelXWithCommonDataCreateSheet(workbookWrite, title, entry.getKey(), headers, null, entry.getValue(), true, null);
            }
            FileOutputStream outputStream = new FileOutputStream(curDir.getAbsolutePath() + File.separator + "out.xlsx");
            workbookWrite.write(outputStream);
            workbookWrite.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("注意: 检查out.xlsx是否关闭");
            e.printStackTrace();
        }
        System.out.println("--------------------------");
        System.out.println("转换完成 新文件: out.xlsx");
    }
}
