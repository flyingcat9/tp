package bloodnet.logic.commands;

import static bloodnet.logic.parser.CliSyntax.PREFIX_BLOOD_TYPE;
import static bloodnet.logic.parser.CliSyntax.PREFIX_DATE_OF_BIRTH;
import static bloodnet.logic.parser.CliSyntax.PREFIX_EMAIL;
import static bloodnet.logic.parser.CliSyntax.PREFIX_NAME;
import static bloodnet.logic.parser.CliSyntax.PREFIX_PHONE;
import static bloodnet.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bloodnet.commons.core.index.Index;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.model.BloodNet;
import bloodnet.model.Model;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.person.NameContainsKeywordsPredicate;
import bloodnet.model.person.Person;
import bloodnet.testutil.EditPersonDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    // Person Test Values
    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "88888888";
    public static final String VALID_PHONE_BOB = "99999999";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_BLOOD_TYPE_AMY = "A+";
    public static final String VALID_BLOOD_TYPE_BOB = "B-";
    public static final String VALID_DATE_OF_BIRTH_AMY = "01-04-1995";
    public static final String VALID_DATE_OF_BIRTH_BOB = "12-12-1992";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String BLOOD_TYPE_DESC_AMY = " " + PREFIX_BLOOD_TYPE + VALID_BLOOD_TYPE_AMY;
    public static final String BLOOD_TYPE_DESC_BOB = " " + PREFIX_BLOOD_TYPE + VALID_BLOOD_TYPE_BOB;
    public static final String DATE_OF_BIRTH_DESC_AMY = " " + PREFIX_DATE_OF_BIRTH + VALID_DATE_OF_BIRTH_AMY;
    public static final String DATE_OF_BIRTH_DESC_BOB = " " + PREFIX_DATE_OF_BIRTH + VALID_DATE_OF_BIRTH_BOB;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "  234a   "; // less than 2 letters in the name
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_BLOOD_TYPE_DESC = " " + PREFIX_BLOOD_TYPE; // " " not allowed for blood types
    public static final String INVALID_DATE_OF_BIRTH_DESC = " "
            + PREFIX_DATE_OF_BIRTH; // " " not allowed for date of births

    // Donation Record Test Values
    public static final String VALID_DONATION_DATE_AMY = "12-12-2024";
    public static final String VALID_DONATION_DATE_BOB = "15-09-2025";
    public static final String VALID_BLOOD_VOLUME_AMY = "350";
    public static final String VALID_BLOOD_VOLUME_BOB = "410";


    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditPersonDescriptor DESC_AMY;
    public static final EditCommand.EditPersonDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withBloodType(VALID_BLOOD_TYPE_AMY)
                .withDateOfBirth(VALID_DATE_OF_BIRTH_AMY).build();
        DESC_BOB = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).withBloodType(VALID_BLOOD_TYPE_BOB)
                .withDateOfBirth(VALID_DATE_OF_BIRTH_BOB).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link InputResponse} matches {@code expectedInputResponse} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, InputResponse expectedInputResponse,
                                            Model expectedModel) {
        try {
            InputResponse response = command.execute(actualModel);
            assertEquals(expectedInputResponse, response);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, InputResponse, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
                                            Model expectedModel) {
        InputResponse expectedInputResponse = new InputResponse(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedInputResponse, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the bloodnet, filtered person list and selected person in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        BloodNet expectedBloodNet = new BloodNet(actualModel.getBloodNet());
        List<Person> expectedFilteredList = new ArrayList<>(actualModel.getFilteredPersonList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedBloodNet, actualModel.getBloodNet());
        assertEquals(expectedFilteredList, actualModel.getFilteredPersonList());
    }

    /**
     * Updates {@code model}'s filtered list to show only the person at the given {@code targetIndex} in the
     * {@code model}'s bloodnet.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredPersonList().size());

        Person person = model.getFilteredPersonList().get(targetIndex.getZeroBased());
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredPersonList().size());
    }

    /**
     * Updates {@code model}'s filtered list to show only the donation record at the given {@code targetIndex} in the
     * {@code model}'s bloodnet.
     *
     * @param model
     * @param targetIndex
     */
    public static void showDonationRecordAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredDonationRecordList().size());

        DonationRecord donationRecord = model.getFilteredDonationRecordList().get(targetIndex.getZeroBased());
        model.updateFilteredDonationRecordList(d -> d.equals(donationRecord));

        assertEquals(1, model.getFilteredDonationRecordList().size());
    }

}
