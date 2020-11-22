package ro.jademy.javafx.demo.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import ro.jademy.javafx.demo.database.CSVDatabase;
import ro.jademy.javafx.demo.model.Contact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {

    @FXML
    ToggleButton addButton;
    @FXML
    ToggleButton editButton;
    @FXML
    ToggleButton deleteButton;


    @FXML
    Button confirmAddButton;
    @FXML
    Button confirmEditButton;
    @FXML
    Button confirmDeleteButton;

    @FXML
    Button addPhoneButton;
    @FXML
    Button deletePhoneButton;

    @FXML
    Button saveButton;


    @FXML
    TableView<Contact> contactsTable;
    @FXML
    TextField searchTextField;

    @FXML
    TextField addPhoneTextField;
    @FXML
    TextField firstNameTextField;
    @FXML
    TextField lastNameTextField;
    @FXML
    ListView<String> phoneListView;

    /// CHOOSE DIFFERENT input files
    //  private final ObservableList<Contact> allContacts = FXCollections.observableList(SQLDatabase.getAllQuestionsFromDB());
    private final ObservableList<Contact> allContacts = FXCollections.observableList(CSVDatabase.getCSV());
    private Contact selectedContact;

    List<String> tempPhoneList = new ArrayList<>();
    private ObservableList<Contact> filteredContactList = allContacts;


    @FXML
    private void initialize() throws IOException {

        // SET TABLE-VIEW
        contactsTable.setEditable(true);

        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setMinWidth(120);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<Contact, String>("firstName"));

        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setMinWidth(120);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Contact, String>("lastName"));

        // contactsTable logic
        contactsTable.setItems(filteredContactList);//Set the contactsTable's items using the filtered list
        contactsTable.getColumns().addAll(firstNameCol, lastNameCol);
        contactsTable.setOnMouseClicked(e -> {
            setSelectedContact();
            addPhoneTextField.setVisible(false);
            confirmAddButton.setVisible(false);
        });

        // searchTextField logic
        searchTextField.setPromptText("Search here...");
        searchTextField.setOnKeyReleased(keyEvent -> doFilterList(searchTextField.getText()));
        phoneListView.setCellFactory(TextFieldListCell.forListView());

