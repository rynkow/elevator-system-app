import React, {useState} from 'react';
import './App.css';
import Grid from "./component/Grid";

const App = () => {
    const [maxFloor, setMaxFloor] = useState(0);
    const [elevatorCount, setElevatorCount] = useState(0);
    const [elevatorFloors, setElevatorFloors] = useState<number[]>([]);
    const [elevatorDirections, setElevatorDirections] = useState<number[]>([]);
    const [elevatorDestinations, setElevatorDestinations] = useState<number[][]>(new Array([]));
    const [upRequests, setUpRequests] = useState<number[]>([]);
    const [downRequests, setDownRequests] = useState<number[]>([]);



    const gridElements: JSX.Element[][] = Array.from(Array(5).keys()).map((i) =>
        Array.from(Array(3).keys()).map((j) =>
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
