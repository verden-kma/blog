import React from "react";
import Dropzone from "./Dropzone";
import axios from 'axios'

class SetUpRecord extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            ...props.authData,
            caption: "",
            description: "",
            file: null
        }
        console.log("this.state")
        console.log(this.state)

        this.handleChange = this.handleChange.bind(this)
        this.handleSubmit = this.handleSubmit.bind(this)
        this.setFile = this.setFile.bind(this)
    }

    handleChange(event) {
        const {name, value} = event.target
        this.setState({[name]: value})
    }

    handleSubmit(event) {
        event.preventDefault();

        const recordData = {
            caption: this.state.caption,
            adText: this.state.description
        }

        let body = new FormData()
        body.append('image', this.state.file);
        body.append('recordData', new Blob([JSON.stringify(recordData)], {
            type: 'application/json'
        }))

        axios({
            method: 'post',
            url: `http://localhost:8080/users/${this.state.username}/records`,
            data: body,
            headers: {
                'Content-Type': 'multipart/form-data',
                'Authorization': `${this.state.authType} ${this.state.token}`
            }
        }).then(success => alert("success"),
            error => alert("error"))
    }

    setFile(image) {
        this.setState({file: image})
    }

    render() {
        return (
            <div>
                <form onSubmit={this.handleSubmit}>
                    Caption <input type="text"
                                   name="caption"
                                   value={this.state.caption}
                                   onChange={this.handleChange}
                />
                    <br/>

                    <Dropzone setFile={this.setFile}/>

                    <br/>
                    Description <input type="textarea"
                                       name="description"
                                       value={this.state.description}
                                       onChange={this.handleChange}
                />
                    <br/>
                    <button>Upload</button>
                </form>
            </div>
        )
    }

}

export default SetUpRecord;