package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static bloodnet.logic.parser.CliSyntax.PREFIX_BLOOD_VOLUME;
import static bloodnet.logic.parser.CliSyntax.PREFIX_DONATION_DATE;
import static java.util.Objects.requireNonNull;

import bloodnet.commons.core.index.Index;
import bloodnet.logic.commands.EditDonationCommand;
import bloodnet.logic.commands.EditDonationCommand.EditDonationRecordDescriptor;
import bloodnet.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditDonationCommand object.
 */
public class EditDonationCommandParser implements Parser<EditDonationCommand> {

    /**
     * Parses the provided {@code String} of arguments in the context of the EditDonationCommand
     * and returns an EditDonationCommand object to execute.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditDonationCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_BLOOD_VOLUME, PREFIX_DONATION_DATE);
        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, EditDonationCommand.getMessageUsage()), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_BLOOD_VOLUME, PREFIX_DONATION_DATE);

        EditDonationRecordDescriptor editDonationRecordDescriptor =
                new EditDonationRecordDescriptor();

        if (argMultimap.getValue(PREFIX_BLOOD_VOLUME).isPresent()) {
            editDonationRecordDescriptor.setBloodVolume(
                    ParserUtil.parseBloodVolume(argMultimap.getValue(PREFIX_BLOOD_VOLUME).get()));
        }

        if (argMultimap.getValue(PREFIX_DONATION_DATE).isPresent()) {
            editDonationRecordDescriptor.setDonationDate(
                    ParserUtil.parseDonationDate(argMultimap.getValue(PREFIX_DONATION_DATE).get()));
        }

        if (!editDonationRecordDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditDonationCommand.getMessageUsage());
        }
        return new EditDonationCommand(index, editDonationRecordDescriptor);
    }


}
