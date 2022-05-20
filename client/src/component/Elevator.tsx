import "./Elevator.css"
import ElevatorButton from "./ElevatorButton";

const Elevator = (props:{maxFloor: number, floor: number, destinations: number[], direction: number, onNewDestination: (floor:number)=>void}) => {

    const elevatorButtons : JSX.Element[] = []
    for (let i = 0; i <= props.maxFloor; i++) {
        const disabled = (props.floor - i) * props.direction > 0 || props.floor === i
        elevatorButtons.push(
            <ElevatorButton onClick={()=>{props.onNewDestination(i)}} disabled={disabled} isSet={props.destinations.includes(i)}>
                <div>{i}</div>
            </ElevatorButton>
        );
    }

    return (
        <div className="elevator">
            <div className="container">{props.direction}</div>
            <div className="container buttons">{elevatorButtons}</div>
        </div>
    );
}

export default Elevator;