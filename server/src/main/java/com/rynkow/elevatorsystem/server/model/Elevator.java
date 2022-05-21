package com.rynkow.elevatorsystem.server.model;

import com.rynkow.elevatorsystem.server.model.interfaces.IElevator;
import org.springframework.core.Ordered;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public class Elevator implements IElevator {
    private Integer floor;
    private final TreeSet<Integer> destinations;
    private Integer direction;

    private Boolean isOpen;

    private Integer priorityFloor;

    public Elevator(){
        this.floor = 0;
        this.destinations = new TreeSet<>();
        this.direction = 0;
        this.isOpen = true;
        this.priorityFloor = null;
    }

    @Override
    public void move() {
        // move elevator based on set destinations

        // if there are no destinations set
        if (destinations.isEmpty()){
            // if there is a priority floor move towards it
            if (priorityFloor != null){
                move(priorityFloor-floor);
                if (floor.equals(priorityFloor)) priorityFloor = null;
            }
            // if there are no destinations don't move and set elevator direction to 0
            direction = 0;
            return;
        }

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
        if (floor.equals(nextDestination)){
            destinations.remove(nextDestination);
            // if we reached the final destination elevator becomes idle
            if (destinations.isEmpty()) direction = 0;
        }
    }

    @Override
    public void move(Integer direction) {
        if (floor + Integer.signum(direction) < 0) return;
        // move elevator one floor towards a specified direction
        this.floor += Integer.signum(direction);
    }

    @Override
    public boolean addDestination(Integer destination) {
        // add new destination to the elevator - return false on failure and true on success

        // we cannot set current floor as a destination
        if (floor.equals(destination))
            return false;

        // direction in which the destination lays
        Integer newDestinationDirection = Integer.signum(destination - floor);

        // if there is a priority target set
        if (priorityFloor != null){
            // only destinations between current floor and farthest destination are allowed
            if (willMovePast(destination) == 0)
                return false;

            destinations.add(destination);
            return true;
        }

        // if elevator is idle all directions are ok, and destination direction becomes elevator direction
        if (direction.equals(0)){
            destinations.add(destination);
            direction = newDestinationDirection;
            return true;
        }

        // if direction is set - allow only destinations in that direction
        if (!direction.equals(newDestinationDirection))
            return false;
        destinations.add(destination);
        return true;
    }

    @Override
    public Integer willMovePast(Integer floor) {
        if (destinations.isEmpty()) return 0;

        Integer floorDirection = Integer.signum(floor - this.floor);

        if (!direction.equals(floorDirection)) return 0;

        Integer floorDistance = Math.abs(floor - this.floor);
        if (direction.equals(1) && destinations.last() >= floor)
            return floorDistance;

        if (direction.equals(-1) && destinations.first() <= floor)
            return floorDistance;

        return 0;
    }

    @Override
    public Integer getDirection() {
        return direction;
    }

    @Override
    public void setDirection(Integer direction) {
        // sets elevator direction - only possible if elevator has no direction
        if (this.direction == 0)
            this.direction = Integer.signum(direction);
    }

    @Override
    public void setPriorityFloor(Integer priorityFloor) {
        this.priorityFloor = priorityFloor;
    }

    @Override
    public Optional<Integer> getPriorityFloor() {
        return Optional.ofNullable(priorityFloor);
    }

    @Override
    public TreeSet<Integer> getDestinations() {
        return destinations;
    }

    @Override
    public Integer getCurrentFloor() {
        return floor;
    }

    @Override
    public Boolean isOpen() {
        return isOpen;
    }

    @Override
    public void toggleDoor() {
        isOpen = !isOpen;
    }
}
