package bloodnet.testutil;

import static bloodnet.logic.commands.CommandTestUtil.VALID_BLOOD_TYPE_AMY;
import static bloodnet.logic.commands.CommandTestUtil.VALID_BLOOD_TYPE_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_ELIGIBILITY_STATUS_AMY;
import static bloodnet.logic.commands.CommandTestUtil.VALID_ELIGIBILITY_STATUS_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static bloodnet.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static bloodnet.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static bloodnet.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static bloodnet.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bloodnet.model.AddressBook;
import bloodnet.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder().withName("Alice Pauline")
            .withBloodType("A+").withEmail("alice@example.com")
            .withEligibilityStatus("deferred")
            .withPhone("94351253")
            .withTags("friends").build();
    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
            .withEligibilityStatus("unscreened")
            .withBloodType("A-")
            .withEmail("johnd@example.com").withPhone("98765432")
            .withTags("owesMoney", "friends").build();
    public static final Person CARL = new PersonBuilder().withName("Carl Kurz").withPhone("95352563")
            .withEmail("heinz@example.com").withEligibilityStatus("eligible").withBloodType("B+").build();
    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@example.com").withBloodType("B-")
            .withEligibilityStatus("cooldown").withTags("friends").build();
    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer").withPhone("94822242")
            .withEmail("werner@example.com").withBloodType("O-")
            .withEligibilityStatus("ineligible").build();
    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz").withPhone("94824271")
            .withEmail("lydia@example.com").withBloodType("O+")
            .withEligibilityStatus("eligible").build();
    public static final Person GEORGE = new PersonBuilder().withName("George Best").withPhone("94824422")
            .withEmail("anna@example.com").withBloodType("AB+").withEligibilityStatus("DEFERRED").build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier").withPhone("84842424")
            .withEmail("stefan@example.com").withEligibilityStatus("eligible").withBloodType("B+").build();
    public static final Person IDA = new PersonBuilder().withName("Ida Mueller").withPhone("84842131")
            .withEmail("hans@example.com").withEligibilityStatus("cooldown").withBloodType("B+").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withBloodType(VALID_BLOOD_TYPE_AMY)
            .withEligibilityStatus(VALID_ELIGIBILITY_STATUS_AMY).withTags(VALID_TAG_FRIEND).build();
    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withBloodType(VALID_BLOOD_TYPE_BOB)
            .withEligibilityStatus(VALID_ELIGIBILITY_STATUS_BOB).withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
