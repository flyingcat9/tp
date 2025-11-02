package bloodnet.logic.commands;

import static bloodnet.logic.commands.CommandTestUtil.DESC_AMY;
import static bloodnet.logic.commands.CommandTestUtil.DESC_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_BLOOD_TYPE_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_DATE_OF_BIRTH_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static bloodnet.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bloodnet.logic.commands.EditCommand.EditPersonDescriptor;
import bloodnet.testutil.EditPersonDescriptorBuilder;

public class EditPersonDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditPersonDescriptor descriptorWithSameValues = new EditPersonDescriptor(DESC_AMY);
        assertTrue(DESC_AMY.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_AMY.equals(DESC_AMY));

        // null -> returns false
        assertFalse(DESC_AMY.equals(null));

        // different types -> returns false
        assertFalse(DESC_AMY.equals(5));

        // different values -> returns false
        assertFalse(DESC_AMY.equals(DESC_BOB));

        // different name -> returns false
        EditPersonDescriptor editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withName(VALID_NAME_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different phone -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withPhone(VALID_PHONE_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different email -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different blood type -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withBloodType(VALID_BLOOD_TYPE_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different date of birth -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withDateOfBirth(VALID_DATE_OF_BIRTH_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

    }

    @Test
    public void toStringMethod() {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        String expected = EditPersonDescriptor.class.getCanonicalName() + "{name="
                + editPersonDescriptor.getName().orElse(null) + ", phone="
                + editPersonDescriptor.getPhone().orElse(null) + ", email="
                + editPersonDescriptor.getEmail().orElse(null) + ", bloodType="
                + editPersonDescriptor.getBloodType().orElse(null) + ", dateOfBirth="
                + editPersonDescriptor.getDateOfBirth().orElse(null) + "}";
        assertEquals(expected, editPersonDescriptor.toString());
    }
}
