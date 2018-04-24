package za.co.hotelbooking.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import static org.apache.http.util.TextUtils.isEmpty;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFileUtilities {

    public static Sheet readExcel(String filePath, String fileName, String sheetName) throws IOException {

        System.out.println("\nReading data from Excel spreadsheet: " + fileName + "....\n");

        //Create an object of File class to open xlsx file
        File file = new File(filePath + "/" + fileName);

        //Create an object of FileInputStream class to read excel file
        FileInputStream inputStream = new FileInputStream(file);

        Workbook h2hData = null;

        //Find the file extension by splitting file name in substring  and getting only extension name
        String fileExtensionName = fileName.substring(fileName.indexOf("."));

        //Check condition if the file is xlsx file
        if (fileExtensionName.equals(".xlsx")) {

            //If it is xlsx file then create object of XSSFWorkbook class
            h2hData = new XSSFWorkbook(inputStream);

        }

        //Check condition if the file is xls file
        else if (fileExtensionName.equals(".xls")) {

            //If it is xls file then create object of XSSFWorkbook class
            h2hData = new HSSFWorkbook(inputStream);

        }

        //Read sheet inside the workbook by its name
        Sheet h2hSheet = h2hData.getSheet(sheetName);

        //Find number of rows in excel file
        int rowCount = h2hSheet.getLastRowNum() - h2hSheet.getFirstRowNum();

        //Create a loop over all the rows of excel file to read it
        for (int i = 0; i < rowCount + 1; i++) {
            Row row = h2hSheet.getRow(i);

            //Create a loop to print cell values in a row
            for (int j = 0; j < row.getLastCellNum(); j++) {

                //Print Excel data in console
                if (isEmpty(row.getCell(j).getStringCellValue())) {
                    System.out.print("Null");

                } else {
                    System.out.print(row.getCell(j).getStringCellValue() + "|| ");
                }

                System.out.println();
            }
        }

                return h2hSheet;

        }
    }