//////////////////////////////////////////////////////////////////////////////

        saveButton.setOnAction(actionEvent -> {
            usedSaveButton(allContacts);
        });


        addButton.setOnAction(actionEvent -> {
            usedAddButton();
        });
        confirmAddButton.setOnAction(actionEvent -> {
            usedConfirmAddButton();
        });


        editButton.setOnAction(actionEvent -> {
            usedEditButton();
        });
        confirmEditButton.setOnAction(actionEvent -> {
            usedConfirmEditButton();
        });


        deleteButton.setOnAction(event -> {
            usedDeleteButton();
        });

        confirmDeleteButton.setOnAction(actionEvent -> {
            usedConfirmDeleteButton();
        });


        ///// PHONE ADD DELETE
        addPhoneButton.setOnAction(actionEvent -> {
            addPhoneTextField.setVisible(true);
        });
        deletePhoneButton.setOnAction(actionEvent -> {
            usedDeletePhoneButton();
        });
        addPhoneTextField.setOnAction(actionEvent -> {
            usedAddPhoneTextField();
        });


    }

    private void usedSaveButton(List<Contact> allContacts) {
        // TODO save to DB eventually ?
        System.out.println("Trying to save on contacts");
        CSVDatabase.saveToCSV(allContacts);
    }

    private void usedAddPhoneTextField() {
        System.out.println("usedAddPhoneTextField-method");
        String newNumber = addPhoneTextField.getText();

        if (newNumber.length() > 0) {
            if (selectedContact != null) {
                selectedContact.getPhoneList().add(newNumber);
                phoneListView.setItems(FXCollections.observableList(selectedContact.getPhoneList()));
            } else if (addButton.isSelected()) {
                tempPhoneList.add(newNumber);
                phoneListView.setItems(FXCollections.observableList(tempPhoneList));
            }
        }
        addPhoneTextField.setText("");
        addPhoneTextField.setVisible(false);
    }

    private void usedDeletePhoneButton() {
        if (phoneListView.getSelectionModel().getSelectedItem() != null) {
            if (selectedContact != null) {
                selectedContact.getPhoneList().remove(phoneListView.getSelectionModel().getSelectedItem());
                phoneListView.refresh();
            } else if (addButton.isSelected()) {
                //phoneListView.setItems(FXCollections.observableList(tempPhoneList));
                tempPhoneList.remove(phoneListView.getSelectionModel().getSelectedItem());
                phoneListView.refresh();
            }
        }
    }


    private void usedAddButton() {
        if (addButton.isSelected()) {
            System.out.println("selectedContact set to null");
            selectedContact = null;

            // LOGIC
            phoneListView.setItems(FXCollections.observableList(tempPhoneList));
            phoneListView.setEditable(true);

            // VISUAL
            searchTextField.setDisable(true);
            contactsTable.setDisable(true);
            editButton.setDisable(true);
            deleteButton.setDisable(true);
            contactsTable.getSelectionModel().clearSelection();

            addPhoneTextField.setVisible(true);
            confirmAddButton.setVisible(true);

            firstNameTextField.setPromptText("Must have a First Name");
            firstNameTextField.setEditable(true);
            firstNameTextField.setText("");

            lastNameTextField.setEditable(true);
            lastNameTextField.setText("");
        } else {
            // deselect
            setInitialScenario();
        }
    }

    private void usedConfirmAddButton() {
        // LOGIC
        if (firstNameTextField.getText().length() > 0
                || lastNameTextField.getText().length() > 0) {
            selectedContact = new Contact();
            selectedContact.setFirstName(firstNameTextField.getText());
            selectedContact.setLastName(lastNameTextField.getText());
            selectedContact.setPhoneList(new ArrayList<>(tempPhoneList));
            allContacts.add(selectedContact);

            tempPhoneList.clear();
            phoneListView.setItems(FXCollections.observableList(selectedContact.getPhoneList()));
            contactsTable.getSelectionModel().selectLast();
        }
        // REVERT TO Initial Scenario
        addButton.setSelected(false);
        setInitialScenario();
    }


    private void usedEditButton() {
        if (selectedContact != null) {
            if (editButton.isSelected()) {
                // SELECTED LOGIC
                tempPhoneList = selectedContact.getPhoneList();

                firstNameTextField.setEditable(true);
                lastNameTextField.setEditable(true);
                phoneListView.setEditable(true);
                // VISUALS
                addButton.setDisable(true);
                deleteButton.setDisable(true);
                searchTextField.setDisable(true);
                contactsTable.setDisable(true);
                confirmEditButton.setVisible(true);
            } else {
                // DE-SELECTED LOGIC
                selectedContact.setPhoneList(new ArrayList<>(tempPhoneList));
                tempPhoneList.clear();
                setSelectedContact();
                setInitialScenario();
            }
        } else {
            editButton.setSelected(false);
        }

    }

    private void usedConfirmEditButton() {
        // LOGIC
        System.out.println("confirm edit pushed");
        selectedContact.setFirstName(firstNameTextField.getText());
        selectedContact.setLastName(lastNameTextField.getText());
        selectedContact.setPhoneList(new ArrayList<>(tempPhoneList));
        tempPhoneList.clear();

        // VISUALS
        contactsTable.refresh();
        editButton.setSelected(false);
        setInitialScenario();
    }


    private void usedDeleteButton() {
        if (selectedContact != null) {
            if (deleteButton.isSelected()) {
                // SELECTED LOGIC
                addButton.setDisable(true);
                editButton.setDisable(true);
                confirmDeleteButton.setVisible((true));
            }
        } else {
            deleteButton.setSelected(false);
        }
        // DE-SELECTED LOGIC
        if (!deleteButton.isSelected()) {
            addButton.setDisable(false);
            editButton.setDisable(false);
            confirmDeleteButton.setVisible(false);
        }
    }

    private void usedConfirmDeleteButton() {
        if (selectedContact != null) {
            // LOGIC
            allContacts.remove(selectedContact);
            filteredContactList.remove(selectedContact);
            selectedContact.getPhoneList().clear();

            phoneListView.setItems(FXCollections.observableList(selectedContact.getPhoneList()));
            selectedContact = null;
            contactsTable.refresh();
            contactsTable.getSelectionModel().clearSelection();

            // VISUAL
            firstNameTextField.setText("");
            lastNameTextField.setText("");
        }
    }


    private void setInitialScenario() {
        // DISABLE - ENABLE
        searchTextField.setDisable(false);
        contactsTable.setDisable(false);
        addButton.setDisable(false);
        editButton.setDisable(false);
        deleteButton.setDisable(false);

        //VISIBLE
        confirmAddButton.setVisible(false);
        confirmEditButton.setVisible(false);
        confirmDeleteButton.setVisible(false);
        addPhoneTextField.setVisible(false);
        addPhoneTextField.setText("");

        // EDITABLE
        firstNameTextField.setEditable(false);
        lastNameTextField.setEditable(false);
        phoneListView.setEditable(false);
    }

    private void setSelectedContact() {
        selectedContact = contactsTable.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            firstNameTextField.setText(selectedContact.getFirstName());
            lastNameTextField.setText(selectedContact.getLastName());
            phoneListView.setItems(FXCollections.observableList(selectedContact.getPhoneList()));
        }
    }

    private void doFilterList(String searchString) {
        // Cleaning the word
        searchString = searchString.trim().toLowerCase();
        String[] searchWordSplit = searchString.split(" ");

        List<Contact> tempList = new ArrayList<>();

        for (String word : searchWordSplit) {
            if (!word.equals("")) {
                if (tempList.isEmpty()) {
                    tempList.addAll(filterAlgorithm(allContacts, word));
                } else {
                    tempList.retainAll(filterAlgorithm(allContacts, word));
                }
            }
        }

        if (tempList.isEmpty() && searchString.isEmpty()) {
            filteredContactList = allContacts;
        } else {
            filteredContactList = FXCollections.observableList(tempList);
        }
        // Refresh with the found contacts
        contactsTable.setItems(filteredContactList);
    }

    private List<Contact> filterAlgorithm(List<Contact> contactList, String searchWord) {
        return contactList.stream().
                filter(d -> d.getFirstName().toLowerCase().contains(searchWord)
                        || d.getLastName().toLowerCase().contains(searchWord)
                        || d.getPhoneList().contains(searchWord))
                .distinct()
                .collect(Collectors.toList());
    }


}
