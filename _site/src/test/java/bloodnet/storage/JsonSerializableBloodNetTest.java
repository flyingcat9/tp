package bloodnet.storage;

import static bloodnet.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import bloodnet.commons.exceptions.IllegalValueException;
import bloodnet.commons.util.JsonUtil;
import bloodnet.model.BloodNet;
import bloodnet.testutil.TypicalPersons;

public class JsonSerializableBloodNetTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableBloodNetTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalPersonsBloodNet.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidPersonBloodNet.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonBloodNet.json");

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializableBloodNet dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableBloodNet.class).get();
        BloodNet bloodNetFromFile = dataFromFile.toModelType();
        BloodNet typicalPersonsBloodNet = TypicalPersons.getTypicalBloodNet();
        assertEquals(bloodNetFromFile, typicalPersonsBloodNet);
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableBloodNet dataFromFile = JsonUtil.readJsonFile(INVALID_PERSON_FILE,
                JsonSerializableBloodNet.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicatePersons_throwsIllegalValueException() throws Exception {
        JsonSerializableBloodNet dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PERSON_FILE,
                JsonSerializableBloodNet.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableBloodNet.MESSAGE_DUPLICATE_PERSON,
                dataFromFile::toModelType);
    }

}
