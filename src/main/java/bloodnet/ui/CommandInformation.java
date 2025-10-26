package bloodnet.ui;

public class CommandInformation {
    private final String description;
    private final String parameters;
    private final String example;

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

