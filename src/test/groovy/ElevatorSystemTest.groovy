import spock.lang.Specification

class ElevatorSystemTest extends Specification {
    def elevatorSystem

    def setup(){
        elevatorSystem = new ElevatorSystem(8, 10)
    }

    def cleanup(){
        elevatorSystem = null
    }

    def "should call the idle elevator and change its direction"(){
        when:
        elevatorSystem.callTheElevator(floor, Direction.UP)

        then:
        def elevator = elevatorSystem.elevators[floor]
        with(elevator){
            getCurrentFloor() == floor
            getDestinationFloor() == floor
            getDirection() == Direction.UP
        }
        where:
        floor << [0,1,2,3,4,5,6,7]
    }

    def "should choose the elevator with right direction"(){
        given:
        elevatorSystem.elevators[6].setDirection(Direction.UP)
        elevatorSystem.elevators[7].setDirection(Direction.DOWN)

        when:
        elevatorSystem.callTheElevator(9, Direction.UP)
        elevatorSystem.makeStep()

        then:
        def pickedElevator = elevatorSystem.elevators[6]
        with(pickedElevator){
            getCurrentFloor() == 7
            getDestinationFloor() == 9
            getDirection() == Direction.UP
        }
    }

    def "should call the elevator with the shortest distance when there is no elevator with right direction"(){
        given:
        def otherElevatorSystem = new ElevatorSystem(4, 10)
        otherElevatorSystem.elevators[0].setDirection(Direction.UP)
        otherElevatorSystem.elevators[1].setDirection(Direction.UP)
        otherElevatorSystem.elevators[2].setDirection(Direction.DOWN)
        otherElevatorSystem.elevators[3].setDirection(Direction.DOWN)

        when:
        otherElevatorSystem.callTheElevator(7, Direction.DOWN)
        otherElevatorSystem.makeStep()

        then:
        def pickedElevator = otherElevatorSystem.elevators[2]
        with(pickedElevator){
            getCurrentFloor() == 1
            getDestinationFloor() == 7
            getDirection() == Direction.DOWN
        }
    }

    def "should not make a step when the elevator is idle"(){
        when:
        elevatorSystem.makeStep()

        then:
        def elevator = elevatorSystem.elevators[floor]
        with(elevator){
            getCurrentFloor() == floor
            getDestinationFloor() == floor
            getDirection() == Direction.IDLE
        }

        where:
        floor << [0,1,2,3,4,5,6,7]
    }

    def "should change the direction when the elevator reaches the 0 floor"(){
        given:
        elevatorSystem.elevators[0].setDirection(Direction.DOWN)
        elevatorSystem.elevators[0].addStop(2)

        when:
        elevatorSystem.makeStep()

        then:
        def elevator = elevatorSystem.elevators[0]
        with(elevator){
            getCurrentFloor() == 1
            getDestinationFloor() == 2
            getDirection() == Direction.UP
        }
    }

    def "should change the direction when the elevator reaches the highest floor"(){
        given:
        def otherElevatorSystem = new ElevatorSystem(5, 4)
        otherElevatorSystem.elevators[4].setDirection(Direction.UP)
        otherElevatorSystem.elevators[4].addStop(1)

        when:
        otherElevatorSystem.makeStep()

        then:
        def elevator = otherElevatorSystem.elevators[4]
        with(elevator){
            getCurrentFloor() == 3
            getDestinationFloor() == 1
            getDirection() == Direction.DOWN
        }
    }

    def "should change the direction where there is no more stops in this direction"(){
        given:
        def otherElevatorSystem = new ElevatorSystem(1, 7)
        otherElevatorSystem.elevators[0].setDirection(Direction.UP)
        otherElevatorSystem.elevators[0].addStop(3)

        when:
        otherElevatorSystem.makeStep()
        otherElevatorSystem.makeStep()
        otherElevatorSystem.callTheElevator(1, Direction.DOWN)
        otherElevatorSystem.makeStep()
        otherElevatorSystem.makeStep()

        then:
        def elevator = otherElevatorSystem.elevators[0]
        with(elevator){
            getCurrentFloor() == 2
            getDestinationFloor() == 1
            getDirection() == Direction.DOWN
        }

    }

}
