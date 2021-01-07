import React from "react";


interface IProps {
    prompt: string,

    handleFile(image: React.ChangeEvent<HTMLInputElement>): void
}

function Dropzone(props: IProps) {
    return (
        <div className="container">
            <div className="row">
                <div className="col-md-6">
                    <div className="form-group files color">
                        <label>{props.prompt}</label>
                        <input required
                               type="file"
                               className="form-control"
                               name="file"
                            // accept=".jpg, .jpeg, .png"
                               onChange={props.handleFile}/>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Dropzone;