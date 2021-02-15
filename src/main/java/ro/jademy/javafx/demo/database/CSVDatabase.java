package ro.jademy.javafx.demo.database;

import javafx.stage.FileChooser;
import ro.jademy.javafx.demo.model.Contact;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVDatabase {


  //  private static File csv = new File("src/main/resources/contacts.csv");



    private static File chosenFile;


    public static File chooseCSVFile() {
        FileChooser fileBrowser = new FileChooser();
        //Set extension filter for text files
        fileBrowser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        //Show OPEN FILE dialog
        File selectedFile = fileBrowser.showOpenDialog(null);
        if(selectedFile != null) {
            chosenFile = selectedFile;
            System.out.println("chooseCSVFile: " + selectedFile.getName());
        }
        return selectedFile;
    }

    public static void newMenuButtonUsed() {
        // TODO possibly making a tempfile in the future.
        chosenFile = null;
    }


    public static File createNewCSVFile() {
        FileChooser fileChooser = new FileChooser();
        //Set extension filter for text files
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        //Show SAVE FILE dialog
        File selectedFile = fileChooser.showSaveDialog(null);
        if(selectedFile!=null) {
            System.out.println("createdNewCSVFile: " + selectedFile.getName() );
        }
        return selectedFile;
    }


    public static List<Contact> readCSV(File chosenFile) {
        List<Contact> contactList = new ArrayList<>();
        Contact tempContact;
        try{
            BufferedReader br = new BufferedReader(new FileReader(chosenFile));
            String strCurrentLine;
            br.readLine();
            while ((strCurrentLine = br.readLine()) != null) {
                tempContact = new Contact();
                String[] words = strCurrentLine.split(",");
                if(words.length == 3) {
                    String[] phoneNumbers = words[2].split("\\|");
                    for(String phone : phoneNumbers) {
                        tempContact.getPhoneList().add(phone);
                    }
                }
                tempContact.setFirstName(words[0]);
                if(words.length >= 2) {
                    tempContact.setLastName(words[1]);
                }
                contactList.add(tempContact);
            }
        }catch (Exception e) {
            System.out.println("readCSV: NULL file");
//            e.printStackTrace();
            return null;
        }
        return contactList;
    }

    public static void saveASNewCSV(List<Contact> contactList) {
        File newFile = createNewCSVFile();
        if(newFile!=null) {
            chosenFile = newFile;
            saveContactsLogic(contactList, newFile);
        }
    }

    public static void saveContactsToChosenCSV(List<Contact> contactList) {
        if(chosenFile != null) {
            saveContactsLogic(contactList, chosenFile);
        } else {
            saveASNewCSV(contactList);
        }
    }


    public static void saveContactsLogic(List<Contact> contactList, File chosenFile) {
        // Creates a BufferedWriter
        System.out.println("SaveToCSV: got a contactlist of "+ contactList.size());
        try{
            BufferedWriter output = new BufferedWriter(new FileWriter(chosenFile));

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
            System.out.println("Data has finished flushing to the file.");

            output.close();
        }catch (Exception e) {
            System.out.println("saveContactsLogic: NULL file or NULL Contacts");
//            e.printStackTrace();
        }
    }



}
