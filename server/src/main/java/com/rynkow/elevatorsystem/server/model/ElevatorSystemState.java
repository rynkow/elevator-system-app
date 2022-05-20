package com.rynkow.elevatorsystem.server.model;

import java.util.List;

public class ElevatorSystemState {
    private final Integer elevatorCount;
    private final Integer maxFlor;
    private final List<Integer> upRequests;
    private final List<Integer> downRequests;
    private final List<Integer> elevatorFloors;
    private final List<Integer> reservedElevators;
    private final List<List<Integer>> elevatorDestinations;

    public ElevatorSystemState(Integer elevatorCount, Integer maxFlor, List<Integer> upRequests, List<Integer> downRequests, List<Integer> elevatorFloors, List<Integer> reservedElevators, List<List<Integer>> elevatorDestinations) {
        this.elevatorCount = elevatorCount;
        this.maxFlor = maxFlor;
        this.upRequests = upRequests;
        this.downRequests = downRequests;
        this.elevatorFloors = elevatorFloors;
        this.reservedElevators = reservedElevators;
        this.elevatorDestinations = elevatorDestinations;
    }

    public static class Builder{
        private Integer elevatorCount;
        private Integer maxFlor;
        private List<Integer> upRequests;
        private List<Integer> downRequests;
        private List<Integer> elevatorFloors;
        private List<Integer> reservedElevators;
        private List<List<Integer>> elevatorDestinations;

        public Builder() {}
        public Builder setElevatorCount(Integer elevatorCount) {
            this.elevatorCount = elevatorCount;
            return this;
        }

        public Builder setMaxFlor(Integer maxFlor) {
            this.maxFlor = maxFlor;
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
                this.maxFlor,
                this.upRequests,
                this.downRequests,
                this.elevatorFloors,
                this.reservedElevators,
                this.elevatorDestinations
            );
        }
    }

    public Integer getElevatorCount() {
        return elevatorCount;
    }

    public Integer getMaxFlor() {
        return maxFlor;
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

    public List<Integer> getReservedElevators() {
        return reservedElevators;
    }

    public List<List<Integer>> getElevatorDestinations() {
        return elevatorDestinations;
    }
}
