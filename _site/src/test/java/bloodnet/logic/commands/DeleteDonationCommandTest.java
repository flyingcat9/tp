package bloodnet.logic.commands;

import static bloodnet.logic.commands.CommandTestUtil.assertCommandFailure;
import static bloodnet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static bloodnet.logic.commands.CommandTestUtil.showDonationRecordAtIndex;
import static bloodnet.testutil.TypicalDonationRecords.getTypicalBloodNet;
import static bloodnet.testutil.TypicalIndexes.INDEX_FIRST_DONATION;
import static bloodnet.testutil.TypicalIndexes.INDEX_SECOND_DONATION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bloodnet.commons.core.index.Index;
import bloodnet.logic.Messages;
import bloodnet.logic.commands.commandsessions.CommandSession;
import bloodnet.logic.commands.commandsessions.ConfirmationCommandSession;
import bloodnet.logic.commands.exceptions.CommandException;
import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.UserPrefs;
import bloodnet.model.donationrecord.DonationRecord;
import bloodnet.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteDonationCommand}.
 */
public class DeleteDonationCommandTest {

    private Model model = new ModelManager(getTypicalBloodNet(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        DonationRecord donationToDelete = model.getFilteredDonationRecordList()
                .get(INDEX_FIRST_DONATION.getZeroBased());
        DeleteDonationCommand deleteDonationCommand = new DeleteDonationCommand(INDEX_FIRST_DONATION);

        Person relatedPerson = model.getFilteredPersonList().stream()
                .filter(p -> p.getId().equals(donationToDelete.getPersonId()))
                .findFirst()
                .orElseThrow();

        String expectedMessage = String.format(DeleteDonationCommand.MESSAGE_DELETE_DONATION_SUCCESS,
                Messages.format(donationToDelete, relatedPerson));

        ModelManager expectedModel = new ModelManager(model.getBloodNet(), new UserPrefs());
        expectedModel.deleteDonationRecord(donationToDelete);

        assertCommandSuccess(deleteDonationCommand, model, expectedMessage, expectedModel);
    }


    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredDonationRecordList().size() + 1);
        DeleteDonationCommand deleteDonationCommand = new DeleteDonationCommand(outOfBoundIndex);

        assertCommandFailure(deleteDonationCommand, model, Messages.MESSAGE_INVALID_DONATION_DISPLAYED_INDEX);
    }


    @Test
    public void execute_validIndexFilteredList_success() {
        showDonationRecordAtIndex(model, INDEX_FIRST_DONATION);

        DonationRecord donationToDelete = model.getFilteredDonationRecordList()
                .get(INDEX_FIRST_DONATION.getZeroBased());
        DeleteDonationCommand deleteDonationCommand = new DeleteDonationCommand(INDEX_FIRST_DONATION);

        Person relatedPerson = model.getFilteredPersonList().stream()
                .filter(p -> p.getId().equals(donationToDelete.getPersonId()))
                .findFirst()
                .orElseThrow();

        String expectedMessage = String.format(DeleteDonationCommand.MESSAGE_DELETE_DONATION_SUCCESS,
                Messages.format(donationToDelete, relatedPerson));

        Model expectedModel = new ModelManager(model.getBloodNet(), new UserPrefs());
        expectedModel.deleteDonationRecord(donationToDelete);
        showNoDonationRecord(expectedModel);

        assertCommandSuccess(deleteDonationCommand, model, expectedMessage, expectedModel);
    }


    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showDonationRecordAtIndex(model, INDEX_FIRST_DONATION);

        Index outOfBoundIndex = INDEX_SECOND_DONATION;
        // ensures that outOfBoundIndex is still in bounds of bloodnet list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getBloodNet().getDonationRecordList().size());

        DeleteDonationCommand deleteDonationCommand = new DeleteDonationCommand(outOfBoundIndex);

        assertCommandFailure(deleteDonationCommand, model, Messages.MESSAGE_INVALID_DONATION_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteDonationCommand deleteFirstCommand = new DeleteDonationCommand(INDEX_FIRST_DONATION);
        DeleteDonationCommand deleteSecondCommand = new DeleteDonationCommand(INDEX_SECOND_DONATION);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteDonationCommand deleteFirstCommandCopy = new DeleteDonationCommand(INDEX_FIRST_DONATION);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different donation record -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteDonationCommand deleteDonationCommand = new DeleteDonationCommand(targetIndex);
        String expected = DeleteDonationCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, deleteDonationCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoDonationRecord(Model model) {
        model.updateFilteredDonationRecordList(p -> false);

        assertTrue(model.getFilteredDonationRecordList().isEmpty());
    }

    @Test
    public void createSession_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> (
                new DeleteDonationCommand(Index.fromZeroBased(0))).createSession(null));
    }

    @Test
    public void createSession_validModel_returnsConfirmationCommandSession() throws CommandException {
        Model model = new ModelManager(getTypicalBloodNet(), new UserPrefs());
        CommandSession session = (new DeleteDonationCommand(Index.fromZeroBased(0))).createSession(model);

        assertTrue(session instanceof ConfirmationCommandSession);
        assertFalse(session.isDone());
    }

}
