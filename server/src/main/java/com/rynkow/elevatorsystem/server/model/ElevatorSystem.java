package com.rynkow.elevatorsystem.server.model;

import com.rynkow.elevatorsystem.server.model.interfaces.IElevator;
import com.rynkow.elevatorsystem.server.model.interfaces.IElevatorSystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ElevatorSystem implements IElevatorSystem {
    public final static Integer MAXIMUM_ELEVATOR_COUNT = 16;
    private final Integer elevatorCount;
    private final Integer maxFlor;
    private List<IElevator> elevators;
    private LinkedList<Request> requests;

    public ElevatorSystem(Integer maxFloor, Integer elevatorCount) {
        if (elevatorCount > MAXIMUM_ELEVATOR_COUNT)
            throw new IllegalArgumentException("elevator count higher than MAXIMUM_ELEVATOR_COUNT");

        this.maxFlor = maxFloor;
        this.elevatorCount = elevatorCount;

        resetSystemState();
    }

    private void resetSystemState() {
        elevators = new ArrayList<>(elevatorCount);
        for (int i = 0; i < elevatorCount; i++) {
            elevators.add(new Elevator());
        }

        requests = new LinkedList<>();
    }

    @Override
    public void nextStep() {
        Double avgWaitingTime = getAverageWaitingTime();

        // for every request try to assign an elevator to it
        for (Request request : requests) {
            if (request.isAssigned()) continue;

            if (!assignRequest(request))
                assignPriorityRequest(request, avgWaitingTime);
            request.incrementTimer();
        }

        // move the elevators and fulfil requests
        for (IElevator elevator : elevators) {
            elevator.move();
            Request requestToFulfil = findRequest(elevator, elevator.getCurrentFloor());
            if (requestToFulfil != null) {
                requests.remove(requestToFulfil);
                elevator.open();
                elevator.setDirection(requestToFulfil.getDirection());
            }
        }
    }

    private boolean assignRequest(Request request) {
        IElevator bestElevator = null;
        double bestTime = Double.POSITIVE_INFINITY;

        // look for elevator with best estimated arrival time
        for (IElevator elevator : elevators) {
            if (elevator.getPriorityFloor().isPresent()) continue;
            Double estimatedTime = Double.POSITIVE_INFINITY;

            // if elevator goes in the direction of the request
            if (elevator.getDirection().equals(request.getDirection()))
                estimatedTime = elevator.estimatedArrivalTime(request.getFloor(), request.getDirection());
            // if elevator is idle
            else if (elevator.isIdle()) {
                Integer elevatorDirection = Integer.signum(elevator.getCurrentFloor() - request.getFloor());
                // if elevator would move in the same direction as the request direction or is on at max or ground-floor
                if (!elevatorDirection.equals(request.getDirection()) || request.getFloor().equals(0) || request.getFloor().equals(maxFlor)) {
                    estimatedTime = (double) Math.abs(request.getFloor() - elevator.getCurrentFloor()) + 1;
                }
                // if elevator would have to be reserved
                else {
                    estimatedTime = ((double) Math.abs(request.getFloor() - elevator.getCurrentFloor()) - 1) * 5;
                }
            }

            if (bestTime > estimatedTime) {
                bestElevator = elevator;
                bestTime = estimatedTime;
            }
        }

        // if such elevator was found - assign it to the request ond return true
        if (bestElevator != null) {
            Integer bestElevatorDirection = Integer.signum(bestElevator.getCurrentFloor() - request.getFloor());
            if (!bestElevatorDirection.equals(request.getDirection()) || request.getFloor().equals(0) || request.getFloor().equals(maxFlor))
                bestElevator.addDestination(request.getFloor());
            else
                bestElevator.setPriorityFloor(request.getFloor());
            request.assign(bestElevator);
            return true;
        }
        // return false - could not find available elevator.
        return false;
    }

    private void assignPriorityRequest(Request request, Double avgWaitingTime) {
        // check if elevator waited long enough to warrant priority treatment
        if (request.getWaitingTime() <= avgWaitingTime) return;

        // look for an elevator that would arrive at request floor fastest in worst case scenario
        IElevator bestElevator = null;
        double bestTime = Double.POSITIVE_INFINITY;
        for (IElevator elevator : elevators) {
            // skip elevators with priority requests
            if (elevator.getPriorityFloor().isPresent()) continue;

            double estimatedTime;

            // estimated time - distance to the end of the building and back to the request
            if (elevator.getDirection().equals(1))
                estimatedTime = elevator.estimatedArrivalTime(maxFlor, 1) + maxFlor - request.getFloor();
            else
                estimatedTime = elevator.estimatedArrivalTime(0, -1) + request.getFloor();

            // we compare found elevator to the current best
            if (bestTime > estimatedTime) {
                bestElevator = elevator;
                bestTime = estimatedTime;
            }
        }

        // if we found an elevator we assign it to the request
        if (bestElevator != null) {
            bestElevator.setPriorityFloor(request.getFloor());
            request.assign(bestElevator);
        }
    }

    private Double getAverageWaitingTime() {
        Double waitingTime = 0.0;
        for (Request request : requests)
            waitingTime += request.getWaitingTime();
        return waitingTime / requests.size();
    }

    @Override
    public ElevatorSystemState getState() {
        return new ElevatorSystemState.Builder()
                .setElevatorCount(elevatorCount)
                .setMaxFloor(maxFlor)
                .setElevatorFloors(elevators.stream().map(IElevator::getCurrentFloor).toList())
                .setDownRequests(requests.stream().filter(r -> r.getDirection() == -1).map(Request::getFloor).toList())
                .setUpRequests(requests.stream().filter(r -> r.getDirection() == 1).map(Request::getFloor).toList())
                .setReservedElevators(elevators.stream().map(IElevator::isOnPathToPriorityFloor).toList())
                .setElevatorDestinations(elevators.stream().map(e -> e.getDestinations().stream().toList()).toList())
                .setElevatorDirections(elevators.stream().map(IElevator::getDirection).toList())
                .setOpenElevators(elevators.stream().map(IElevator::isOpen).toList())
                .build();
    }

    private Request findRequest(IElevator assignedElevator, Integer floor) {
        for (Request request : requests)
            if (assignedElevator.equals(request.getAssignedElevator()) && request.getFloor().equals(floor))
                return request;
        return null;
    }

    @Override
    public boolean newUpRequest(Integer floor) {
        // creates new request to move upwards from a floor
        Request newRequest = new Request(floor, 1);

        return newRequest(newRequest);
    }

    @Override
    public boolean newDownRequest(Integer floor) {
        // creates new request to move downwards from a floor
        Request newRequest = new Request(floor, -1);

        return newRequest(newRequest);
    }

    private boolean newRequest(Request request) {
        // return false if request is already placed, or max floor is exceeded
        if (requests.contains(request) || request.getFloor() > maxFlor)
            return false;

        // return false if there is an open elevator that passenger could use instead
        for (IElevator elevator : elevators) {
            if (elevator.getCurrentFloor().equals(request.getFloor())
                    && elevator.getDirection().equals(request.getDirection())
                    && elevator.isOpen())
                return false;
        }

        // add the request to the end of the unhandled list, return ture
        requests.addLast(request);
        return true;
    }

    @Override
    public boolean newElevatorDestination(Integer elevatorId, Integer destinationFloor) {
        // ignore if max floor is exceeded
        if (destinationFloor > maxFlor)
            return false;

        // adds new destination to the elevator and returns op status
        return elevators.get(elevatorId).addDestination(destinationFloor);
    }
}
