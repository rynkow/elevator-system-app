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

    private boolean onPathToPriorityFloor;

    public Elevator(){
        this.floor = 0;
        this.destinations = new TreeSet<>();
        this.direction = 0;
        this.isOpen = true;
        this.priorityFloor = null;
        this.onPathToPriorityFloor = false;
    }

    @Override
    public void move() {
        // move elevator based on set destinations
        this.close();

        // if there are no destinations set
        if (destinations.isEmpty()){
            // if there is a priority floor start moving towards it
            if (priorityFloor != null){
                move(priorityFloor-floor);
                onPathToPriorityFloor = true;

                // if we arrived at priority floor we set priority to null
                if (floor.equals(priorityFloor)) {
                    onPathToPriorityFloor = false;
                    priorityFloor = null;
                    direction = 0;
                }
                return;
            }
            // else don't move and set elevator direction to 0 and open doors
            direction = 0;
            this.open();
            return;
        }

        // move towards next destination
        int destinationDirection = Integer.signum(destinations.first() - floor);
        floor += destinationDirection;

        // remove current floor from destinations and open door
        if (destinations.remove(floor)) this.open();

        // if we reached the final destination and there is no priority set elevator becomes idle
        if (destinations.isEmpty() && priorityFloor == null) direction = 0;
    }

    @Override
    public void move(Integer direction) {
        if (floor + Integer.signum(direction) < 0) return;
        // move elevator one floor towards a specified direction
        this.floor += Integer.signum(direction);
        this.direction = Integer.signum(direction);
    }

    @Override
    public boolean addDestination(Integer destination) {
        // add new destination to the elevator - return false on failure and true on success

        // we cannot set current floor as a destination
        if (floor.equals(destination))
            return false;

        // direction in which the destination lays
        Integer newDestinationDirection = Integer.signum(destination - floor);

        // if elevator is going towards priority floor do not allow new destinations
        if (onPathToPriorityFloor) return false;

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
        if (direction == 0)
            direction = Integer.signum(priorityFloor - floor);
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
    public void open() {
        this.isOpen = true;
    }

    @Override
    public void close() {
        this.isOpen = false;
    }

    @Override
    public boolean isIdle() {
        return direction.equals(0) && destinations.isEmpty() && getPriorityFloor().isEmpty();
    }

    @Override
    public boolean isOnPathToPriorityFloor() {
        return onPathToPriorityFloor;
    }

    @Override
    public Double estimatedArrivalTime(Integer floor, Integer direction){
        Integer floorDirection = Integer.signum(floor - this.floor);
        if (!floorDirection.equals(this.direction)) return Double.POSITIVE_INFINITY;
        return (double) Math.abs(this.floor - floor);
    }
}
