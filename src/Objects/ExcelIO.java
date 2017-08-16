package Objects;

import Objects.Exceptions.StudentAlreadyFoundException;
import au.com.bytecode.opencsv.CSVWriter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Created by Myles on 7/9/17.
 */
public class ExcelIO {

    private File excelFile;

    public ExcelIO(String excelFile) {
        setExcelFile(new File(excelFile));
    }

    public ExcelIO() {
    }

    public File getExcelFile() {
        return excelFile;
    }

    public void setExcelFile(File excelFile) {
        this.excelFile = excelFile;
    }

    /**
     * This method imports every single line of input from an excel file as long as no empty column is between them
     * Returns an arraylist of the data
     * @return ArrayList<ArrayList>
     */
    public ArrayList<ArrayList> singleColumnInput()
    {
        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(this.excelFile);

            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            ArrayList<ArrayList> excelList = new ArrayList();
            int columnCount = -1;
            while (iterator.hasNext())
            {
                Row currentRow = iterator.next();
                if(currentRow.getRowNum() == 0)
                {
                    columnCount = currentRow.getPhysicalNumberOfCells();
                }
                Iterator<Cell> cellIterator = currentRow.iterator();
                ArrayList rowList = new ArrayList();
                ArrayList<Integer> cellNums = new ArrayList();
                while (cellIterator.hasNext())
                {
                    Cell cell = cellIterator.next();
                    CellType cellType = cell.getCellTypeEnum();
                    cellNums.add(cell.getColumnIndex());
                    if(cellType.equals(CellType.STRING))
                    {
                        if(cell.getStringCellValue().equals("")) {
                            rowList.add("");
                        }
                    }
                    switch (cellType)
                    {
                        case NUMERIC:
                            rowList.add(cell.getNumericCellValue());
                            break;
                        case STRING:
                            rowList.add(cell.getStringCellValue());
                            break;
                    }
                }
                if(rowList.size() < columnCount)
                {
                    ArrayList<Integer> cellNums1 = new ArrayList();
                    for (int i = 0; i < columnCount; i++) {
                        if(cellNums.indexOf(i) == -1) {
                            cellNums1.add(i);
                        }
                    }
                    for (int i = 0; i < cellNums1.size(); i++) {
                        rowList.add(cellNums1.get(i), "");
                    }
                }
                if(!checkList(rowList)) {
                    excelList.add(rowList);
                }
            }
            return excelList;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (InvalidFormatException e)
        {
            e.printStackTrace();
        }
        return new ArrayList();
    }

    public boolean checkList(ArrayList list)
    {
        boolean empty;
        int listSize = list.size();
        int emptyCount = 0;
        for (int i = 0; i < listSize; i++) {
            if(list.get(i) == null)
            {
                emptyCount++;
            }
        }
        if (emptyCount == listSize)
        {
            empty = true;
        }
        else {
            empty = false;
        }
        return empty;
    }

    /**
     * This method takes a row of data and converts it into an object that is passed into it and returns the object.
     * (Cast it when it is being returned)
     *
     * Pass a String ArrayList into this to denote what fields match what row number
     * @param a
     * @param fields
     * @param data
     * @return
     */
    public Object convertRowToObject(Object a, ArrayList<String> fields, ArrayList data)
    {
        try {
            for (int i = 0; i < fields.size(); i++) {
                Field field = a.getClass().getDeclaredField(fields.get(i));
                Class type = field.getType();
                Converter converter = new Converter();
                //System.out.print(fields.get(i) + ": " + data.get(i) + " ");
                converter.dataConverter(data.get(i));
                field.setAccessible(true);
                field.set(a, converter.returnObject(type));
            }
            //System.out.println();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return a;
    }

    public ArrayList separate(ArrayList list, int index)
    {
        ArrayList list1 = new ArrayList();
        if(index >= 0)
        {
            String name = (String)list.get(index);
            String[] names = name.split("  ", 2);
            for (int j = 0; j < names.length; j++) {
                names[j] = names[j].replaceAll("\\s+$", "");
                list1.add(names[j]);
            }
            list.remove(index);
            for (int i = 0; i < list.size(); i++) {
                list1.add(list.get(i));
            }
        }
        return list1;
    }

    public int getIndex(ArrayList list)
    {
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals("Name"))
            {
                index = i;
            }
        }
        return index;
    }

    public TreeMap<Long, Student> importStudents() throws StudentAlreadyFoundException {
        ArrayList<ArrayList> lists = singleColumnInput();
        ArrayList<String> fields = new ArrayList<>(Arrays.asList("lastName", "firstName", "emailAddress", "idNumber"));
        //int index = getIndex(lists.get(0));
        StudentDatabase studentDatabase = new StudentDatabase();
        for (int i = 0; i < lists.size(); i++) {
            ArrayList row = separate(lists.get(i), 0);
            studentDatabase.addStudent((Student) convertRowToObject(new Student(), fields, row));
        }
        studentDatabase.writeStudents();
        return studentDatabase.getStudents();
    }

    public void exportCSV(TreeMap<Long, Student> students) throws IOException {
        FileProperty fileProperty = new FileProperty();
        fileProperty.save("Comma Separated Value File (*.csv)", "*.csv", "Export Students");
        File file = fileProperty.getFile();
        //File file = new File("export.csv");
        CSVWriter writer = new CSVWriter(new FileWriter(file), ',', '\0');
        ArrayList<Student> student = new ArrayList<>(students.values());
        for (int i = 0; i < student.size(); i++) {
            String[] row = new String[4];
            row[0] = student.get(i).getLastName();
            row[1] = student.get(i).getFirstName();
            row[2] = String.valueOf(student.get(i).getIdNumber());
            row[3] = student.get(i).getEmailAddress();
            writer.writeNext(row);
        }
        writer.close();
    }

    public static void main(String[] args) throws StudentAlreadyFoundException, IOException {
        StudentDatabase studentDatabase = new StudentDatabase();
        studentDatabase.readStudents();
        ExcelIO excelIO = new ExcelIO();
        excelIO.exportCSV(studentDatabase.getStudents());
    }
}
