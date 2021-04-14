import React from "react";
import {Link, withRouter} from "react-router-dom";
import Search from "./Search";
import {RouteComponentProps} from "react-router";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";
import UserOptionsDropdown from "./UserOptionsDropdown";

interface IProps extends RouteComponentProps<any> {
    username: string
}

class Header extends React.Component<IProps, any> {
    render() {
        return (
            <header>
                <Navbar expand="lg">
                    <Navbar.Brand><Link to={"/digest"}>Sprout</Link></Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                    <Navbar.Collapse>
                        <Nav className="mr-auto">
                            <Nav.Link href={"/publishers"}>Publishers</Nav.Link>
                            <Nav.Link href={"/records"}>Records</Nav.Link>
                            <Nav.Link href={"/post-record"}>NewRecord</Nav.Link>
                            <Search/>
                            <UserOptionsDropdown username={this.props.username}/>
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
            </header>)
    }
}

export default withRouter(Header);