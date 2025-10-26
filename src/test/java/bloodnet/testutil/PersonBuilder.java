package bloodnet.testutil;

import java.util.UUID;

import bloodnet.model.person.BloodType;
import bloodnet.model.person.DateOfBirth;
import bloodnet.model.person.Email;
import bloodnet.model.person.Name;
import bloodnet.model.person.Person;
import bloodnet.model.person.Phone;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_ID = "cb3fed29-2fa4-416e-9739-50e38ae5aa38";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_BLOOD_TYPE = "B+";
    public static final String DEFAULT_DATE_OF_BIRTH = "14-02-2000";

    private UUID id;
    private Name name;
    private Phone phone;
    private Email email;
    private BloodType bloodType;
    private DateOfBirth dateOfBirth;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        id = UUID.fromString(DEFAULT_ID);
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        bloodType = new BloodType(DEFAULT_BLOOD_TYPE);
        dateOfBirth = new DateOfBirth(DEFAULT_DATE_OF_BIRTH);
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        id = personToCopy.getId();
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        bloodType = personToCopy.getBloodType();
        dateOfBirth = personToCopy.getDateOfBirth();
    }

    /**
     * Sets the {@code ID} of the {@code Person} that we are building.
     */
    public PersonBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }


    /**
     * Sets the {@code BloodType} of the {@code Person} that we are building.
     */
    public PersonBuilder withBloodType(String bloodType) {
        this.bloodType = new BloodType(bloodType);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code DateOfBirth} of the {@code Person} that we are building.
     */
    public PersonBuilder withDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = new DateOfBirth(dateOfBirth);
        return this;
    }

    public Person build() {
        return new Person(id, name, phone, email, bloodType, dateOfBirth);
    }

}
