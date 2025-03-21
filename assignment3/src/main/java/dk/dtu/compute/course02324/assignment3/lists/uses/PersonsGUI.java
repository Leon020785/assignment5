package dk.dtu.compute.course02324.assignment3.lists.uses;


import dk.dtu.compute.course02324.assignment3.lists.implementations.GenericComparator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * A GUI element that is allows the user to interact and
 * change a list of persons.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class PersonsGUI extends GridPane {

    /**
     * The list of persons to be maintained in this GUI.
     */
    final private List<Person> persons;
    private GridPane personsPane;
    //private int weightCount = 1;
    private Label avgWeightLabel;
    private Label mostCommonNameLabel;


    /**
     * Constructor which sets up the GUI attached a list of persons.
     *
     * @param persons the list of persons which is to be maintained in
     *                this GUI component; it must not be <code>null</code>
     */


    public PersonsGUI(@NotNull List<Person> persons) {
        this.persons = persons;
        this.setVgap(5.0);
        this.setHgap(5.0);

        // text filed for user entering a name
        TextField nameField = new TextField();
        nameField.setPrefColumnCount(5);


        TextField weightField = new TextField();
        weightField.setPrefColumnCount(5);

        TextField indexField = new TextField();
        indexField.setPrefColumnCount(5);

        Label errorLabel = new Label("");


        avgWeightLabel = new Label("Average weight: 0");
        mostCommonNameLabel = new Label("Most common name: N/A");


        Button addButton = new Button("Add at the end of the list");
        addButton.setOnAction(e -> {
            try {
                errorLabel.setText("");
                int weight = Integer.parseInt(weightField.getText());
                if (weight < 0) throw new NumberFormatException("Weight must be positive");
                Person person = new Person(nameField.getText(), weight);
                persons.add(person);
                // makes sure that the GUI is updated accordingly
                update();
            } catch (NumberFormatException ex) {
                errorLabel.setText(ex.getMessage());
                System.out.println("Invalid weight input " + weightField.getText());
            }
        });

        Button addAtIndexButton = new Button("Add at index:");
        addAtIndexButton.setOnAction(e -> {
            try {
                errorLabel.setText("");
                int index = Integer.parseInt(indexField.getText());
                int weight = Integer.parseInt(weightField.getText());
                if (weight < 0) throw new NumberFormatException("Index is not valid");
                Person person = new Person(nameField.getText(), weight);
                persons.add(index, person);
                update();
            } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                errorLabel.setText("Index is not valid");
                System.out.println("Invalid index input " + indexField.getText());
            }
        });

        Comparator<Person> comparator = new GenericComparator<>();

        // button for sorting the list (according to the order of Persons,
        // which implement the interface Comparable, which is converted
        // to a Comparator by the GenericComparator above)
        Button sortButton = new Button("Sort");
        sortButton.setOnAction(
                e -> {
                    persons.sort(Comparator.naturalOrder());
                    // makes sure that the GUI is updated accordingly
                    update();
                });

        // button for clearing the list
        Button clearButton = new Button("Clear");
        clearButton.setOnAction(
                e -> {
                    persons.clear();
                    // makes sure that the GUI is updated accordingly
                    update();
                });

        // combines the above elements into vertically arranged boxes
        // which are then added to the left column of the grid pane


        Label name = new Label("Name");
        Label weight = new Label("Weight");
        HBox overskrifter = new HBox(name, weight);
        overskrifter.setSpacing(45.0);
        HBox inputFelter = new HBox(nameField, weightField);
        inputFelter.setSpacing(2.0);

        indexField.setMaxWidth(40);
        //indexField.setMaxWidth(20);
        HBox addIndexHBox = new HBox(addAtIndexButton, indexField);
        addIndexHBox.setSpacing(8.0);
        HBox spacer = new HBox();


        VBox actionBox = new VBox(overskrifter, inputFelter, addButton, addIndexHBox, sortButton, clearButton, avgWeightLabel, mostCommonNameLabel, errorLabel);
        //VBox actionBox = new VBox(nameField, weightField, indexField, addButton, addAtIndexButton, sortButton, clearButton);

        actionBox.setSpacing(5.0);
        this.add(actionBox, 0, 0);

        //Navigation - venstre menu

        // create the elements of the right column of the GUI
        // (scrollable person list) ...
        Label labelPersonsList = new Label("Persons:");

        personsPane = new GridPane();
        personsPane.setPadding(new Insets(5));
        personsPane.setHgap(5);
        personsPane.setVgap(5);

        ScrollPane scrollPane = new ScrollPane(personsPane);
        scrollPane.setMinWidth(300);
        scrollPane.setMaxWidth(300);
        scrollPane.setMinHeight(300);
        scrollPane.setMaxHeight(300);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // ... and adds these elements to the right-hand columns of
        // the grid pane
        VBox personsList = new VBox(labelPersonsList, scrollPane);
        personsList.setSpacing(5.0);
        this.add(personsList, 1, 0);

        // updates the values of the different components with the values from
        // the stack
        update();
    }

    /**
     * Updates the values of the GUI elements with the current values
     * from the list.
     */
    private void update() {
        personsPane.getChildren().clear();

        double totalWeight = 0;
        for (int i = 0; i < persons.size(); i++) {
            totalWeight += persons.get(i).weight;
        }
        if (persons.size() > 0) {
            avgWeightLabel.setText("Average weight: " + (totalWeight / persons.size()));
        } else {
            avgWeightLabel.setText("Average weight: 0");

        }
        HashMap<String, Integer> nameCount = new HashMap<>();
        for (int i = 0; i < persons.size(); i++) {
            nameCount.put(persons.get(i).getName(), nameCount.getOrDefault(persons.get(i).getName(), 0) + 1);
        }
        String mostCommonName = "N/A";
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : nameCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostCommonName = entry.getKey();
                maxCount = entry.getValue();
            }
        }
        mostCommonNameLabel.setText("Most common name: " + mostCommonName);
        // adds all persons to the list in the personsPane (with
        // a delete button in front of it)
        for (int i = 0; i < persons.size(); i++) {
            Person person = persons.get(i);
            Label personLabel = new Label(i + ": " + person.toString());
            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(
                    e -> {
                        persons.remove(person);
                        update();
                    }
            );
            HBox entry = new HBox(deleteButton, personLabel);
            entry.setSpacing(5.0);
            entry.setAlignment(Pos.BASELINE_LEFT);
            personsPane.add(entry, 0, i);
        }
    }

    // TODO this GUI could be extended by some additional widgets for issuing other
    //      operations of lists. And the possibly thrown exceptions should be caught
    //      in the event handler (and possibly shown in an additional text area for
    //      exceptions; see Assignment 2).

}
