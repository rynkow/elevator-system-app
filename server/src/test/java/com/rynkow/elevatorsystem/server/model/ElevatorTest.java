package com.rynkow.elevatorsystem.server.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ElevatorTest {

    @Test
    void shouldNotMoveBelowGroundFloor(){
        // given elevator on floor 0
        Elevator elevator = new Elevator();
        assertEquals(0, elevator.getCurrentFloor());

        // when trying to move downwards
        elevator.move(-1);

        // then floor does not change
        assertEquals(0, elevator.getCurrentFloor());
    }

    @Test
    void shouldMoveTowardsDestination(){
        // given elevators with destinations above and below current floor
        Elevator elevatorUp = new Elevator();
        Elevator elevatorDown = new Elevator();
        for (int i = 0; i < 3; i++) {
            elevatorUp.move(1);
            elevatorDown.move(1);
        }
        elevatorDown.move(1);
        elevatorDown.move(-1);
        elevatorUp.addDestination(4);
        elevatorDown.addDestination(0);

        // when moving towards destination
        elevatorUp.move();
        elevatorDown.move();

        // floor changes by 1 in the direction of the destination
        assertEquals(3+1, elevatorUp.getCurrentFloor());
        assertEquals(3-1, elevatorDown.getCurrentFloor());
    }

    @Test
    void shouldNotMoveWithoutDestination(){
        // given elevator without destination
        Elevator elevator = new Elevator();
        for (int i = 0; i < 3; i++)
            elevator.move(1);

        // when trying to move it
        elevator.move();

        // then floor does not change
        assertEquals(3, elevator.getCurrentFloor());
    }

    @Test
    void shouldNotMoveMoreThanOneFloor(){
        // given an elevator
        Elevator elevatorUp = new Elevator();
        Elevator elevatorDown = new Elevator();
        elevatorDown.move(1);
        elevatorUp.move(1);

        // when calling move method with argument higher than one
        elevatorUp.move(5465);
        elevatorDown.move(-789);

        // then floor changes by one
        assertEquals(2, elevatorUp.getCurrentFloor());
        assertEquals(0, elevatorDown.getCurrentFloor());
    }

    @Test
    void shouldSetDirectionToZeroWhenLastDestinationIsReached(){
        // given an elevator with set destinations
        Elevator elevatorUp = new Elevator();
        Elevator elevatorDown = new Elevator();
        for (int i = 0; i < 3; i++) {
            elevatorDown.move(1);
            elevatorUp.move(1);
        }
        elevatorDown.addDestination(1);
        elevatorDown.addDestination(2);
        elevatorUp.addDestination(4);
        elevatorUp.addDestination(5);

        // when last destination is reached
        for (int i = 0; i < 2; i++) {
            elevatorDown.move();
            elevatorUp.move();
        }

        // then direction is set to zero
        assertEquals(0, elevatorUp.getDirection());
        assertEquals(0, elevatorDown.getDirection());
    }

    @Test
    void shouldNotSetDirectionToZeroWhenThereAreDestinationsLeft(){
        // given an elevator with set destinations
        Elevator elevatorUp = new Elevator();
        Elevator elevatorDown = new Elevator();
        for (int i = 0; i < 3; i++) {
            elevatorDown.move(1);
            elevatorUp.move(1);
        }
        elevatorDown.move(1);
        elevatorDown.move(-1);
        elevatorDown.addDestination(0);
        elevatorDown.addDestination(1);
        elevatorUp.addDestination(5);
        elevatorUp.addDestination(6);

        // when we don't reach the final destination
        for (int i = 0; i < 2; i++) {
            elevatorDown.move();
            elevatorUp.move();
        }

        // then direction is not reset
        assertEquals(1, elevatorUp.getDirection());
        assertEquals(-1, elevatorDown.getDirection());
    }

    @Test
    void shouldNotAddDestinationEqualToTheCurrentFloor(){
        // given an elevator
        Elevator elevatorWithoutDestination = new Elevator();
        Elevator elevatorWithDestination = new Elevator();
        elevatorWithoutDestination.move(1);
        elevatorWithDestination.move(1);
        elevatorWithDestination.addDestination(4);

        // when trying to add current floor
        elevatorWithoutDestination.addDestination(1);
        elevatorWithDestination.addDestination(1);

        // then destination is not added
        assertFalse(elevatorWithoutDestination.getDestinations().contains(1));
        assertFalse(elevatorWithDestination.getDestinations().contains(1));
    }

    @Test
    void shouldAddDestinationInCorrectDirection(){
        // given an elevator with set direction
        Elevator elevatorUp = new Elevator();
        Elevator elevatorDown = new Elevator();
        Elevator elevatorStill1 = new Elevator();
        Elevator elevatorStill2 = new Elevator();

        elevatorDown.move(1);
        elevatorUp.move(1);
        elevatorStill1.move(1);
        elevatorStill2.move(1);
        elevatorDown.move(1);
        elevatorDown.move(-1);
        elevatorStill1.move(0);
        elevatorStill2.move(0);

        elevatorUp.setDirection(1);
        elevatorStill1.setDirection(0);
        elevatorStill2.setDirection(0);
        elevatorDown.setDirection(-1);

        // when trying to set new destination in correct direction
        elevatorDown.addDestination(0);
        elevatorUp.addDestination(2);
        elevatorStill1.addDestination(0);
        elevatorStill2.addDestination(2);

        // then destination is added correctly
        assertTrue(elevatorDown.getDestinations().contains(0));
        assertTrue(elevatorUp.getDestinations().contains(2));
        assertTrue(elevatorStill1.getDestinations().contains(0));
        assertTrue(elevatorStill2.getDestinations().contains(2));
    }

    @Test
    void shouldNotAddDestinationInWrongDirection(){
        // given an elevator with set direction
        Elevator elevatorUp = new Elevator();
        Elevator elevatorDown = new Elevator();
        elevatorDown.move(1);
        elevatorUp.move(1);
        elevatorDown.move(1);
        elevatorDown.move(-1);
        elevatorDown.setDirection(-1);

        // when trying to set new destination in correct direction
        elevatorDown.addDestination(2);
        elevatorUp.addDestination(0);

        // then destination is added correctly
        assertFalse(elevatorDown.getDestinations().contains(2));
        assertFalse(elevatorUp.getDestinations().contains(0));
    }

    @Test
    void shouldSetDirectionWhenAddingDestination(){
        // given elevator with no direction
        Elevator elevatorUp = new Elevator();
        Elevator elevatorDown = new Elevator();
        elevatorDown.move(1);
        elevatorUp.move(1);
        elevatorDown.move(1);
        elevatorDown.move(-1);

        // when setting new destination
        elevatorUp.addDestination(2);
        elevatorDown.addDestination(0);

        // then direction is set corresponding to the destination
        assertEquals(1, elevatorUp.getDirection());
        assertEquals(-1, elevatorDown.getDirection());
    }

    @Test
    void shouldOnlyAddRequestedDestination(){
        // given an elevator and a list of destination
        ArrayList<Integer> destinations = new ArrayList<>();
        destinations.add(1);
        destinations.add(34);
        destinations.add(92);
        Elevator elevator = new Elevator();

        // when destinations are set
        for (int destination: destinations)
            elevator.addDestination(destination);

        // then only requested destinations are set
        for (int i = 0; i < 100; i++) {
            if (destinations.contains(i))
                assertTrue(elevator.getDestinations().contains(i));
            else
                assertFalse(elevator.getDestinations().contains(i));
        }
    }
    @Test
    void ShouldBeAbleToSetPriorityFloor(){
        // given an elevator without set priority floor
        Elevator elevator = new Elevator();
        assertTrue(elevator.getPriorityFloor().isEmpty());

        // when setting the priority floor
        elevator.setPriorityFloor(33);

        // then priority floor is set correctly
        assertEquals(33, elevator.getPriorityFloor().get());
    }

    @Test
    void ShouldNotAllowNewDestinationsWhenMovingTowardsPriorityFloor(){
        // given an elevator on way to priority target
        Elevator elevator = new Elevator();
        elevator.move(1);
        elevator.move(1);
        elevator.setPriorityFloor(8);
        elevator.move();

        // when adding new destinations
        elevator.addDestination(0);
        elevator.addDestination(3);
        elevator.addDestination(6);
        elevator.addDestination(8);

        // no destinations are set
        assertFalse(elevator.getDestinations().contains(3));
        assertFalse(elevator.getDestinations().contains(6));
        assertFalse(elevator.getDestinations().contains(0));
        assertFalse(elevator.getDestinations().contains(8));
    }

    @Test
    void shouldMoveTowardsPriorityFloor(){
        // given an elevator with set destination and priority floor
        Elevator elevator = new Elevator();
        elevator.move(1);
        elevator.addDestination(3);
        elevator.addDestination(4);
        elevator.setPriorityFloor(2);
        elevator.addDestination(5);
        int[] expectedPath = {2,3,4,5,4,3,2,2,2};

        // when moving
        // then elevator moves directly to priority floor if no further destinations are set
        for (int i = 0; i < 7; i++) {
            elevator.move();
            assertEquals(expectedPath[i], elevator.getCurrentFloor());
        }
        assertTrue(elevator.getPriorityFloor().isEmpty());
    }

    @Test
    void shouldCloseTheDoorsWenMoving(){
        // given an elevator with open doors
        Elevator elevator = new Elevator();
        elevator.addDestination(3);
        elevator.open();

        // when moving
        elevator.move();

        // then doors close
        assertFalse(elevator.isOpen());
    }

    @Test
    void shouldOpenDoorsAfterArrivingAtDestination(){
        // given an elevator
        Elevator elevator = new Elevator();
        elevator.addDestination(3);
        elevator.move();
        elevator.move();

        // when elevator arrives at destination floor
        elevator.move();

        // then doors should open
        assertTrue(elevator.isOpen());
    }


}
