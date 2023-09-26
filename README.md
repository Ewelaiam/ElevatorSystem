# Elevator system

## Description
The elevator control system, which allows:
* pick up and drop off passengers
* update the status of the elevator (moving up, moving down, idle)
* make a simulation step by moving the elevators between floors
* get the status of the elevator system

## Simulation

The simulation allows you to perform one of the following steps:
* pickup - get the information about the floor where the passenger is waiting and in which direction he wants to go (UP/DOWN). Then find the best elevator to pickup the passenger. 
* update - update the position of the elevator (without any invocation from the passengers)
* step - make a single step of the system (move every elevator by one position)
* status - show the status of the system (information about all the elevators)
* quit - exit the simulation

## Algorithm

The algorithm for finding the best elevator is based on the following priorities:
* the elevator is idle and on the same floor as the passenger
* the elevator is idle on the other floor or is moving in the same direction as the passenger wants to go and is before the passenger floors
* the elevator passed the passenger and need to return to pick him up

After picking the best elevator the passenger's floor is added to the elevator queue and the elevator is updated.
When the elevator reaches the lowest or highest floor and still have passengers to pick up or drop off, the direction is changed.
Also when the elevator does not have more passengers to pick up or drop off in current direction, but has in the opposite direction, the direction will be changed.
When the elevator reaches the destination floor, it is set to idle.
When the passenger is picked up, the elevator system asks for the floor where the passenger wants to go and adds it to the elevator queue.

## How to run
Compile the project using the following command:
```javac <name of the class>.java```

Run the project using the following command:
```java <name of the class>```

## How to test
The tests are written using the Spock framework. They are located in the `src/test/groovy` directory.
