import React, {useEffect, useState} from 'react';
import './App.css';
import Grid from "./component/Grid";
import {ElevatorSystemState} from "./interface/elevatorSystemState.interface";
import FetcherService from "./service/fetcherService";
import Elevator from "./component/Elevator";
import RequestControls from "./component/RequestControls";

const App = () => {
    const [maxFloor, setMaxFloor] = useState(0);
    const [elevatorCount, setElevatorCount] = useState(0);
    const [elevatorFloors, setElevatorFloors] = useState<number[]>([]);
    const [elevatorDirections, setElevatorDirections] = useState<number[]>([]);
    const [elevatorDestinations, setElevatorDestinations] = useState<number[][]>(new Array([]));
    const [upRequests, setUpRequests] = useState<number[]>([]);
    const [downRequests, setDownRequests] = useState<number[]>([]);

    const setState = (state: ElevatorSystemState) => {
        console.log(state);
        setMaxFloor(state.maxFloor);
        setElevatorCount(state.elevatorCount);
        setElevatorFloors(state.elevatorFloors);
        setDownRequests(state.downRequests);
        setUpRequests(state.upRequests);
        setElevatorDirections(state.elevatorDirections);
        setElevatorDestinations(state.elevatorDestinations);
    }

    useEffect(()=>{
        const fetchState = async () => {
            const systemState: ElevatorSystemState | undefined = await FetcherService.systemState();
            if (systemState !== undefined) setState(systemState);
        };
        fetchState().then();
    },[])

    const addDestination = async (elevatorId: number, destination: number) => {
        const ok = await FetcherService.newDestination(elevatorId, destination);
        if (ok){
            const newDestinations = elevatorDestinations[elevatorId].slice();
            newDestinations.push(destination);
            setElevatorDestinations(elevatorDestinations.map((destinations, id)=>{
                if (id !== elevatorId) return destinations;
                else return newDestinations
            }))
        }
    }

    const gridElements: JSX.Element[][] = Array.from(Array(maxFloor + 1).keys()).map((row) =>
        Array.from(Array(elevatorCount + 1).keys()).map((column) => {
            if (column < elevatorCount && elevatorFloors[column] === maxFloor - row)
                return (
                    <Elevator
                        key={column}
                        maxFloor={maxFloor}
                        floor={elevatorFloors[column]}
                        destinations={elevatorDestinations[column]}
                        onNewDestination={(destination)=>addDestination(column, destination)}
                        direction={elevatorDirections[column]}
                    />
                );
            if (column === elevatorCount) return <RequestControls></RequestControls>
            return <div></div>
        })
    )

    return (
        <div className="App">
            <Grid elements={gridElements}/>
        </div>
    );
}


export default App;
