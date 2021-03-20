import React from "react";
import {Link, RouteComponentProps, withRouter} from "react-router-dom";
import searchIcon from "./../assets/icon-search.png";

type SearchMode = string
const searchModes: Array<SearchMode> = ["record", "publisher"];

interface IProps extends RouteComponentProps<any> {
    searchCallback(searchData: ISearchData): void
}

interface ISearchData {
    query: string,
    mode: SearchMode
}

class Search extends React.Component<IProps, ISearchData> {
    constructor(props: IProps) {
        super(props);
        this.state = {
            query: "",
            mode: searchModes[0]
        }
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event: React.ChangeEvent<HTMLInputElement>) {
        const {value, name} = event.target
        this.setState((oldState: ISearchData) => {
            return {...oldState, [name]: value}
        })
    }

    handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault();
        this.props.searchCallback({...this.state}) // copy
    }

    render() {
        return (<div>
            <form onSubmit={this.handleSubmit}>
                <input type={"text"}
                       name={"query"}
                       value={this.state.query}
                       onChange={this.handleChange}/>
                <div>
                    <input type={"radio"}
                           name={"mode"}
                           value={searchModes[0]}
                           checked={this.state.mode === searchModes[0]}
                           onChange={this.handleChange}/>
                    Records
                </div>
                <div>
                    <input type={"radio"}
                           name={"mode"}
                           value={searchModes[1]}
                           checked={this.state.mode === searchModes[1]}
                           onChange={this.handleChange}/>
                    Publishers
                </div>
                <Link to={`/search/${this.state.mode}?query=${this.state.query}`}>
                    <button><img src={searchIcon} alt={"searchIcon"}/></button>
                </Link>
            </form>
        </div>)
    }
}

export {searchModes};
export default withRouter(Search);