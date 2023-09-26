import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ElevatorSystem {
    private final int numberOfFloors;
    private final List<Elevator> elevators;
    private final HashMap<Integer, Integer> waitingPeopleForUp;
    private final HashMap<Integer, Integer> waitingPeopleForDown;
    private static final Scanner scanner = new Scanner(System.in);

    public ElevatorSystem(int numberOfElevators, int numberOfFloors){
        this.numberOfFloors = numberOfFloors;

        elevators = new ArrayList<>();
        for(int i = 0; i < numberOfElevators; i++){
            elevators.add(new Elevator(i, i));
        }
        waitingPeopleForDown = new HashMap<>();
        waitingPeopleForUp = new HashMap<>();

    }

    private Elevator findIdleElevatorOnTheFloor(int floor){
        List<Elevator> idleElevatorsOnTheFloor = elevators.stream()
                .filter(elevator -> elevator.getCurrentFloor() == elevator.getDestinationFloor()
                        && elevator.getCurrentFloor() == floor)
                .toList();
        return !idleElevatorsOnTheFloor.isEmpty() ? idleElevatorsOnTheFloor.get(0) : null;
    }

    private void addWaitingPersonForUp(int floor){
        if(waitingPeopleForUp.containsKey(floor)){
            waitingPeopleForUp.put(floor, waitingPeopleForUp.get(floor) + 1);
        } else {
            waitingPeopleForUp.put(floor, 1);
        }
    }

    private void addWaitingPersonForDown(int floor){
        if(waitingPeopleForDown.containsKey(floor)){
            waitingPeopleForDown.put(floor, waitingPeopleForDown.get(floor) + 1);
        } else {
            waitingPeopleForDown.put(floor, 1);
        }
    }

    private Elevator findTheNearestElevator(List<Elevator> candidates, int floor, boolean isRightDirection){
        Elevator nearestElevator = null;
        int minDistance = Integer.MAX_VALUE;
        for (Elevator candidate : candidates){
            int distance = countDistance(candidate, floor, isRightDirection);
            if (distance < minDistance){
                minDistance = distance;
                nearestElevator = candidate;
            }
        }
        return nearestElevator;
    }

    private int countDistance(Elevator candidate, int floor, boolean isRightDirection){
        return isRightDirection
                ? Math.abs(candidate.getCurrentFloor() - floor)
                : Direction.UP.equals(candidate.getDirection())
                    ? (numberOfFloors - candidate.getCurrentFloor()) + (numberOfFloors - floor)
                    : candidate.getCurrentFloor() + floor;
    }

    private Elevator findIdleOrRightDirectionElevator(int floor, Direction direction){
        List<Elevator> rightDirectionElevators = elevators.stream()
                .filter( elevator -> {
                        Direction elevatorDirection = elevator.getDirection();
                        boolean isRightDirection = (Direction.UP.equals(direction) && elevator.getCurrentFloor() < floor)
                                || (Direction.DOWN.equals(direction) && elevator.getCurrentFloor() > floor);
                        return elevatorDirection.equals(Direction.IDLE) || (elevatorDirection.equals(direction) && isRightDirection);
                })
                .toList();
        return rightDirectionElevators.isEmpty() ? null : findTheNearestElevator(rightDirectionElevators, floor, true);
    }

    private Elevator findAnyElevator(int floor){
        return findTheNearestElevator(elevators, floor, false);
    }

    public void callTheElevator(int currentFloor, Direction direction){
        if (Direction.UP.equals(direction)) {
            addWaitingPersonForUp(currentFloor);
        } else {
            addWaitingPersonForDown(currentFloor);
        }

        Elevator pickedElevator = findIdleElevatorOnTheFloor(currentFloor);

        if (pickedElevator != null){
            pickedElevator.addStop(currentFloor);
            pickedElevator.setDirection(direction);
            return;
        }

        pickedElevator = findIdleOrRightDirectionElevator(currentFloor, direction);

        if (pickedElevator != null){
            pickedElevator.addStop(currentFloor);
            if (Direction.IDLE.equals(pickedElevator.getDirection())){
                pickedElevator.setDirection(direction);
            }
            return;
        }

        pickedElevator = findAnyElevator(currentFloor);

        if (pickedElevator != null){
            pickedElevator.addStop(currentFloor);
        }
    }

    public void askForTheFloor(Elevator elevator, int numberOfPeopleWaiting){
        for (int i = 0; i < numberOfPeopleWaiting; i++) {
            System.out.println(Command.PICK_THE_FlOOR);
            int pickedFloor = scanner.nextInt();
            if (pickedFloor < 0 || pickedFloor > numberOfFloors){
                System.out.println("Wrong floor number");
                continue;
            }
            elevator.addStop(pickedFloor);
        }
    }

    public void makeStep(){
        for (Elevator elevator : elevators){
            if (elevator.shouldElevatorStop()){
                if(elevator.getDirection().equals(Direction.UP)){
                    int waitingPeople = waitingPeopleForUp.getOrDefault(elevator.getCurrentFloor(), 0);
                    askForTheFloor(elevator, waitingPeople);
                    waitingPeopleForUp.put(elevator.getCurrentFloor(), 0);
                } else {
                    int waitingPeople = waitingPeopleForDown.get(elevator.getCurrentFloor());
                    askForTheFloor(elevator, waitingPeople);
                    waitingPeopleForDown.put(elevator.getCurrentFloor(), 0);
                }
            }
            if(!Direction.IDLE.equals(elevator.getDirection()) && elevator.getCurrentFloor() == elevator.getDestinationFloor()){
                elevator.setDirection(Direction.IDLE);
            }
            if(elevator.getCurrentFloor() == 0 && Direction.DOWN.equals(elevator.getDirection())){
                elevator.setDirection(Direction.UP);
            }
            if(elevator.getCurrentFloor() == numberOfFloors && Direction.UP.equals(elevator.getDirection())){
                elevator.setDirection(Direction.DOWN);
            }
            elevator.tryToDecreaseDistance();
            elevator.move();
        }
    }

    public void update(int id, int currentFloor, int destinationFloor, Direction direction){
        Elevator elevator = elevators.get(id);

        try {
            elevator.update(currentFloor, destinationFloor, direction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showStatus(){
        for (Elevator elevator : elevators){
            System.out.println(elevator);
        }
    }
}
