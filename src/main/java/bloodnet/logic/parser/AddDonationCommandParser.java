package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static bloodnet.logic.parser.CliSyntax.PREFIX_BLOOD_VOLUME;
import static bloodnet.logic.parser.CliSyntax.PREFIX_DONATION_DATE;
import static bloodnet.logic.parser.CliSyntax.PREFIX_PERSON_INDEX_ONE_BASED;

import java.util.stream.Stream;

import bloodnet.commons.core.index.Index;
import bloodnet.logic.commands.AddDonationCommand;
import bloodnet.logic.parser.exceptions.ParseException;
import bloodnet.model.donationrecord.BloodVolume;
import bloodnet.model.donationrecord.DonationDate;

/**
 * Parses input arguments and creates a new AddDonationCommand object
 */
public class AddDonationCommandParser implements Parser<AddDonationCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddDonationCommand
     * and returns an AddDonationCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddDonationCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
            ArgumentTokenizer.tokenize(args, PREFIX_PERSON_INDEX_ONE_BASED, PREFIX_DONATION_DATE,
                PREFIX_BLOOD_VOLUME);

        if (!arePrefixesPresent(argMultimap, PREFIX_PERSON_INDEX_ONE_BASED, PREFIX_DONATION_DATE,
                PREFIX_BLOOD_VOLUME)
            || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddDonationCommand.getMessageUsage()));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_PERSON_INDEX_ONE_BASED, PREFIX_DONATION_DATE,
                PREFIX_BLOOD_VOLUME);
        Index index = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_PERSON_INDEX_ONE_BASED).get());
        DonationDate donationDate = ParserUtil.parseDonationDate(argMultimap.getValue(PREFIX_DONATION_DATE).get());
        BloodVolume bloodVolume = ParserUtil.parseBloodVolume(argMultimap.getValue(PREFIX_BLOOD_VOLUME).get());

        return new AddDonationCommand(index, donationDate, bloodVolume);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
