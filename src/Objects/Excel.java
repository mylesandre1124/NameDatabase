package Objects;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetDimension;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Myles on 9/11/17.
 */
public class Excel {

    private File excelFile;

    public File getExcelFile() {
        return excelFile;
    }

    public void setExcelFile(File excelFile) {
        this.excelFile = excelFile;
    }

    public Sheet getExcelSheet() throws IOException, InvalidFormatException {
        XSSFWorkbook workbook = new XSSFWorkbook(this.excelFile);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        return datatypeSheet;
    }



    public ArrayList<Row> getRows() throws IOException, InvalidFormatException {
        Sheet excelSheet = getExcelSheet();
        int rowCount = excelSheet.getPhysicalNumberOfRows();
        ArrayList<Row> excelRows = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            excelRows.add(excelSheet.getRow(i));
        }
        return excelRows;
    }

    public Row getRow(int rowNum) throws IOException, InvalidFormatException {
        ArrayList<Row> rows = getRows();
        return rows.get(rowNum);
    }

    public Object cellTypeChecker(Cell cellToCheck)
    {
        CellType cellType = cellToCheck.getCellTypeEnum();
        if(cellType.equals(CellType.STRING))
        {
            return cellToCheck.getStringCellValue();
        }
        else if(cellType.equals(CellType.NUMERIC))
        {
            return cellToCheck.getNumericCellValue();
        }
        else if(cellToCheck.getStringCellValue().isEmpty())
        {
            System.out.println("empty");
            return "";
        }
        else {
            return null;
        }
    }

    public ArrayList getCellsInRow(Row row)
    {
        int cellCount = row.getPhysicalNumberOfCells();
        ArrayList cellArrayList = new ArrayList<>();
        for (int i = 0; i < cellCount; i++) {
            Object cell = cellTypeChecker(row.getCell(i));
            if(cell != null) {
                cellArrayList.add(cell);
            }
        }
        cellArrayList = separate(cellArrayList);
        return cellArrayList;
    }

    public ArrayList separate(ArrayList rowData)
    {
        return new ExcelIO().separate(rowData, 0);
    }

    public Object convertRowToObject(ArrayList data)
    {
        Student student = new Student();
        ArrayList<String> fields =  new ArrayList<>(Arrays.asList("lastName", "firstName", "emailAddress", "idNumber"));
        return new ExcelIO().convertRowToObject(student, fields, data);
    }

    public ArrayList<Student> importData() throws IOException, InvalidFormatException {
        ArrayList<Row> rows = getRows();
        ArrayList<Student> students = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            ArrayList cells = getCellsInRow(getRow(i));
            System.out.println(cells.size());
            System.out.println(cells.get(2));
            students.add((Student) convertRowToObject(cells));
        }
        return students;
    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
        Excel excel = new Excel();
        excel.setExcelFile(new File("testData.xlsx"));
        //excel.setExcelFile(new File("students 8-1-17.xlsx"));
        ArrayList<Student> students = excel.importData();
        //System.out.println(students.size());
        students.toString();
    }
}
