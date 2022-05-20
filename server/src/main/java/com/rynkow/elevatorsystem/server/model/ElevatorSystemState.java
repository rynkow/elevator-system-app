package com.rynkow.elevatorsystem.server.model;

import java.util.List;

public class ElevatorSystemState {
    private final Integer elevatorCount;
    private final Integer maxFloor;
    private final List<Integer> upRequests;
    private final List<Integer> downRequests;
    private final List<Integer> elevatorFloors;

    private final List<Integer> elevatorDirections;
    private final List<Integer> reservedElevators;
    private final List<List<Integer>> elevatorDestinations;

    public ElevatorSystemState(Integer elevatorCount, Integer maxFloor, List<Integer> upRequests, List<Integer> downRequests, List<Integer> elevatorFloors, List<Integer> elevatorDirections, List<Integer> reservedElevators, List<List<Integer>> elevatorDestinations) {
        this.elevatorCount = elevatorCount;
        this.maxFloor = maxFloor;
        this.upRequests = upRequests;
        this.downRequests = downRequests;
        this.elevatorFloors = elevatorFloors;
        this.elevatorDirections = elevatorDirections;
        this.reservedElevators = reservedElevators;
        this.elevatorDestinations = elevatorDestinations;
    }

    public static class Builder{
        private Integer elevatorCount;
        private Integer maxFloor;
        private List<Integer> upRequests;
        private List<Integer> downRequests;
        private List<Integer> elevatorFloors;
        private List<Integer> elevatorDirections;
        private List<Integer> reservedElevators;
        private List<List<Integer>> elevatorDestinations;

        public Builder() {}
        public Builder setElevatorCount(Integer elevatorCount) {
            this.elevatorCount = elevatorCount;
            return this;
        }

        public Builder setMaxFloor(Integer maxFloor) {
            this.maxFloor = maxFloor;
            return this;
        }

        public Builder setUpRequests(List<Integer> upRequests) {
            this.upRequests = upRequests;
            return this;
        }

        public Builder setDownRequests(List<Integer> downRequests) {
            this.downRequests = downRequests;
            return this;
        }

        public Builder setElevatorFloors(List<Integer> elevatorFloors) {
            this.elevatorFloors = elevatorFloors;
            return this;
        }
        public Builder setElevatorDirections(List<Integer> elevatorDirections) {
            this.elevatorDirections = elevatorDirections;
            return this;
        }

        public Builder setReservedElevators(List<Integer> reservedElevators) {
            this.reservedElevators = reservedElevators;
            return this;
        }

        public Builder setElevatorDestinations(List<List<Integer>> elevatorDestinations) {
            this.elevatorDestinations = elevatorDestinations;
            return this;
        }

        public ElevatorSystemState build(){
            return new ElevatorSystemState(
                this.elevatorCount,
                this.maxFloor,
                this.upRequests,
                this.downRequests,
                this.elevatorFloors,
                this.elevatorDirections,
                this.reservedElevators,
                this.elevatorDestinations
            );
        }
    }

    public Integer getElevatorCount() {
        return elevatorCount;
    }

    public Integer getMaxFloor() {
        return maxFloor;
    }

    public List<Integer> getUpRequests() {
        return upRequests;
    }

    public List<Integer> getDownRequests() {
        return downRequests;
    }

    public List<Integer> getElevatorFloors() {
        return elevatorFloors;
    }

    public List<Integer> getElevatorDirections() {
        return elevatorDirections;
    }

    public List<Integer> getReservedElevators() {
        return reservedElevators;
    }

    public List<List<Integer>> getElevatorDestinations() {
        return elevatorDestinations;
    }
}
