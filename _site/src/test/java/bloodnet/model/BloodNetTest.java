package bloodnet.model;

import static bloodnet.logic.commands.CommandTestUtil.VALID_BLOOD_TYPE_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_DATE_OF_BIRTH_BOB;
import static bloodnet.testutil.Assert.assertThrows;
import static bloodnet.testutil.TypicalPersons.ALICE;
import static bloodnet.testutil.TypicalPersons.getTypicalBloodNet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.person.Person;
import bloodnet.model.person.exceptions.DuplicatePersonException;
import bloodnet.testutil.PersonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BloodNetTest {

    private final BloodNet bloodNet = new BloodNet();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), bloodNet.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> bloodNet.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyBloodNet_replacesData() {
        BloodNet newData = getTypicalBloodNet();
        bloodNet.resetData(newData);
        assertEquals(newData, bloodNet);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE)
                .withBloodType(VALID_BLOOD_TYPE_BOB)
                .withDateOfBirth(VALID_DATE_OF_BIRTH_BOB).build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        List<DonationRecord> newDonationRecords = List.of();
        BloodNetStub newData = new BloodNetStub(newPersons, newDonationRecords);

        assertThrows(DuplicatePersonException.class, () -> bloodNet.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> bloodNet.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInBloodNet_returnsFalse() {
        assertFalse(bloodNet.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInBloodNet_returnsTrue() {
        bloodNet.addPerson(ALICE);
        assertTrue(bloodNet.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInBloodNet_returnsTrue() {
        bloodNet.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE)
                .withBloodType(VALID_BLOOD_TYPE_BOB)
                .withDateOfBirth(VALID_DATE_OF_BIRTH_BOB).build();
        assertTrue(bloodNet.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> bloodNet.getPersonList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = BloodNet.class.getCanonicalName()
                + "{"
                + "persons=" + bloodNet.getPersonList()
                + ", donationRecords=" + bloodNet.getDonationRecordList()
                + "}";
        assertEquals(expected, bloodNet.toString());
    }

    /**
     * A stub ReadOnlyBloodNet whose persons list can violate interface constraints.
     */
    private static class BloodNetStub implements ReadOnlyBloodNet {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<DonationRecord> donationRecords = FXCollections.observableArrayList();

        BloodNetStub(Collection<Person> persons, Collection<DonationRecord> donationRecords) {
            this.persons.setAll(persons);
            this.donationRecords.setAll(donationRecords);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<DonationRecord> getDonationRecordList() {
            return donationRecords;
        }
    }

}
