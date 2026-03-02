package simulation.entities;

import com.fasterxml.jackson.databind.node.ObjectNode;
import simulation.Pair;
import simulation.Robot;
import simulation.Simulation;
import simulation.Territory;
import simulation.entities.airs.Air;
import simulation.entities.animals.Animal;
import simulation.entities.plants.Plant;
import simulation.entities.soils.Soil;
import simulation.entities.waters.Water;

public abstract class Entity {
    private static final double NORMALIZE_VALUE = 1000.0;
    protected String name;
    protected double mass;
    protected Pair position;

    public Entity() {
        this.name = "";
        this.mass = 0;
        this.position = new Pair();
    }

    public Entity(final String name, final double mass, final Pair position) {
        this.name = name;
        this.mass = mass;
        this.position = new Pair(position);
    }

    /**
     *
     * @return the name of the entity
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the mass of the entity
     */
    public double getMass() {
        return mass;
    }

    /**
     * The method it s passed to the entitie's sublclasses
     * @return the type of the entity
     */
    public abstract String getType();

    /**
     *
     * @return the coordinates of the entity
     */
    public Pair getPosition() {
        return position;
    }

    /**
     * sets the position of the entity
     * @param position the position we want to set the entity
     */
    public void setPosition(final Pair position) {
        this.position = position;
    }

    /**
     * sets the mass of the entity
     * @param mass the mass we want to set the entity
     */
    public void setMass(final double mass) {
        this.mass = mass;
    }

    /**
     * This method is overwritten in its subclasses
     * @param out
     * @param simulation
     * @return the output node with the details of the entity
     */
    public ObjectNode printDetails(final ObjectNode out, final Simulation simulation) {
        out.put("type", getType());
        out.put("name", getName());
        out.put("mass", getMass());
        return out;
    }

    /**
     *
     * @param value we want to normalize
     * @return the normalized value
     */
    public static double normalizeValue(final double value) {
        double normalizeScore = Math.max(0, Math.min(NORMALIZE_VALUE, value));
        return Math.round(normalizeScore * NORMALIZE_VALUE) / NORMALIZE_VALUE;
    }

    /**
     * This method is making the interaction beetween entities
     * it is overrided by it s subclasses (plant, water, animal)
     * @param soil
     * @param water
     * @param air
     * @param plant
     * @param animal
     * @param map
     * @param pos
     * @param robot
     */
    public void interaction(final Soil soil, final Water water, final Air air,
                            final Plant plant, final Animal animal, final Territory map,
                            final Pair pos, final Robot robot) {
        return;
    }

}
