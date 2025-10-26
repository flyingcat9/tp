package bloodnet.logic.commands;

/**
 * A class that stores the message usage components for each command.
 */
public class CommandInformation {
    private final String description;
    private final String parameters;
    private final String example;

    /**
     * Creates a CommandInformation with the different components.
     * @param description Description of the command.
     * @param parameters Parameters for the command.
     * @param example Example using the command.
     */
    public CommandInformation(String description, String parameters, String example) {
        this.description = description;
        this.parameters = parameters;
        this.example = example;
    }

    public String getDescription() {
        return this.description;
    }

    public String getParameters() {
        return this.parameters;
    }

    public String getExample() {
        return this.example;
    }
}

