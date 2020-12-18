import React from "react";
import "./headerStyles.css"

function Search() {
    return (
        <form className={"search-block"}>
            <input type={"text"} placeholder={"search"}/>
            <input type={"submit"} name={"submit"} onClick={handleSubmission}/>
        </form>

    )
}

function handleSubmission(event) {
    event.preventDefault();
    alert("checked")
}

export default Search;