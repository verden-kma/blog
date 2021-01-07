import React from "react";
import Dropzone from "../Dropzone";
import axios from "axios";

interface IProps {
    username: string,
    authType: string,
    token: string
}

interface IState {
    caption: string,
    adText?: string,
    file?: File
}

class PostRecord extends React.Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);
        this.state = {
            caption: "",
            adText: ""
        }
        this.handleChange = this.handleChange.bind(this);
        this.handleFile = this.handleFile.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event: React.ChangeEvent<HTMLInputElement>) {
        const {name, value} = event.target;
        this.setState(current => ({...current, [name]: value}))
    }

    handleFile({target: {files}}: React.ChangeEvent<HTMLInputElement>) {
        if (files === null) return;
        this.setState(current => ({...current, file: files[0]}))
    }

    handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault();

        if (this.state.file === undefined) {
            alert("image should be specified");
            return;
        }

        const recordData = {
            caption: this.state.caption,
            adText: this.state.adText
        }

        let body = new FormData()
        body.append('image', this.state.file);
        body.append('recordData', new Blob([JSON.stringify(recordData)], {
            type: 'application/json'
        }))

        axios({
            method: 'post',
            url: `http://localhost:8080/users/${this.props.username}/records`,
            data: body,
            headers: {
                'Content-Type': 'multipart/form-data',
                'Authorization': `${this.props.authType} ${this.props.token}`
            }
        }).then(success => {
                alert("success")
                this.setState({});
            },
            error => alert("error"))
    }

    render() {
        return (
            <div>
                <form onSubmit={this.handleSubmit}>
                    Caption&nbsp;
                    <input required
                           type={"text"}
                           name={"caption"}
                           value={this.state.caption}
                           onChange={this.handleChange}/>
                    <br/>
                    Description&nbsp;
                    <input type={"text"}
                           name={"adText"}
                           value={this.state.adText}
                           onChange={this.handleChange}/>
                    <br/>
                    <Dropzone prompt={"Record image "} handleFile={this.handleFile}/>
                    <br/>
                    <button>upload</button>
                </form>
            </div>
        )
    }
}

export default PostRecord;