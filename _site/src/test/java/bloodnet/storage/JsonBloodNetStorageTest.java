package bloodnet.storage;

import static bloodnet.testutil.Assert.assertThrows;
import static bloodnet.testutil.TypicalPersons.ALICE;
import static bloodnet.testutil.TypicalPersons.HOON;
import static bloodnet.testutil.TypicalPersons.IDA;
import static bloodnet.testutil.TypicalPersons.getTypicalBloodNet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import bloodnet.commons.exceptions.DataLoadingException;
import bloodnet.model.BloodNet;
import bloodnet.model.ReadOnlyBloodNet;

public class JsonBloodNetStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonBloodNetStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readBloodNet_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readBloodNet(null));
    }

    private java.util.Optional<ReadOnlyBloodNet> readBloodNet(String filePath) throws Exception {
        return new JsonBloodNetStorage(Paths.get(filePath)).readBloodNet(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readBloodNet("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> readBloodNet("notJsonFormatBloodNet.json"));
    }

    @Test
    public void readBloodNet_invalidPersonBloodNet_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readBloodNet("invalidPersonBloodNet.json"));
    }

    @Test
    public void readBloodNet_invalidAndValidPersonBloodNet_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readBloodNet("invalidAndValidPersonBloodNet.json"));
    }

    @Test
    public void readAndSaveBloodNet_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempBloodNet.json");
        BloodNet original = getTypicalBloodNet();
        JsonBloodNetStorage jsonBloodNetStorage = new JsonBloodNetStorage(filePath);

        // Save in new file and read back
        jsonBloodNetStorage.saveBloodNet(original, filePath);
        ReadOnlyBloodNet readBack = jsonBloodNetStorage.readBloodNet(filePath).get();
        assertEquals(original, new BloodNet(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addPerson(HOON);
        original.removePerson(ALICE);
        jsonBloodNetStorage.saveBloodNet(original, filePath);
        readBack = jsonBloodNetStorage.readBloodNet(filePath).get();
        assertEquals(original, new BloodNet(readBack));

        // Save and read without specifying file path
        original.addPerson(IDA);
        jsonBloodNetStorage.saveBloodNet(original); // file path not specified
        readBack = jsonBloodNetStorage.readBloodNet().get(); // file path not specified
        assertEquals(original, new BloodNet(readBack));

    }

    @Test
    public void saveBloodNet_nullBloodNet_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveBloodNet(null, "SomeFile.json"));
    }

    /**
     * Saves {@code bloodNet} at the specified {@code filePath}.
     */
    private void saveBloodNet(ReadOnlyBloodNet bloodNet, String filePath) {
        try {
            new JsonBloodNetStorage(Paths.get(filePath))
                    .saveBloodNet(bloodNet, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveBloodNet_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveBloodNet(new BloodNet(), null));
    }
}
