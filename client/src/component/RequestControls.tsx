import "./RequestControls.css"

const RequestControls = (props:{onNewRequest: (direction: number)=>void, floor: number, maxFloor:number, upDisabled: boolean, downDisabled: boolean, upActive:boolean, downActive: boolean}) => {

    return (
        <div className="request-controls-container">
            <div className="text">Floor {props.floor}</div>
            <button
                className={`request-button ${props.upActive?"active":""}`}
                disabled={props.upDisabled || props.upActive}
                hidden={props.floor === props.maxFloor}
                onClick={()=>props.onNewRequest(1)}
            >
                U
            </button>
            <button
                className={`request-button ${props.downActive?"active":""}`}
                disabled={props.downDisabled || props.downActive}
                hidden={props.floor === 0}
                onClick={()=>props.onNewRequest(-1)}
            >
                D
            </button>
        </div>
    );
}

export default RequestControls;