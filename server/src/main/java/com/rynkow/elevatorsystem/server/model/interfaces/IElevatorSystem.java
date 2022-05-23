package com.rynkow.elevatorsystem.server.model.interfaces;

import com.rynkow.elevatorsystem.server.model.ElevatorSystemState;

public interface IElevatorSystem {
    void nextStep();

    ElevatorSystemState getState();

    boolean newUpRequest(Integer floor);

    boolean newDownRequest(Integer floor);

    boolean newElevatorDestination(Integer elevatorId, Integer destinationFloor);
}
