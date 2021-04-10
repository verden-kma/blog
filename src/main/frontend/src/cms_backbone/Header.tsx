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
    constructor(props: IProps) {
        super(props);
    }

    render() {
        return (<Navbar expand="lg">
            <Navbar.Brand><Link to={"/digest"}>Sprout</Link></Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav"/>
            <Navbar.Collapse>
                <Nav className="mr-auto">
                    <Nav.Link><Link to={"/publishers"}>Publishers</Link></Nav.Link>
                    <Nav.Link><Link to={"/records"}>Records</Link></Nav.Link>
                    <Nav.Link><Link to={"/post-record"}>NewRecord</Link></Nav.Link>
                    <Search {...this.props}/>
                    <Nav.Link><UserOptionsDropdown username={this.props.username}/></Nav.Link>
                </Nav>
            </Navbar.Collapse>
        </Navbar>)
    }
}

export default withRouter(Header);