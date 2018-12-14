import React from 'react';
import { Menu, Icon } from 'antd';
import { SiderWrapper } from './Layout.style';
import { Link } from 'react-router-dom';
class Siders extends React.Component {
  handleClick = () => {
    window.location.reload();
  };
  render() {
    return (
      <SiderWrapper>
        <Menu>
          <Menu.Item>
            <Link to="/partner" onClick={this.handleClick}>
              <span>
                <Icon type="home" />
                <span>Tổng quan </span>
              </span>
            </Link>
          </Menu.Item>

          <Menu.Item onClick={this.handleClick}>
            <Link to="/manager-book">
              <span>
                <Icon type="book" />
                <span>Quản lý sách</span>
              </span>
            </Link>
          </Menu.Item>

          <Menu.Item>
            <Link to="/manager-category" onClick={this.handleClick}>
              <span>
                <Icon type="bars" />
                <span>Quản lý thể loại</span>
              </span>
            </Link>
          </Menu.Item>
          <Menu.Item>
            <Link to="/manager-author" onClick={this.handleClick}>
              <span>
                <Icon type="appstore" />
                <span>Quản lý tác giả</span>
              </span>
            </Link>
          </Menu.Item>
          <Menu.Item>
            <Link to="/statistical" onClick={this.handleClick}>
              <span>
                <Icon type="bar-chart" />
                <span>Thống kê</span>
              </span>
            </Link>
          </Menu.Item>
          <Menu.Item>
            <Link to="/user-role" onClick={this.handleClick}>
              <span>
                <Icon type="team" />
                <span>Người dùng và quyền</span>
              </span>
            </Link>
          </Menu.Item>
        </Menu>
      </SiderWrapper>
    );
  }
}

export default Siders;
