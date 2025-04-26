package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {
    private static final String FILE_PATH = "src/test/resources/testData.xlsx";

    public static String getCellData(String sheetName, int rowNum, int colNum) {
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Row row = sheet.getRow(rowNum);
            Cell cell = row.getCell(colNum);

            return cell.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
    public static List<Map<String, String>> getTestData(String excelPath, String sheetName) {
        List<Map<String, String>> testDataAllRows = new ArrayList<>();
        try (FileInputStream file = new FileInputStream(excelPath);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet: " + sheetName + " not found in Excel: " + excelPath);
            }

            // First row for headers
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("No header row found in sheet: " + sheetName);
            }

            int lastRowNum = sheet.getLastRowNum();
            int lastColNum = headerRow.getLastCellNum();

            for (int i = 1; i <= lastRowNum; i++) { // Start from row 1 (skip headers)
                Row currentRow = sheet.getRow(i);
                if (currentRow == null) continue; // skip empty rows

                Map<String, String> dataMap = new LinkedHashMap<>();
                for (int j = 0; j < lastColNum; j++) {
                    Cell headerCell = headerRow.getCell(j);
                    Cell currentCell = currentRow.getCell(j);

                    String headerName = (headerCell != null) ? headerCell.toString().trim() : "Column" + j;
                    String cellValue = (currentCell != null) ? currentCell.toString().trim() : "";

                    dataMap.put(headerName, cellValue);
                }
                testDataAllRows.add(dataMap);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading Excel file: " + excelPath);
        }

        return testDataAllRows;
    }
}
