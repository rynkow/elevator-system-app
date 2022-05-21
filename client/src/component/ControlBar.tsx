import "./ControlBar.css"

const ControlBar = (props: {onNextStep: ()=>void}) => {

    return (
        <div>
            <div className="filler">{' '}</div>
            <div className="control-bar">
                <button className="next-step-button" onClick={props.onNextStep}>Next Step</button>
            </div>
        </div>
    );

}

export default ControlBar