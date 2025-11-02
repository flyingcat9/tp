package bloodnet.storage;

import static bloodnet.testutil.TypicalPersons.getTypicalBloodNet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import bloodnet.commons.core.GuiSettings;
import bloodnet.model.BloodNet;
import bloodnet.model.ReadOnlyBloodNet;
import bloodnet.model.UserPrefs;

public class StorageManagerTest {

    @TempDir
    public Path testFolder;

    private StorageManager storageManager;

    @BeforeEach
    public void setUp() {
        JsonBloodNetStorage bloodNetStorage = new JsonBloodNetStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(bloodNetStorage, userPrefsStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void bloodNetReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonBloodNetStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonBloodNetStorageTest} class.
         */
        BloodNet original = getTypicalBloodNet();
        storageManager.saveBloodNet(original);
        ReadOnlyBloodNet retrieved = storageManager.readBloodNet().get();
        assertEquals(original, new BloodNet(retrieved));
    }

    @Test
    public void getBloodNetFilePath() {
        assertNotNull(storageManager.getBloodNetFilePath());
    }

}
