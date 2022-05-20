package com.rynkow.elevatorsystem.server.model;

import com.rynkow.elevatorsystem.server.model.interfaces.IElevator;
import org.springframework.core.Ordered;

import java.util.Set;
import java.util.TreeSet;

public class Elevator implements IElevator {
    private Integer floor;
    private TreeSet<Integer> destinations;

    @Override
    public void move() {
        // move elevator based on set destinations
        if (destinations.isEmpty())
            return;     // if there are no destinations don't move

        // move towards next destination
        Integer nextDestination = destinations.first();
        if (floor < nextDestination){
            // if next destination is above move up
            floor += 1;
        }
        else {
            // if next destination is below the elevator set next destination to the highest one in the set, and move downwards
            nextDestination = destinations.last();
            floor -= 1;
        }
        // if we arrived at the destination floor, we remove it from the set
        if (floor.equals(nextDestination))
            destinations.remove(nextDestination);
    }

    @Override
    public void move(Integer direction) {
        // move elevator one floor towards a specified direction
        this.floor += Integer.signum(direction);
    }

    @Override
    public boolean addDestination(Integer destination) {
        // add new destination to the elevator - return false on failure and true on success

        // we cannot set current floor as a destination
        if (floor.equals(destination))
            return false;

        // if elevator does not have a destination all directions are ok
        if (destinations.isEmpty()){
            destinations.add(destination);
            return true;
        }

        // check if new destination is in the same direction as current destinations
        Integer currentDestinationDirection = Integer.signum(destinations.first() - floor);
        Integer newDestinationDirection = Integer.signum(destination - floor);
        if (!currentDestinationDirection.equals(newDestinationDirection))
            return false;
        destinations.add(destination);
        return true;
    }

    @Override
    public Integer willMovePast(Integer floor) {
        if (destinations.isEmpty()) return 0;

        Integer currentDestinationDirection = Integer.signum(destinations.first() - this.floor);
        Integer floorDirection = Integer.signum(floor - this.floor);

        if (!currentDestinationDirection.equals(floorDirection)) return 0;

        Integer floorDistance = Math.abs(floor - this.floor);
        if (currentDestinationDirection.equals(1) && destinations.last() >= floor)
            return floorDistance;

        if (currentDestinationDirection.equals(-1) && destinations.first() <= floor)
            return floorDistance;

        return 0;
    }

    @Override
    public Integer getDirection() {
        if (destinations.isEmpty()) return 0;
        return Integer.signum(destinations.first() - this.floor);
    }

    @Override
    public TreeSet<Integer> getDestinations() {
        return destinations;
    }

    @Override
    public Integer getCurrentFloor() {
        return floor;
    }
}
