import React from "react";

const Header = () => {
  return (
    <>
      <a className="navbar-brand col-md-3 col-lg-2 me-0 px-3" href="/">${fns.capitalizeMultiWord(context.artifactId?replace('-', ' '))}</a>
      <button className="navbar-toggler position-absolute d-md-none collapsed" type="button"
              data-bs-toggle="collapse" data-bs-target="#sidebarMenu" aria-controls="sidebarMenu"
              aria-expanded="false" aria-label="Toggle navigation">
        <span className="navbar-toggler-icon"></span>
      </button>
    </>
  )
};

export default Header;
