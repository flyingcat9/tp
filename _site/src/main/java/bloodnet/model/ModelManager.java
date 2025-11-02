package bloodnet.model;

import static bloodnet.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import bloodnet.commons.core.GuiSettings;
import bloodnet.commons.core.LogsCenter;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.person.Person;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * Represents the in-memory model of the bloodnet data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final BloodNet bloodNet;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<DonationRecord> filteredDonationRecords;

    /**
     * Initializes a ModelManager with the given bloodNet and userPrefs.
     */
    public ModelManager(ReadOnlyBloodNet bloodNet, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(bloodNet, userPrefs);

        logger.fine("Initializing with BloodNet: " + bloodNet + " and user prefs " + userPrefs);

        this.bloodNet = new BloodNet(bloodNet);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.bloodNet.getPersonList());
        filteredDonationRecords = new FilteredList<>(this.bloodNet.getDonationRecordList());
    }

    public ModelManager() {
        this(new BloodNet(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getBloodNetFilePath() {
        return userPrefs.getBloodNetFilePath();
    }

    @Override
    public void setBloodNetFilePath(Path bloodNetFilePath) {
        requireNonNull(bloodNetFilePath);
        userPrefs.setBloodNetFilePath(bloodNetFilePath);
    }

    //=========== BloodNet ================================================================================

    @Override
    public void setBloodNet(ReadOnlyBloodNet bloodNet) {
        this.bloodNet.resetData(bloodNet);
    }

    @Override
    public ReadOnlyBloodNet getBloodNet() {
        return bloodNet;
    }

    //=========== Person methods =============================================================

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return bloodNet.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        bloodNet.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        bloodNet.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        bloodNet.setPerson(target, editedPerson);
    }

    @Override
    public boolean hasDonationRecordFor(Person person) {
        requireNonNull(person);
        return bloodNet.hasDonationRecordFor(person);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedBloodNet}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    //=========== DonationRecord methods =============================================================

    @Override
    public boolean hasDonationRecord(DonationRecord donationRecord) {
        requireNonNull(donationRecord);
        return bloodNet.hasDonationRecord(donationRecord);
    }

    @Override
    public void deleteDonationRecord(DonationRecord target) {
        bloodNet.removeDonationRecord(target);
    }

    @Override
    public void addDonationRecord(DonationRecord donationRecord) {
        bloodNet.addDonationRecord(donationRecord);
        updateFilteredDonationRecordList(PREDICATE_SHOW_ALL_DONATION_RECORDS);
    }

    @Override
    public void setDonationRecord(DonationRecord target, DonationRecord editedDonationRecord) {
        requireAllNonNull(target, editedDonationRecord);

        bloodNet.setDonationRecord(target, editedDonationRecord);
    }

    //=========== Filtered DonationRecord List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code DonationRecord} backed by the internal list of
     * {@code versionedBloodNet}
     */
    @Override
    public ObservableList<DonationRecord> getFilteredDonationRecordList() {
        return filteredDonationRecords;
    }

    @Override
    public void updateFilteredDonationRecordList(Predicate<DonationRecord> predicate) {
        requireNonNull(predicate);
        filteredDonationRecords.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return bloodNet.equals(otherModelManager.bloodNet)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons)
                && filteredDonationRecords.equals(otherModelManager.filteredDonationRecords);
    }

}
