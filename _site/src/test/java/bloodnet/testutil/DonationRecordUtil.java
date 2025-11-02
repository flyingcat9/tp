package bloodnet.testutil;

import static bloodnet.logic.parser.CliSyntax.PREFIX_BLOOD_VOLUME;
import static bloodnet.logic.parser.CliSyntax.PREFIX_DONATION_DATE;

import bloodnet.logic.commands.EditDonationCommand;
import bloodnet.model.donationrecord.DonationRecord;

/**
 * A utility class for Person.
 */
public class DonationRecordUtil {


    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getDonationRecordDetails(DonationRecord donationRecord) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_BLOOD_VOLUME + donationRecord.getBloodVolume().toString() + " ");
        sb.append(PREFIX_DONATION_DATE + donationRecord.getBloodVolume().toString() + " ");
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditDonationRecordDescriptorDetails(EditDonationCommand.EditDonationRecordDescriptor
                                                                        descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getBloodVolume().ifPresent(bloodVolume -> sb.append(PREFIX_BLOOD_VOLUME)
                .append(bloodVolume.toString()).append(" "));
        descriptor.getDonationDate().ifPresent(donationDate -> sb.append(PREFIX_DONATION_DATE)
                .append(donationDate.value).append(" "));
        return sb.toString();
    }
}
