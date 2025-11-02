package bloodnet.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import bloodnet.commons.util.ToStringBuilder;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.donationrecord.UniqueDonationRecordList;
import bloodnet.model.person.Person;
import bloodnet.model.person.UniquePersonList;
import javafx.collections.ObservableList;


/**
 * Wraps all data at the blood-net level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class BloodNet implements ReadOnlyBloodNet {

    private final UniquePersonList persons;
    private final UniqueDonationRecordList donationRecords;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */

    {
        persons = new UniquePersonList();
        donationRecords = new UniqueDonationRecordList();
    }

    public BloodNet() {
    }

    /**
     * Creates an BloodNet using the Persons & Donation Records in the {@code toBeCopied}
     */
    public BloodNet(ReadOnlyBloodNet toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Replaces the contents of the donationRecord list with {@code donationRecords}.
     * {@code donationRecords} must not contain duplicate donationRecords.
     */
    public void setDonationRecords(List<DonationRecord> donationRecords) {
        this.donationRecords.setDonationRecords(donationRecords);
    }

    /**
     * Resets the existing data of this {@code BloodNet} with {@code newData}.
     */
    public void resetData(ReadOnlyBloodNet newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setDonationRecords(newData.getDonationRecordList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the bloodnet.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the bloodnet.
     * The person must not already exist in the bloodnet.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the bloodnet.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the bloodnet.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code BloodNet}.
     * {@code key} must exist in the bloodnet.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    /**
     * Checks if {@code person} has any {@code DonationRecord}
     * in bloodnet.
     */
    public boolean hasDonationRecordFor(Person person) {
        requireNonNull(person);
        return donationRecords.asUnmodifiableObservableList()
                .stream()
                .anyMatch(record -> record.getPersonId().equals(person.getId()));
    }

    //// donationRecord-level operations

    /**
     * Returns true if a donationRecord with the same identity as {@code donationRecord} exists in the bloodnet.
     */
    public boolean hasDonationRecord(DonationRecord donationRecord) {
        requireNonNull(donationRecord);
        return donationRecords.contains(donationRecord);
    }

    /**
     * Adds a donationRecord to the bloodnet.
     * The donationRecord must not already exist in the bloodnet.
     */
    public void addDonationRecord(DonationRecord donationRecord) {
        donationRecords.add(donationRecord);
    }

    /**
     * Replaces the given donationRecord {@code target} in the list with {@code editedDonationRecord}.
     * {@code target} must exist in the bloodnet.
     * The donationRecord identity of {@code editedDonationRecord} must not be the same as another existing
     * donationRecord in the bloodnet.
     */
    public void setDonationRecord(DonationRecord target, DonationRecord editedDonationRecord) {
        requireNonNull(editedDonationRecord);

        donationRecords.setDonationRecord(target, editedDonationRecord);
    }

    /**
     * Removes {@code key} from this {@code BloodNet}.
     * {@code key} must exist in the bloodnet.
     */
    public void removeDonationRecord(DonationRecord key) {
        donationRecords.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .add("donationRecords", donationRecords)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<DonationRecord> getDonationRecordList() {
        return donationRecords.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof BloodNet)) {
            return false;
        }

        BloodNet otherBloodNet = (BloodNet) other;
        return persons.equals(otherBloodNet.persons)
                && donationRecords.equals(otherBloodNet.donationRecords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(persons.hashCode(), donationRecords.hashCode());
    }
}
