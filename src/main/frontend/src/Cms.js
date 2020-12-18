import React from 'react';
import store from 'store'
import {BrowserRouter, Redirect} from "react-router-dom";
import {isLoggedIn} from "./App";
import Header from "./header/Header";
import Footer from "./footer/Footer";

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
        console.log(store.get('username'))
        console.log(store.get('authType'))
        console.log(store.get('token'))

        const userData = {
            username: store.get('username'),
            authType: store.get('authType'),
            token: store.get('token')
        }

        return <div>
            <BrowserRouter>
                <Header key={userData.username} userData={userData}/>
            </BrowserRouter>
            <Footer/>
            {/*<br/>*/}
            {/*<button onClick={() => this.handleLogout(this.state.history)}>log out</button>*/}
        </div>
    }
}

export default Cms;