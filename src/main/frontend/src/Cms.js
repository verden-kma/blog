import React from 'react';
import store from 'store'
import {Redirect} from "react-router-dom";
import {isLoggedIn} from "./App";

// import Header from "./header/Header.js"
// import Digest from "./digest/Digest.js"
// import Footer from "./footer/Footer.js"

class Cms extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            history: props.history
        }
        this.handleLogout = this.handleLogout.bind(this)
    }

    handleLogout(history) {
        store.remove('loggedIn');
        history.push('/login');
    }

    render() {
        if (!isLoggedIn()) {
            return <Redirect to="/login"/>;
        }
        return <div>this is cms
            <br/>
            <button onClick={() => this.handleLogout(this.state.history)}>log out</button>
        </div>
    }
}

export default Cms;