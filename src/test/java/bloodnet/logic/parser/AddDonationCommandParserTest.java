package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static bloodnet.logic.parser.CliSyntax.PREFIX_BLOOD_VOLUME;
import static bloodnet.logic.parser.CliSyntax.PREFIX_DONATION_DATE;
import static bloodnet.logic.parser.CliSyntax.PREFIX_PERSON_INDEX_ONE_BASED;
import static bloodnet.logic.parser.CommandParserTestUtil.assertParseFailure;
import static bloodnet.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import bloodnet.commons.core.index.Index;
import bloodnet.logic.commands.AddDonationCommand;
import bloodnet.model.donationrecord.BloodVolume;
import bloodnet.model.donationrecord.DonationDate;

public class AddDonationCommandParserTest {

    private AddDonationCommandParser parser = new AddDonationCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Index targetIndex = Index.fromOneBased(1);
        String userInput = " " + PREFIX_PERSON_INDEX_ONE_BASED + "1 "
                + PREFIX_DONATION_DATE + "15-10-2023 "
                + PREFIX_BLOOD_VOLUME + "450";

        AddDonationCommand expectedCommand = new AddDonationCommand(
                targetIndex,
                new DonationDate("15-10-2023"),
                new BloodVolume("450"));

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingPersonIndex_failure() {
        String userInput = " " + PREFIX_DONATION_DATE + "15-10-2023 "
                + PREFIX_BLOOD_VOLUME + "450";

        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddDonationCommand.getMessageUsage()));
    }

    @Test
    public void parse_missingDonationDate_failure() {
        String userInput = " " + PREFIX_PERSON_INDEX_ONE_BASED + "1 "
                + PREFIX_BLOOD_VOLUME + "450";

        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddDonationCommand.getMessageUsage()));
    }

    @Test
    public void parse_missingBloodVolume_failure() {
        String userInput = " " + PREFIX_PERSON_INDEX_ONE_BASED + "1 "
                + PREFIX_DONATION_DATE + "15-10-2023";

        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddDonationCommand.getMessageUsage()));
    }

    @Test
    public void parse_allFieldsMissing_failure() {
        String userInput = "";

        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddDonationCommand.getMessageUsage()));
    }

    @Test
    public void parse_preamblePresent_failure() {
        String userInput = "some preamble " + PREFIX_PERSON_INDEX_ONE_BASED + "1 "
                + PREFIX_DONATION_DATE + "15-10-2023 "
                + PREFIX_BLOOD_VOLUME + "450";

        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddDonationCommand.getMessageUsage()));
    }

    @Test
    public void parse_invalidIndex_failure() {
        String userInput = " " + PREFIX_PERSON_INDEX_ONE_BASED + "-1 "
                + PREFIX_DONATION_DATE + "15-10-2023 "
                + PREFIX_BLOOD_VOLUME + "450";

        assertParseFailure(parser, userInput, ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_invalidDonationDate_failure() {
        String userInput = " " + PREFIX_PERSON_INDEX_ONE_BASED + "1 "
                + PREFIX_DONATION_DATE + "invalid-date "
                + PREFIX_BLOOD_VOLUME + "450";

        assertParseFailure(parser, userInput, DonationDate.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidBloodVolume_failure() {
        String userInput = " " + PREFIX_PERSON_INDEX_ONE_BASED + "1 "
                + PREFIX_DONATION_DATE + "15-10-2023 "
                + PREFIX_BLOOD_VOLUME + "invalid";

        assertParseFailure(parser, userInput, BloodVolume.MESSAGE_CONSTRAINTS);
    }
}
