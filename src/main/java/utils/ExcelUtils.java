package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    private static final String TESTDATA_FILE_PATH = "src/test/resources/testdata/metadata_testdata.xlsx";
    private static final String SHEET_NAME = "Sheet1"; // Change if your sheet name is different

    public static Map<String, String> getMetadataForTestCase(String testCaseId) {
        Map<String, String> metadata = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(TESTDATA_FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(SHEET_NAME);
            Iterator<Row> rows = sheet.iterator();
            Row headerRow = rows.next(); // First row assumed as header

            int testCaseIdColumnIndex = -1;
            for (Cell cell : headerRow) {
                if (cell.getStringCellValue().equalsIgnoreCase("TestCaseID")) {
                    testCaseIdColumnIndex = cell.getColumnIndex();
                    break;
                }
            }

            if (testCaseIdColumnIndex == -1) {
                throw new RuntimeException("TestCaseID column not found in Excel");
            }

            while (rows.hasNext()) {
                Row row = rows.next();
                Cell testCaseCell = row.getCell(testCaseIdColumnIndex);

                if (testCaseCell != null && testCaseCell.getStringCellValue().equalsIgnoreCase(testCaseId)) {
                    for (Cell cell : row) {
                        String headerName = headerRow.getCell(cell.getColumnIndex()).getStringCellValue();
                        String cellValue = getCellValueAsString(cell);
                        metadata.put(headerName, cellValue);
                    }
                    break; // Found matching row, exit loop
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read Excel file: " + e.getMessage());
        }

        if (metadata.isEmpty()) {
            throw new RuntimeException("TestCaseID " + testCaseId + " not found in Excel sheet");
        }

        return metadata;
    }

    private static String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long)cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}
