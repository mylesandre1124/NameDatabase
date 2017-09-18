package UI;

import Objects.*;
import Objects.Exceptions.MessageNotSentException;
import Objects.Exceptions.StudentAlreadyFoundException;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class StudentUI {

    //Student Table List
    @FXML
    TableView<Student> studentTable = new TableView<>();
    @FXML
    TableColumn<Student, String> firstName = new TableColumn<>();
    @FXML
    TableColumn<Student, String> lastName = new TableColumn<>();
    @FXML
    TableColumn<Student, Long> studentId = new TableColumn<>();
    @FXML
    TableColumn<Student, String> emailAddress = new TableColumn<>();


    //Email Table list
    @FXML
    TableView<Student> emailTable = new TableView<>();
    @FXML
    TableColumn<Student, String> emailFirstName = new TableColumn<>();
    @FXML
    TableColumn<Student, String> emailLastName = new TableColumn<>();


    //Student and Email Data
    private TreeMap<Long, Student> studentTreeMap = new TreeMap<>();
    private TreeMap<Long, Student> emailStudentList = new TreeMap<>();

    private static Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage1)
    {
        primaryStage = primaryStage1;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @FXML
    public void initialize()
    {
        populateStudentTable();
        searchBarEventHandler();
        //test();
    }

    public void importStudentData() {
        StudentDatabase studentDatabase = new StudentDatabase();
        studentTreeMap = studentDatabase.readStudents();
    }

    public void populateStudentTable()
    {
        importStudentData();
        studentTableEventHandler();
        TableViewPopulator tableViewPopulator = new TableViewPopulator();
        ArrayList<Student> studentData = new ArrayList<>(studentTreeMap.values());
        ObservableList<Student> studentObservableList = (ObservableList<Student>) tableViewPopulator.getObjectList(studentData);

        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        studentId.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        emailAddress.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));
        studentTable.setItems(studentObservableList);
        studentTable.getSortOrder().add(firstName);
        if(studentTable.getColumns().size() == 0) {
            studentTable.getColumns().addAll(firstName, lastName, studentId, emailAddress);
        }
    }

    public void studentTableEventHandler()
    {
        studentTable.setRowFactory(tv -> {
            TableRow<Student> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (!row.isEmpty()))
                {
                    Student student = row.getItem();
                    emailStudentList.put(student.getIdNumber(), student);
                    populateEmailList();
                }
            });
            return row;
        });
    }

    public void emailTableEventHandler()
    {
        emailTable.setRowFactory(tv ->
        {
            TableRow<Student> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (!row.isEmpty()))
                {
                    Student student = row.getItem();
                    emailStudentList.remove(student.getIdNumber());
                    populateEmailList();
                }
            });
            return row;
        });
    }

    public void populateEmailList()
    {
        emailTableEventHandler();
        TableViewPopulator populator = new TableViewPopulator();
        ArrayList<Student> emailList = new ArrayList(emailStudentList.values());
        ObservableList<Student> list = (ObservableList<Student>) populator.getObjectList(emailList);
        emailFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        emailLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailTable.setItems(list);
        if(emailTable.getColumns().size() == 0) {
            emailTable.getColumns().addAll(emailFirstName, emailLastName);
        }
    }

    @FXML
    TextField searchBar = new TextField();

    public ArrayList<Student> search(String search)
    {
        Search search1 = new Search();
        ArrayList<Student> students = search1.search(search);
        //System.out.println("Text: " + search);
        if(search.isEmpty())
        {
            students = new ArrayList<>(studentTreeMap.values());
        }
        return students;
    }

    public void searchBarEventHandler()
    {
        searchBar.setOnKeyTyped(event ->  {
            String search = searchBar.getText() + event.getCharacter();
            ArrayList<Student> results = search(search);
            ObservableList<Student> resultsReturned = (ObservableList<Student>) new TableViewPopulator().getObjectList(results);
            studentTable.setItems(resultsReturned);
        });
    }

    public void sendEmail() throws IOException {
        ArrayList<Student> emailList = new ArrayList(emailStudentList.values());
        Email email = new Email();
        email.openCredentials();
        email.login(0);
        try {
            email.sendEmailsFromStudentList(emailList);
        } catch (MessageNotSentException e) {
            e.printStackTrace();
            CreateDialog cd = new CreateDialog();
            cd.showDialog(File.separator + "UI " + File.separator + "Message.fxml", "Message Not Sent");
        }
        progressBar.setProgress(1.0);
    }

    public void importStudents() throws StudentAlreadyFoundException, IOException {
        FileProperty fileProperty = new FileProperty();
        fileProperty.setStage(getPrimaryStage());
        fileProperty.open("Excel File (*.xlsx)", "*.xlsx", "Import Students");
        ExcelIO excelIO = new ExcelIO();
        excelIO.setExcelFile(fileProperty.getFile());
        studentTreeMap = excelIO.importStudents();
        populateStudentTable();
    }


    @FXML
    private ProgressBar progressBar = new ProgressBar();


    public void exportCSV() throws IOException {
        ExcelIO excelIO = new ExcelIO();
        excelIO.exportCSV(studentTreeMap);
    }

    public void startHost()
    {
        HostManager hostManager = new HostManager();
        if(!hostManager.checkIfLocalHostIsCreated())
        {

        }
        new Thread(() -> {
            Server server = new Server(new StudentDatabase().readStudents());
            server.start();
        }).start();
    }

    public void startClient()
    {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                Client client = new Client("168.28.186.130", 1025);
                client.connect();
                TreeMap<Long, Student> students = (TreeMap<Long, Student>) client.receiveObject();
                client.sendAcknowledgement(students.size());
                StudentDatabase studentDatabase = new StudentDatabase(students);
                studentDatabase.writeStudents();
                importStudentData();
                Platform.runLater(() -> populateStudentTable());
                client.close();
                return null;
            }
        };
        new Thread(task).start();
    }

    public void updateHosts()
    {

    }

    @FXML
    TextField usernameTextField = new TextField();

    public void getUsername()
    {
    }

}
