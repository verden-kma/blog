import React from 'react';
import NavDropdown from "react-bootstrap/NavDropdown";
import store from "store"
import {Link} from "react-router-dom";
import axios from "axios";
import {IAuthProps} from "./CMSNavbarRouting";

class UserOptionsDropdown extends React.Component<any, IAuthProps> {
    constructor(props: any) {
        super(props);
        this.state = {
            username: store.get("username"),
            token: store.get("token")
        }
        this.doLogout = this.doLogout.bind(this);
    }

    doLogout() {
        store.clearAll();
        window.location.reload();
        axios.post("http://localhost:8080/logout", {}, {
            headers: {'Authorization': `Bearer ${this.state.token}`}
        }).then(() => console.log("logged out on server"), (error) => console.log("failed to log out" + error.data));
    }

    render() {
        return (
            <NavDropdown title={this.state.username} id="user-options-navbar">
                <NavDropdown.Item><Link to={`/profile/${this.state.username}`}>Profile</Link></NavDropdown.Item>
                <NavDropdown.Item><Link to={"/edit-user-details"}>Edit profile</Link></NavDropdown.Item>
                <NavDropdown.Item><Link to={"/change-password"}>Change password</Link></NavDropdown.Item>
                <NavDropdown.Divider/>
                <NavDropdown.Item onClick={this.doLogout}>Log out</NavDropdown.Item>
            </NavDropdown>
        );
    }
}

export default UserOptionsDropdown;