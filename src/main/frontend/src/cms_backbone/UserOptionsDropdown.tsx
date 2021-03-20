import React from 'react';
import store from "store"
import NavDropdown from "react-bootstrap/NavDropdown";
import {handleLogOut} from "../auth/Login";


interface IProps {
    refreshAction(): void
}

class UserOptionsDropdown extends React.Component<IProps, any> {
    constructor(props: IProps) {
        super(props);

        this.doLogout = this.doLogout.bind(this);
    }

    doLogout() {
        handleLogOut();
        this.props.refreshAction();
    }

    render() {
        return (
            <NavDropdown title={store.get("username")} id="user-options-navbar">
                <NavDropdown.Item>Edit profile</NavDropdown.Item>
                <NavDropdown.Divider/>
                <NavDropdown.Item onClick={this.doLogout}>Log out</NavDropdown.Item>
            </NavDropdown>
        );
    }
}

export default UserOptionsDropdown;