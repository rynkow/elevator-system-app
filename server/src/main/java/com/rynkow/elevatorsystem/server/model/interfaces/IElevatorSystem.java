package com.rynkow.elevatorsystem.server.model.interfaces;

import com.rynkow.elevatorsystem.server.model.ElevatorSystemState;

public interface IElevatorSystem {
    // next step in the simulation
    void nextStep();

    // gets the state of the system,
    ElevatorSystemState getState();

    // new request to go up from a floor
    boolean newUpRequest(Integer floor);

    // new request to go down from a floor
    boolean newDownRequest(Integer floor);

    // new destination for elevator
    boolean newElevatorDestination(Integer elevatorId, Integer destinationFloor);
}
