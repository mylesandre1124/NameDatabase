package Objects;

import Objects.Exceptions.StudentAlreadyFoundException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by Myles on 7/22/17.
 */
public class StudentDatabase {

    private TreeMap<Long, Student> students = new TreeMap<>();

    public StudentDatabase(TreeMap<Long, Student> students) {
        this.students = students;
    }

    public StudentDatabase() {
    }

    public TreeMap<Long, Student> getStudents() {
        return students;
    }

    public void setStudents(TreeMap<Long, Student> students) {
        this.students = students;
    }

    public Student searchStudentNumber(Long id)
    {
        return students.get(id);
    }

    public void updateStudent(Student student)
    {
        if(inDatabase(student.getIdNumber())) {
            students.replace(student.getIdNumber(), student);
        }
        else if(!inDatabase(student.getIdNumber()))
        {
            students.put(student.getIdNumber(), student);
        }
    }

    public void addStudent(Student student) throws StudentAlreadyFoundException
    {
        if(!inDatabase(student.getIdNumber()))
        {
            students.put(student.getIdNumber(), student);
        }
        else if(inDatabase(student.getIdNumber()))
        {
            throw new StudentAlreadyFoundException(student);
        }
    }

    public void writeStudents()
    {
        ObjectIO objectIO = new ObjectIO(new File("students.sts"));
        objectIO.writeObject(students);
    }

    public TreeMap<Long, Student> readStudents()
    {
        ObjectIO objectIO = new ObjectIO(new File("students.sts"));
        if(objectIO.readObject() == null)
        {
            students = new TreeMap<>();
        }
        else {
            students = (TreeMap<Long, Student>) objectIO.readObject();
        }
        return students;
    }

    public boolean inDatabase(Long id)
    {
        boolean inDb = students.containsKey(id);
        return inDb;
    }

    public static void main(String[] args) {
    }
}
