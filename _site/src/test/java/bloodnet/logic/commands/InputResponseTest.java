package bloodnet.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class InputResponseTest {
    @Test
    public void equals() {
        InputResponse inputResponse = new InputResponse("feedback");

        // same values -> returns true
        assertTrue(inputResponse.equals(new InputResponse("feedback")));
        assertTrue(inputResponse.equals(new InputResponse("feedback", false, false)));

        // same object -> returns true
        assertTrue(inputResponse.equals(inputResponse));

        // null -> returns false
        assertFalse(inputResponse.equals(null));

        // different types -> returns false
        assertFalse(inputResponse.equals(0.5f));

        // different feedbackToUser value -> returns false
        assertFalse(inputResponse.equals(new InputResponse("different")));

        // different showHelp value -> returns false
        assertFalse(inputResponse.equals(new InputResponse("feedback", true, false)));

        // different exit value -> returns false
        assertFalse(inputResponse.equals(new InputResponse("feedback", false, true)));
    }

    @Test
    public void hashcode() {
        InputResponse inputResponse = new InputResponse("feedback");

        // same values -> returns same hashcode
        assertEquals(inputResponse.hashCode(), new InputResponse("feedback").hashCode());

        // different feedbackToUser value -> returns different hashcode
        assertNotEquals(inputResponse.hashCode(), new InputResponse("different").hashCode());

        // different showHelp value -> returns different hashcode
        assertNotEquals(inputResponse.hashCode(), new InputResponse("feedback", true, false).hashCode());

        // different exit value -> returns different hashcode
        assertNotEquals(inputResponse.hashCode(), new InputResponse("feedback", false, true).hashCode());
    }

    @Test
    public void toStringMethod() {
        InputResponse inputResponse = new InputResponse("feedback");
        String expected = InputResponse.class.getCanonicalName() + "{feedbackToUser="
                + inputResponse.getFeedbackToUser() + ", showHelp=" + inputResponse.isShowHelp()
                + ", exit=" + inputResponse.isExit() + "}";
        assertEquals(expected, inputResponse.toString());
    }
}
