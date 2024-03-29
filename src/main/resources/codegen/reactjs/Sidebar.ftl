import React from "react";
import {NavLink} from "react-router-dom";

const MenuItem = ({label, link}) => {
  return (
    <li className="nav-item">
      <NavLink className="nav-link" activeClassName="active" aria-current="page" to={link}>
        {label}
      </NavLink>
    </li>
  )
};

const Sidebar = () => {
  return (
    <div className="position-sticky pt-3">
      <ul className="nav flex-column">
        <#list aggregates as aggregate>
        <#if aggregate.stateFields?filter(field -> field.isCompositeId)?has_content>
          <MenuItem label={'${aggregate.aggregateName}'} link={'/app${aggregate.apiRootPath?replace("{", ":")?replace("}", "")}'}/>
        <#else>
          <MenuItem label={'${aggregate.aggregateName}'} link={'/app${aggregate.apiRootPath}'}/>
        </#if>
        </#list>
      </ul>
    </div>
  )
};

export default Sidebar;
