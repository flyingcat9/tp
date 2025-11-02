package bloodnet.testutil;

import static bloodnet.logic.commands.CommandTestUtil.VALID_BLOOD_TYPE_AMY;
import static bloodnet.logic.commands.CommandTestUtil.VALID_BLOOD_TYPE_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_DATE_OF_BIRTH_AMY;
import static bloodnet.logic.commands.CommandTestUtil.VALID_DATE_OF_BIRTH_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static bloodnet.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static bloodnet.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static bloodnet.logic.commands.CommandTestUtil.VALID_PHONE_BOB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import bloodnet.model.BloodNet;
import bloodnet.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder()
            .withId(UUID.fromString("a7460411-cc1f-4b17-a75c-3009a766679a")).withName("Alice Pauline")
            .withBloodType("A+").withEmail("alice@example.com")
            .withPhone("94351253").withDateOfBirth("17-03-2000")
            .build();
    public static final Person BENSON = new PersonBuilder()
            .withId(UUID.fromString("74227cde-d5d3-455e-8852-7bbe6e15e351")).withName("Benson Meier")
            .withBloodType("A-").withDateOfBirth("20-04-1996")
            .withEmail("johnd@example.com").withPhone("98765432")
            .build();
    public static final Person CARL = new PersonBuilder()
            .withId(UUID.fromString("0bd76fb4-244b-41ee-b220-b4f03b12d6c7")).withName("Carl Kurz")
            .withPhone("95352563")
            .withEmail("heinz@example.com").withDateOfBirth("04-05-1994").withBloodType("B+").build();
    public static final Person DANIEL = new PersonBuilder()
            .withId(UUID.fromString("68e79db6-37d9-4e0a-a02a-1831bb6a9f20")).withName("Daniel Meier")
            .withPhone("87652533")
            .withEmail("cornelia@example.com").withDateOfBirth("12-12-1992")
            .withBloodType("B-").build();
    public static final Person ELLE = new PersonBuilder()
            .withId(UUID.fromString("84901d92-c6af-438a-9f87-7b24e926f01c")).withName("Elle Meyer")
            .withPhone("94822242")
            .withEmail("werner@example.com").withBloodType("O-").withDateOfBirth("03-04-1998").build();
    public static final Person FIONA = new PersonBuilder()
            .withId(UUID.fromString("781f4d25-e281-4e9e-ba2a-e903446d1191")).withName("Fiona Kunz")
            .withPhone("94824271")
            .withEmail("lydia@example.com").withBloodType("O+").withDateOfBirth("01-02-1996").build();
    public static final Person GEORGE = new PersonBuilder()
            .withId(UUID.fromString("c633e609-304e-4d8f-ad18-8c4694ad05fd")).withName("George Best")
            .withDateOfBirth("01-01-2002")
            .withPhone("94824422")
            .withEmail("anna@example.com").withBloodType("AB+").build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier").withPhone("84842424")
            .withDateOfBirth("17-07-2004").withEmail("stefan@example.com").withBloodType("B+").build();
    public static final Person IDA = new PersonBuilder().withName("Ida Mueller").withPhone("84842131")
            .withDateOfBirth("11-08-1980").withEmail("hans@example.com").withBloodType("B+").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withBloodType(VALID_BLOOD_TYPE_AMY)
            .withDateOfBirth(VALID_DATE_OF_BIRTH_AMY).build();
    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withBloodType(VALID_BLOOD_TYPE_BOB)
            .withDateOfBirth(VALID_DATE_OF_BIRTH_BOB)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {
    } // prevents instantiation

    /**
     * Returns an {@code BloodNet} with all the typical persons.
     */
    public static BloodNet getTypicalBloodNet() {
        BloodNet ab = new BloodNet();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
