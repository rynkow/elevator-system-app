package com.rynkow.elevatorsystem.server.model;

import com.rynkow.elevatorsystem.server.model.interfaces.IElevator;

import java.util.Objects;

public class Request {
    private final Integer floor;
    private final Integer direction;
    private IElevator assignedElevator;
    private Integer timer;

    public Request(Integer floor, Integer direction) {
        this.floor = floor;
        this.direction = Integer.signum(direction);
        this.timer = 0;
        this.assignedElevator = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return floor.equals(request.floor) && (direction.equals(request.direction));
    }

    @Override
    public int hashCode() {
        return Objects.hash(floor, direction);
    }

    public void incrementTimer(){
        this.timer += 1;
    }
    public Integer getWaitingTime() {
        return timer;
    }

    public void assign(IElevator elevator){
        this.assignedElevator = elevator;
    }

    public Boolean isAssigned() {
        return assignedElevator != null;
    }

    public IElevator getAssignedElevator() {
        return assignedElevator;
    }

    public Integer getFloor() {
        return floor;
    }

    public Integer getDirection() {
        return direction;
    }
}
