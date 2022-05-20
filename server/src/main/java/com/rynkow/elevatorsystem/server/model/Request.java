package com.rynkow.elevatorsystem.server.model;

import com.rynkow.elevatorsystem.server.model.interfaces.IElevator;

import java.util.Objects;

public class Request {
    public final Integer floor;
    public final Integer direction;

    private Integer reservedElevator;

    public Request(Integer floor, Integer direction) {
        this.floor = floor;
        this.direction = Integer.signum(direction);
        this.reservedElevator = -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        // TODO check if works
        return floor.equals(request.floor) && (direction.equals(request.direction) || request.direction.equals(0) || direction.equals(0));
    }

    @Override
    public int hashCode() {
        return Objects.hash(floor, direction);
    }

    public Integer getReservedElevator() {
        return reservedElevator;
    }

    public Boolean hasReservedElevator() {
        return reservedElevator >= 0;
    }

    public void setReservedElevator(Integer reservedElevator) {
        if (reservedElevator < 0 ) throw new IllegalArgumentException("elevator id lower than 0");
        this.reservedElevator = reservedElevator;
    }
}
