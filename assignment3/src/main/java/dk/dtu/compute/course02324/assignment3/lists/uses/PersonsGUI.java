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





public class PersonsGUI extends GridPane {

    /**
     * The list of persons to be maintained in this GUI.
     */
    final private List<Person> persons;
    private GridPane personsPane;
    private Label avgWeightLabel;
    private Label mostCommonNameLabel;
    private Label minAgeLabel;
    private Label maxAgeLabel;




    public PersonsGUI(@NotNull List<Person> persons) {
        this.persons = persons;
        this.setVgap(5.0);
        this.setHgap(5.0);

        // text filed for user entering a name
        TextField nameField = new TextField();
        nameField.setPrefColumnCount(5);

        TextField ageField = new TextField();
        ageField.setPrefColumnCount(5);


        TextField weightField = new TextField();
        weightField.setPrefColumnCount(5);

        TextField indexField = new TextField();
        indexField.setPrefColumnCount(5);

        Label errorLabel = new Label("");



        avgWeightLabel = new Label("Average weight: 0");
        mostCommonNameLabel = new Label("Most common name: N/A");
        minAgeLabel = new Label("min age: 0");
        maxAgeLabel = new Label("max age: 0");


        Button addButton = new Button("Add at the end of the list");
        addButton.setOnAction(e -> {
            try {
                errorLabel.setText("");
                int weight = Integer.parseInt(weightField.getText());
                if (weight < 0) throw new NumberFormatException("Weight must be positive");

                int age = Integer.parseInt(ageField.getText());
                if (age < 0) throw new NumberFormatException("Age must be positive");

                Person person = new Person(nameField.getText(), weight, age);
                persons.add(person);
                // makes sure that the GUI is updated accordingly
                update();
            } catch (NumberFormatException ex) {
                errorLabel.setText(ex.getMessage());
                System.out.println("Invalid weight input " + weightField.getText());
                System.out.println("Invalid age input " + ageField.getText());

            }
        });

        Button addAtIndexButton = new Button("Add at index:");
        addAtIndexButton.setOnAction(e -> {
            try {
                errorLabel.setText("");

                int index = Integer.parseInt(indexField.getText());
                int weight = Integer.parseInt(weightField.getText());
                if (weight < 0) throw new NumberFormatException("Index is not valid");

                int age = Integer.parseInt(ageField.getText());
                if (age < 0) throw new NumberFormatException("Age is not valid");

                Person person = new Person(nameField.getText(), weight, age);
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
        Label age = new Label("Age");
        HBox overskrifter = new HBox(name, weight, age);
        overskrifter.setSpacing(45.0);
        HBox inputFelter = new HBox(nameField, weightField, ageField);
        inputFelter.setSpacing(2.0);

        indexField.setMaxWidth(40);
        //indexField.setMaxWidth(20);
        HBox addIndexHBox = new HBox(addAtIndexButton, indexField);
        addIndexHBox.setSpacing(8.0);
        HBox spacer = new HBox();


        VBox actionBox = new VBox(overskrifter, inputFelter, addButton, addIndexHBox, sortButton, clearButton, avgWeightLabel, mostCommonNameLabel, maxAgeLabel,minAgeLabel,errorLabel);
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

        /*double totalWeight = 0;
        for (int i = 0; i < persons.size(); i++) {
            totalWeight += persons.get(i).weight;
        }*/
        double avgWeight = persons.stream().mapToDouble(Person::getWeight)
                .average().orElse(0.0);
        avgWeightLabel.setText("Average weight: " + avgWeight);


        /*if (persons.size() > 0) {
            avgWeightLabel.setText("Average weight: " + (totalWeight / persons.size()));
        } else {
            avgWeightLabel.setText("Average weight: 0");

        }
         */
        if(!persons.isEmpty()) {
            int minAge = persons.stream().mapToInt(Person::getAge)
                    .min().orElse(0);

            int maxAge = persons.stream().mapToInt(Person::getAge)
                    .max().orElse(0);

            minAgeLabel.setText("Min age: " + minAge);
            maxAgeLabel.setText("Max age: " + maxAge);
        } else {
            minAgeLabel.setText("Min age: 0");
            maxAgeLabel.setText("Max age: 0");
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
}
