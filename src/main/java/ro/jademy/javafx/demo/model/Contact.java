package ro.jademy.javafx.demo.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Contact {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> phoneList;


    private BooleanProperty toBeDeleted = new SimpleBooleanProperty(false);

    //private List<Role> roles = new ArrayList<>();

    public Contact(String firstName, String lastName, List<String> phoneList) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneList = phoneList;
    }


    public Contact() {
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.toBeDeleted.setValue(false);
        phoneList = new ArrayList<>();
    }


    public boolean isToBeDeleted() {
        return toBeDeleted.get();
    }

    public BooleanProperty toBeDeletedProperty() {
        return toBeDeleted;
    }

    public void setToBeDeleted(boolean toBeDeleted) {
        this.toBeDeleted.set(toBeDeleted);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(List<String> phoneList) {
        this.phoneList = phoneList;
    }


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;
        Contact contact = (Contact) o;
        return getId() == contact.getId() &&
                Objects.equals(getFirstName(), contact.getFirstName()) &&
                Objects.equals(getLastName(), contact.getLastName()) &&
                Objects.equals(getEmail(), contact.getEmail()) &&
                Objects.equals(getPhoneList(), contact.getPhoneList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getEmail(), getPhoneList());
    }
}
