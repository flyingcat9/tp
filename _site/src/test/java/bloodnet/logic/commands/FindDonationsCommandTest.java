package bloodnet.logic.commands;

import static bloodnet.logic.Messages.MESSAGE_DONATIONS_LISTED_OVERVIEW;
import static bloodnet.logic.commands.CommandTestUtil.assertCommandFailure;
import static bloodnet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static bloodnet.testutil.TypicalDonationRecords.ALICE_DONATION_RECORDS;
import static bloodnet.testutil.TypicalDonationRecords.getTypicalBloodNet;
import static bloodnet.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static bloodnet.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static bloodnet.testutil.TypicalPersons.ALICE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bloodnet.commons.core.index.Index;
import bloodnet.logic.Messages;
import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.UserPrefs;
import bloodnet.model.donationrecord.DonorIsSamePersonPredicate;
import bloodnet.model.person.Person;

public class FindDonationsCommandTest {
    private Model model = new ModelManager(getTypicalBloodNet(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalBloodNet(), new UserPrefs());

    @Test
    public void equals() {
        FindDonationsCommand findDonationsFirstCommand = new FindDonationsCommand(INDEX_FIRST_PERSON);
        FindDonationsCommand findDonationsSecondCommand = new FindDonationsCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(findDonationsFirstCommand.equals(findDonationsFirstCommand));

        // same values -> returns true
        FindDonationsCommand findDonationsFirstCommandCopy = new FindDonationsCommand(INDEX_FIRST_PERSON);
        assertTrue(findDonationsFirstCommand.equals(findDonationsFirstCommandCopy));

        // different types -> returns false
        assertFalse(findDonationsFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findDonationsFirstCommand.equals(null));

        //different index -> returns false
        assertFalse(findDonationsFirstCommand.equals(findDonationsSecondCommand));
    }

    @Test
    public void execute_validIndex_success() {
        String expectedMessage = String.format(MESSAGE_DONATIONS_LISTED_OVERVIEW, 2, "s", ALICE.getName());

        Person personToFindRecordsOf = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        DonorIsSamePersonPredicate predicate = new DonorIsSamePersonPredicate(personToFindRecordsOf);

        FindDonationsCommand findDonationsCommand = new FindDonationsCommand(INDEX_FIRST_PERSON);

        expectedModel.updateFilteredDonationRecordList(predicate);
        assertCommandSuccess(findDonationsCommand, model, expectedMessage, expectedModel);
        assertEquals(ALICE_DONATION_RECORDS, model.getFilteredDonationRecordList());
    }

    @Test
    public void execute_invalidIndex_success() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        FindDonationsCommand findDonationsCommand = new FindDonationsCommand(outOfBoundIndex);

        assertCommandFailure(findDonationsCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }
}
