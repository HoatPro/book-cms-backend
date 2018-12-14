import React from 'react';
import {
  Form,
  Icon,
  Input,
  Button,
  Breadcrumb,
  message,
  Checkbox,
  Row,
  Col,
} from 'antd';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { UsersRolesWrapper } from './UsersRoles.style';

const FormItem = Form.Item;

class AddUser extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      dataRoles: [],
      listRoles: [],
      disableAdmin: false,
      disableManager: false,
      disableEditor: false,
    };
  }
  //API get data Roles
  componentDidMount() {
    axios({
      method: 'GET',
      url: `http://localhost:8080/api/v1/roles`,
      withCredentials: true,
    }).then(res => {
      if (res.status) {
        const dataGet = res.data.results;
        const dataRoles = [...dataGet];
        dataRoles.map((data, index) => {
          if (data.name !== 'Legal') return data;
          dataRoles.splice(index, 1);
        });
        this.setState({
          dataRoles: dataRoles,
        });
      }
    });
  }
  //Function get roleId
  convertCheckedToId = string => {
    const { dataRoles } = this.state;
    const listId = [];
    dataRoles.filter(item => {
      if (item.name === string) {
        return listId.push(item.id);
      }
    });
    return listId;
  };
  //Function change select
  onChange = checkedList => {
    if (checkedList.length === 0) {
      this.setState({
        disableAdmin: false,
        disableEditor: false,
        disableManager: false,
      });
    } else {
      checkedList.map(checked => {
        if (checked === 'Admin') {
          this.setState({
            disableAdmin: false,
            disableManager: true,
            disableEditor: true,
            listRoles: this.convertCheckedToId(checked),
          });
        } else if (checked === 'Manager') {
          this.setState({
            disableAdmin: false,
            disableManager: false,
            disableEditor: true,
            listRoles: this.convertCheckedToId(checked),
          });
        } else {
          this.setState({
            disableAdmin: false,
            disableManager: false,
            disableEditor: false,
            listRoles: this.convertCheckedToId(checked),
          });
        }
      });
    }
  };

  //Function check first name or last name
  checkCondition = (a, b) => {
    if (typeof a !== 'undefined' && typeof b !== 'undefined') {
      return a + ` ` + b;
    } else if ((typeof a !== 'undefined') & (typeof b === 'undefined')) {
      return a;
    } else if ((typeof a === 'undefined') & (typeof b !== 'undefined')) {
      return b;
    } else {
      return '';
    }
  };
  //Function save change role of user
  handleSubmit = e => {
    const { listRoles } = this.state;
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        console.log(values.userName);
        axios({
          method: 'POST',
          url: `http://localhost:8080/api/v1/users`,
          headers: {
            'Content-Type': 'application/json',
          },
          withCredentials: true,
          data: {
            email: values.email,
            name: this.checkCondition(values.firstName, values.userName),
            roleIds: listRoles,
          },
        }).then(res => {
          if (res.status) {
            if (res.data.status === 0) {
              message.warning(
                'Người dùng đã tồn tại trong hệ thống! Vui lòng đăng ký bằng email khác .',
              );
            } else {
              message.success('Thêm user thành  công!');
              let path = `/user-role`;
              this.props.history.push(path);
            }
          }
        });
      }
    });
  };
  handleCancel = () => {
    let path = `/user-role`;
    this.props.history.push(path);
  };
  render() {
    const { getFieldDecorator } = this.props.form;
    const formItemLayout = {
      labelCol: { span: 20 },
      wrapperCol: { span: 20 },
    };
    return (
      <UsersRolesWrapper>
        <Breadcrumb>
          <Breadcrumb.Item>
            <Link to="/user-role">Uses and Roles</Link>
          </Breadcrumb.Item>
          <Breadcrumb.Item>
            <Link to="/user-role/add-user">Thêm User</Link>
          </Breadcrumb.Item>
        </Breadcrumb>
        <hr />
        <h6>Thêm thông tin User</h6>
        <hr />
        <div className="form_user_infomation">
          <Form onSubmit={this.handleSubmit} className="login-form">
            <FormItem label="First Name:">
              {getFieldDecorator('firstName')(
                <Input placeholder="First Name" />,
              )}
            </FormItem>
            <FormItem label="Last Name:">
              {getFieldDecorator('userName')(<Input placeholder="Last Name" />)}
            </FormItem>
            <FormItem label="Email (This will be the user 's UserID)">
              {getFieldDecorator('email', {
                rules: [
                  {
                    type: 'email',
                    message: 'Tên email không hợp lệ !!',
                  },
                  {
                    required: true,
                    message: 'Email không được để trống',
                  },
                ],
              })(
                <Input
                  prefix={
                    <Icon type="mail" style={{ color: 'rgba(0,0,0,.25)' }} />
                  }
                  placeholder="Email"
                />,
              )}
            </FormItem>
            <FormItem {...formItemLayout} label="Lựa chọn quyền cho User">
              {getFieldDecorator('radio-group', {
                rules: [
                  {
                    required: true,
                    message: 'Quyền của người dùng không được để trống',
                  },
                ],
              })(
                //Checkbox group
                <Checkbox.Group
                  style={{ width: '100%' }}
                  onChange={this.onChange}
                >
                  <Row style={{ width: '100%' }}>
                    <Col span={8}>
                      <Checkbox
                        value="Admin"
                        disabled={this.state.disableAdmin}
                      >
                        Admin
                      </Checkbox>
                    </Col>
                    <Col span={8}>
                      <Checkbox
                        value="Manager"
                        disabled={this.state.disableManager}
                      >
                        Manager
                      </Checkbox>
                    </Col>
                    <Col span={8}>
                      <Checkbox
                        value="Editor"
                        disabled={this.state.disableEditor}
                      >
                        Editor
                      </Checkbox>
                    </Col>
                  </Row>
                </Checkbox.Group>,
              )}
            </FormItem>
            <FormItem>
              <Button
                type="primary"
                htmlType="submit"
                className="form-button"
                style={{ marginRight: '7px' }}
              >
                Thêm
              </Button>
              &nbsp;
              <Button type="danger" onClick={this.handleCancel}>
                Hủy
              </Button>
            </FormItem>
          </Form>
        </div>
      </UsersRolesWrapper>
    );
  }
}
const UserAndRole = Form.create()(AddUser);
export default UserAndRole;
