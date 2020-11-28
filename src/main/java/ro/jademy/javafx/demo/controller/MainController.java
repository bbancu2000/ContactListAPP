package ro.jademy.javafx.demo.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

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
    VBox rightPanel;


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

    // INPUT Limiting Filter
    int maxInputCharLimit = 40;
    String forbiddenChars = "!@#;|,";


    @FXML
    private void initialize() throws IOException {
        rightPanel.setVisible(false);


        // SET TABLE-VIEW
        contactsTable.setEditable(true);

        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setMinWidth(120);
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("firstName"));

        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setMinWidth(120);
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("lastName"));

        //// NEW CHECK BOX DELETE COL
        TableColumn deleteCol = new TableColumn("Delete");
        deleteCol.setMinWidth(50);
        deleteCol.setCellValueFactory(new PropertyValueFactory<Contact, Boolean>("toBeDeleted"));

        deleteCol.setCellFactory(cell -> new CheckBoxTableCell<>());
        deleteCol.setVisible(false);

        // contactsTable logic
        contactsTable.setItems(filteredContactList);//Set the contactsTable's items using the filtered list
        contactsTable.getColumns().addAll(firstNameCol, lastNameCol, deleteCol);
        contactsTable.setOnMouseClicked(e -> {
            setSelectedContact();
        });


        // searchTextField logic
        searchTextField.setPromptText("Search here...");
        searchTextField.setOnKeyReleased(keyEvent -> doFilterList(searchTextField.getText()));
        phoneListView.setCellFactory(TextFieldListCell.forListView());


        // LIMITING INPUT
        firstNameTextField.setOnKeyTyped(limitMaxInput(firstNameTextField, maxInputCharLimit, forbiddenChars));
        lastNameTextField.setOnKeyTyped(limitMaxInput(lastNameTextField, maxInputCharLimit, forbiddenChars));
        addPhoneTextField.setOnKeyTyped(limitMaxInput(addPhoneTextField, maxInputCharLimit, forbiddenChars));
        searchTextField.setOnKeyTyped(limitMaxInput(searchTextField, maxInputCharLimit, forbiddenChars));


//////////////////////////////////////////////////////////////////////////////////
        /*             HANDLERS for BUTTONS
         *///////////////////////////////////////////////////////////////////////

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

        deleteButton.setOnMouseClicked(event -> {
            usedDeleteButton(deleteCol);
        });


        confirmDeleteButton.setOnAction(actionEvent -> {
            usedConfirmDeleteButton();
        });


        ///// PHONE ADD DELETE
        addPhoneButton.setOnAction(actionEvent -> {
            if (addButton.isSelected()
                    || editButton.isSelected()
                    || selectedContact != null) {
                addPhoneTextField.setVisible(true);
            }

        });
        deletePhoneButton.setOnAction(actionEvent -> {
            usedDeletePhoneButton();
        });
        addPhoneTextField.setOnAction(actionEvent -> {
            usedAddPhoneTextField();
        });

    }

    /////////////////////////////////////////////////////////////////////////////////


    private void usedSaveButton(List<Contact> allContacts) {
        // TODO save to DB eventually ?
        System.out.println("Trying to save on contacts");
        CSVDatabase.saveToCSV(allContacts);
    }

    private void usedAddPhoneTextField() {
        String newNumber = addPhoneTextField.getText();

        if (newNumber.length() > 0) {
            if (selectedContact != null) {
                phoneListView.getItems().add(newNumber);
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
        rightPanel.setVisible(true);
        if (addButton.isSelected()) {
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
            firstNameTextField.setText("");
            firstNameTextField.setPromptText("");
            lastNameTextField.setText("");
            tempPhoneList.clear();
            phoneListView.refresh();
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
            rightPanel.setVisible(true);
            if (editButton.isSelected()) {
                // SELECTED LOGIC
                tempPhoneList = new ArrayList<>(selectedContact.getPhoneList());

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
        selectedContact.setFirstName(firstNameTextField.getText());
        selectedContact.setLastName(lastNameTextField.getText());
        tempPhoneList.clear();

        // VISUALS
        contactsTable.refresh();
        editButton.setSelected(false);
        setInitialScenario();
    }

    private void usedDeleteButton(TableColumn deleteCol) {
        rightPanel.setVisible(true);

        if (deleteButton.isSelected()) {
            // SELECTED LOGIC
            confirmDeleteButton.setVisible(true);
            deleteCol.setVisible(true);

            addButton.setDisable(true);
            editButton.setDisable(true);

            addPhoneButton.setDisable(true);
            deletePhoneButton.setDisable(true);
        } else {
            // DE-SELECTED LOGIC
            confirmDeleteButton.setVisible(false);
            deleteCol.setVisible(false);

            for (Contact item : contactsTable.getItems()) {
                if (item.isToBeDeleted()) {
                    item.setToBeDeleted(false);
                }
            }
            addPhoneButton.setDisable(false);
            deleteButton.setSelected(false);
            deletePhoneButton.setDisable(false);
            setInitialScenario();
        }
    }

    private void usedConfirmDeleteButton() {
        // LOGIC
        List<Contact> toBeDeletedList = new ArrayList<>();

        for (Contact item : contactsTable.getItems()) {
            if (item.isToBeDeleted()) {
                toBeDeletedList.add(item);
            }
        }
        allContacts.removeAll(toBeDeletedList);
        filteredContactList.removeAll(toBeDeletedList);

        // VISUAL
        firstNameTextField.setText("");
        lastNameTextField.setText("");
        phoneListView.getItems().clear();
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

        addPhoneTextField.setVisible(false);
        addPhoneTextField.setText("");


        // EDITABLE
        firstNameTextField.setEditable(false);
        lastNameTextField.setEditable(false);
        phoneListView.setEditable(false);
    }

    private void setSelectedContact() {
        addPhoneTextField.setVisible(false);
        selectedContact = contactsTable.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {

            rightPanel.setVisible(true);
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


    private EventHandler<KeyEvent> limitMaxInput(TextField textField, int maxInputChars, String forbiddenChars) {
        return t -> {
            if (textField.getText().length() > maxInputChars) {
                int pos = textField.getCaretPosition();
                textField.setText(textField.getText(0, maxInputChars));
                textField.positionCaret(pos); //To reposition caret since setText sets it at the beginning by default
            } else {
                for (int i = 0; i < forbiddenChars.length(); i++) {
                    if (textField.getText().contains(String.valueOf(forbiddenChars.charAt(i)))) {
                        String filteredString = textField.getText().replace(String.valueOf(forbiddenChars.charAt(i)), "");
                        int pos = textField.getCaretPosition();
                        textField.setText(filteredString);
                        textField.positionCaret(pos); //To reposition caret since setText sets it at the beginning by default
                    }
                }
            }
        };

    }


}
