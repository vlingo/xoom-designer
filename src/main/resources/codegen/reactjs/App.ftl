import React from 'react';
import {BrowserRouter, Switch, Route} from "react-router-dom";
import axios from "axios";
import Sidebar from './components/Sidebar';
import Header from "./components/Header";
import Home from "./components/Home";

<#list aggregates as aggregate>
  <#assign aggPlural=fns.makePlural("${aggregate.aggregateName}") />
  <#assign aggPluralDecap="${fns.decapitalize(aggPlural)}" />
import ${aggPlural} from "./components/${aggPluralDecap}/${aggPlural}";
import ${aggregate.aggregateName} from "./components/${aggPluralDecap}/${aggregate.aggregateName}";
</#list>

axios.defaults.baseURL = 'http://localhost:${turboSettings.httpServerPort?c}';

<#macro aggregatesRoutes aggregate>
  <#assign pluralCapitalized=fns.makePlural("${fns.capitalize(aggregate.aggregateName)}") />
  <#assign capitalized="${fns.capitalize(aggregate.aggregateName)}" />
  <#if aggregate.stateFields?filter(field -> field.isCompositeId)?has_content>
            <Route path="/app${aggregate.apiRootPath?replace("{", ":")?replace("}", "")}" exact={true}><${pluralCapitalized} /></Route>
            <Route path="/app${aggregate.apiRootPath?replace("{", ":")?replace("}", "")}/:id" exact={true}><${capitalized} /></Route>
  <#else>
            <Route path="/app${aggregate.apiRootPath}" exact={true}><${pluralCapitalized} /></Route>
            <Route path="/app${aggregate.apiRootPath}/:id" exact={true}><${capitalized} /></Route>
  </#if>
</#macro>
function App() {
  return (
    <BrowserRouter>
      <header className="navbar navbar-dark sticky-top bg-dark flex-md-nowrap p-0 shadow">
        <Header />
      </header>
      <div className={'container-fluid'}>
        <div className={'row'}>
          <nav className={'col-md-3 col-lg-2 d-md-block bg-light sidebar collapse'}>
            <Sidebar />
          </nav>
          <main className={'col-md-9 ms-sm-auto col-lg-10 px-md-4'}>
          <Switch>
            <Route path="/" exact={true}><Home /></Route>
            <Route path="/app" exact={true}><Home /></Route>
            <Route path="/app/" exact={true}><Home /></Route>
            <#list aggregates as aggregate>
              <@aggregatesRoutes aggregate/>
            </#list>
          </Switch>
          </main>
        </div>
      </div>
    </BrowserRouter>
  );
}

export default App;
