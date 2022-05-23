import React, {useEffect, useState} from 'react';
import './App.css';
import Grid from "./component/Grid";
import {ElevatorSystemState} from "./interface/elevatorSystemState.interface";
import FetcherService from "./service/fetcherService";
import Elevator from "./component/Elevator";
import RequestControls from "./component/RequestControls";
import ControlBar from "./component/ControlBar";

const App = () => {
    const [maxFloor, setMaxFloor] = useState(0);
    const [elevatorCount, setElevatorCount] = useState(0);
    const [elevatorFloors, setElevatorFloors] = useState<number[]>([]);
    const [elevatorDirections, setElevatorDirections] = useState<number[]>([]);
    const [elevatorDestinations, setElevatorDestinations] = useState<number[][]>(new Array([]));
    const [upRequests, setUpRequests] = useState<number[]>([]);
    const [downRequests, setDownRequests] = useState<number[]>([]);
    const [reservedElevators, setReservedElevators] = useState<boolean[]>([])
    const [openElevators, setOpenElevators] = useState<boolean[]>([])

    const setState = (state: ElevatorSystemState) => {
        console.log(state);
        setMaxFloor(state.maxFloor);
        setElevatorCount(state.elevatorCount);
        setElevatorFloors(state.elevatorFloors);
        setDownRequests(state.downRequests);
        setUpRequests(state.upRequests);
        setElevatorDirections(state.elevatorDirections);
        setElevatorDestinations(state.elevatorDestinations);
        setReservedElevators(state.reservedElevators);
        setOpenElevators(state.openElevators);
    }

    useEffect(()=>{
        const fetchState = async () => {
            const systemState: ElevatorSystemState | undefined = await FetcherService.systemState();
            if (systemState !== undefined) setState(systemState);
        };
        fetchState().then();
    },[])

    const nextStep = async () => {
        const systemState: ElevatorSystemState | undefined = await FetcherService.nextStep();
        if (systemState !== undefined) setState(systemState);
    }

    const setSystemParameters = async (maxFloor: number, elevatorCount: number) => {
        const systemState: ElevatorSystemState | undefined = await FetcherService.setSystemParameters(maxFloor, elevatorCount);
        console.log(systemState);
        if (systemState !== undefined) setState(systemState);
    }

    const addDestination = async (elevatorId: number, destination: number) => {
        const ok = await FetcherService.newDestination(elevatorId, destination);
        if (ok){
            const newDestinations = elevatorDestinations[elevatorId].slice();
            newDestinations.push(destination);
            setElevatorDestinations(elevatorDestinations.map((destinations, id)=>{
                if (id !== elevatorId) return destinations;
                else return newDestinations;
            }))
            const destinationDirection = Math.sign(destination - elevatorFloors[elevatorId]);
            const newDirections = elevatorDirections.slice();
            newDirections[elevatorId] = destinationDirection;
            setElevatorDirections(newDirections);
        }
    }

    const addRequest = async (floor: number, direction: number) => {
        const ok = await FetcherService.newRequest(floor, direction);
        if (ok){
            if (direction < 0){
                const newDownRequests = downRequests.slice();
                newDownRequests.push(floor);
                setDownRequests(newDownRequests)
            }
            else {
                const newUpRequests = upRequests.slice();
                newUpRequests.push(floor);
                setUpRequests(newUpRequests)
            }
        }
    }

    const requestButtonDisabled = (floor: number, direction: number) => {
        for (let i = 0; i < elevatorCount; i++)
            if (elevatorFloors[i] === floor && !reservedElevators[i] && elevatorDirections[i] !== direction*(-1) && openElevators[i])
                return true;

        return false;
    }


    const gridElements: JSX.Element[][] = Array.from(Array(maxFloor + 1).keys()).map((row) =>
        Array.from(Array(elevatorCount + 1).keys()).map((column) => {
            if (column < elevatorCount && elevatorFloors[column] === maxFloor - row) return (
                <Elevator
                    key={column}
                    maxFloor={maxFloor}
                    floor={elevatorFloors[column]}
                    destinations={elevatorDestinations[column]}
                    onNewDestination={(destination)=>addDestination(column, destination)}
                    direction={elevatorDirections[column]}
                    isReserved={reservedElevators[column]}
                    isOpen={openElevators[column]}
                />
            );
            if (column === elevatorCount) return (
                <RequestControls
                    onNewRequest={(direction)=>addRequest(maxFloor - row, direction)}
                    key={column}
                    floor={maxFloor - row}
                    maxFloor={maxFloor}
                    upDisabled={requestButtonDisabled(maxFloor - row, 1)}
                    downDisabled={requestButtonDisabled(maxFloor - row, -1)}
                    upActive={upRequests.includes(maxFloor - row)}
                    downActive={downRequests.includes(maxFloor - row)}
                />
            );
            return <div></div>
        })
    )

    return (
        <div className="App">
            <Grid elements={gridElements}/>
            <ControlBar onNextStep={nextStep} onNewParameters={setSystemParameters}/>
        </div>
    );
}


export default App;
