import React from 'react';
import { Table, Icon, Input } from 'antd';
import { UsersRolesWrapper } from './UsersRoles.style';
import axios from 'axios';
import reqwest from 'reqwest';
const Search = Input.Search;
class UsersRoles extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      data: [],
      count: null,
    };
  }
  //thay đổi trang
  handleTableChange = (pagination, filters) => {
    const pager = { ...this.state.pagination };
    pager.current = pagination.current;
    this.setState({
      pagination: pager,
    });
    this.callApi({
      results: pagination.pageSize,
      page: pagination.current - 1,
      keyword: this.state.keyword,
      ...filters,
    });
  };

  async componentDidMount() {
    axios({
      method: 'GET',
      url: `http://localhost:8080/api/v1/users?size=10`,
      withCredentials: true,
    }).then(res => {
      const pagination = { ...this.state.pagination };
      pagination.total = 10 * res.data.results.totalPages;
      const dataUser = res.data.results.items;
      this.setState({ data: dataUser, pagination });
    });
    axios({
      method: 'GET',
      url: `http://localhost:8080/api/v1/users?size=1`,
      withCredentials: true,
    }).then(res => {
      const count = res.data.results.totalPages;
      this.setState({ count: count });
    });
  }
  callApi = (params = {}) => {
    this.setState({ loading: true });
    reqwest({
      url: `http://localhost:8080/api/v1/users?size=10`,
      method: 'GET',
      withCredentials: true,
      data: {
        results: 10,
        ...params,
      },
      type: 'json',
    }).then(data => {
      const pagination = { ...this.state.pagination };
      // Read total count from server
      const {
        results: { totalPages, items },
      } = data;
      pagination.total = 10 * totalPages;
      const dataUser = items;
      this.setState({
        loading: false,
        data: dataUser,
        pagination,
      });
    });
  };
  getColums = () => {
    const columns = [
      {
        title: 'UserID',
        dataIndex: 'email',
        key: 'email',
        width: '40%',
      },
      {
        title: 'Name',
        dataIndex: 'name',
        key: 'name',
        width: '30%',
      },
      {
        title: 'Role',
        dataIndex: 'roles',
        key: 'roles',
        width: '30%',
        render: record => {
          return record.map(roles => roles.name).join(',');
        },
      },
    ];
    return columns;
  };
  handleClick = () => {
    let path = `/user-role/add-user`;
    this.props.history.push(path);
  };
  handleDetail = id => {
    const userId = id;
    let path = `/user-role/detail-user/${userId}`;
    this.props.history.push(path);
  };
  //chuẩn hóa keyword
  standardized = string => {
    return string.charAt(0).toLowerCase() + string.slice(1);
  };
  //Tìm kiếm người dùng
  onSearch = keyword => {
    let keywordUp = this.standardized(keyword);
    this.setState({ keyword: keywordUp });
    axios({
      method: 'GET',
      url: `http://localhost:8080/api/v1/users?keyword=${keywordUp}&size=10`,
      withCredentials: true,
    }).then(res => {
      console.log(res);
      if (res.status) {
        const pagination = { ...this.state.pagination };
        const {
          results: { totalPages, items },
        } = res.data;
        pagination.total = 10 * totalPages;
        const dataUser = items;
        this.setState({ data: dataUser, pagination });
      }
    });
  };
  render() {
    const { count } = this.state;
    return (
      <UsersRolesWrapper>
        <h4>USERS AND ROLES </h4>
        <hr />
        <div className="user_wrapper">
          <p onClick={this.handleClick}>
            User({count})&nbsp;
            <Icon type="plus-circle" />
          </p>
          <Search
            className="search"
            placeholder="Tìm kiếm theo email..."
            enterButton={true}
            onSearch={keyword => this.onSearch(keyword)}
          />
          <div className="table_user">
            <Table
              className="table_user_role"
              columns={this.getColums()}
              dataSource={this.state.data}
              bordered
              pagination={this.state.pagination}
              loading={this.state.loading}
              onChange={this.handleTableChange}
              rowKey={record => record.id}
              onRow={record => {
                return {
                  onClick: () => {
                    this.handleDetail(record.id);
                  },
                };
              }}
            />
          </div>
        </div>
      </UsersRolesWrapper>
    );
  }
}
export default UsersRoles;
