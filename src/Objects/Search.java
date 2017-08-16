package Objects;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by Myles on 7/24/17.
 */
public class Search {

    public ArrayList<Student> search(String search)
    {
        StudentDatabase studentDatabase = new StudentDatabase();
        studentDatabase.readStudents();
        TreeMap<Long, Student> studentTreeMap = studentDatabase.getStudents();
        ArrayList<Student> list = new ArrayList<>(studentTreeMap.values());
        ArrayList<Student> results = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Student student = contains(search, list.get(i));
            if(student != null)
            {
                results.add(student);
            }
        }
        return results;
    }

    public Student contains(String search, Student student)
    {
        String id = String.valueOf(student.getIdNumber());
        search = search.toLowerCase();
        if (id.contains(search)) {
            return student;
        } else if (student.getFirstName().toLowerCase().contains(search)) {
            return student;
        } else if (student.getLastName().toLowerCase().contains(search)) {
            return student;
        } else if (student.getEmailAddress().toLowerCase().contains(search)) {
            return student;
        }
        return null;
    }

    /*Pre-deprecate*/
    public String[] split(String search)
    {
        String[] searches = {""};
        if(search.contains(" "))
        {
            searches = search.split(" ");
        }
        else{
            searches[0] = search;
        }
        return searches;
    }


}
