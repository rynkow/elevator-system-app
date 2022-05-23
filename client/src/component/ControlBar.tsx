import "./ControlBar.css"
import {useState} from "react";

const ControlBar = (props: { onNextStep: () => void, onNewParameters: (maxFloor: number, elevatorCount: number) => void }) => {
    const [maxFloor, setMaxFloor] = useState<number>()
    const [elevatorCount, setElevatorCount] = useState<number>()
    const [incorrectInput, setIncorrectInput] = useState(false);

    const onSubmit = () => {
        if (maxFloor === undefined || elevatorCount === undefined) return;
        let correct = true
        if (maxFloor > 20 || maxFloor < 0) correct = false;
        if (elevatorCount > 16 || elevatorCount < 0) correct = false;
        setIncorrectInput(!correct);
        if (correct)
            props.onNewParameters(maxFloor, elevatorCount);
    }

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
                        <input placeholder="max 20" type="number" value={maxFloor}
                               onChange={e =>setMaxFloor(parseInt(e.target.value))}/>
                    </label>
                    <label>
                        Number of Elevators
                        <br/>
                        <input placeholder="max 16" type="number" value={elevatorCount}
                               onChange={e => setElevatorCount(parseInt(e.target.value))}/>
                    </label>
                    <button className="control-button"
                            onClick={onSubmit}>Submit
                    </button>
                </div>
                <p hidden={!incorrectInput}>Please enter correct values</p>

            </div>
        </div>
    );

}

export default ControlBar