import "./RequestControls.css"

const RequestControls = (props:{onNewRequest: (direction: number)=>void, floor: number, maxFloor:number, idleElevatorOnFloor: boolean, upActive:boolean, downActive: boolean}) => {

    return (
        <div className="request-controls-container">
            <div className="text">Floor {props.floor}</div>
            <button
                className={`request-button ${props.upActive?"active":""}`}
                disabled={props.idleElevatorOnFloor || props.upActive}
                hidden={props.floor === props.maxFloor}
                onClick={()=>props.onNewRequest(1)}
            >
                U
            </button>
            <button
                className={`request-button ${props.downActive?"active":""}`}
                disabled={props.idleElevatorOnFloor || props.downActive}
                hidden={props.floor === 0}
                onClick={()=>props.onNewRequest(-1)}
            >
                D
            </button>
        </div>
    );
}

export default RequestControls;