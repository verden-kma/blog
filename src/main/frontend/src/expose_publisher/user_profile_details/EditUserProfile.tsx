import React from 'react';
import {IAuthProps} from "../../cms_backbone/CMSNavbarRouting";
import {Button, Form, FormCheck, FormControl, FormFile, FormGroup, FormLabel} from "react-bootstrap";
import axios from "axios";
import {Redirect} from "react-router";

interface IState {
    oldStatus?: string,
    oldDescription?: string,

    newStatus: string,
    newDescription: string,

    newAvatar?: File,
    newBanner?: File,

    avatarUpdateMode: string,
    bannerUpdateMode: string,

    hasEdited: boolean
}

class EditUserProfile extends React.Component<IAuthProps, IState> {
    constructor(props: IAuthProps) {
        super(props);
        this.state = {
            newStatus: "",
            newDescription: "",
            avatarUpdateMode: "update",
            bannerUpdateMode: "update",
            hasEdited: false
        }
        this.handleChange = this.handleChange.bind(this);
        this.resetOld = this.resetOld.bind(this);
        this.handleUpdate = this.handleUpdate.bind(this);
        this.checkImageValid = this.checkImageValid.bind(this);
        this.updateImage = this.updateImage.bind(this);
    }

    componentDidMount() {
        axios.get(`http://localhost:8080/users/${this.props.username}`, {
            headers: {'Authorization': `Bearer ${this.props.token}`}
        }).then(success => {
            this.setState({
                oldStatus: success.data.status,
                newStatus: success.data.status,
                oldDescription: success.data.description,
                newDescription: success.data.description
            });
        }, error => console.log(error))
    }

    resetOld(property: string) {
        let newProp: string, oldVal: string;
        if (property === "status") {
            newProp = "newStatus";
            oldVal = this.state.oldStatus || "";
        } else if (property === "description") {
            newProp = "newDescription";
            oldVal = this.state.oldDescription || "";
        }

        this.setState(oldState => ({...oldState, [newProp]: oldVal}));
    }

    handleChange(event: React.ChangeEvent<any>) {
        const {name, value, type} = event.target;
        if (type === "file") this.setState(oldState => ({...oldState, [name]: event.target.files[0]}));
        else this.setState(oldState => ({...oldState, [name]: value}));
    }

    checkImageValid(imageField: string): boolean {
        if (imageField === "newAvatar" && this.state.newAvatar) {
            const maybeGoodImage: File = this.state.newAvatar;
            const fileType: string = maybeGoodImage.type;
            console.log(fileType)
        }
        // todo: real validation
        return true;
    }

    handleUpdate(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault();
        let updatePromises: Array<Promise<any> | undefined> = [];

        let textUpdate: any = {};
        if (this.state.newStatus !== this.state.oldStatus) {
            textUpdate.status = this.state.newStatus;
        }
        if (this.state.newDescription !== this.state.oldDescription) {
            textUpdate.description = this.state.newDescription;
        }
        if (Object.keys(textUpdate).length > 0) {
            const textPromise: Promise<any> =
                axios.patch(`http://localhost:8080/users/details`, textUpdate, {
                    headers: {'Authorization': `Bearer ${this.props.token}`}
                }).then(() => {
                }, error => console.log(error));
            updatePromises.push(textPromise);
        }

        const avatarPromise: Promise<any> | undefined = this.updateImage(this.state.avatarUpdateMode, 'avatar', this.state.newAvatar);
        const bannerPromise: Promise<any> | undefined = this.updateImage(this.state.bannerUpdateMode, 'top-banner', this.state.newBanner);

        updatePromises.push(avatarPromise);
        updatePromises.push(bannerPromise);

        Promise.all(updatePromises).then(() => this.setState(oldState => ({...oldState, hasEdited: true})));
    }

    updateImage(mode: string, urlSuffix: string, newImage?: File): Promise<any> | undefined {
        if (mode === "delete") {
            return axios.delete(`http://localhost:8080/users/${urlSuffix}`,
                {
                    headers: {'Authorization': `Bearer ${this.props.token}`}
                }).then(() => {
            }, error => console.log(error))
        } else if (mode === "update" && newImage !== undefined) {
            let body = new FormData();
            body.append("image", newImage);
            return axios.put(`http://localhost:8080/users/${urlSuffix}`, body, {
                headers: {'Authorization': `Bearer ${this.props.token}`}
            }).then(() => {
            }, error => console.log(error))
        }
    }

    render() {
        // todo: add clear button (for files), restore current
        if (this.state.hasEdited) {
            return <Redirect to={`/profile/${this.props.username}`}/>
        }
        return (
            <div>
                <Form onSubmit={this.handleUpdate}>
                    <FormGroup>
                        <FormLabel>Edit status:</FormLabel>
                        <FormControl name={"newStatus"} type={"text"} size={"lg"} value={this.state.newStatus}
                                     onChange={this.handleChange}/>
                        <Button onClick={this.resetOld.bind(this, "status")}>Restore current</Button>
                    </FormGroup>
                    <FormGroup>
                        <FormLabel>Edit description:</FormLabel>
                        <FormControl name={"newDescription"} type={"text"} size={"sm"} value={this.state.newDescription}
                                     onChange={this.handleChange}/>
                        <Button onClick={this.resetOld.bind(this, "description")}>Restore current</Button>
                    </FormGroup>

                    <FormGroup>
                        <FormGroup>
                            <FormCheck name={"avatarUpdateMode"} type={"radio"} label={"Update"} value={"update"}
                                       checked={this.state.avatarUpdateMode === "update"} onChange={this.handleChange}/>
                            <FormFile name={"newAvatar"} label={"New avatar:"}
                                      disabled={this.state.avatarUpdateMode !== "update"}
                                      isValid={this.checkImageValid("newAvatar")}
                                      onChange={this.handleChange}/>
                        </FormGroup>
                        <FormGroup>
                            <FormCheck name={"avatarUpdateMode"} type={"radio"} label={"Delete"} value={"delete"}
                                       checked={this.state.avatarUpdateMode === "delete"} onChange={this.handleChange}/>
                        </FormGroup>
                    </FormGroup>

                    <FormGroup>
                        <FormGroup>
                            <FormCheck name={"bannerUpdateMode"} type={"radio"} label={"Update"} value={"update"}
                                       checked={this.state.bannerUpdateMode === "update"} onChange={this.handleChange}/>
                            <FormFile name={"newBanner"} label={"New banner:"}
                                      disabled={this.state.bannerUpdateMode !== "update"}
                                      isValid={this.checkImageValid("newBanner")}
                                      onChange={this.handleChange}/>
                        </FormGroup>
                        <FormGroup>
                            <FormCheck name={"bannerUpdateMode"} type={"radio"} label={"Delete"} value={"delete"}
                                       checked={this.state.bannerUpdateMode === "delete"} onChange={this.handleChange}/>
                        </FormGroup>
                    </FormGroup>

                    <Button type={"submit"}>Edit my profile</Button>
                </Form>
            </div>
        );
    }
}

export default EditUserProfile;