package bloodnet.testutil;


import bloodnet.logic.commands.EditDonationCommand.EditDonationRecordDescriptor;
import bloodnet.model.donationrecord.BloodVolume;
import bloodnet.model.donationrecord.DonationDate;
import bloodnet.model.donationrecord.DonationRecord;

/**
 * A utility class to help with building EditPersonDescriptor objects.
 */
public class EditDonationRecordsDescriptorBuilder {

    private EditDonationRecordDescriptor descriptor;

    public EditDonationRecordsDescriptorBuilder() {
        descriptor = new EditDonationRecordDescriptor();
    }

    public EditDonationRecordsDescriptorBuilder(EditDonationRecordDescriptor descriptor) {
        this.descriptor = new EditDonationRecordDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing {@code person}'s details
     */
    public EditDonationRecordsDescriptorBuilder(DonationRecord donationRecord) {
        descriptor = new EditDonationRecordDescriptor();
        descriptor.setBloodVolume(donationRecord.getBloodVolume());
        descriptor.setDonationDate(donationRecord.getDonationDate());
    }

    /**
     * Sets the {@code DonationDate} of the {@code EditDonationRecordDescriptor} that we are building.
     */
    public EditDonationRecordsDescriptorBuilder withDonationDate(String donationDate) {
        descriptor.setDonationDate(new DonationDate(donationDate));
        return this;
    }

    /**
     * Sets the {@code Blood Volume} of the {@code EditDonationRecordDescriptor} that we are building.
     */
    public EditDonationRecordsDescriptorBuilder withBloodVolume(String bloodVolume) {
        descriptor.setBloodVolume(new BloodVolume(bloodVolume));
        return this;
    }

    public EditDonationRecordDescriptor build() {
        return descriptor;
    }


}
