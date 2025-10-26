package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static bloodnet.logic.commands.CommandTestUtil.BLOOD_TYPE_DESC_AMY;
import static bloodnet.logic.commands.CommandTestUtil.BLOOD_TYPE_DESC_BOB;
import static bloodnet.logic.commands.CommandTestUtil.DATE_OF_BIRTH_DESC_AMY;
import static bloodnet.logic.commands.CommandTestUtil.DATE_OF_BIRTH_DESC_BOB;
import static bloodnet.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static bloodnet.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static bloodnet.logic.commands.CommandTestUtil.INVALID_BLOOD_TYPE_DESC;
import static bloodnet.logic.commands.CommandTestUtil.INVALID_DATE_OF_BIRTH_DESC;
import static bloodnet.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static bloodnet.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static bloodnet.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static bloodnet.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static bloodnet.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static bloodnet.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static bloodnet.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static bloodnet.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static bloodnet.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static bloodnet.logic.commands.CommandTestUtil.VALID_BLOOD_TYPE_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_DATE_OF_BIRTH_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static bloodnet.logic.parser.CliSyntax.PREFIX_BLOOD_TYPE;
import static bloodnet.logic.parser.CliSyntax.PREFIX_DATE_OF_BIRTH;
import static bloodnet.logic.parser.CliSyntax.PREFIX_EMAIL;
import static bloodnet.logic.parser.CliSyntax.PREFIX_NAME;
import static bloodnet.logic.parser.CliSyntax.PREFIX_PHONE;
import static bloodnet.logic.parser.CommandParserTestUtil.assertParseFailure;
import static bloodnet.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static bloodnet.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import bloodnet.logic.Messages;
import bloodnet.logic.commands.AddCommand;
import bloodnet.model.person.BloodType;
import bloodnet.model.person.DateOfBirth;
import bloodnet.model.person.Email;
import bloodnet.model.person.Name;
import bloodnet.model.person.Person;
import bloodnet.model.person.Phone;
import bloodnet.testutil.PersonBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(BOB).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + BLOOD_TYPE_DESC_BOB + DATE_OF_BIRTH_DESC_BOB, new AddCommand(expectedPerson));


    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + BLOOD_TYPE_DESC_BOB + DATE_OF_BIRTH_DESC_BOB;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple emails
        assertParseFailure(parser, EMAIL_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple blood types
        assertParseFailure(parser, BLOOD_TYPE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_BLOOD_TYPE));

        // multiple birth dates
        assertParseFailure(parser, DATE_OF_BIRTH_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATE_OF_BIRTH));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPersonString + PHONE_DESC_AMY + EMAIL_DESC_AMY
                        + NAME_DESC_AMY + BLOOD_TYPE_DESC_AMY + DATE_OF_BIRTH_DESC_AMY
                        + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_BLOOD_TYPE,
                        PREFIX_EMAIL, PREFIX_PHONE, PREFIX_DATE_OF_BIRTH));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, INVALID_EMAIL_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid blood type
        assertParseFailure(parser, INVALID_BLOOD_TYPE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_BLOOD_TYPE));

        // invalid date of birth
        assertParseFailure(parser, INVALID_DATE_OF_BIRTH_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATE_OF_BIRTH));

        // valid value followed by an invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, validExpectedPersonString + INVALID_EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, validExpectedPersonString + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid blood type
        assertParseFailure(parser, validExpectedPersonString + INVALID_BLOOD_TYPE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_BLOOD_TYPE));

        // invalid date of birth
        assertParseFailure(parser, validExpectedPersonString + INVALID_DATE_OF_BIRTH_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATE_OF_BIRTH));
    }


    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.getMessageUsage());

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB
                        + PHONE_DESC_BOB + EMAIL_DESC_BOB + BLOOD_TYPE_DESC_BOB + DATE_OF_BIRTH_DESC_BOB,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB
                        + BLOOD_TYPE_DESC_BOB + DATE_OF_BIRTH_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB
                        + BLOOD_TYPE_DESC_BOB + DATE_OF_BIRTH_DESC_BOB,
                expectedMessage);

        // missing blood type prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB
                        + EMAIL_DESC_BOB + VALID_BLOOD_TYPE_BOB + DATE_OF_BIRTH_DESC_BOB,
                expectedMessage);

        // missing date of birth prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB
                        + EMAIL_DESC_BOB + BLOOD_TYPE_DESC_BOB + VALID_DATE_OF_BIRTH_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB
                        + VALID_BLOOD_TYPE_BOB + VALID_DATE_OF_BIRTH_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + BLOOD_TYPE_DESC_BOB
                + DATE_OF_BIRTH_DESC_BOB, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + BLOOD_TYPE_DESC_BOB
                + DATE_OF_BIRTH_DESC_BOB, Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC + BLOOD_TYPE_DESC_BOB
                + DATE_OF_BIRTH_DESC_BOB, Email.MESSAGE_CONSTRAINTS);

        // invalid blood type
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_BLOOD_TYPE_DESC
                + DATE_OF_BIRTH_DESC_BOB, BloodType.MESSAGE_CONSTRAINTS);

        // invalid date of birth
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + BLOOD_TYPE_DESC_BOB
                + INVALID_DATE_OF_BIRTH_DESC, DateOfBirth.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + INVALID_BLOOD_TYPE_DESC + DATE_OF_BIRTH_DESC_BOB,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + BLOOD_TYPE_DESC_BOB + DATE_OF_BIRTH_DESC_BOB,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.getMessageUsage()));
    }
}
