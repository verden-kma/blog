import * as React from "react";
import {SyntheticEvent} from "react";
import "./headerStyles.css"

function Search() {
    return (
        <form className={"search-block"}>
            <input type={"text"} placeholder={"search"}/>
            <input type={"submit"} name={"submit"} onClick={handleSubmission}/>
        </form>

    )
}

function handleSubmission(event: SyntheticEvent) {
    event.preventDefault();
    alert("check")
}

export default Search;