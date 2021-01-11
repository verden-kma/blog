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
        axios.post("http://localhost:8080/users/login", {
            username: this.state.username,
            password: this.state.password
        }).then((response) => {
            alert("logged in");
            store.set('username', this.state.username)
            store.set('authType', response.data.authType)
            store.set('token', response.data.token)
            this.props.history.push('/');
        }, (error) => {
            alert("wrong credentials " + (error.response && error.response.status))
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
                    <button>Log in</button>
                </form>
            </div>
        )
    }
}

export default withRouter(Login);