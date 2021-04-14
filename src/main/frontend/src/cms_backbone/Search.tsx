import React from "react";
import {Link, RouteComponentProps, withRouter} from "react-router-dom";
import searchIcon from "./../assets/icon-search.png";
import Form from "react-bootstrap/Form";
import {Button, FormControl} from "react-bootstrap";

type SearchMode = string
const searchModes: Array<SearchMode> = ["record", "publisher"];

interface ISearchData {
    query: string,
    mode: SearchMode
}

class Search extends React.Component<RouteComponentProps<any>, ISearchData> {
    constructor(props: RouteComponentProps<any>) {
        super(props);
        this.state = {
            query: "",
            mode: searchModes[0]
        }
        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(event: React.ChangeEvent<HTMLInputElement>) {
        const {value, name} = event.target
        this.setState((oldState: ISearchData) => {
            return {...oldState, [name]: value}
        })
    }

    render() {
        return (
            <Form inline onSubmit={event => event.preventDefault()}>
                <FormControl type={"text"}
                             name={"query"}
                             value={this.state.query}
                             onChange={this.handleChange}
                             className="mr-sm-2"/>
                <div>
                    <FormControl type={"radio"}
                                 name={"mode"}
                                 value={searchModes[0]}
                                 checked={this.state.mode === searchModes[0]}
                                 onChange={this.handleChange}/>
                    Records
                </div>
                <div>
                    <FormControl type={"radio"}
                                 name={"mode"}
                                 value={searchModes[1]}
                                 checked={this.state.mode === searchModes[1]}
                                 onChange={this.handleChange}/>
                    Publishers
                </div>

                <Link to={`/search/${this.state.mode}?query=${this.state.query}`}>
                    <Button type={"submit"}>
                        <img src={searchIcon} alt={"searchIcon"}/>
                    </Button>
                </Link>
            </Form>
        )
    }
}

export {searchModes};
export default withRouter(Search);