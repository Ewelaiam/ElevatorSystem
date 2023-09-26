import java.util.*;

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

    //                .filter(elevator -> elevator.getCurrentFloor() == floor && elevator.getDirection() == Direction.IDLE)

    private Optional<Elevator> findIdleElevatorOnTheFloor(int floor){
        List<Elevator> idleElevatorsOnTheFloor = elevators.stream()
                .filter(elevator -> Direction.IDLE.equals(elevator.getDirection())
                        && elevator.getCurrentFloor() == floor)
                .toList();
        return idleElevatorsOnTheFloor.isEmpty() ? Optional.empty() : Optional.ofNullable(idleElevatorsOnTheFloor.get(0));
    }

    private void addWaitingPersonForUp(int floor){
        var newCounter = waitingPeopleForUp.getOrDefault(floor, 0);
        waitingPeopleForUp.put(floor, newCounter + 1);
    }

    private void addWaitingPersonForDown(int floor){
        var newCounter = waitingPeopleForDown.getOrDefault(floor, 0);
        waitingPeopleForDown.put(floor, newCounter + 1);
    }

    private Optional<Elevator> findTheNearestElevator(List<Elevator> candidates, int floor, boolean isRightDirection){
        Optional<Elevator> nearestElevator = Optional.empty();
        int minDistance = Integer.MAX_VALUE;
        for (Elevator candidate : candidates){
            int distance = countDistance(candidate, floor, isRightDirection);
            if (distance < minDistance){
                minDistance = distance;
                nearestElevator = Optional.of(candidate);
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

    private Optional<Elevator> findIdleOrRightDirectionElevator(int floor, Direction direction){
        List<Elevator> rightDirectionElevators = elevators.stream()
                .filter( elevator -> {
                        Direction elevatorDirection = elevator.getDirection();
                        boolean isRightDirection = (Direction.UP.equals(direction) && elevator.getCurrentFloor() < floor)
                                || (Direction.DOWN.equals(direction) && elevator.getCurrentFloor() > floor);
                        return elevatorDirection.equals(Direction.IDLE) || (elevatorDirection.equals(direction) && isRightDirection);
                })
                .toList();
        return findTheNearestElevator(rightDirectionElevators, floor, true);
    }

    private Optional<Elevator> findAnyElevator(int floor){
        return findTheNearestElevator(elevators, floor, false);
    }

    public void callTheElevator(int floor, Direction direction){
        if (Direction.UP.equals(direction)) {
            addWaitingPersonForUp(floor);
        } else {
            addWaitingPersonForDown(floor);
        }

        var pickedElevator = findIdleElevatorOnTheFloor(floor)
                .orElse(findIdleOrRightDirectionElevator(floor, direction).orElse(findAnyElevator(floor).orElse(null)));

        if (pickedElevator != null){
            pickedElevator.addStop(floor);
            if (Direction.IDLE.equals(pickedElevator.getDirection())){
                pickedElevator.setDirection(direction);
            }
        }

    }

    public void askForTheFloor(Elevator elevator, int numberOfPeopleWaiting){
        for (int i = 0; i < numberOfPeopleWaiting; i++) {
            System.out.println(Command.PICK_THE_FlOOR);
            try{
                int pickedFloor = scanner.nextInt();
                if (pickedFloor < 0 || pickedFloor > numberOfFloors){
                    throw new IllegalArgumentException("Invalid floor number - should be between 0 and " + numberOfFloors);
                }
                elevator.addStop(pickedFloor);
            } catch (NumberFormatException e) {
                System.out.println("Invalid first argument - should be the number provided\n");
            } catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
            }
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
