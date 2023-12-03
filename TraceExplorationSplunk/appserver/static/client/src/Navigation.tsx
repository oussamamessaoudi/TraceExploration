import React from "react";

export function Navigation() {
    return (
        <nav className="navbar navbar-dark bg-dark">
            <div className="container-fluid">
                <a className="navbar-brand">Trace Explorer</a>
                <form className="d-flex" role="search">
                    <input className="form-control me-2" type="search" placeholder="Trace ID" aria-label="Search"/>
                    <button className="btn btn-outline-success" type="submit">Search</button>
                </form>
            </div>
        </nav>
    );
}