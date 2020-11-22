package ro.jademy.javafx.demo.database;

import ro.jademy.javafx.demo.model.Contact;

import java.sql.*;
import java.util.*;

public class SQLDatabase {

    private static final String javaURL = "jdbc:mysql://";
    private static final String serverURL = "localhost:3306/contactlist";
    private static final String username = "root";
    private static final String password = "secret";

    public static Connection sqlConn = getConnection();


    public static Connection getConnection() {
        try {
            System.out.println("connection to DB");
            return DriverManager.getConnection(javaURL + serverURL, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public static List<Contact> getAllContactsFromDB() {
        List<Contact> contactList = new ArrayList<>();


        String contactsQuery = "SELECT * FROM contactlist.contacts;";
        String phoneNumbersQuery = "SELECT contact_id, phone FROM contactlist.phonenumbers WHERE contact_id = ";
        try (Statement stmt = sqlConn.createStatement()) {
            ResultSet rs = stmt.executeQuery(contactsQuery);
            while (rs.next()) {
                Contact tempContact = new Contact();

                tempContact.setId(rs.getLong("contact_id"));
                tempContact.setFirstName(rs.getString("first_name"));
                tempContact.setLastName(rs.getString("last_name"));
                tempContact.setEmail(rs.getString("email"));

                Statement phoneStmt = sqlConn.createStatement();
                ResultSet phoneResult = phoneStmt.executeQuery(phoneNumbersQuery+ tempContact.getId());
                List<String> phoneNumbers = new ArrayList<>();
                while(phoneResult.next()) {
                    phoneNumbers.add(phoneResult.getString("phone"));
                }

                tempContact.setPhoneList(phoneNumbers);
                contactList.add(tempContact);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactList;
    }


}
