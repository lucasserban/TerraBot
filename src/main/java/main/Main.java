package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import fileio.InputLoader;
import fileio.SimulationInput;
import simulation.Simulation;
import simulation.Territory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {

    private Main() {
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final ObjectWriter WRITER = MAPPER.writer().withDefaultPrettyPrinter();

    /**
     * @param inputPath input file path
     * @param outputPath output file path
     * @throws IOException when files cannot be loaded.
     */
    public static void action(final String inputPath,
                              final String outputPath) throws IOException {

        InputLoader inputLoader = new InputLoader(inputPath);
        ArrayNode output = MAPPER.createArrayNode();

        /*
         * TODO Implement your function here
         *
         * How to add output to the output array?
         * There are multiple ways to do this, here is one example:
         *
         *
         * ObjectNode objectNode = MAPPER.createObjectNode();
         * objectNode.put("field_name", "field_value");
         *
         * ArrayNode arrayNode = MAPPER.createArrayNode();
         * arrayNode.add(objectNode);
         *
         * output.add(arrayNode);
         * output.add(objectNode);
         *
         */

        ArrayList<CommandInput> commandList = inputLoader.getCommands();
        SimulationInput simDetails = inputLoader.getSimulations().getFirst();
        Simulation simulation = new Simulation();

        //initializing the map, robot
        Territory map = new Territory(simDetails);

        for (CommandInput command : commandList) {
            int timestampDiff = command.getTimestamp() - simulation.getTimestamp();
            if (timestampDiff > 0) {
                for (int i = 0; i < timestampDiff - 1; i++) {
                    simulation.interactionsBeetweenEntities();
                }
            }
            if (simulation.getChargingLeftTimestamps() > 0) {
                simulation.setChargingLeftTimestamps(simulation.getChargingLeftTimestamps() - timestampDiff);
                if (simulation.getChargingLeftTimestamps() > 0) {
                    if (simulation.getIsCharging()) {
                        ObjectNode out = MAPPER.createObjectNode();
                        out.put("command", command.getCommand());
                        out.put("message", "ERROR: Robot still charging. Cannot perform action");
                        out.put("timestamp", command.getTimestamp());
                        output.add(out);
                        continue;
                    }
                } else {
                    if (simulation.getIsCharging()) {
                        simulation.setIsCharging(false);
                    } else if (simulation.getIsChangeWeather()) {
                        simulation.processingAir(simulation.getChangeWeatherType(), null, false);
                        simulation.setIsChangeWeather(false);
                    }
                }
            }
            if (command.getCommand().equals("startSimulation")) {
                output.add(simulation.startSimulation(command, inputLoader));
            } else if (command.getCommand().equals("endSimulation")) {
                output.add(simulation.endSimulation(command));
            } else if (command.getCommand().equals("printEnvConditions")) {
                output.add(simulation.printEnvConditions(command));
            } else if (command.getCommand().equals("printMap")) {
                output.add(simulation.printMap(command));
            } else if (command.getCommand().equals("moveRobot")) {
                output.add(simulation.moveRobot(command));
            } else if (command.getCommand().equals("getEnergyStatus")) {
                output.add(simulation.getEnergyStatus(command));
            } else if (command.getCommand().equals("rechargeBattery")) {
                output.add(simulation.rechargeBattery(command));
            } else if (command.getCommand().equals("changeWeatherConditions")) {
                output.add(simulation.changeWeatherConditions(command));
            } else if (command.getCommand().equals("scanObject")) {
                output.add(simulation.scanObject(command));
            } else if (command.getCommand().equals("learnFact")) {
                output.add(simulation.learnFact(command));
            } else if (command.getCommand().equals("printKnowledgeBase")) {
                output.add(simulation.printKnowledgeBase(command));
            } else if (command.getCommand().equals("improveEnvironment")) {
                output.add(simulation.improveEnviroment(command));
            }
            simulation.interactionsBeetweenEntities();
        }

        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs();
        WRITER.writeValue(outputFile, output);
    }
}
