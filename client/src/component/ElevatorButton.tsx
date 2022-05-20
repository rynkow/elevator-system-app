import "./ElevatorButton.css"

const ElevatorButton = (props: {children: JSX.Element, onClick: ()=>void, disabled: boolean, isSet: boolean}) => {

    return (
        <button
            className={`elevator-button ${props.isSet?"set":""} ${props.disabled?"inactive":""}`}
            onClick={props.onClick}
            disabled={props.disabled || props.isSet}
        >
            {props.children}
        </button>
    );
}

export default ElevatorButton;