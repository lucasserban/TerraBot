package simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import fileio.InputLoader;
import fileio.SimulationInput;
import simulation.entities.Entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/* the Simulation class helps us to have track of
the simulation status: the current timestamp
if the simulation started/finished */
public class Simulation {
    private static final int SCAN_ENERGY = 7;
    private static final int IMPROVE_ENERGY = 10;
    private static final double IMPROVE_GROW_1 = 0.3;
    private static final double IMPROVE_GROW_2 = 0.2;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private int timestamp;
    private boolean hasStarted;
    private boolean hasFinished;
    //how many timestamps we need to wait for charging
    private int chargingLeftTimestamps;
    private Robot robot;
    private Territory map;
    private boolean isCharging;
    private boolean isChangeWeather;
    private String changeWeatherType;
    private int numberOfSimulation = 0;

    public Simulation() {
        this.timestamp = 0;
        this.hasStarted = false;
        this.hasFinished = false;
        chargingLeftTimestamps = 0;
        this.isCharging = false;
        this.isChangeWeather = false;
    }

    /**
     *
     * @return the timestamp of the simulation
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     *
     * @return how many timestamps left of the charging robot
     */
    public int getChargingLeftTimestamps() {
        return chargingLeftTimestamps;
    }

    /**
     * sets the timestamps left for charging the robot
     * @param chargingLeftTimestamps
     */
    public void setChargingLeftTimestamps(final int chargingLeftTimestamps) {
        this.chargingLeftTimestamps = chargingLeftTimestamps;
    }

    /**
     *
     * @return if is Charged returns true, if not false
     */
    public boolean getIsCharging() {
        return isCharging;
    }

    /**
     * sets charging status
     * @param isCharging
     */
    public void setIsCharging(final boolean isCharging) {
        this.isCharging = isCharging;
    }

    /**
     *
     * @return if the weather is changing returns true, else false
     */
    public boolean getIsChangeWeather() {
        return isChangeWeather;
    }

    /**
     * sets the IsChangeWeather field
     * @param isChangeWeather
     */
    public void setIsChangeWeather(final boolean isChangeWeather) {
        this.isChangeWeather = isChangeWeather;
    }

    /**
     * sets the timestamp of the simulation
     * @param timestamp
     */
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     *
     * @return the type of the WeatherType
     */
    public String getChangeWeatherType() {
        return changeWeatherType;
    }

    /**
     * This method starts the simulation
     * @param command the command from the input
     * @param inputLoader
     * @return the output(if it started)
     */
    public ObjectNode startSimulation(final CommandInput command, final InputLoader inputLoader) {
        ObjectNode out = MAPPER.createObjectNode();
        out.put("command", command.getCommand());
        //if the simulation already started
        if (this.hasStarted) {
            out.put("message", "ERROR: Simulation already started. Cannot perform action");
        } else {
            SimulationInput simDetails = inputLoader.getSimulations().get(numberOfSimulation);
            map = new Territory(simDetails);
            //initializing HasStarted to true
            this.hasStarted = true;
            hasFinished = false;
            this.timestamp = command.getTimestamp();

            robot = new Robot();
            robot.setPosition(new Pair());
            robot.setEnergy(simDetails.getEnergyPoints());
            robot.setMaxEnergy(simDetails.getEnergyPoints());

            out.put("message", "Simulation has started.");
            numberOfSimulation++;
        }
        out.put("timestamp", command.getTimestamp());
        timestamp =  command.getTimestamp();
        return out;
    }

