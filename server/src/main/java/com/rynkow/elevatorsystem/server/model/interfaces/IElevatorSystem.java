package com.rynkow.elevatorsystem.server.model.interfaces;

import java.util.List;

public interface IElevatorSystem {
    void setSystemParameters(Integer maxFloor, Integer elevatorCount);
    void nextStep();
    // TODO set data type
    List<?> getState();
    boolean newUpRequest(Integer floor);
    boolean newDownRequest(Integer floor);
    boolean newElevatorDestination(Integer elevatorId, Integer destinationFloor);
}
