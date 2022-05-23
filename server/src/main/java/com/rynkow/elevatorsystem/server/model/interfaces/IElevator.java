package com.rynkow.elevatorsystem.server.model.interfaces;

import java.util.Optional;
import java.util.SortedSet;

public interface IElevator {
    void move();

    void move(Integer direction);
    Integer getCurrentFloor();

    boolean addDestination(Integer destination);

    SortedSet<Integer> getDestinations();

    Integer getDirection();

    void setDirection(Integer direction);

    void setPriorityFloor(Integer priorityFloor);
    Optional<Integer> getPriorityFloor();

    Boolean isOpen();

    void close();
    void open();
    Double estimatedArrivalTime(Integer floor, Integer direction);
    boolean isIdle();

    boolean isOnPathToPriorityFloor();
}
