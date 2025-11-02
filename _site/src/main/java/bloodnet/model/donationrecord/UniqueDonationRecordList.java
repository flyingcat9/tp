package bloodnet.model.donationrecord;

import static bloodnet.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.List;

import bloodnet.model.donationrecord.exceptions.DonationRecordNotFoundException;
import bloodnet.model.donationrecord.exceptions.DuplicateDonationRecordException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A list of donationRecords that enforces uniqueness between its elements and does not allow nulls.
 * A donationRecord is considered unique by comparing using
 * {@code DonationRecord#isSameDonationRecord(DonationRecord)}. As such, adding and updating of
 * donationRecords uses DonationRecord#isSameDonationRecord(DonationRecord) for equality so as to ensure that the
 * donationRecord being added or updated is
 * unique in terms of identity in the UniqueDonationRecordList. However, the removal of a donationRecord uses
 * DonationRecord#equals(Object) so
 * as to ensure that the donationRecord with exactly the same fields will be removed.
 * <p>
 * Supports a minimal set of list operations.
 *
 * @see DonationRecord#isSameDonationRecord(DonationRecord)
 */
public class UniqueDonationRecordList implements Iterable<DonationRecord> {

    private final ObservableList<DonationRecord> internalList = FXCollections.observableArrayList();
    private final ObservableList<DonationRecord> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent donationRecord as the given argument.
     */
    public boolean contains(DonationRecord toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameDonationRecord);
    }

    /**
     * Adds a donationRecord to the list.
     * The donationRecord must not already exist in the list.
     */
    public void add(DonationRecord toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateDonationRecordException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the donationRecord {@code target} in the list with {@code editedDonationRecord}.
     * {@code target} must exist in the list.
     * The donationRecord identity of {@code editedDonationRecord} must not be the same as another existing
     * donationRecord in the list.
     */
    public void setDonationRecord(DonationRecord target, DonationRecord editedDonationRecord) {
        requireAllNonNull(target, editedDonationRecord);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new DonationRecordNotFoundException();
        }

        if (!target.isSameDonationRecord(editedDonationRecord) && contains(editedDonationRecord)) {
            throw new DuplicateDonationRecordException();
        }

        internalList.set(index, editedDonationRecord);
    }

    /**
     * Removes the equivalent donationRecord from the list.
     * The donationRecord must exist in the list.
     */
    public void remove(DonationRecord toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new DonationRecordNotFoundException();
        }
    }

    public void setDonationRecords(UniqueDonationRecordList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code donationRecords}.
     * {@code donationRecords} must not contain duplicate donationRecords.
     */
    public void setDonationRecords(List<DonationRecord> donationRecords) {
        requireAllNonNull(donationRecords);
        if (!donationRecordsAreUnique(donationRecords)) {
            throw new DuplicateDonationRecordException();
        }

        internalList.setAll(donationRecords);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<DonationRecord> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<DonationRecord> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueDonationRecordList)) {
            return false;
        }

        UniqueDonationRecordList otherUniqueDonationRecordList = (UniqueDonationRecordList) other;
        return internalList.equals(otherUniqueDonationRecordList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code donationRecords} contains only unique donation records.
     */
    private boolean donationRecordsAreUnique(List<DonationRecord> donationRecords) {
        for (int i = 0; i < donationRecords.size() - 1; i++) {
            for (int j = i + 1; j < donationRecords.size(); j++) {
                if (donationRecords.get(i).isSameDonationRecord(donationRecords.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
