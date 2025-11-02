package bloodnet.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import bloodnet.commons.core.LogsCenter;
import bloodnet.commons.exceptions.DataLoadingException;
import bloodnet.model.ReadOnlyBloodNet;
import bloodnet.model.ReadOnlyUserPrefs;
import bloodnet.model.UserPrefs;

/**
 * Manages storage of BloodNet data in local storage.
 */
public class StorageManager implements Storage {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";
    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private BloodNetStorage bloodNetStorage;
    private UserPrefsStorage userPrefsStorage;


    /**
     * Creates a {@code StorageManager} with the given {@code BloodNetStorage} and {@code UserPrefStorage}.
     */
    public StorageManager(BloodNetStorage bloodNetStorage, UserPrefsStorage userPrefsStorage) {
        this.bloodNetStorage = bloodNetStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ BloodNet methods ==============================

    @Override
    public Path getBloodNetFilePath() {
        return bloodNetStorage.getBloodNetFilePath();
    }

    @Override
    public Optional<ReadOnlyBloodNet> readBloodNet() throws DataLoadingException {
        return readBloodNet(bloodNetStorage.getBloodNetFilePath());
    }

    @Override
    public Optional<ReadOnlyBloodNet> readBloodNet(Path filePath) throws DataLoadingException {
        logger.fine("Attempting to read data from file: " + filePath);
        return bloodNetStorage.readBloodNet(filePath);
    }

    @Override
    public void saveBloodNet(ReadOnlyBloodNet bloodNet) throws IOException {
        saveBloodNet(bloodNet, bloodNetStorage.getBloodNetFilePath());
    }

    @Override
    public void saveBloodNet(ReadOnlyBloodNet bloodNet, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        bloodNetStorage.saveBloodNet(bloodNet, filePath);
    }

}
