import React from 'react';
import { FooterWrapper } from './Layout.style';

export default class Footer extends React.Component {
  render() {
    return (
      <FooterWrapper>
        Book Management ©2018 created by
        <a href="https://google.com.vn">Google </a>
      </FooterWrapper>
    );
  }
}
