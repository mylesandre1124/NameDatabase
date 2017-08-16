package Objects.Exceptions;

import Objects.Student;

/**
 * Created by Myles on 7/22/17.
 */
public class StudentAlreadyFoundException extends Exception{

    public StudentAlreadyFoundException(Student student) {
        super("Student with ID#" + student.getIdNumber() + " has already been found in the database.");
    }
}
