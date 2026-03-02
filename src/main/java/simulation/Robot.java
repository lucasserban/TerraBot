package simulation;

import simulation.entities.Entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;


public class Robot {
    private Pair position;
    private int energy;
    private int maxEnergy;
    private List<Entity> inventory;
    private Map<String, ArrayList<String>> factList;
    private List<String> subjectsType;

    public Robot() {
        this.position = new Pair();
        this.energy = 0;
        this.inventory = new ArrayList<>();
        factList = new LinkedHashMap<>();
        subjectsType = new ArrayList<>();
    }

    /**
     *
     * @return the position of the robot
     */
    public Pair getPosition() {
        return position;
    }

    /**
     * sets the position of the robot by param
     * @param position
     */
    public void setPosition(final Pair position) {
        this.position = position;
    }

    /**
     *
     * @return the energy of the robot
     */
    public int getEnergy() {
        return energy;
    }

    /**
     * sets the energy of the robot
     * @param energy
     */
    public void setEnergy(final int energy) {
        this.energy = energy;
    }

    /**
     *
     * @param maxEnergy
     */
    public void setMaxEnergy(final int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    /**
     * sets the robot by another's robot params
     * @param robot
     */
    public void setRobot(final Robot robot) {
        this.position = robot.position;
        this.energy = robot.energy;
    }

    /**
     * checks if the robot can use an amount of energy
     * @param energyToUse
     * @return
     */
    public boolean canUseEnergy(final int energyToUse) {
        if (energy >= energyToUse) {
            return true;
        }
        return false;
    }

    /**
     * uses an amount of energy
     * @param energyToUse the amount of energy used
     */
    public void useEnergy(final int energyToUse) {
        this.energy -= energyToUse;
    }

    /**
     *
     * @return the size of the robot's inventory
     */
    public int getSizeOfInventory() {
        return this.inventory.size();
    }

    /**
     *
     * @param i the index of the entity we want in inventory
     * @return the entity at index i
     */
    public Entity getEntityFromInventory(final int i) {
        return this.inventory.get(i);
    }

    /**
     * removes an entity from the inventory
     * @param entity to be removed
     */
    public void removeEntityFromInventory(final Entity entity) {
        this.inventory.remove(entity);
    }

    /**
     * adds an entity to the inventory
     * @param entity to be added
     */
    public void addEntityInInventory(final Entity entity) {
        this.inventory.add(entity);
    }

    /**
     * Adds new fact to the factList
     * @param subject the subject of the fact
     * @param fact the fact
     * @param type the type of the entity (subject)
     */
    public void addNewFact(final String subject, final String fact, final String type) {
        if (this.factList.containsKey(subject)) {
            this.factList.get(subject).add(fact);
        } else {
            this.factList.put(subject, new ArrayList<>());
            this.factList.get(subject).add(fact);
            this.subjectsType.add(type);
        }
    }

    /**
     *
     * @return the factList
     */
    public Map<String, ArrayList<String>> getFactList() {
        return factList;
    }

    /**
     * checks if there is a name in the factList
     * @param subject the name to be checked
     * @return ture if found, false if not
     */
    public boolean containsName(final String subject) {
        return this.factList.containsKey(subject);
    }

    /**
     * checks if there is a subject in the inventory
     * @param subject to be checked
     * @return true if is found, false if not
     */
    public boolean containsItem(final String subject) {
        Iterator<Entity> it = this.inventory.iterator();
        while (it.hasNext()) {
            Entity entity = it.next();
            if (entity.getName().equals(subject)) {
                return true;
            }
        }
        return false;
    }

    /**
     * checks if there is a given entity in the inventory
     * @param entity to be checked
     * @return true if found, false if not
     */
    public boolean containsEntity(final Entity entity) {
        Iterator<Entity> it = this.inventory.iterator();
        while (it.hasNext()) {
            Entity entityInventory = it.next();
            if (entityInventory == entity) {
                return true;
            }
        }
        return false;
    }


}
