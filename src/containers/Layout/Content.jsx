import React from 'react';
import { BrowserRouter, Switch } from 'react-router-dom';

import { ContentWrapper } from './Layout.style';

class Content extends React.Component {
  render() {
    const { children } = this.props;
    return (
      <BrowserRouter>
        <Switch>
          <ContentWrapper>{children}</ContentWrapper>
        </Switch>
      </BrowserRouter>
    );
  }
}

export default Content;
