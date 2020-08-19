import * as React from "react";
import "./headerStyles.css"

function Search() {
    return (
        <form className={"search-block"}>
            <input type={"text"} placeholder={"search"}/>
            <input type={"submit"} name={"submit"}/>
        </form>

    )
}

export default Search;