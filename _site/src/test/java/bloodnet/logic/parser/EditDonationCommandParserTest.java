package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static bloodnet.logic.parser.CliSyntax.PREFIX_BLOOD_VOLUME;
import static bloodnet.logic.parser.CliSyntax.PREFIX_DONATION_DATE;
import static bloodnet.logic.parser.CommandParserTestUtil.assertParseFailure;
import static bloodnet.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static bloodnet.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import bloodnet.commons.core.index.Index;
import bloodnet.logic.Messages;
import bloodnet.logic.commands.EditDonationCommand;
import bloodnet.logic.commands.EditDonationCommand.EditDonationRecordDescriptor;
import bloodnet.model.donationrecord.BloodVolume;
import bloodnet.model.donationrecord.DonationDate;

public class EditDonationCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditDonationCommand.getMessageUsage());

    private final EditDonationCommandParser parser = new EditDonationCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, " v/400", MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditDonationCommand.getMessageUsage());

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + " v/400", MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + " v/100", MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + " v/400x",
                BloodVolume.MESSAGE_CONSTRAINTS); // invalid blood volume
        assertParseFailure(parser, "1" + " d/01-20-xxxx",
                DonationDate.MESSAGE_CONSTRAINTS); // invalid donation date

        // invalid blood volume and donation date
        assertParseFailure(parser, "1" + " v/x00" + " d/05-25-2020",
                BloodVolume.MESSAGE_CONSTRAINTS);

        // both fields are invalid, but only the first invalid value is captured
        assertParseFailure(parser, "1" + " v/xx"
                        + "d/02-10-xxxx",
                BloodVolume.MESSAGE_CONSTRAINTS);
    }


    @Test
    public void parse_multipleRepeatedFields_failure() {

        // valid followed by invalid blood type
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " v/200" + " v/200xxx";

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_BLOOD_VOLUME));

        // invalid followed by valid blood type
        userInput = targetIndex.getOneBased() + " v/1x0" + " " + "v/330";

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_BLOOD_VOLUME));

        // multiple valid fields repeated
        userInput = targetIndex.getOneBased() + " d/01-01-2022" + " d/01-01-2023"
                + " v/100" + " v/1000";

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_BLOOD_VOLUME,
                        PREFIX_DONATION_DATE));
    }

    @Test
    public void parse_validInputString_success() {
        Index targetIndex = Index.fromOneBased(1);
        EditDonationRecordDescriptor edit = new EditDonationRecordDescriptor();

        edit.setBloodVolume(new BloodVolume("400"));
        edit.setDonationDate(new DonationDate("01-01-2000"));

        EditDonationCommand expectedCommand = new EditDonationCommand(
                targetIndex, edit);

        assertParseSuccess(parser, "1 d/01-01-2000 v/400", expectedCommand);

    }

}