    /**
     * ends the simulation
     * @param command the command from the input
     * @return the output(if it finished)
     */
    public ObjectNode endSimulation(final CommandInput command) {
        ObjectNode out = MAPPER.createObjectNode();
        out.put("command", command.getCommand());
        if (!(this.hasStarted)) {
            out.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else {
            out.put("message", "Simulation has ended.");
            hasFinished = true;
            hasStarted = false;
        }
        out.put("timestamp", command.getTimestamp());
        timestamp =  command.getTimestamp();
        return out;
    }

    /**
     * prints the enviromental conditions of the map
     * @param command from the input
     * @return the output(the enviromental conditions)
     */
    public ObjectNode printEnvConditions(final CommandInput command) {
        ObjectNode out = MAPPER.createObjectNode();
        out.put("command", command.getCommand());
        if (!(this.hasStarted) || this.hasFinished) {
            out.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else {
            ObjectNode auxList = MAPPER.createObjectNode();

            Pair robotPos = robot.getPosition();
            int x = robotPos.getX();
            int y = robotPos.getY();

            TerritorySquare square = map.getInfo(x, y);

            if (square.getSoil() != null) {
                ObjectNode auxEntity = MAPPER.createObjectNode();
                Entity entity = square.getSoil();
                auxList.put("soil", entity.printDetails(auxEntity, this));
            }

            if (square.getPlant() != null) {
                ObjectNode auxEntity = MAPPER.createObjectNode();
                Entity entity = square.getPlant();
                auxList.put("plants", entity.printDetails(auxEntity, this));
            }

            if (square.getAnimal() != null) {
                ObjectNode auxEntity = MAPPER.createObjectNode();
                Entity entity = square.getAnimal();
                auxList.put("animals", entity.printDetails(auxEntity, this));
            }

            if (square.getWater() != null) {
                ObjectNode auxEntity = MAPPER.createObjectNode();
                Entity entity = square.getWater();
                auxList.put("water", entity.printDetails(auxEntity, this));
            }

            if (square.getAir() != null) {
                ObjectNode auxEntity = MAPPER.createObjectNode();
                Entity entity = square.getAir();
                auxList.put("air", entity.printDetails(auxEntity, this));
            }

            out.put("output", auxList);
        }
        out.put("timestamp", command.getTimestamp());
        timestamp =  command.getTimestamp();
        return out;
    }

    /**
     * prints the map, the number of entities in every square
     * @param command from the input
     * @return the output(the map)
     */
    public ObjectNode printMap(final CommandInput command) {
        ObjectNode out = MAPPER.createObjectNode();
        out.put("command", command.getCommand());

        if (!(hasStarted) || hasFinished) {
            out.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else {
            Pair mapDims = map.getDims();
            ArrayNode listOfSquares = MAPPER.createArrayNode();
            for (int y = 0; y < mapDims.getY(); y++) {
                for (int x = 0; x < mapDims.getX(); x++) {
                    ObjectNode squareDetails = MAPPER.createObjectNode();
                    ArrayNode section = MAPPER.createArrayNode();
                    section.add(x);
                    section.add(y);
                    squareDetails.put("section", section);
                    squareDetails.put("totalNrOfObjects", map.getInfo(x, y).getNumberOfEntities());
                    squareDetails.put("airQuality", map.getInfo(x, y).getAir().getAirQualityResult());
                    squareDetails.put("soilQuality", map.getInfo(x, y).getSoil().getSoilQualityResult());
                    listOfSquares.add(squareDetails);
                }
            }
            out.put("output", listOfSquares);
        }
        out.put("timestamp", command.getTimestamp());
        timestamp =  command.getTimestamp();
        return out;
    }

    /**
     * moves the robot base on the enviroment
     * @param command from the input
     * @return the output (the position the robot moved)
     */
    public ObjectNode moveRobot(final CommandInput command) {
        ObjectNode out = MAPPER.createObjectNode();
        out.put("command", command.getCommand());
        if (!hasStarted || hasFinished) {
            out.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else {
            Pair robotPos = robot.getPosition();
            int x = robotPos.getX();
            int y = robotPos.getY();
            if (map.getInfo(x, y).getBestNeighbor(robotPos, map, robot) == 0) {
                out.put("message", "ERROR: Not enough battery left. Cannot perform action");
            } else {
                int newX = robot.getPosition().getX();
                int newY = robot.getPosition().getY();
                out.put("message", "The robot has successfully moved to position ("
                        + newX + ", " + newY + ").");
            }
        }
        out.put("timestamp", command.getTimestamp());
        timestamp =  command.getTimestamp();
        return out;

    }

    /**
     * prints the energy status of the robot
     * @param command from the input
     * @return the output (the energy left)
     */
    public ObjectNode getEnergyStatus(final CommandInput command) {
        ObjectNode out = MAPPER.createObjectNode();
        out.put("command", command.getCommand());
        if (!(hasStarted) || hasFinished) {
            out.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else {
            out.put("message", "TerraBot has " + robot.getEnergy() + " energy points left.");
        }
        out.put("timestamp", command.getTimestamp());
        timestamp =  command.getTimestamp();
        return out;
    }

    /**
     * recharged the battery of the robot
     * @param command from the input
     * @return the output(if it started to recharge)
     */
    public ObjectNode rechargeBattery(final CommandInput command) {
        ObjectNode out = MAPPER.createObjectNode();
        out.put("command", command.getCommand());
        if (!(hasStarted) || hasFinished) {
            out.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else {
            isCharging = true;
            chargingLeftTimestamps = command.getTimeToCharge();
            robot.setEnergy(robot.getEnergy() + chargingLeftTimestamps);
            out.put("message", "Robot battery is charging.");
        }
        out.put("timestamp", command.getTimestamp());
        timestamp =  command.getTimestamp();
        return out;
    }

    /**
     * changes the weathe conditions of the simulation
     * @param command from the input
     * @return the output (if the weather changed or not)
     */
    public ObjectNode changeWeatherConditions(final CommandInput command) {
        ObjectNode out = MAPPER.createObjectNode();
        out.put("command", command.getCommand());
        if (!(hasStarted) || hasFinished) {
            out.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else {
            isChangeWeather = true;
            if (command.getType().equalsIgnoreCase("desertStorm")
                    && map.doesTypeOfEntityExist("DesertAir")) {
                processingAir("DesertAir", command, true);
                changeWeatherType = "DesertAir";
            } else if (command.getType().equalsIgnoreCase("peopleHiking")
                    && map.doesTypeOfEntityExist("MountainAir")) {
                processingAir("MountainAir", command, true);
                changeWeatherType = "MountainAir";
            } else if (command.getType().equalsIgnoreCase("newSeason")
                    &&  map.doesTypeOfEntityExist("TemperateAir")) {
                processingAir("TemperateAir", command, true);
                changeWeatherType = "TemperateAir";
            } else if (command.getType().equalsIgnoreCase("polarStorm")
                    && map.doesTypeOfEntityExist("PolarAir")) {
                processingAir("PolarAir", command, true);
                changeWeatherType = "PolarAir";
            } else if (command.getType().equalsIgnoreCase("rainFall")
                    && map.doesTypeOfEntityExist("TropicalAir")) {
                processingAir("TropicalAir", command, true);
                changeWeatherType = "TropicalAir";
            } else {
                out.put("message", "ERROR: The weather change does not"
                        + " affect the environment. Cannot perform action");
                out.put("timestamp", command.getTimestamp());
                timestamp =  command.getTimestamp();
                return out;
            }
            chargingLeftTimestamps = 2;
            out.put("message", "The weather has changed.");
        }
        out.put("timestamp", command.getTimestamp());
        timestamp =  command.getTimestamp();
        return out;

    }

    /**
     * It processes different typef of air, it's a helper method for
     * the changeWeatherConditions() method
     * @param type
     * @param command
     * @param isChanging
     */
    public void processingAir(final String type, final CommandInput command,
                              final boolean isChanging) {
        for (int i = 0; i < map.getDims().getX(); i++) {
            for (int j = 0; j < map.getDims().getY(); j++) {
                if (isChanging) {
                    if (map.getInfo(i, j).getAir().getType().equals(type)) {
                        map.getInfo(i, j).getAir().changeWeather(command);
                    }
                } else {
                    if (map.getInfo(i, j).getAir().getType().equals(type)) {
                        map.getInfo(i, j).getAir().calculateAirQuality();
                    }
                }
            }
        }
    }

    /**
     * It scans an object
     * @param command from the input
     * @return the output (what object was scanned / was the scan successful?)
     */
    public ObjectNode scanObject(final CommandInput command) {

        class Scan {
            private String color;
            private String smell;
            private String sound;

            Scan(final String color, final String smell, final String sound) {
                this.color = color;
                this.smell = smell;
                this.sound = sound;
            }

            String typeOfObject() {
                if (color.equals("none") && smell.equals("none") && sound.equals("none")) {
                    return "Water";
                } else if (sound.equals("none")) {
                    return "Plant";
                } else {
                    return "Animal";
                }
            }
        }

        ObjectNode out = MAPPER.createObjectNode();
        out.put("command", command.getCommand());
        if (!(hasStarted) || hasFinished) {
            out.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else {
            if (robot.canUseEnergy(SCAN_ENERGY)) {
                Scan scan = new Scan(command.getColor(), command.getSmell(), command.getSound());
                Pair position = robot.getPosition();
                int x = position.getX();
                int y = position.getY();
                if (scan.typeOfObject().equals("Plant") && map.getInfo(x, y).getPlant() != null) {
                    out.put("message", "The scanned object is a plant.");
                    robot.addEntityInInventory(map.getInfo(x, y).getPlant());
                    map.getInfo(x, y).getPlant().markAsScanned();
                    robot.useEnergy(SCAN_ENERGY);
                } else if (scan.typeOfObject().equals("Animal")
                        && map.getInfo(x, y).getAnimal() != null) {
                    out.put("message", "The scanned object is an animal.");
                    robot.addEntityInInventory(map.getInfo(x, y).getAnimal());
                    map.getInfo(x, y).getAnimal().markAsScanned();
                    robot.useEnergy(SCAN_ENERGY);
                } else if (scan.typeOfObject().equals("Water") && map.getInfo(x, y).getWater() != null) {
                    out.put("message", "The scanned object is water.");
                    robot.addEntityInInventory(map.getInfo(x, y).getWater());
                    map.getInfo(x, y).getWater().markAsScanned();
                    robot.useEnergy(SCAN_ENERGY);
                } else {
                    out.put("message", "ERROR: Object not found. Cannot perform action");
                }
            } else {
                out.put("message", "ERROR: Not enough energy to perform action");
            }
        }
        out.put("timestamp", command.getTimestamp());
        timestamp =  command.getTimestamp();
        return out;
    }

    /**
     * this method procceses the interaction between
     * the entities
     */
    public void interactionsBeetweenEntities() {
        if (robot == null) {
            return;
        }
        int length = robot.getSizeOfInventory();
        for (int i = 0; i < length; i++) {
            Entity currEntity = robot.getEntityFromInventory(i);
            Pair position = currEntity.getPosition();
            int x = position.getX();
            int y = position.getY();
            currEntity.interaction(map.getInfo(x, y).getSoil(), map.getInfo(x, y).getWater(),
                    map.getInfo(x, y).getAir(), map.getInfo(x, y).getPlant(),
                    map.getInfo(x, y).getAnimal(), map, position, robot);
        }
    }

    /**
     * learns a fact about scanned entities
     * @param command from the input
     * @return if it learned it successfully or if the entity is not scanned
     */
    public ObjectNode learnFact(final CommandInput command) {
        ObjectNode out = MAPPER.createObjectNode();
        out.put("command", command.getCommand());
        if (!(hasStarted) || hasFinished) {
            out.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else {
            if (robot.canUseEnergy(2)) {
                String subject = command.getComponents();
                boolean canSaveFact = false;

                for (int i = 0; i < robot.getSizeOfInventory(); i++) {
                    Entity currEntity = robot.getEntityFromInventory(i);
                    String currName = currEntity.getName();
                    if (currName.equals(subject)) {
                        canSaveFact = true;
                        robot.addNewFact(command.getComponents(), command.getSubject(),
                                currEntity.getType());
                        out.put("message", "The fact has been successfully saved in the database.");
                        break;
                    }
                }
                if (canSaveFact) {
                    robot.useEnergy(2);
                } else {
                    out.put("message", "ERROR: Subject not yet saved. Cannot perform action");
                }
            } else {
                out.put("message", "ERROR: Not enough battery left. Cannot perform action");
            }
        }
        out.put("timestamp", command.getTimestamp());
        timestamp =  command.getTimestamp();
        return out;

    }

    /**
     * prints the knowledge of the robot (the learned facts)
     * @param command from the input
     * @return the output
     */
    public ObjectNode printKnowledgeBase(final CommandInput command) {
        ObjectNode out = MAPPER.createObjectNode();
        out.put("command", command.getCommand());
        if (!(hasStarted) || hasFinished) {
            out.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else {
            Map<String, ArrayList<String>> factList = robot.getFactList();
            Iterator<String> iterator = factList.keySet().iterator();
            ArrayNode listOfFactsArray = MAPPER.createArrayNode();
            while (iterator.hasNext()) {
                ObjectNode subject = MAPPER.createObjectNode();
                String key = iterator.next();
                subject.put("topic", key);

                ArrayList<String> value = factList.get(key);
                Iterator<String> iterator2 = value.iterator();
                ArrayNode fact = MAPPER.createArrayNode();
                while (iterator2.hasNext()) {
                    String oneFact = iterator2.next();
                    fact.add(oneFact);
                }
                subject.put("facts", fact);
                listOfFactsArray.add(subject);
            }
            out.put("output", listOfFactsArray);
        }
        out.put("timestamp", command.getTimestamp());
        timestamp =  command.getTimestamp();
        return out;
    }

    /**
     * It improves the enviroment of the map
     * @param command from the input
     * @return the output (how was the enviroment improved)
     */
    public ObjectNode improveEnviroment(final CommandInput command) {
        ObjectNode out = MAPPER.createObjectNode();
        out.put("command", command.getCommand());
        if (!(hasStarted) || hasFinished) {
            out.put("message", "ERROR: Simulation not started. Cannot perform action");
        } else {
            if  (robot.canUseEnergy(IMPROVE_ENERGY)) {
                String improvementType = command.getImprovementType();
                String improvementComponent = command.getName();
                if (robot.containsName(improvementComponent)) {
                    Pair pos = robot.getPosition();
                    int x = pos.getX();
                    int y = pos.getY();
                    if (improvementType.equals("plantVegetation")) {
                        map.getInfo(x, y).getAir().growOxygenLevel(IMPROVE_GROW_1);
                        map.getInfo(x, y).getAir().calculateAirQuality();
                        out.put("message", "The " + improvementComponent
                                + " was planted successfully.");
                        robot.removeEntityFromInventory(map.getInfo(x, y).getPlant());
                    } else if (improvementType.equals("fertilizeSoil")) {
                        map.getInfo(x, y).getSoil().growOrganicMatter(IMPROVE_GROW_1);
                        map.getInfo(x, y).getSoil().calculateSoilQuality();
                        out.put("message", "The soil was successfully fertilized using "
                                + improvementComponent);
                    } else if (improvementType.equals("increaseHumidity")) {
                        map.getInfo(x, y).getAir().growHumidity(IMPROVE_GROW_2);
                        map.getInfo(x, y).getAir().calculateAirQuality();
                        out.put("message", "The humidity was successfully increased using "
                                + improvementComponent);
                    } else if (improvementType.equals("increaseMoisture")) {
                        map.getInfo(x, y).getSoil().growWaterRetention(IMPROVE_GROW_2);
                        map.getInfo(x, y).getSoil().calculateSoilQuality();
                        out.put("message", "The moisture was successfully increased using "
                                + improvementComponent);
                    }
                    robot.useEnergy(IMPROVE_ENERGY);
                } else if (!robot.containsItem(improvementComponent)) {
                    out.put("message", "ERROR: Subject not yet saved. Cannot perform action");
                } else {
                    out.put("message", "ERROR: Fact not yet saved. Cannot perform action");
                }
            } else {
                out.put("message", "ERROR: Not enough battery left. Cannot perform action");
            }
        }
        out.put("timestamp", command.getTimestamp());
        timestamp =  command.getTimestamp();
        return out;
    }
}
