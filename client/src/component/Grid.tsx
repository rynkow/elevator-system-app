import "./Grid.css"

const Grid = (props:{elements: JSX.Element[][]}) => {

    return (
        <div className="grid">
            {props.elements.map((row, row_index)=>
                <div className="grid-row" key={row_index}>
                    {row.map((element, column_index)=>
                        <div className={`grid-cell ${column_index===row.length-1?"last-column-cell":""}`} key={column_index}>
                            {element}
                        </div>
                    )}
                </div>
            )}
        </div>
    );

}

export default Grid;