import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Elevator {
    private final int id;
    private int currentFloor;
    private int destinationFloor;
    private Direction direction;
    private final TreeSet<Integer> stops;

    public Elevator(int id, int currentFloor) {
        this.id = id;
        this.currentFloor = currentFloor;
        this.destinationFloor = currentFloor;
        this.direction = Direction.IDLE;

        stops = new TreeSet<>();
    }

    public Elevator(int id, int currentFloor, int destinationFloor){
        this.id = id;
        this.currentFloor = currentFloor;
        this.destinationFloor = destinationFloor;

        if (destinationFloor - currentFloor == 0){
            this.direction = Direction.IDLE;
        } else if (destinationFloor - currentFloor > 0){
            this.direction = Direction.UP;
        } else {
            this.direction = Direction.DOWN;
        }

        stops = new TreeSet<>();
    }

    @Override
    public String toString() {
        return "Elevator " + id +
                " is on the floor : " + currentFloor +
                " with destination floor: " + destinationFloor +
                " and direction: " + direction.toString() + "\n";
    }

    public void tryToDecreaseDistance(){
        if (Direction.UP.equals(direction) && stops.stream().filter(stop -> stop > currentFloor).collect(Collectors.toSet()).isEmpty() && !stops.isEmpty()){
            direction = Direction.DOWN;
        } else if (Direction.DOWN.equals(direction) && stops.stream().filter(stop -> stop < currentFloor).collect(Collectors.toSet()).isEmpty() && !stops.isEmpty()){
            direction = Direction.UP;
        }
    }
    public void addStop(int floor) {
        stops.add(floor);

        if(currentFloor < floor){
            destinationFloor = Collections.max(stops);
        } else if (currentFloor > floor) {
            destinationFloor = Collections.min(stops);
        }
    }

    public void move(){
        if(Direction.UP.equals(direction)){
            currentFloor++;
        } else if (Direction.DOWN.equals(direction)){
            currentFloor--;
        }
    }

    public boolean shouldElevatorStop(){
        if (stops.contains(currentFloor)){
            stops.remove(currentFloor);
            return true;
        }
        return false;
    }

    public void update(int currentFloor, int destinationFloor, Direction direction){
        if (!stops.isEmpty()){
            throw new RuntimeException("Elevator has stops, cannot be updated");
        }
        if (this.currentFloor != currentFloor){
            throw new RuntimeException("Elevator is not on the current floor");
        }
        this.destinationFloor = destinationFloor;
        this.direction = direction;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
