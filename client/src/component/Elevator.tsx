import "./Elevator.css"
import ElevatorButton from "./ElevatorButton";

const Elevator = (props:{maxFloor: number, floor: number, destinations: number[], onNewDestination: (floor:number)=>void}) => {

    const elevatorButtons : JSX.Element[] = []
    for (let i = 0; i <= props.maxFloor; i++) {
        elevatorButtons.push(
            <ElevatorButton onClick={()=>{props.onNewDestination(i)}} disabled={i===props.floor} isSet={props.destinations.includes(i)}>
                <div>{i}</div>
            </ElevatorButton>
        );
    }

    return (
        <div className="elevator">
            <div className="direction">{}</div>
            {elevatorButtons}
        </div>
    );
}

export default Elevator;