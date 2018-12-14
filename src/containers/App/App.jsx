import React from 'react';
import { BrowserRouter, Switch, Route, NavLink } from 'react-router-dom';
import routes from './routes';

import Layout from '../Layout/Layout';
import DashBoard from '../DashBoard/DashBoard';
import ManagerBook from '../ManagerBook/ManagerBook';
import AddBook from '../ManagerBook/AddBook/AddBook';
import AddBookUI from '../ManagerBook/AddBook/AddBookUI';
import EditBook from '../ManagerBook/EditBook/EditBook';
import Login from '../Login/Login';
import Register from '../Register/Register';
import ManagerAuthor from '../ManagerAuthor/ManagerAuthor';
import ManagerCategory from '../ManagerCategory/ManagerCategory';
import Statistical from '../Statistical/Statistical';
import UsersRoles from '../UsersAndRoles/UsersRoles';
import AddUser from '../UsersAndRoles/AddUser';
import DetailUser from '../UsersAndRoles/DetailUser';
export default function App() {
  return (
    <BrowserRouter>
      <Switch>
        <Route exact path={routes.login} component={Login} />
        <Route exact path={routes.register} component={Register} />
        <Layout>
          <Switch>
            <Route path={routes.dashboard} component={DashBoard} />
            <Route exact path={routes.managerBook} component={ManagerBook} />
            <Route path={routes.addbook} component={AddBook} />
            <Route path={routes.addbookui} component={AddBookUI} />
            <Route path={routes.editbook} component={EditBook} />
            <Route
              exact
              path={routes.managerAuthor}
              component={ManagerAuthor}
            />
            <Route
              exact
              path={routes.managerCategory}
              component={ManagerCategory}
            />
            <Route exact path={routes.statistical} component={Statistical} />
            <Route exact path={routes.userAndRole} component={UsersRoles} />
            <Route path={routes.addUser} component={AddUser} />
            <Route path={routes.detailUser} component={DetailUser} />
          </Switch>
        </Layout>
      </Switch>
    </BrowserRouter>
  );
}
