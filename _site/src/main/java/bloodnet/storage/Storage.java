package bloodnet.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import bloodnet.commons.exceptions.DataLoadingException;
import bloodnet.model.ReadOnlyBloodNet;
import bloodnet.model.ReadOnlyUserPrefs;
import bloodnet.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends BloodNetStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataLoadingException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getBloodNetFilePath();

    @Override
    Optional<ReadOnlyBloodNet> readBloodNet() throws DataLoadingException;

    @Override
    void saveBloodNet(ReadOnlyBloodNet bloodNet) throws IOException;

}
