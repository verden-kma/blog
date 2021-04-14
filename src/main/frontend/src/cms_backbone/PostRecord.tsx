import React from "react";
import axios from "axios";
import {IAuthProps} from "./CMSNavbarRouting";
import {Button, Form, FormControl, FormFile, FormGroup, FormLabel, Modal, ModalTitle, Spinner} from "react-bootstrap";
import ModalHeader from "react-bootstrap/ModalHeader";

interface IState {
    caption: string,
    adText: string,
    file?: File,
    isBeingSent: boolean,
    uploadResult?: boolean,
    selectedFileName: string
}

class PostRecord extends React.Component<IAuthProps, IState> {
    constructor(props: IAuthProps) {
        super(props);
        this.state = {
            caption: "",
            adText: "",
            isBeingSent: false,
            selectedFileName: ""
        }
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event: React.ChangeEvent<HTMLInputElement>) {
        const {name, value, files} = event.target;
        if (files) this.setState({file: files[0]});
        this.setState(current => ({...current, [name]: value}))
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

        this.setState({isBeingSent: true});

        axios({
            method: 'post',
            url: `http://localhost:8080/users/${this.props.username}/records`,
            data: body,
            headers: {
                'Content-Type': 'multipart/form-data',
                'Authorization': `Bearer ${this.props.token}`
            }
        }).then(success => {
                this.setState({
                    caption: "",
                    adText: "",
                    file: undefined,
                    isBeingSent: false,
                    uploadResult: true,
                    selectedFileName: ""
                });
            },
            error => this.setState({
                isBeingSent: false, uploadResult: false
            }))
    }

    render() {
        // todo: validation
        return (
            <div>
                <Modal show={this.state.uploadResult !== undefined}
                       onHide={() => this.setState({uploadResult: undefined})}>
                    <ModalHeader closeButton>
                        <ModalTitle>
                            {this.state.uploadResult ? "Your record is successfully uploaded." : "Failed to upload your record."}
                        </ModalTitle>
                    </ModalHeader>
                </Modal>
                <Form onSubmit={this.handleSubmit}>
                    <FormGroup>
                        <FormLabel>Caption:</FormLabel>
                        <FormControl type="text"
                                     name="caption"
                                     value={this.state.caption}
                                     onChange={this.handleChange}
                                     placeholder="this will be displayed as a title of your record"/>
                    </FormGroup>
                    <FormGroup>
                        <FormLabel>Description:</FormLabel>
                        <FormControl type="text"
                                     name="adText"
                                     value={this.state.adText}
                                     onChange={this.handleChange}
                                     placeholder="what else do you have to tell?"/>
                    </FormGroup>
                    <FormGroup>
                        <FormFile name={"selectedFileName"} value={this.state.selectedFileName}
                                  label="Select an image to upload."
                                  onChange={this.handleChange}/>
                    </FormGroup>
                    {this.state.isBeingSent
                        ? <Spinner animation={"border"}><span className="sr-only">Uploading...</span></Spinner>
                        : <Button type="submit">Upload</Button>}
                </Form>
            </div>
        )
    }
}

export default PostRecord;