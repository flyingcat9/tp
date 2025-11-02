package bloodnet.model.person;

import static bloodnet.logic.commands.CommandTestUtil.VALID_BLOOD_TYPE_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_DATE_OF_BIRTH_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static bloodnet.testutil.TypicalPersons.ALICE;
import static bloodnet.testutil.TypicalPersons.BOB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bloodnet.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name, all other attributes different -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withBloodType(VALID_BLOOD_TYPE_BOB).withDateOfBirth(VALID_DATE_OF_BIRTH_BOB)
                .build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // same number, all other attributes different -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).withEmail(VALID_EMAIL_BOB)
                .withBloodType(VALID_BLOOD_TYPE_BOB).withDateOfBirth(VALID_DATE_OF_BIRTH_BOB)
                .build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // same name and same number, all other attributes different -> return true
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB)
                .withBloodType(VALID_BLOOD_TYPE_BOB).withDateOfBirth(VALID_DATE_OF_BIRTH_BOB)
                .build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different name and number, all other attributes same -> return false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // name differs in case and different number, all other attributes same -> returns false
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase())
                .withPhone(VALID_PHONE_BOB).build();
        assertFalse(BOB.isSamePerson(editedBob));

        // name has trailing spaces and different number, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces).withPhone(VALID_PHONE_BOB).build();
        assertFalse(BOB.isSamePerson(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(2));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different blood type -> returns false
        editedAlice = new PersonBuilder(ALICE).withBloodType(VALID_BLOOD_TYPE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different date of birth -> returns false
        editedAlice = new PersonBuilder(ALICE).withDateOfBirth(VALID_DATE_OF_BIRTH_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // testing two different hash codes
        editedAlice = new PersonBuilder(ALICE).withDateOfBirth(VALID_DATE_OF_BIRTH_BOB).build();
        assertNotEquals(ALICE.hashCode(), editedAlice.hashCode());

        // testing the same hash
        editedAlice = new PersonBuilder(ALICE).build();
        assertEquals(ALICE.hashCode(), ALICE.hashCode());
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName()
                + "{"
                + "id=" + ALICE.getId()
                + ", name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail()
                + ", bloodType=" + ALICE.getBloodType()
                + ", dateOfBirth=" + ALICE.getDateOfBirth()
                + "}";
        assertEquals(expected, ALICE.toString());
    }
}
