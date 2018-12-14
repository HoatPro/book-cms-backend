import React from 'react';
import { UsersRolesWrapper } from './UsersRoles.style';
import {
  Button,
  Checkbox,
  Row,
  Col,
  Tooltip,
  Icon,
  Breadcrumb,
  Modal,
  message,
} from 'antd';
import { Link } from 'react-router-dom';
import axios from 'axios';

const confirm = Modal.confirm;

class DetailUser extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showUserInfo: true,
      colorInfo: '#5ECCFF',
      colorRole: '#000',
      dataUser: [],
      features: [],
      checkedList: [],
      disableLegal: true,
      disableAdmin: false,
      disableManager: false,
      disableEditor: false,
      getIdRole: [],
      disableButtonSave: false,
      disableButtonDelete: false,
    };
  }
  //API get info detail user
  componentDidMount() {
    const userId = this.props.match.params.userId;
    axios({
      method: 'GET',
      url: `http://localhost:8080/api/v1/users/${userId}`,
      withCredentials: true,
    }).then(res => {
      if (res.status) {
        const dataUser = res.data.results;
        const roles = dataUser.roles;
        //Case user is Legal , roles of Legal have field isOwner
        // Legal have role Admin
        if (roles.length > 1 || roles.isOwner) {
          const checkList = [];
          const features = [];
          roles.map(role => checkList.push(role.name));
          roles.map(role => {
            if (role.features) {
              features.push(role.features);
            } else return;
          });
          this.setState({
            dataUser: dataUser,
            disableLegal: true,
            disableAdmin: true,
            disableManager: true,
            disableEditor: true,
            features: features[1],
            checkedList: checkList,
            disableButtonSave: true,
            disableButtonDelete: true,
          });
        }
        //Case user is not Legal
        else {
          const features = roles[0].features;
          const roleName = roles[0].name;
          const getIdRole = roles[0].id;
          if (roleName === 'Admin') {
            this.setState({
              disableAdmin: false,
              disableManager: true,
              disableEditor: true,
              getIdRole: getIdRole,
            });
          } else if (roleName === 'Manager') {
            this.setState({
              disableAdmin: false,
              disableManager: false,
              disableEditor: true,
              getIdRole: getIdRole,
            });
          } else {
            this.setState({
              disableAdmin: false,
              disableManager: false,
              disableEditor: false,
              getIdRole: getIdRole,
            });
          }
          const defaultCheckedList = [`${roleName}`];
          this.setState({
            dataUser: dataUser,
            features: features,
            checkedList: defaultCheckedList,
            getIdRole: getIdRole,
          });
        }
      }
    });
    //API get data Role
    axios({
      method: 'GET',
      url: `http://localhost:8080/api/v1/roles`,
      withCredentials: true,
    }).then(res => {
      const dataFeatures = res.data.results;
      this.setState({
        dataFeatures: dataFeatures,
      });
    });
  }
  //Function delete user
  handleDelete = () => {
    const userId = this.props.match.params.userId;
    confirm({
      title: 'Bạn có muốn xóa không?',
      content: (
        <div>
          <a>
            Thao tác này sẽ xóa toàn bộ quyền và thông tin người dùng ra khỏi hệ
            thống
          </a>
        </div>
      ),
      okType: 'danger',
      okText: 'Có',
      cancelText: 'Không',
      maskClosable: true,
      onOk() {
        axios({
          method: 'DELETE',
          url: `http://localhost:8080/api/v1/users/${userId}`,
          withCredentials: true,
        }).then(response => {
          if (response.status) {
            message.success('Xóa người dùng thành công');
          }
        });
      },
      onCancel() {
        console.log('Hủy');
      },
    });
  };
  //Function change color when change component Info user or Role user
  handleRole = () => {
    this.setState({
      showUserInfo: false,
      colorInfo: '#000',
      colorRole: '#5ECCFF',
    });
  };

  handleUserInfo = () => {
    this.setState({
      showUserInfo: true,
      colorInfo: '#5ECCFF',
      colorRole: '#000',
    });
  };
  //Function when change selected role of user
  //Role of Legal is oneness and never changes
  onChange = checkedList => {
    const dataFeatures = [...this.state.dataFeatures];
    if (checkedList.length === 0) {
      this.setState({
        disableAdmin: false,
        disableEditor: false,
        disableManager: false,
        features: [],
        getIdRole: [],
        disableButtonSave: true,
      });
    } else {
      checkedList.map(checked => {
        if (checked === 'Admin') {
          const featuresNew = dataFeatures.filter(
            item => item.name === 'Admin',
          );
          const listFeatures = featuresNew[0].features;
          const getIdRole = featuresNew[0].id;
          this.setState({
            disableAdmin: false,
            disableManager: true,
            disableEditor: true,
            features: listFeatures,
            getIdRole: getIdRole,
            disableButtonSave: false,
          });
        } else if (checked === 'Manager') {
          const featuresNew = dataFeatures.filter(
            item => item.name === 'Manager',
          );
          const listFeatures = featuresNew[0].features;
          const getIdRole = featuresNew[0].id;
          this.setState({
            disableAdmin: false,
            disableManager: false,
            disableEditor: true,
            features: listFeatures,
            getIdRole: getIdRole,
            disableButtonSave: false,
          });
        } else {
          const featuresNew = dataFeatures.filter(
            item => item.name === 'Editor',
          );
          const listFeatures = featuresNew[0].features;
          const getIdRole = featuresNew[0].id;
          this.setState({
            disableAdmin: false,
            disableManager: false,
            disableEditor: false,
            features: listFeatures,
            getIdRole: getIdRole,
            disableButtonSave: false,
          });
        }
      });
    }
  };
  //Function save change role of user
  handleSave = () => {
    const userId = this.props.match.params.userId;
    const { getIdRole } = this.state;
    axios({
      method: 'PUT',
      url: `http://localhost:8080/api/v1/users/${userId}`,
      headers: {
        'Content-Type': 'application/json',
      },
      withCredentials: true,
      data: {
        roleIds: [`${getIdRole}`],
      },
    }).then(res => {
      console.log(res);
      if (res.status) {
        message.success('Thay đổi quyền cho người dùng thành công!');
        let path = `/user-role`;
        this.props.history.push(path);
      }
    });
  };
  render() {
    const { dataUser, features } = this.state;
    //Get  feature of user
    const featuresUser = [];
    features.map((feature, index) => {
      featuresUser.push(<li key={index}>{feature.displayName}</li>);
    });
    //Choice component Info User or Role User
    let take = [];
    if (this.state.showUserInfo === true) {
      take = (
        <div className="info_user">
          <p>User Infomation</p>
          <hr />
          <div className="detail_user">
            <div>
              <h5>Name</h5>
              <h6>{dataUser.name}</h6>
            </div>
            <div>
              <h5>Email</h5>
              <h6>{dataUser.email}</h6>
            </div>
          </div>
          <hr />
          <div className="delete_user">
            <Button
              type="danger"
              onClick={this.handleDelete}
              disabled={this.state.disableButtonDelete}
            >
              Delete User
            </Button>
          </div>
        </div>
      );
    } else {
      take = (
        <div className="role_user">
          <p>
            Role&nbsp;
            <Tooltip title="Phân quyền user trong hệ thống">
              <Icon type="question-circle-o" />
            </Tooltip>
          </p>
          <Button
            type="primary"
            onClick={this.handleSave}
            className="save-role"
            disabled={this.state.disableButtonSave}
          >
            Lưu
          </Button>
          <hr />
          <div className="role_detail_user">
            <Checkbox.Group
              style={{ width: '100%' }}
              onChange={this.onChange}
              defaultValue={this.state.checkedList}
            >
              <Row style={{ width: '100%' }}>
                <Col span={6}>
                  <Checkbox value="Legal" disabled={this.state.disableLegal}>
                    Legal
                  </Checkbox>
                </Col>
                <Col span={6}>
                  <Checkbox value="Admin" disabled={this.state.disableAdmin}>
                    Admin
                  </Checkbox>
                </Col>
                <Col span={6}>
                  <Checkbox
                    value="Manager"
                    disabled={this.state.disableManager}
                  >
                    Manager
                  </Checkbox>
                </Col>
                <Col span={6}>
                  <Checkbox value="Editor" disabled={this.state.disableEditor}>
                    Editor
                  </Checkbox>
                </Col>
              </Row>
            </Checkbox.Group>
            <h5 style={{ marginBottom: '-10px', marginTop: '10px' }}>
              Features
            </h5>
            <hr />
            <ul>{featuresUser}</ul>
          </div>
        </div>
      );
    }
    return (
      <UsersRolesWrapper>
        <Breadcrumb>
          <Breadcrumb.Item>
            <Link to="/user-role">Uses and Roles</Link>
          </Breadcrumb.Item>
          <Breadcrumb.Item>Chi tiết thông tin User</Breadcrumb.Item>
        </Breadcrumb>
        <hr />
        <div className="info_user">
          <h5>{dataUser.name}</h5>
          <h6>{dataUser.email}</h6>
        </div>
        {/* Divide components */}
        <div className="tag">
          <p
            onClick={this.handleUserInfo}
            className="tag_info_user"
            style={{ color: `${this.state.colorInfo}` }}
          >
            User Detail
          </p>
          <p
            onClick={this.handleRole}
            className="tag_role_user"
            style={{ color: `${this.state.colorRole}` }}
          >
            Role
          </p>
        </div>
        <hr />
        {take}
      </UsersRolesWrapper>
    );
  }
}

export default DetailUser;
