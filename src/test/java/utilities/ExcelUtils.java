package utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class ExcelUtils {

	 private static final String FILE_PATH = "src/test/resources/data/endpoints.xlsx";

	    public static Map<String, String> getDataByTestName(String TestName) {
	        Map<String, String> rowData = new HashMap<>();

	        try (FileInputStream fis = new FileInputStream(FILE_PATH);
	             Workbook workbook = new XSSFWorkbook(fis)) {

	            Sheet sheet = workbook.getSheetAt(0);
	            Row headerRow = sheet.getRow(0);

	            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
	                Row row = sheet.getRow(i);
	                if (row == null || row.getCell(0) == null) continue;

	                String testCaseCell = getCellValueAsString(row.getCell(0));
	                if (testCaseCell.equalsIgnoreCase(TestName)) {
	                    for (int j = 0; j < headerRow.getLastCellNum(); j++) {
	                        String key = getCellValueAsString(headerRow.getCell(j));
	                        String value = getCellValueAsString(row.getCell(j));
	                        rowData.put(key, value);
	                    }
	                    break;
	                }
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new RuntimeException("Failed to read Excel test data: " + e.getMessage());
	        }

	        return rowData;
	    }

	    private static String getCellValueAsString(Cell cell) {
	        if (cell == null) return "";
	        switch (cell.getCellType()) {
	            case STRING:
	                return cell.getStringCellValue().trim();
	            case NUMERIC:
	                if (DateUtil.isCellDateFormatted(cell)) {
	                    return cell.getDateCellValue().toString();
	                } else {
	                    return String.valueOf((int) cell.getNumericCellValue());
	                }
	            case BOOLEAN:
	                return String.valueOf(cell.getBooleanCellValue());
	            case FORMULA:
	                return cell.getCellFormula();
	            case BLANK:
	                return "";
	            default:
	                return cell.toString().trim();
	        }
	    }
	}