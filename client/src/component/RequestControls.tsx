import "./RequestControls.css"

const RequestControls = (props:{onNewRequest: (direction: number)=>void, upHidden: boolean, downHidden: boolean, idleElevatorOnFloor: boolean, upActive:boolean, downActive: boolean}) => {

    return (
        <div className="request-controls-container">
            <button
                className={`request-button ${props.upActive?"active":""}`}
                disabled={props.idleElevatorOnFloor || props.upActive}
                hidden={props.upHidden}
                onClick={()=>props.onNewRequest(1)}
            >
                U
            </button>
            <button
                className={`request-button ${props.downActive?"active":""}`}
                disabled={props.idleElevatorOnFloor || props.downActive}
                hidden={props.downHidden}
                onClick={()=>props.onNewRequest(-1)}
            >
                D
            </button>
        </div>
    );
}

export default RequestControls;