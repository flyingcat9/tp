package bloodnet.testutil;

import bloodnet.model.BloodNet;
import bloodnet.model.person.Person;

/**
 * A utility class to help with building bloodNet objects.
 * Example usage: <br>
 * {@code BloodNet ab = new BloodNetBuilder().withPerson("John", "Doe").build();}
 */
public class BloodNetBuilder {

    private BloodNet bloodNet;

    public BloodNetBuilder() {
        bloodNet = new BloodNet();
    }

    public BloodNetBuilder(BloodNet bloodNet) {
        this.bloodNet = bloodNet;
    }

    /**
     * Adds a new {@code Person} to the {@code BloodNet} that we are building.
     */
    public BloodNetBuilder withPerson(Person person) {
        bloodNet.addPerson(person);
        return this;
    }

    public BloodNet build() {
        return bloodNet;
    }
}
