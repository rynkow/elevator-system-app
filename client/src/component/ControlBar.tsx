import "./ControlBar.css"
import {useState} from "react";

const ControlBar = (props: { onNextStep: () => void, onNewParameters: (maxFloor: number, elevatorCount: number) => void }) => {
    const [maxFloor, setMaxFloor] = useState(1)
    const [elevatorCount, setElevatorCount] = useState(1)

    return (
        <div>
            <div className="filler">{' '}</div>
            <div className="control-bar">
                <button className="control-button" onClick={props.onNextStep}>Next Step</button>
                <div className="v-roll"/>
                <p>Change System Parameters:</p>
                <div className="inputs">
                    <label>
                        Maximum Floor
                        <br/>
                        <input type="number" min="1" max="20" value={maxFloor}
                               onChange={e => setMaxFloor(parseInt(e.target.value))}/>
                    </label>
                    <label>
                        Number of Elevators
                        <br/>
                        <input type="number" min="1" max="16" value={elevatorCount}
                               onChange={e => setElevatorCount(parseInt(e.target.value))}/>
                    </label>
                    <button className="control-button"
                            onClick={() => props.onNewParameters(maxFloor, elevatorCount)}>Submit
                    </button>
                </div>

            </div>
        </div>
    );

}

export default ControlBar