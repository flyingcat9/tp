package bloodnet.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import bloodnet.commons.exceptions.DataLoadingException;
import bloodnet.model.BloodNet;
import bloodnet.model.ReadOnlyBloodNet;

/**
 * Represents a storage for {@link BloodNet}.
 */
public interface BloodNetStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getBloodNetFilePath();

    /**
     * Returns BloodNet data as a {@link ReadOnlyBloodNet}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataLoadingException if loading the data from storage failed.
     */
    Optional<ReadOnlyBloodNet> readBloodNet() throws DataLoadingException;

    /**
     * @see #getBloodNetFilePath()
     */
    Optional<ReadOnlyBloodNet> readBloodNet(Path filePath) throws DataLoadingException;

    /**
     * Saves the given {@link ReadOnlyBloodNet} to the storage.
     *
     * @param bloodNet cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveBloodNet(ReadOnlyBloodNet bloodNet) throws IOException;

    /**
     * @see #saveBloodNet(ReadOnlyBloodNet)
     */
    void saveBloodNet(ReadOnlyBloodNet bloodNet, Path filePath) throws IOException;

}
