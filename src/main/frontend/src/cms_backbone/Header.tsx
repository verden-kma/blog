import React from "react";
import {Link, withRouter} from "react-router-dom";
import Search from "./Search";
import {IAuthProps} from "./CMSNavbarRouting";
import {RouteComponentProps} from "react-router";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";
import UserOptionsDropdown from "./UserOptionsDropdown";

interface IProps extends IAuthProps, RouteComponentProps<any> {
    loginCallback(): void
}

interface IState { // todo: query user data
    userAva: string,
    userPageBanner: string,
    status: string,
    description: string
}

class Header extends React.Component<IProps, IState> {
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
                    <Nav.Link><UserOptionsDropdown refreshAction={() => this.props.loginCallback()}/></Nav.Link>

                </Nav>
            </Navbar.Collapse>
            {/*<ul className={"navbar-nav ml-auto"}>*/}
            {/*    <li className={"nav-item"}><Link to={"/digest"}>{Sprout()}</Link></li>*/}
            {/*    <br/>*/}
            {/*    <li className={"nav-item"}><Link to={"/publishers"}>{Publishers()}</Link></li>*/}
            {/*    <br/>*/}
            {/*    <li className={"nav-item"}><Link to={"/records"}>{Records()}</Link></li>*/}
            {/*    <br/>*/}
            {/*    <li className={"nav-item"}>*/}
            {/*        <Search {...this.props} searchCallback={(searchData) => {console.log(searchData)}}/>*/}
            {/*    </li>*/}
            {/*    <br/>*/}
            {/*    <li className={"nav-item"}><Link to={"/post-record"}>{NewRecord()}</Link></li>*/}
            {/*    <br/>*/}
            {/*    <li className={"nav-item"}><Link to={`/profile/${this.props.username}`}>{Profile()}</Link></li>*/}
            {/*    <br/>*/}
            {/*    <li className={"nav-item"}>*/}
            {/*        <button onClick={() => {*/}
            {/*            handleLogOut();*/}
            {/*            this.props.loginCallback()*/}
            {/*        }}>log out*/}
            {/*        </button>*/}
            {/*    </li>*/}
            {/*</ul>*/}
        </Navbar>)
    }
}

export default withRouter(Header);