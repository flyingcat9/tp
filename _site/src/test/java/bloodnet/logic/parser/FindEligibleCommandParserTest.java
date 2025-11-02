package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static bloodnet.logic.parser.CommandParserTestUtil.assertParseFailure;
import static bloodnet.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import bloodnet.logic.commands.FindEligibleCommand;
import bloodnet.model.person.BloodType;


public class FindEligibleCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindEligibleCommand.getMessageUsage());

    private final FindEligibleCommandParser parser = new FindEligibleCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        // only spaces included
        assertParseFailure(parser, "    ", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidArg_throwsParseException() {
        // invalid blood type, then valid blood type
        assertParseFailure(parser, "T+ A+ B+", BloodType.MESSAGE_CONSTRAINTS);

        // valid blood type, then invalid blood type
        assertParseFailure(parser, "A+ T+ A+", BloodType.MESSAGE_CONSTRAINTS);

        // valid blood type, then invalid blood type
        assertParseFailure(parser, "1232312321", BloodType.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_validArg_success() {
        String[] arrayOfBloodTypes = new String[]{"O+", "A+", "AB+"};
        FindEligibleCommand findEligibleCommand = new FindEligibleCommand(Arrays.asList(arrayOfBloodTypes));
        assertParseSuccess(parser, "O+ A+ AB+", findEligibleCommand);
    }
}
