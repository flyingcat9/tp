package bloodnet.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import bloodnet.commons.exceptions.IllegalValueException;
import bloodnet.model.BloodNet;
import bloodnet.model.ReadOnlyBloodNet;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.person.Person;

/**
 * An Immutable BloodNet that is serializable to JSON format.
 */
@JsonRootName(value = "bloodnet")
class JsonSerializableBloodNet {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedDonationRecord> donationRecords = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableBloodNet} with the given persons.
     */
    @JsonCreator
    public JsonSerializableBloodNet(@JsonProperty("persons") List<JsonAdaptedPerson> persons,
                                    @JsonProperty("donationRecords") List<JsonAdaptedDonationRecord> donationRecords) {
        this.persons.addAll(persons);
        this.donationRecords.addAll(donationRecords);
    }

    /**
     * Converts a given {@code ReadOnlyBloodNet} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableBloodNet}.
     */
    public JsonSerializableBloodNet(ReadOnlyBloodNet source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
        donationRecords.addAll(source.getDonationRecordList().stream().map(JsonAdaptedDonationRecord::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this bloodnet into the model's {@code BloodNet} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public BloodNet toModelType() throws IllegalValueException {
        BloodNet bloodNet = new BloodNet();

        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            if (bloodNet.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            bloodNet.addPerson(person);
        }

        for (JsonAdaptedDonationRecord jsonAdaptedDonationRecord : donationRecords) {
            DonationRecord donationRecord = jsonAdaptedDonationRecord.toModelType();
            if (bloodNet.hasDonationRecord(donationRecord)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            bloodNet.addDonationRecord(donationRecord);
        }

        return bloodNet;
    }

}
