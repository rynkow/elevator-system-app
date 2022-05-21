import {ElevatorSystemState} from "../interface/elevatorSystemState.interface";

class FetcherService{
    public static systemState = async () => {
        const request = await fetch("http://localhost:8080/elevators", {method: "GET"});
        if (!request.ok) return undefined;
        const systemState: ElevatorSystemState = await request.json()
        return systemState;
    }

    public static nextStep = async () => {
        const request = await fetch("http://localhost:8080/elevators", {method: "PATCH"});
        if (!request.ok) return undefined;
        const systemState: ElevatorSystemState = await request.json()
        return systemState;
    }

    public static newDestination = async (elevatorId: number, destination: number) => {
        const request = await fetch(`http://localhost:8080/destination/${elevatorId}/${destination}`, {method: "PUT"});
        return request.ok;
    }

    public static newRequest = async (floor: number, direction: number) => {
        const request = await fetch(`http://localhost:8080/request/${floor}/${direction}`, {method: "PUT"});
        return request.ok;
    }

    public static setSystemParameters = async (maxFloor: number, elevatorCount: number) => {
        console.log("new params", maxFloor, elevatorCount)
        const request = await fetch(`http://localhost:8080/elevators/${maxFloor}/${elevatorCount}`, {method: "PUT"});
        if (!request.ok) return undefined;
        const systemState: ElevatorSystemState = await request.json()
        return systemState;
    }

}

export default FetcherService;