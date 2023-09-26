import spock.lang.Specification

class ElevatorTest extends Specification {

    def setup(){

    }

    def "move the elevator"(){
        given:
        def elevator = new Elevator(1, 8, 10)

        when:
        elevator.move()

        then:
        elevator.getCurrentFloor() == 9
    }

    def "change the destination for up moving elevator when the stop is higher"(){
        given:
        def elevator = new Elevator(1, 4, 7)

        when:
        elevator.addStop(floor)

        then:
        elevator.getDestinationFloor() == floor

        where:
        floor << [8, 10, 12]
    }

    def "change the destination for down moving elevator when the stop is lower"(){
        given:
        def elevator = new Elevator(1, 6, 7)

        when:
        elevator.addStop(floor)

        then:
        elevator.getDestinationFloor() == floor

        where:
        floor << [5, 4, 3]
    }

    def "do not change the up moving elevator destination floor when the floor is in the middle of the path"(){
        given:
        def elevator = new Elevator(1, 1, 10)
        elevator.addStop(10)

        when:
        elevator.addStop(floor)

        then:
        elevator.getDestinationFloor() == 10

        where:
        floor << [2, 3, 4, 5, 6, 7, 8, 9]
    }

    def "do not change the down moving elevator destination floor when the floor is in the middle of the path"(){
        given:
        def elevator = new Elevator(1, 10, 1)
        elevator.addStop(1)

        when:
        elevator.addStop(floor)

        then:
        elevator.getDestinationFloor() == 1

        where:
        floor << [2, 3, 4, 5, 6, 7, 8, 9]
    }

    def "change destination when adding the stop to the different direction"(){
        given:
        def elevator = new Elevator(1, 4, 1)
        elevator.addStop(1)

        when:
        elevator.addStop(floor)

        then:
        elevator.getDestinationFloor() == floor

        where:
        floor << [5, 6, 7, 8, 9]
    }

    def "should the elevator stop on the given floor"(){
        given:
        def elevator = new Elevator(1, currentFloor, 1)
        elevator.addStop(currentFloor)

        when:
        def result = elevator.shouldElevatorStop()

        then:
        result

        where:
        currentFloor << [2, 3, 4, 5, 6, 7, 8, 9]

    }

    def "should not the elevator stop on the given floor"(){
        given:
        def elevator = new Elevator(1, currentFloor, 1)
        elevator.addStop(0)

        when:
        def result = elevator.shouldElevatorStop()

        then:
        !result

        where:
        currentFloor << [2, 3, 4, 5, 6, 7, 8, 9]
    }

    def "should not update the elevator if there are the stops to reach"(){
        given:
        def exceptionThrown = false
        def elevator = new Elevator(1, 1, 10)
        elevator.addStop(10)

        when:
        try {
            elevator.update(3,5, Direction.UP)
        } catch (Exception e) {
            exceptionThrown = true
        }

        then:
        exceptionThrown
    }

    def "should not update the elevator if the current floor is invalid"(){
        given:
        def exceptionThrown = false
        def elevator = new Elevator(1, 1, 10)

        when:
        try {
            elevator.update(3,5, Direction.UP)
        } catch (Exception e) {
            exceptionThrown = true
        }

        then:
        exceptionThrown
    }

}
