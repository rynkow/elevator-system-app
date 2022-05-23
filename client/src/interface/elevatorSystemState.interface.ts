export interface ElevatorSystemState{
    maxFloor: number,                       // max floor
    elevatorCount: number,                  // number of elevators in the system
    upRequests: number[],                   // floor numbers of floors with active up request
    downRequests: number[],                 // floor numbers of floors with active down request
    elevatorFloors: number[],               // current floors of the elevators
    elevatorDirections: number[],           // current directions of the elevators
    openElevators: boolean[],
    reservedElevators: boolean[],            // indices of reserved elevators
    elevatorDestinations: number[][],       // for each elevator there is a list of set destinations
}
