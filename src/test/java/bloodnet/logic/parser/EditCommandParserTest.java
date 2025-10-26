package bloodnet.logic.parser;

import static bloodnet.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static bloodnet.logic.commands.CommandTestUtil.BLOOD_TYPE_DESC_AMY;
import static bloodnet.logic.commands.CommandTestUtil.BLOOD_TYPE_DESC_BOB;
import static bloodnet.logic.commands.CommandTestUtil.DATE_OF_BIRTH_DESC_AMY;
import static bloodnet.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static bloodnet.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static bloodnet.logic.commands.CommandTestUtil.INVALID_BLOOD_TYPE_DESC;
import static bloodnet.logic.commands.CommandTestUtil.INVALID_DATE_OF_BIRTH_DESC;
import static bloodnet.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static bloodnet.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static bloodnet.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static bloodnet.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static bloodnet.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static bloodnet.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_BLOOD_TYPE_AMY;
import static bloodnet.logic.commands.CommandTestUtil.VALID_DATE_OF_BIRTH_AMY;
import static bloodnet.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static bloodnet.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static bloodnet.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static bloodnet.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static bloodnet.logic.parser.CliSyntax.PREFIX_BLOOD_TYPE;
import static bloodnet.logic.parser.CliSyntax.PREFIX_DATE_OF_BIRTH;
import static bloodnet.logic.parser.CliSyntax.PREFIX_EMAIL;
import static bloodnet.logic.parser.CliSyntax.PREFIX_PHONE;
import static bloodnet.logic.parser.CommandParserTestUtil.assertParseFailure;
import static bloodnet.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static bloodnet.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static bloodnet.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static bloodnet.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import org.junit.jupiter.api.Test;

import bloodnet.commons.core.index.Index;
import bloodnet.logic.Messages;
import bloodnet.logic.commands.EditCommand;
import bloodnet.logic.commands.EditCommand.EditPersonDescriptor;
import bloodnet.model.person.BloodType;
import bloodnet.model.person.DateOfBirth;
import bloodnet.model.person.Email;
import bloodnet.model.person.Name;
import bloodnet.model.person.Phone;
import bloodnet.testutil.EditPersonDescriptorBuilder;

public class EditCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.getMessageUsage());

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS); // invalid phone
        assertParseFailure(parser, "1" + INVALID_EMAIL_DESC, Email.MESSAGE_CONSTRAINTS); // invalid email
        assertParseFailure(parser, "1" + INVALID_BLOOD_TYPE_DESC,
                BloodType.MESSAGE_CONSTRAINTS); // invalid blood type
        assertParseFailure(parser, "1" + INVALID_DATE_OF_BIRTH_DESC,
                DateOfBirth.MESSAGE_CONSTRAINTS); // invalid date of birth

        // invalid phone followed by valid email
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC + EMAIL_DESC_AMY, Phone.MESSAGE_CONSTRAINTS);


        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_NAME_DESC
                        + INVALID_EMAIL_DESC + VALID_DATE_OF_BIRTH_AMY
                        + VALID_BLOOD_TYPE_AMY + VALID_PHONE_AMY,
                Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB
                + EMAIL_DESC_AMY + BLOOD_TYPE_DESC_AMY
                + DATE_OF_BIRTH_DESC_AMY + NAME_DESC_AMY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY)
                .withBloodType(VALID_BLOOD_TYPE_AMY).withDateOfBirth(VALID_DATE_OF_BIRTH_AMY)
                .build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + EMAIL_DESC_AMY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY;
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = targetIndex.getOneBased() + EMAIL_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // blood type
        userInput = targetIndex.getOneBased() + BLOOD_TYPE_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withBloodType(VALID_BLOOD_TYPE_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // date of birth
        userInput = targetIndex.getOneBased() + DATE_OF_BIRTH_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withDateOfBirth(VALID_DATE_OF_BIRTH_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections is done in
        // AddCommandParserTest#parse_repeatedNonTagValue_failure()

        // valid followed by invalid
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + PHONE_DESC_BOB;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid followed by valid
        userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + INVALID_PHONE_DESC;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // mulltiple valid fields repeated
        userInput = targetIndex.getOneBased() + DATE_OF_BIRTH_DESC_AMY + PHONE_DESC_AMY + BLOOD_TYPE_DESC_AMY
                + EMAIL_DESC_AMY + PHONE_DESC_AMY + BLOOD_TYPE_DESC_AMY
                + EMAIL_DESC_AMY + PHONE_DESC_BOB + BLOOD_TYPE_DESC_BOB
                + EMAIL_DESC_BOB
                + DATE_OF_BIRTH_DESC_AMY;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATE_OF_BIRTH,
                        PREFIX_PHONE, PREFIX_EMAIL, PREFIX_BLOOD_TYPE));

        // multiple invalid values
        userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + INVALID_DATE_OF_BIRTH_DESC
                + INVALID_BLOOD_TYPE_DESC + INVALID_EMAIL_DESC
                + INVALID_PHONE_DESC + INVALID_BLOOD_TYPE_DESC + INVALID_EMAIL_DESC
                + INVALID_DATE_OF_BIRTH_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_BLOOD_TYPE,
                        PREFIX_DATE_OF_BIRTH));
    }

}
