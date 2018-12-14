import React from 'react';
import { HeaderWrapper } from './Layout.style';
import { Dropdown, Icon, Avatar, Menu } from 'antd';
import { Link } from 'react-router-dom';

class Header extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      user: 'Ha',
      email: '',
      key: '',
    };
  }
  componentWillMount() {
    if (this.props.userData.values) {
      const email = this.props.userData.values.userName;
      this.getKey(email);
      this.setState({
        email: email,
        key: email,
      });
    } else {
      this.setState({
        email: 'vodanh1204@gmail.com',
      });
    }
  }
  getKey = email => {
    console.log(email);
  };
  render() {
    const menu = (
      <Menu onClick={this.handleMenuClick}>
        <Menu.Item key="email">{this.state.email}</Menu.Item>
        <Menu.Item key="logout">
          <Link to="/">Đăng xuất</Link>
        </Menu.Item>
      </Menu>
    );

    return (
      <HeaderWrapper>
        <div className="header-wrapper">
          <h3>Hệ thống quản lý sách </h3>
          <div className="info-wrapper">
            <Dropdown overlay={menu}>
              <div className="avatar-header">
                <Avatar
                  style={{
                    backgroundColor: '#00a2ae',
                    verticalAlign: 'middle',
                  }}
                  size="default"
                >
                  {this.state.user}
                  <Icon type="down" />
                </Avatar>
              </div>
            </Dropdown>
          </div>
        </div>
      </HeaderWrapper>
    );
  }
}
export default Header;
