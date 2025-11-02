package bloodnet.model.donationrecord.exceptions;

/**
 * Signals that the operation will result in duplicate Persons (Persons are considered duplicates if they have the same
 * identity).
 */
public class DuplicateDonationRecordException extends RuntimeException {
    public DuplicateDonationRecordException() {
        super("Operation would result in duplicate donation records");
    }
}
