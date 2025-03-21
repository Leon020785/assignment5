package dk.dtu.compute.course02324.assignment3.lists.uses;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.function.ToIntFunction;

public class Person implements Comparable<Person> {

    final public String name;
    final public double weight;
    public int age;

    public Person(@NotNull String name, @NotNull double weight,@NotNull int age) {
        if (name == null || weight <= 0 || age < 0) {
            throw new IllegalArgumentException("A persons must be initialized with a" +
                    "(non null) name and an age greater than 0");
        }
        this.name = name;
        this.weight = weight;
        this.age = age;

    }
    public int getAge(){
        return age;
    }

    public void setAge(int age) {
        if (age < 0) {throw new IllegalArgumentException("Age must be greater than 0");
    }
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }


    @Override
    public int compareTo(@NotNull Person o) {
        if (o == null) {
            throw new IllegalArgumentException("Argument of compareTo() must not be null");
        }
        return Double.compare(this.weight, o.weight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Double.compare(person.weight, weight) == 0 && name.equals(person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, weight);
    }

    /**
     * Computes a simple string representation of this object.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        // This could be automatically generated, but this automatically
        // generated representation is a bit too verbose. Therefore, we
        // chose a simpler representation here.
        return name + ", " + weight + "kg, " + age + " years";
    }

    /*
     * The following two methods must be implemented in order to respect the contract of objects
     * with respect to equals(), hashCode() and compareTo() methods. The details and reasons
     * as to why will be discussed much later.
     *
     * The implementations can be done fully automatically my IntelliJ (using the two attributes
     * of person).
     *
     * @param o
     * @return
     */


}






