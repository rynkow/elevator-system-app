import React, {useEffect, useState} from 'react';
import './App.css';
import Grid from "./component/Grid";
import {ElevatorSystemState} from "./interface/elevatorSystemState.interface";
import FetcherService from "./service/fetcherService";

const App = () => {
    const [maxFloor, setMaxFloor] = useState(0);
    const [elevatorCount, setElevatorCount] = useState(0);
    const [elevatorFloors, setElevatorFloors] = useState<number[]>([]);
    const [elevatorDirections, setElevatorDirections] = useState<number[]>([]);
    const [elevatorDestinations, setElevatorDestinations] = useState<number[][]>(new Array([]));
    const [upRequests, setUpRequests] = useState<number[]>([]);
    const [downRequests, setDownRequests] = useState<number[]>([]);

    const setState = (state: ElevatorSystemState) => {
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

    const gridElements: JSX.Element[][] = Array.from(Array(maxFloor).keys()).map((i) =>
        Array.from(Array(elevatorCount + 1).keys()).map((j) =>
            <div className="element" key={`${i}_${j}`}> {`${i}_${j}`} </div>
        )
    )

    return (
        <div className="App">
            <Grid elements={gridElements}/>
        </div>
    );
}


export default App;
