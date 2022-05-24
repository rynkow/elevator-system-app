import "./ElevatorButton.css"

const ElevatorButton = (props: { children: JSX.Element, onClick: () => void, disabled: boolean, isSet: boolean, isCurrentFloor: boolean}) => {

    return (
        <button
            className={`text elevator-button ${props.isSet ? "set" : ""} ${props.disabled ? "inactive" : ""} ${props.isCurrentFloor?"current-floor" : ""}`}
            onClick={props.onClick}
            disabled={props.disabled}
        >
            {props.children}
        </button>
    );
}

export default ElevatorButton;