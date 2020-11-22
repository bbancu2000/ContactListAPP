package ro.jademy.javafx.demo.database;

import ro.jademy.javafx.demo.model.Contact;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVDatabase {


    static File csv = new File("src/main/resources/contacts.csv");





    public static List<Contact> getCSV() {
        List<Contact> contactList = new ArrayList<>();
        Contact tempContact;

        try{

            BufferedReader br = new BufferedReader(new FileReader(csv));

            String strCurrentLine;
            br.readLine();
            while ((strCurrentLine = br.readLine()) != null) {
                tempContact = new Contact();
                String[] words = strCurrentLine.split(",");

                String[] phoneNumbers = words[2].split("\\|");

                for(String phone : phoneNumbers) {
                  tempContact.getPhoneList().add(phone);
                }
                tempContact.setFirstName(words[0]);
                tempContact.setLastName(words[1]);
                contactList.add(tempContact);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }


        return contactList;
    }


    public static void saveToCSV(List<Contact> contactList) {
        // Creates a BufferedWriter
        System.out.println("got a contactlist in SaveToCSV of "+ contactList.size());
        try{
            BufferedWriter output = new BufferedWriter(new FileWriter(csv));

            output.write("FirstName,LastName,Phone");
            output.newLine();

            StringBuffer tempString = new StringBuffer();
            for (Contact tempContact : contactList) {
                tempString.delete(0, tempString.length());
                tempString.append(tempContact.getFirstName() + ",");
                tempString.append(tempContact.getLastName() + ",");

                for (String phoneNr: tempContact.getPhoneList()) {
                    tempString.append(phoneNr + "|");
                }
                output.write(tempString.toString());
                output.newLine();
            }


            // Flushes data to the destination
            output.flush();
            System.out.println("Data is flushed to the file.");

            output.close();






        }catch (Exception e) {
            e.printStackTrace();
        }




    }



}
