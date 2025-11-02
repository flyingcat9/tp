package bloodnet.testutil;

import static bloodnet.testutil.TypicalPersons.getTypicalPersons;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import bloodnet.model.BloodNet;
import bloodnet.model.donationrecord.BloodVolume;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.person.Person;

/**
 * A utility class containing a list of {@code DonationRecord} objects to be used in tests.
 */
public class TypicalDonationRecords {

    public static final List<DonationRecord> ALICE_DONATION_RECORDS = List.of(
            new DonationRecord(UUID.fromString("662cd422-ec25-4170-8656-d161a641d3f8"),
                    UUID.fromString("a7460411-cc1f-4b17-a75c-3009a766679a"),
                    new DonationDate("15-01-2025"), new BloodVolume("400")),
            new DonationRecord(UUID.fromString("01ab240a-2e7a-4beb-a8a1-5f9325019b28"),
                    UUID.fromString("a7460411-cc1f-4b17-a75c-3009a766679a"),
                    new DonationDate("15-05-2025"), new BloodVolume("450"))
    );
    public static final DonationRecord ALICE_DONATION_RECORD = ALICE_DONATION_RECORDS.get(0);

    public static final List<DonationRecord> BENSON_DONATION_RECORDS = List.of(
            new DonationRecord(UUID.fromString("8e84ac9b-da7d-4da2-af22-d713a6337098"),
                    UUID.fromString("74227cde-d5d3-455e-8852-7bbe6e15e351"),
                    new DonationDate("21-03-2025"), new BloodVolume("400")),
            new DonationRecord(UUID.fromString("5b2e97d5-42ce-44eb-a1c2-895ea5f9b049"),
                    UUID.fromString("74227cde-d5d3-455e-8852-7bbe6e15e351"),
                    new DonationDate("25-07-2025"), new BloodVolume("400"))
    );
    public static final DonationRecord BENSON_DONATION_RECORD = BENSON_DONATION_RECORDS.get(0);

    public static final List<DonationRecord> CARL_DONATION_RECORDS = List.of(
            new DonationRecord(UUID.fromString("35c36cfb-e4c9-4411-a5b6-36ac5b19489b"),
                    UUID.fromString("0bd76fb4-244b-41ee-b220-b4f03b12d6c7"),
                    new DonationDate("18-02-2025"), new BloodVolume("400")),
            new DonationRecord(UUID.fromString("27e01424-af32-4be4-a08b-b93f7870b45f"),
                    UUID.fromString("0bd76fb4-244b-41ee-b220-b4f03b12d6c7"),
                    new DonationDate("15-10-2025"), new BloodVolume("499"))
    );
    public static final DonationRecord CARL_DONATION_RECORD = CARL_DONATION_RECORDS.get(0);


    private TypicalDonationRecords() {
    } // prevents instantiation

    /**
     * Returns an {@code BloodNet} with all the typical donation records and their corresponding persons.
     */
    public static BloodNet getTypicalBloodNet() {
        BloodNet ab = new BloodNet();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }

        for (DonationRecord donationRecord : getTypicalDonationRecords()) {
            ab.addDonationRecord(donationRecord);
        }
        return ab;
    }

    public static List<DonationRecord> getTypicalDonationRecords() {
        List<DonationRecord> list = new ArrayList<>();
        list.addAll(ALICE_DONATION_RECORDS);
        list.addAll(BENSON_DONATION_RECORDS);

        // Carl's donation records are left out so that they can be added to the list in tests

        return list;
    }
}
