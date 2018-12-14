import React from 'react';
import { Icon, Input, Button, Table, Select, Dropdown, Menu } from 'antd';
import { Link } from 'react-router-dom';
import reqwest from 'reqwest';
import axios from 'axios';
import { ManagerBookWrapper } from './ManagerBook.style';
const Search = Input.Search;
const Option = Select.Option;
// Function get selected element data in table
const rowSelection = {
  onChange: (selectedRowKeys, selectedRows) => {
    console.log(`selectedRowKeys: ${selectedRowKeys}`, selectedRows[0]);
  },
};

class ManagerBook extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      data: [],
      pagination: {},
      loading: false,
      filteredInfo: null,
      sortedInfo: null,
      keyword: '',
    };
  }
  //API get data table
  callApi = (params = {}) => {
    this.setState({ loading: true });
    reqwest({
      url: `http://localhost:8080/api/v1/books?fields=title,createdBy,publicYear,categories,authors,status,slug,translator,pageNumber`,
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
      pagination.total = 10 * data.results.totalPages;
      this.setState({
        loading: false,
        data: data.results.items,
        pagination,
      });
    });
  };

  componentDidMount() {
    this.callApi();
  }
  //Function change data table when change pagination
  handleTableChange = (pagination, filters, sorter) => {
    const pager = { ...this.state.pagination };
    pager.current = pagination.current;
    this.setState({
      pagination: pager,
      sortedInfo: sorter,
    });
    if (filters.status === null || sorter === {}) {
      this.callApi({
        results: pagination.pageSize,
        page: pagination.current - 1,
        keyword: this.state.keyword,
      });
    } else {
      this.callApi({
        results: pagination.pageSize,
        page: pagination.current - 1,
        keyword: this.state.keyword,
        statusIds: filters.status[0],
      });
    }
  };

  //Function normalization keyword
  standardized = string => {
    return string.charAt(0).toUpperCase() + string.slice(1);
  };
  // Function search
  onSearch = keyword => {
    let keywordUp = this.standardized(keyword);
    this.setState({ keyword: keywordUp });
    axios({
      method: 'GET',
      url: `http://localhost:8080/api/v1/books?keyword=${keywordUp}&size=10`,
      withCredentials: true,
    }).then(res => {
      if (res.status) {
        const dataTable = res.data.results.items;
        const pagination = { ...this.state.pagination };
        // Read total count from server
        pagination.total = 10 * res.data.results.totalPages;
        this.setState({
          loading: false,
          data: dataTable,
          pagination,
        });
      }
    });
  };
  render() {
    let { sortedInfo, filteredInfo } = this.state;
    sortedInfo = sortedInfo || {};
    filteredInfo = filteredInfo || {};
    const columns = [
      {
        title: 'Tên sách',
        dataIndex: 'title',
        key: 'title',
        width: '15%',
      },
      {
        title: 'Tác giả',
        dataIndex: 'authors',
        key: 'authors',
        width: '15%',
        render: record => {
          return record.map(author => author.name).join(', ');
        },
      },
      {
        title: 'Thể loại',
        dataIndex: 'categories',
        key: 'categories',
        width: '20%',
        render: record => {
          return record.map(categories => categories.name).join(', ');
        },
      },
      {
        title: 'Năm xuất bản',
        dataIndex: 'publicYear',
        key: 'publicYear',
        width: '10%',
        sorter: (a, b) => a.publicYear - b.publicYear,
        sortOrder: sortedInfo.columnKey === 'publicYear' && sortedInfo.order,
      },
      {
        title: 'Người tạo',
        dataIndex: 'createdBy',
        key: 'createdBy',
        width: '15%',
      },
      {
        title: 'Trạng thái',
        dataIndex: 'status',
        key: 'status',
        filters: [
          { text: 'Chưa có nội dung', value: 0 },
          { text: 'Đã có nội dung', value: 1 },
          { text: 'Đang chuẩn hóa', value: 2 },
          { text: 'Đã chuẩn hóa', value: 3 },
          { text: 'Sẵn sàng tổng hợp', value: 4 },
          { text: 'Đang tổng hợp', value: 5 },
          { text: 'Tổng hợp lỗi', value: 6 },
          { text: 'Tổng hợp thành công', value: 7 },
        ],
        filteredValue: filteredInfo.name || null,
        onFilter: record => record.name,
        render: status => {
          return status.name;
        },
      },
      {
        title: 'Hành động',
        key: 'action',
        render: record => (
          <div>
            <Dropdown
              overlay={
                <Menu>
                  <Menu.Item>
                    <a>Tổng hợp</a>
                  </Menu.Item>
                  <Menu.Item>
                    <a>Chuẩn hóa</a>
                  </Menu.Item>
                </Menu>
              }
              placement="bottomLeft"
            >
              <Button
                onClick={() => {
                  this.props.history.push(`/edit/${record.slug}`);
                }}
              >
                <Icon type="down" theme="outlined" />
                Chi tiết
              </Button>
            </Dropdown>
          </div>
        ),
      },
    ];
    return (
      <ManagerBookWrapper>
        <h3>Quản lý sách</h3>
        <hr />
        <div className="home_content">
          <Search
            placeholder="Tìm kiếm tên sách,tác giả ,thể loại..."
            onSearch={keyword => this.onSearch(keyword)}
          />
          <Button className="add_book" type="primary">
            <Link to="/addbook">
              <Icon
                type="plus-circle"
                theme="outlined"
                style={{ marginRight: '2px' }}
              />
              Thêm sách
            </Link>
          </Button>
          <hr />
          <div>
            <div>
              <Select
                showSearch
                style={{ width: 180 }}
                placeholder="Lựa chọn hành động"
              >
                <Option value="normalization">Chuẩn hóa</Option>
                <Option value="synthesis">Tổng hợp</Option>
                <Option value="detail">Tủy chỉnh</Option>
              </Select>
            </div>
            <Table
              className="table-books"
              bordered={true}
              rowSelection={rowSelection}
              columns={columns}
              dataSource={this.state.data}
              pagination={this.state.pagination}
              loading={this.state.loading}
              onChange={this.handleTableChange}
              rowKey={record => record.id}
            />
          </div>
        </div>
      </ManagerBookWrapper>
    );
  }
}

export default ManagerBook;
