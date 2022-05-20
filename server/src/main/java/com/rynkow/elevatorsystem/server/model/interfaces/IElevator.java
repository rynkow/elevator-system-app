package com.rynkow.elevatorsystem.server.model.interfaces;

import java.util.SortedSet;

public interface IElevator {
    void move();

    void move(Integer direction);
    Integer getCurrentFloor();

    boolean addDestination(Integer destination);

    SortedSet<Integer> getDestinations();

    Integer willMovePast(Integer floor);

    Integer getDirection();

    void setDirection(Integer direction);
}
