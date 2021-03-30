import React from 'react';
import NavDropdown from "react-bootstrap/NavDropdown";
import store from "store"
import {Link} from "react-router-dom";


interface IProps {
    username: string
}

class UserOptionsDropdown extends React.Component<IProps, any> {
    constructor(props: IProps) {
        super(props);

        this.doLogout = this.doLogout.bind(this);
    }

    doLogout() {
        store.clearAll();
        window.location.reload();
    }

    render() {
        return (
            <NavDropdown title={this.props.username} id="user-options-navbar">
                <NavDropdown.Item><Link to={`/profile/${this.props.username}`}>Profile</Link></NavDropdown.Item>
                <NavDropdown.Item><Link to={"/edit-user-details"}>Edit profile</Link></NavDropdown.Item>
                <NavDropdown.Item><Link to={"/change-password"}>Change password</Link></NavDropdown.Item>
                <NavDropdown.Divider/>
                <NavDropdown.Item onClick={this.doLogout}>Log out</NavDropdown.Item>
            </NavDropdown>
        );
    }
}

export default UserOptionsDropdown;