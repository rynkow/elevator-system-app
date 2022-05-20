package com.rynkow.elevatorsystem.server.model.interfaces;

import com.rynkow.elevatorsystem.server.model.ElevatorSystemState;

import java.util.List;

public interface IElevatorSystem {
    void setSystemParameters(Integer maxFloor, Integer elevatorCount);
    void nextStep();
    ElevatorSystemState getState();
    boolean newUpRequest(Integer floor);
    boolean newDownRequest(Integer floor);
    boolean newElevatorDestination(Integer elevatorId, Integer destinationFloor);
}
