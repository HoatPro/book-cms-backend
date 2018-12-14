import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Form, Icon, Input, Button, Checkbox, message } from 'antd';
import { LoginWrapper } from './Login.style';
import axios from 'axios';
import { isThisSecond } from 'date-fns';

const FormItem = Form.Item;
class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      dataUser: [],
    };
  }
  // componentDidMount() {
  //   axios({
  //     method: 'GET',
  //     url: `http://localhost:8080/api/v1/users`,
  //     withCredentials: true,
  //   }).then(res => {
  //     const dataUser = res.data.results.items;
  //     this.setState({
  //       dataUser: dataUser,
  //     });
  //   });
  // }
  handleSubmit = e => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        axios({
          method: 'POST',
          url: `https://sso.vbee.vn/api/oath`,
          headers: {
            'Content-Type': 'application/json',
          },
          withCredentials: true,
          data: {
            email: values.userName,
            password: values.password,
            type: 'password',
          },
        })
          .then(res => {
            if (res.status) {
              const access_token = `${res.data.access_token}`;
              document.cookie =
                ' accessToken' + '=' + access_token + ';' + ';path=/';
            }
          })
          .catch(err => {
            console.log(err);
          });
        this.props.history.push({
          pathname: '/partner',
          state: { values },
        });
      }
    });
  };
  render() {
    const { getFieldDecorator } = this.props.form;
    return (
      <LoginWrapper>
        <Form onSubmit={this.handleSubmit} className="login-form">
          <h2>Đăng nhập hệ thống quản lý sách</h2>
          <FormItem label="Email:">
            {getFieldDecorator('userName', {
              rules: [
                {
                  required: true,
                  message: 'Please input your username!',
                },
              ],
            })(
              <Input
                prefix={
                  <Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />
                }
                placeholder="Username"
              />,
            )}
          </FormItem>
          <FormItem label="Mật khẩu">
            {getFieldDecorator('password', {
              rules: [
                {
                  required: true,
                  message: 'Please input your Password!',
                },
              ],
            })(
              <Input
                prefix={
                  <Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />
                }
                type="password"
                placeholder="Password"
              />,
            )}
          </FormItem>
          <FormItem>
            {getFieldDecorator('remember', {
              valuePropName: 'checked',
              initialValue: true,
            })(<Checkbox>Ghi nhớ</Checkbox>)}
            <a className="login-form-forgot" href="">
              Quên mật khẩu
            </a>
            <br />

            <Button
              type="primary"
              htmlType="submit"
              className="login-form-button"
            >
              Đăng nhập
            </Button>
            {/* &nbsp; Bạn chưa có tài khoản{' '}
            <Link to="/register" className="FormField__Link">
              Đăng ký
            </Link> */}
          </FormItem>
        </Form>
      </LoginWrapper>
    );
  }
}
const App = Form.create()(Login);
export default App;
