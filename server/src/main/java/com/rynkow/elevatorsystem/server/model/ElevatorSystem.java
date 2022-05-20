package com.rynkow.elevatorsystem.server.model;

import com.rynkow.elevatorsystem.server.model.interfaces.IElevator;
import com.rynkow.elevatorsystem.server.model.interfaces.IElevatorSystem;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
@Component
public class ElevatorSystem implements IElevatorSystem {
    public final static Integer MAXIMUM_ELEVATOR_COUNT = 16;

    // system parameters
    private Integer elevatorCount;
    private Integer maxFlor;

    private List<IElevator> elevators;
    private List<Boolean> reservedElevators;
    private LinkedList<Request> requests;

    public ElevatorSystem(){
        this(4, 4);
    }

    public ElevatorSystem(Integer maxFloor, Integer elevatorCount){
        if (elevatorCount > MAXIMUM_ELEVATOR_COUNT)
            throw new IllegalArgumentException("elevator count higher than MAXIMUM_ELEVATOR_COUNT");

        this.maxFlor = maxFloor;
        this.elevatorCount = elevatorCount;

        resetSystemState();
    }

    @Override
    public void setSystemParameters(Integer maxFloor, Integer elevatorCount) {
        if (elevatorCount > MAXIMUM_ELEVATOR_COUNT)
            throw new IllegalArgumentException("elevator count higher than MAXIMUM_ELEVATOR_COUNT");

        this.maxFlor = maxFloor;
        this.elevatorCount = elevatorCount;

        resetSystemState();
    }

    private void resetSystemState(){
        elevators = new ArrayList<>(elevatorCount);
        reservedElevators = new ArrayList<>(elevatorCount);
        for (int i = 0; i < elevatorCount; i++) {
            elevators.add(new Elevator());
            reservedElevators.add(false);
        }

        requests = new LinkedList<>();
    }

    @Override
    public void nextStep() {
        ArrayList<IElevator> idleElevators = new ArrayList<>();

        // move elevators towards set destination, and check which ones are idle
        for (int i = 0; i < elevators.size(); i++) {
            // ignore reserved elevators
            if (reservedElevators.get(i))
                continue;
            IElevator elevator = elevators.get(i);

            // add idle elevators to idle list, and ignore them
            if (elevator.getDestinations().isEmpty()){
                idleElevators.add(elevator);
                continue;
            }

            //move elevator towards current destination
            elevator.move();

            // if there is a request on current floor - fulfill it
            // TODO what if elevator became idle (ie arrived at the final destination)? - changed request equals method to consider requests with direction 0 equal to any other request on the same floor
            int removedRequestIndex = requests.indexOf(new Request(elevator.getCurrentFloor(), elevator.getDirection()));
            if (removedRequestIndex >= 0){
                Request request = requests.get(removedRequestIndex);

                // free reserved elevator
                if (request.hasReservedElevator())
                    reservedElevators.set(request.getReservedElevator(), false);

                // remove request from list
                requests.remove(removedRequestIndex);

                // if elevator was direction was 0 - it becomes the direction of the request
                elevator.setDirection(request.direction);
            }
        }

        // handle requests
        for (int i = 0; i < requests.size(); i++) {
            Request request = requests.get(i);

            // check if an elevator will move past request
            boolean willBeHandled = false;
            for (IElevator elevator: elevators){
                // TODO request floor is the final destination - if there are 2 types of requests 1 will not be fulfilled
                if (elevator.willMovePast(request.floor) > 0) {
                    willBeHandled = true;
                    break;
                }
            }
            // if it will request will be handled.
            if (willBeHandled) {
                if (request.hasReservedElevator())
                    reservedElevators.set(request.getReservedElevator(), false);
                continue;
            }

            // check if there is an elevator reserved for the request
            if (request.hasReservedElevator()){
                IElevator requestElevator = elevators.get(request.getReservedElevator());
                requestElevator.move(Integer.signum(request.floor - requestElevator.getCurrentFloor()));
                if (request.floor.equals(requestElevator.getCurrentFloor())){
                    reservedElevators.set(request.getReservedElevator(), false);
                    requests.remove(i);
                    i--;
                }
                continue;
            }

            // check for closest idle elevator
            if (!idleElevators.isEmpty()){
                int closestIdleElevatorIndex = getClosestElevatorIndex(idleElevators, request.floor);
                IElevator closestIdleElevator = idleElevators.get(closestIdleElevatorIndex);
                request.setReservedElevator(closestIdleElevatorIndex);
                closestIdleElevator.move(Integer.signum(request.floor - closestIdleElevator.getCurrentFloor()));
                if (request.floor.equals(closestIdleElevator.getCurrentFloor())){
                    reservedElevators.set(request.getReservedElevator(), false);
                    requests.remove(i);
                    i--;
                } else reservedElevators.set(closestIdleElevatorIndex, true);
            }
        }
    }

    private Integer getClosestElevatorIndex(List<IElevator> elevators, Integer floor){
        int closestElevatorIndex = -1;
        int smallestDistance = maxFlor + 1;

        for (int i = 0; i < elevators.size(); i++) {
            IElevator elevator = elevators.get(i);
            int distance = Math.abs(floor-elevator.getCurrentFloor());
            if (distance < smallestDistance){
                closestElevatorIndex = i;
                smallestDistance = distance;
            }
        }

        return closestElevatorIndex;
    }

    @Override
    public ElevatorSystemState getState() {
        return new ElevatorSystemState.Builder()
                .setElevatorCount(elevatorCount)
                .setMaxFlor(maxFlor)
                .setElevatorFloors(elevators.stream().map(IElevator::getCurrentFloor).toList())
                .setDownRequests(requests.stream().filter(r->r.direction==-1).map(r->r.floor).toList())
                .setUpRequests(requests.stream().filter(r->r.direction==1).map(r->r.floor).toList())
                .setReservedElevators(IntStream.range(0, elevatorCount).filter(i->reservedElevators.get(i)).boxed().toList())
                .setElevatorDestinations(elevators.stream().map(e->e.getDestinations().stream().toList()).toList())
                .build();
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
        if (requests.contains(request) || request.floor > maxFlor)
            return false;

        // add the request to the end of the list, return ture
        requests.addLast(request);
        return true;
    }

    @Override
    public boolean newElevatorDestination(Integer elevatorId, Integer destinationFloor) {
        // ignore if elevator is reserved (e.g. elevator is assigned to a request) or max floor is exceeded
        if (reservedElevators.get(elevatorId) || destinationFloor > maxFlor)
            return false;

        // adds new destination to the elevator and returns op status
        return elevators.get(elevatorId).addDestination(destinationFloor);
    }
}
