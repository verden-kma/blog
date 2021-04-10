import React from "react";
import {withRouter} from "react-router-dom";
import axios from "axios";
import store from "store";

interface IState {
    username: string,
    password: string
}

class Login extends React.Component<any, IState> {
    constructor(props: any) {
        super(props);
        this.state = {
            username: "",
            password: ""
        }
        this.handleChange = this.handleChange.bind(this);
        this.handleLogin = this.handleLogin.bind(this);
        this.switchToRegister = this.switchToRegister.bind(this);
    }

    handleChange(event: React.ChangeEvent<HTMLInputElement>) {
        const {name, value} = event.target;
        this.setState(current => ({...current, [name]: value}));
    }

    handleLogin(event: React.ChangeEvent<HTMLInputElement>) {
        event.preventDefault();
        axios.post("http://localhost:8080/login", {
            username: this.state.username,
            password: this.state.password
        }).then((response) => {
            console.log("logged in");
            store.set('username', this.state.username)
            store.set('token', response.data)
            store.set('isAuthorized', true);

            let {from} = this.props.location.state || {from: {pathname: "/"}};
            this.props.history.replace(from);
        }, (error) => {
            alert("wrong credentials ")
            console.log(error)
        })
    }

    switchToRegister() {
        this.props.history.push("/register");
    }

    render() {
        return (
            <div onSubmit={this.handleLogin}>
                <button onClick={this.switchToRegister}>Create account</button>
                <form>
                    <input type={"text"}
                           name={"username"}
                           value={this.state.username}
                           placeholder={"username"}
                           onChange={this.handleChange}/>
                    <br/>
                    <input type={"password"}
                           name={"password"}
                           value={this.state.password}
                           placeholder={"password"}
                           onChange={this.handleChange}/>
                    <br/>
                    <button>Log in</button>
                </form>
            </div>
        )
    }
}


export default withRouter(Login);