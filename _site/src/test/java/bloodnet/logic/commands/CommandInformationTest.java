package bloodnet.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CommandInformationTest {

    @Test
    public void create_commandInformationObject_success() {
        String command = AddCommand.COMMAND_INFORMATION.getCommand();
        String description = AddCommand.COMMAND_INFORMATION.getDescription();
        String parameters = AddCommand.COMMAND_INFORMATION.getParameters();
        String examples = AddCommand.COMMAND_INFORMATION.getExample();
        CommandInformation commandInformation = new CommandInformation(command, description, parameters, examples);

        assertEquals(command, commandInformation.getCommand());
        assertEquals(description, commandInformation.getDescription());
        assertEquals(parameters, commandInformation.getParameters());
        assertEquals(examples, commandInformation.getExample());
    }
}
