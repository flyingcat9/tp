package bloodnet.logic.commands;

import static bloodnet.logic.Messages.MESSAGE_PEOPLE_LISTED_OVERVIEW;
import static bloodnet.logic.commands.CommandTestUtil.assertCommandSuccess;
import static bloodnet.testutil.Assert.assertThrows;
import static bloodnet.testutil.TypicalPersons.ALICE;
import static bloodnet.testutil.TypicalPersons.CARL;
import static bloodnet.testutil.TypicalPersons.getTypicalBloodNet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import bloodnet.model.Model;
import bloodnet.model.ModelManager;
import bloodnet.model.UserPrefs;
import bloodnet.model.person.HasBloodTypeAndIsEligibleToDonatePredicate;
import bloodnet.model.person.HasBloodTypePredicate;
import bloodnet.model.person.IsEligibleToDonatePredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindEligibleCommand}.
 */
public class FindEligibleCommandTest {
    private final Model model = new ModelManager(getTypicalBloodNet(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalBloodNet(), new UserPrefs());

    @Test
    public void equals() {
        List<String> listOfBloodTypes = Arrays.asList("B+", "O+", "A+");
        List<String> secondListOfBloodTypes = Arrays.asList("AB+", "A+");

        FindEligibleCommand findEligibleFirstCommand = new FindEligibleCommand(listOfBloodTypes);
        FindEligibleCommand findEligibleFirstCommandCopy = new FindEligibleCommand(listOfBloodTypes);
        FindEligibleCommand findEligibleSecondCommand = new FindEligibleCommand(secondListOfBloodTypes);

        // same object -> returns true
        assertTrue(findEligibleFirstCommand.equals(findEligibleFirstCommand));

        // different types -> returns false
        assertFalse(findEligibleFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findEligibleFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findEligibleFirstCommand.equals(findEligibleSecondCommand));

        // same values and different object -> returns false
        assertTrue(findEligibleFirstCommand.equals(findEligibleFirstCommandCopy));
    }

    @Test
    public void toStringMethod() {
        List<String> listOfBloodTypes = Arrays.asList("B+", "O+", "A+");
        FindEligibleCommand findEligibleCommand = new FindEligibleCommand(listOfBloodTypes);

        String expected = FindEligibleCommand.class.getCanonicalName() + "{bloodTypes=" + listOfBloodTypes + "}";
        assertEquals(expected, findEligibleCommand.toString());
    }


    @Test
    public void execute_nullModelExecutes_throwsNullPointerException() {
        List<String> listOfBloodTypes = Arrays.asList("B+", "O+", "A+");
        FindEligibleCommand findEligibleCommand = new FindEligibleCommand(listOfBloodTypes);
        assertThrows(NullPointerException.class, () -> findEligibleCommand.execute(null));
    }


    @Test
    public void execute_zeroMatch_executesSuccessfully() {
        String expectedMessage = String.format(MESSAGE_PEOPLE_LISTED_OVERVIEW, 0, "");
        List<String> listOfBloodTypes = Arrays.asList("AB-");
        FindEligibleCommand findEligibleCommand = new FindEligibleCommand(listOfBloodTypes);

        HasBloodTypeAndIsEligibleToDonatePredicate predicate =
                new HasBloodTypeAndIsEligibleToDonatePredicate(
                        new HasBloodTypePredicate(listOfBloodTypes),
                        new IsEligibleToDonatePredicate(model));

        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(findEligibleCommand, model, expectedMessage, expectedModel);

        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleMatches_executesSuccessfully() {
        String expectedMessage = String.format(MESSAGE_PEOPLE_LISTED_OVERVIEW, 2, "s");
        List<String> listOfBloodTypes = Arrays.asList("B+", "A+");
        FindEligibleCommand findEligibleCommand = new FindEligibleCommand(listOfBloodTypes);

        HasBloodTypeAndIsEligibleToDonatePredicate predicate =
                new HasBloodTypeAndIsEligibleToDonatePredicate(
                        new HasBloodTypePredicate(listOfBloodTypes),
                        new IsEligibleToDonatePredicate(model));

        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(findEligibleCommand, model, expectedMessage, expectedModel);

        assertEquals(Arrays.asList(ALICE, CARL), model.getFilteredPersonList());
    }



}

