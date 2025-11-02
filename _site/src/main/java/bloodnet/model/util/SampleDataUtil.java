package bloodnet.model.util;


import java.util.UUID;

import bloodnet.model.BloodNet;
import bloodnet.model.ReadOnlyBloodNet;
import bloodnet.model.donationrecord.BloodVolume;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.person.BloodType;
import bloodnet.model.person.DateOfBirth;
import bloodnet.model.person.Email;
import bloodnet.model.person.Name;
import bloodnet.model.person.Person;
import bloodnet.model.person.Phone;

/**
 * Contains utility methods for populating {@code BloodNet} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[]{
                new Person(UUID.fromString("04adb966-5b89-4951-bdaf-0471b76b6349"),
                        new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                        new BloodType("A+"), new DateOfBirth("28-03-1995")),
                new Person(UUID.fromString("929cae0a-c20f-4a6c-8bff-782443910534"),
                        new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                        new BloodType("AB+"), new DateOfBirth("01-01-2000")),
                new Person(UUID.fromString("225a1def-43a0-4c8f-9256-18f88e162bcd"),
                        new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                        new BloodType("O-"), new DateOfBirth("04-01-2003")),
                new Person(UUID.fromString("773ef4ee-ec45-4a9c-bbc5-aeee7585895c"),
                        new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                        new BloodType("B+"), new DateOfBirth("12-12-2000")),
                new Person(UUID.fromString("763927e1-342b-468b-b539-3197e55cd53b"),
                        new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                        new BloodType("B-"), new DateOfBirth("09-06-1990")),
                new Person(UUID.fromString("cfc7e2e5-26cc-40e5-aaa2-1273a083e8af"),
                        new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                        new BloodType("O+"), new DateOfBirth("21-03-2004"))
        };
    }

    public static ReadOnlyBloodNet getSampleBloodNet() {
        BloodNet sampleBloodNet = new BloodNet();
        for (Person samplePerson : getSamplePersons()) {
            sampleBloodNet.addPerson(samplePerson);
        }

        for (DonationRecord sampleDonationRecord : getSampleDonationRecords()) {
            sampleBloodNet.addDonationRecord(sampleDonationRecord);
        }

        return sampleBloodNet;
    }

    public static DonationRecord[] getSampleDonationRecords() {
        return new DonationRecord[]{
                new DonationRecord(UUID.fromString("662cd422-ec25-4170-8656-d161a641d3f8"),
                        UUID.fromString("04adb966-5b89-4951-bdaf-0471b76b6349"),
                        new DonationDate("15-01-2025"), new BloodVolume("400")),
                new DonationRecord(UUID.fromString("01ab240a-2e7a-4beb-a8a1-5f9325019b28"),
                        UUID.fromString("04adb966-5b89-4951-bdaf-0471b76b6349"),
                        new DonationDate("15-10-2025"), new BloodVolume("450")),
                new DonationRecord(UUID.fromString("8e84ac9b-da7d-4da2-af22-d713a6337098"),
                        UUID.fromString("225a1def-43a0-4c8f-9256-18f88e162bcd"),
                        new DonationDate("21-03-2025"), new BloodVolume("400")),
                new DonationRecord(UUID.fromString("5b2e97d5-42ce-44eb-a1c2-895ea5f9b049"),
                        UUID.fromString("225a1def-43a0-4c8f-9256-18f88e162bcd"),
                        new DonationDate("25-07-2025"), new BloodVolume("400")),
                new DonationRecord(UUID.fromString("35c36cfb-e4c9-4411-a5b6-36ac5b19489b"),
                        UUID.fromString("763927e1-342b-468b-b539-3197e55cd53b"),
                        new DonationDate("18-02-2025"), new BloodVolume("400")),
                new DonationRecord(UUID.fromString("27e01424-af32-4be4-a08b-b93f7870b45f"),
                        UUID.fromString("763927e1-342b-468b-b539-3197e55cd53b"),
                        new DonationDate("15-10-2025"), new BloodVolume("499")),
        };
    }
}
