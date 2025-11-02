package bloodnet.model.donationrecord;

import static bloodnet.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class BloodVolumeTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new BloodVolume(null));
    }

    @Test
    public void constructor_invalidBloodVolume_throwsIllegalArgumentException() {
        String invalidBloodVolume = "";
        assertThrows(IllegalArgumentException.class, () -> new BloodVolume(invalidBloodVolume));
    }

    @Test
    public void isValidBloodVolume() {
        // null bloodVolume
        assertThrows(NullPointerException.class, () -> BloodVolume.isValidBloodVolume(null));

        // blank bloodVolume
        assertFalse(BloodVolume.isValidBloodVolume("")); // empty string
        assertFalse(BloodVolume.isValidBloodVolume(" ")); // spaces only

        // invalid volumes (not integers)
        assertFalse(BloodVolume.isValidBloodVolume("400.0")); // not integer
        assertFalse(BloodVolume.isValidBloodVolume("qddewr2")); // letters
        assertFalse(BloodVolume.isValidBloodVolume("-1")); // negative number
        assertFalse(BloodVolume.isValidBloodVolume("500")); // invalid according to HSA guidelines


        // valid volumes
        assertTrue(BloodVolume.isValidBloodVolume("400"));
        assertTrue(BloodVolume.isValidBloodVolume("499"));
    }

    @Test
    public void equals() {
        BloodVolume bloodVolume = new BloodVolume("420");

        // same values -> returns true
        assertTrue(bloodVolume.equals(new BloodVolume("420")));

        // same object -> returns true
        assertTrue(bloodVolume.equals(bloodVolume));

        // null -> returns false
        assertFalse(bloodVolume.equals(null));

        // different types -> returns false
        assertFalse(bloodVolume.equals(5.0f));

        // different values -> returns false
        assertFalse(bloodVolume.equals(new BloodVolume("430")));
    }
}
