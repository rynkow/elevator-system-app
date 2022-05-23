import "./Elevator.css"
import ElevatorButton from "./ElevatorButton";

const Elevator = (props: { maxFloor: number, floor: number, destinations: number[], direction: number, onNewDestination: (floor: number) => void, isReserved: boolean, isOpen: boolean }) => {

    const elevatorButtons: JSX.Element[] = []
    for (let i = 0; i <= props.maxFloor; i++) {
        const disabled = (props.floor - i) * props.direction > 0 || props.floor === i
        elevatorButtons.push(
            <ElevatorButton key={i} onClick={() => {
                props.onNewDestination(i)
            }} disabled={disabled || props.isReserved} isSet={props.destinations.includes(i)}>
                <div>{i}</div>
            </ElevatorButton>
        );
    }
    let directionMarker;
    if (props.isReserved)
        directionMarker = "reserved-marker";
    else if (props.direction === 1)
        directionMarker = "up-marker";
    else if (props.direction === -1)
        directionMarker = "down-marker"
    else
        directionMarker = "idle-marker"


    return (
        <div className={`elevator ${props.isOpen ? "open" : "closed"} ${props.isReserved ? "reserved" : ""}`}>
            <div className={`text container direction-marker ${directionMarker}`}></div>
            <div className="container buttons">{elevatorButtons}</div>
        </div>
    );
}

export default Elevator;