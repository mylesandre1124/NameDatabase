package Objects;

import java.io.Serializable;

/**
 * Created by Myles on 7/22/17.
 */
public class Student implements Serializable{

    private String firstName;
    private String lastName;
    private long idNumber;
    private String emailAddress;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(long idNumber) {
        this.idNumber = idNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString()
    {
        String object = "Last Name: " + getLastName() + " First Name: " + getFirstName()
                + " ID#: " + getIdNumber() + " Email Address: " + getEmailAddress();
        System.out.println(object);
        return object;
    }
}
