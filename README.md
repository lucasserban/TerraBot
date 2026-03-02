# Assignment 1 POO - TerraBot
### Serban Lucas Nicolae - 325CA
## Main logic:
Firstly, I parsed the input with the help of constructors and I made the hierarchy
of the entities, every entity has a constructor that has a parameter an input class.
For every command I made a method in the `Simulation` class and in main I iterate through
the input commands and for every command I call the method from the simulation class.
For the next part I will explain the usage of every class and the implementation of them.

### The hierarchy:
The hierarchy of the projects looks like this, the Entity class is the superclass(explained below) ,
then it comes the subclasses that are types of entities `plant`, `animal`, `air`, `soil` and `water` and this
subclasses also have subclasses. The methods like `getAirQuality`, `getType`, etc are overrided in every
subclass and have its own implementation.

## Entity:
The entity class is the superclass of all the types of entities in the project, it has all the
commune attributes like `mass`, `name`, `type`, `position` on the map, getters, setters and common methods
like `printDetails` that is overrided in its subclasses (it prints the details of an entity, helps for
printEnvCondition command) and the method `interaction` that is also overrided by it s subclasses.

## Simulation:
The simulation class represents the simulation itself, it has fields that keeps track of
the simulation timestamp, if the simulation already started/finish, how many timestamps are
left for the robot to charge, if the weather is changing in the current timestamp,
the number of the simulation if there are multiple simulations . I also have a Robot object
and a Territory object that represents the map of the simulation. In this class are the main methods that are called
when a command is executed, every method is explained in the source code.

## TerritorySquare:
This class represents a square of the simulation's map, it has a field for every entity that is possible to be in a
square (`plant`, `animal`, `air`, `soil`, `water`). It has methods that help me get track of how many entities are there on
the square, or to find the best neighbor square of a robot when the robot moves.


## Territory:
The Territory class represents the map of the simulation, it has a field that is a matrix of TerritorySquare objects
that is the variable that represents the map. Its constructor is where I parse the data from the input. It implements
methods that help me for example to find the best neighbor of an animal when it wants to move, or a method that checks
if there is a type of entity in the entire map.

## Robot:
The Robot class represents the robot of the simulation, it has field that help us keep track of the robot's `position`,
`energy`, `inventory` and the `factList`. Most of the methods are getters and setters and methods to add or remove entities/facts
from the inventory/factList.
