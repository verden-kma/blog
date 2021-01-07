import React from "react";
import axios from "axios";
import {withRouter} from "react-router-dom"

class Registration extends React.Component<any, any> {
    constructor(props: any) {
        super(props);
        this.state = {
            username: "",
            password: "",
            passwordConfirm: "",
            status: "",
            description: ""
        }
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.switchToLogin = this.switchToLogin.bind(this);
    }


    handleSubmit(event: React.FormEvent) {
        event.preventDefault();
        if (this.state.password !== this.state.passwordConfirm) {
            alert("error: passwords do not match!");
            return;
        }
        axios.post("http://localhost:8080/users", {
            username: this.state.username,
            password: this.state.password,
            status: this.state.status,
            description: this.state.description
        }).then((response) => {
            alert("registration is successful")
        }, (error) => {
            alert("failed to register " + (error.response && error.response.status))
        })
    }

    handleChange(event: React.ChangeEvent<HTMLInputElement>) {
        const {name, value} = event.target;
        this.setState({[name]: value});
    }

    switchToLogin() {
        this.props.history.push("/login");
    }

    render() {
        return (
            <div>
                <button onClick={this.switchToLogin}>Already have an account</button>
                <form onSubmit={this.handleSubmit}>
                    <input type={"text"}
                           name={"username"}
                           placeholder={"username"}
                           onChange={this.handleChange}
                           required/>
                    <br/>
                    <input type={"text"}
                           name={"status"}
                           placeholder={"status"}
                           onChange={this.handleChange}/>
                    <br/>
                    <input type={"password"}
                           name={"password"}
                           placeholder={"password"}
                           onChange={this.handleChange}
                           required/>
                    <br/>
                    <input type={"password"}
                           name={"passwordConfirm"}
                           placeholder={"repeat password"}
                           onChange={this.handleChange}
                           required/>
                    <br/>
                    <button>Register</button>
                </form>
            </div>
        );
    }
}

export default withRouter(Registration);