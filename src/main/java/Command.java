public class Command {
    public static String WELCOME = "Welcome to the Elevator System";
    public static String NUMBER_OF_ELEVATORS = "Please enter the number of elevators (1-16)";
    public static String NUMBER_OF_FLOORS = "Please enter the number of floors (5-20)";
    public static String MENU = "Please choose one of the following options:\n" +
            "> PICKUP\n" +
            "> UPDATE\n" +
            "> STEP\n" +
            "> STATUS\n" +
            "> QUIT\n";

    public static String PICKUP_PARAMETERS = "Please enter the current floor and the direction. Format <floor> <direction> (e.g. 7 UP)";
    public static String PICK_THE_FlOOR = "Please enter the floor you want to go to";
    public static String UPDATE_PARAMETERS = "Please enter the elevator id, current floor, destination floor and direction. Format <id> <current floor> " +
            "<destination floor> <direction> (e.g. 1 7 10 UP)";

    public static String INVALID_NUMBER_OF_ELEVATORS = "Invalid number of elevators. Please enter a number between 1 and 16";
    public static String INVALID_NUMBER_OF_FLOORS = "Invalid number of floors. Please enter a number between 5 and 20";
}
