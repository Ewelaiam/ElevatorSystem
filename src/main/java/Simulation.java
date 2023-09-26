import java.util.Scanner;

public class Simulation {
    private int numberOfElevators;
    private int numberOfFloors;

    private Scanner scanner = new Scanner(System.in);

    private void setNumberOfElevators(){
        System.out.println(Command.NUMBER_OF_ELEVATORS);
        numberOfElevators = scanner.nextInt();
        scanner.nextLine();
        while (numberOfElevators < 1 || numberOfElevators > 16){
            System.out.println(Command.INVALID_NUMBER_OF_ELEVATORS);
            numberOfElevators = scanner.nextInt();
            scanner.nextLine();
        }
    }

    private void setNumberOfFloors(){
        System.out.println(Command.NUMBER_OF_FLOORS);
        numberOfFloors = scanner.nextInt();
        scanner.nextLine();
        while (numberOfFloors < 5 || numberOfFloors > 20){
            System.out.println(Command.INVALID_NUMBER_OF_FLOORS);
            numberOfFloors = scanner.nextInt();
            scanner.nextLine();
        }
    }

    private void setInitialData(){
        try {
            setNumberOfElevators();
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
        setNumberOfFloors();
    }

    private void verifyPickupParameters(int currentFloor, Direction direction){
        if (currentFloor < 0 || currentFloor > numberOfFloors){
            throw new IllegalArgumentException("Invalid current floor. Please enter a number between 0 and " + numberOfFloors + "\n");
        }
        if (!Direction.UP.equals(direction) && !Direction.DOWN.equals(direction)){
            throw new IllegalArgumentException("Invalid direction. Please enter UP or DOWN\n");
        }
    }

    private void verifyUpdateParameters(int id, int currentFloor, int destinationFloor, Direction direction){
        if (id < 0 || id > numberOfElevators - 1){
            throw new IllegalArgumentException("Invalid elevator id. Please enter a number between 0 and " + (numberOfElevators - 1) + "\n");
        }
        if (destinationFloor < 0 || destinationFloor > numberOfFloors){
            throw new IllegalArgumentException("Invalid destination floor. Please enter a number between 0 and " + numberOfFloors + "\n");
        }
        verifyPickupParameters(currentFloor, direction);
    }
    public void run(){
        System.out.println(Command.WELCOME);
        setInitialData();

        ElevatorSystem elevatorSystem = new ElevatorSystem(numberOfElevators, numberOfFloors);

        boolean isRunning = true;
        while(isRunning){
            System.out.println(Command.MENU);
            String chosenOption = scanner.nextLine().trim().toUpperCase();
            switch (chosenOption){
                case "PICKUP":
                    try {
                        System.out.println(Command.PICKUP_PARAMETERS);
                        String[] params = scanner.nextLine().split(" ");
                        verifyPickupParameters(Integer.parseInt(params[0]), Direction.valueOf(params[1].toUpperCase()));
                        elevatorSystem.callTheElevator(Integer.parseInt(params[0]), Direction.valueOf(params[1].toUpperCase()));
                    } catch (NumberFormatException e){
                        System.out.println("Invalid first argument - should be the number provided\n");
                    } catch (IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case "UPDATE":
                    try{
                        System.out.println(Command.UPDATE_PARAMETERS);
                        String[] updateParams = scanner.nextLine().split(" ");
                        verifyUpdateParameters(Integer.parseInt(updateParams[0]), Integer.parseInt(updateParams[1]), Integer.parseInt(updateParams[2]), Direction.valueOf(updateParams[3].toUpperCase()));
                        elevatorSystem.update(Integer.parseInt(updateParams[0]), Integer.parseInt(updateParams[1]), Integer.parseInt(updateParams[2]), Direction.valueOf(updateParams[3].toUpperCase()));
                    } catch (NumberFormatException e){
                        System.out.println("Invalid first argument - should be the number provided\n");
                    } catch (IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case "STEP":
                    elevatorSystem.makeStep();
                    break;
                case "STATUS":
                    elevatorSystem.showStatus();
                    break;
                case "QUIT":
                    isRunning = false;
                    break;
                default:
                    System.out.println("Command not recognized - try again\n");
            }

        }
    }

}
