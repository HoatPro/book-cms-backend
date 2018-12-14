import React from 'react';
import {
  Table,
  Menu,
  Button,
  Input,
  Dropdown,
  Icon,
  Modal,
  message,
  Form,
} from 'antd';
import { ManagerAuthorWrapper } from './ManagerAuthor.style';
import ModalAddAuthor from './ModalAddAuthor';
import reqwest from 'reqwest';
import axios from 'axios';

const Search = Input.Search;
const FormItem = Form.Item;
const { TextArea } = Input;

class ManagerAuthor extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      dataTable: [],
      dataObj: {},
      pagination: {},
      visibleEdit: false,
      visibleAdd: false,
      keyEdit: null,
      keyword: '',
    };
  }

  //Function change data table when change pagination
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
  // API get the first data table
  async componentDidMount() {
    axios({
      method: 'GET',
      url: `http://localhost:8080/api/v1/authors?size=10`,
      withCredentials: true,
    })
      .then(res => {
        const pagination = { ...this.state.pagination };
        pagination.total = 10 * res.data.results.totalPages;
        const dataAuthor = res.data.results.items;
        const dataObj = {};
        const dataTable = dataAuthor.map((data, index) => {
          dataObj[data.id] = data;
          return {
            ...data,
            key: data.id,
            index: index + 1,
          };
        });
        this.setState({
          dataTable: dataTable,
          dataObj,
          pagination,
        });
      })
      .catch(err => {
        message.error('Lỗi kết nối : ' + err);
      });
  }
  // API get data table
  callApi = (params = {}) => {
    this.setState({ loading: true });
    reqwest({
      url: `http://localhost:8080/api/v1/authors?size=10`,
      method: 'GET',
      withCredentials: true,
      data: {
        results: 10,
        ...params,
      },
      type: 'json',
    })
      .then(data => {
        const pagination = { ...this.state.pagination };
        // Read total count from server
        const {
          results: { totalPages, items },
        } = data;
        pagination.total = 10 * totalPages;
        const dataAuthor = items;
        const dataObj = {};
        const dataTable = dataAuthor.map((data, index) => {
          dataObj[data.id] = data;
          return {
            ...data,
            key: data.id,
            index: index + 1 + (pagination.current - 1) * 10,
          };
        });
        this.setState({
          loading: false,
          dataTable: dataTable,
          pagination,
          dataObj,
        });
      })
      .catch(err => {
        message.error('Lỗi kết nối : ' + err);
      });
  };
  //Add Author
  showModalAdd = () => {
    this.setState({ visibleAdd: true });
  };
  closeModal = visible => {
    this.setState({ visibleAdd: visible });
  };

  // Edit Author
  handleEdit = key => {
    const { dataObj } = this.state;
    this.props.form.setFieldsValue({
      nameEdit: dataObj[key].name,
      birthDateEdit: dataObj[key].birthDate,
      descriptionEdit: dataObj[key].description,
    });
    this.setState({
      visibleEdit: true,
      keyEdit: key,
    });
  };
  handleSaveEdit = () => {
    const { keyEdit } = this.state;
    let authorId = keyEdit;
    this.props.form.validateFields((err, fieldsValue) => {
      if (!err) {
        axios({
          method: 'PUT',
          url: `http://localhost:8080/api/v1/authors/${authorId}`,
          headers: {
            'Content-Type': 'application/json',
          },
          withCredentials: true,
          data: {
            name: fieldsValue['nameEdit'],
            birthDate: fieldsValue['birthDateEdit'],
            description: fieldsValue['descriptionEdit'],
          },
        })
          .then(res => {
            if (res.status) {
              const { dataTable } = this.state;
              message.success('Sửa tác giả thành công !');
              this.setState(
                {
                  visibleEdit: false,
                  dataTable: dataTable.map(item => {
                    if (item.id !== keyEdit) return item;
                    return {
                      ...item,
                      name: fieldsValue['nameEdit'],
                      birthDate: fieldsValue['birthDateEdit'],
                      description: fieldsValue['descriptionEdit'],
                    };
                  }),
                },
                function() {
                  this.updateDataObj();
                },
              );
            }
          })
          .catch(err => {
            message.warning(' Lỗi  :' + err);
          });
      }
    });
  };
  //Update data convert
  updateDataObj = () => {
    const { dataTable } = this.state;
    const dataObj = {};
    dataTable.map(data => {
      dataObj[data.id] = data;
    });
    this.setState({
      dataObj: dataObj,
    });
  };
  handleCancel = () => {
    this.setState({ visibleEdit: false });
  };
  //Delete author
  handleDelete = key => {
    const { dataObj } = this.state;
    const authorId = dataObj[key].id;
    axios({
      method: 'DELETE',
      url: `http://localhost:8080/api/v1/authors/${authorId}`,
      headers: {
        'Content-Type': 'application/json',
      },
      withCredentials: true,
    }).then(res => {
      if (res.status) {
        const dataTable = [...this.state.dataTable];
        this.setState({
          dataTable: dataTable.filter(item => item.id !== key),
        });
        message.success('Xóa tác giả thành công');
      } else {
        message.warning('Xóa bị lỗi!!');
      }
    });
  };
  //Normalization keyword
  standardized = string => {
    return string.charAt(0).toUpperCase() + string.slice(1);
  };
  //Function find author
  onSearch = keyword => {
    let keywordUp = this.standardized(keyword);
    this.setState({ keyword: keywordUp });
    axios({
      method: 'GET',
      url: `http://localhost:8080/api/v1/authors?keyword=${keywordUp}&size=10`,
      withCredentials: true,
    }).then(res => {
      if (res.status) {
        const dataAuthor = res.data.results.items;
        const dataObj = {};
        const dataTable = dataAuthor.map((data, index) => {
          dataObj[data.id] = data;
          return {
            ...data,
            key: data.id,
            index: index + 1,
          };
        });
        this.setState({ dataTable: dataTable, dataObj });
      }
    });
  };
  render() {
    const { getFieldDecorator } = this.props.form;
    const columns = [
      {
        title: 'STT',
        dataIndex: 'index',
        key: 'index',
      },
      {
        title: 'Tên tác giả',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: 'Người tạo',
        dataIndex: 'createdBy',
        key: 'createdBy',
      },
      {
        title: 'Ngày tạo',
        dataIndex: 'createdAt',
        key: 'createdAt',
        render: record => {
          var date = new Date(record);
          var myDate = new Date(date);
          return myDate.toLocaleDateString();
        },
      },
      {
        title: 'Lựa chọn',
        key: 'choice',
        render: record => {
          const menu = (
            <Menu onClick={() => this.handleDelete(record.key)}>
              <Menu.Item style={{ width: 80 }}>Xóa</Menu.Item>
            </Menu>
          );
          return (
            <Dropdown.Button
              overlay={menu}
              trigger={['click']}
              placement="bottomCenter"
              onClick={() => this.handleEdit(record.key)}
            >
              <span>Chỉnh sửa</span>
            </Dropdown.Button>
          );
        },
      },
    ];
    return (
      <ManagerAuthorWrapper>
        <h3>Quản lý tác giả</h3>
        <hr />
        <div className="manager-author">
          <div className="action">
            <Button type="primary" onClick={this.showModalAdd}>
              <Icon type="plus" />
              Thêm
            </Button>
            <Search
              className="search"
              placeholder="Tìm kiếm tác giả..."
              enterButton={true}
              onSearch={keyword => this.onSearch(keyword)}
            />
          </div>
          <div className="table-author">
            <Table
              className="table-detail"
              columns={columns}
              dataSource={this.state.dataTable}
              bordered
              pagination={this.state.pagination}
              loading={this.state.loading}
              onChange={this.handleTableChange}
              rowKey={record => record.id}
            />
          </div>
          <div className="modal-add">
            <ModalAddAuthor
              visible={this.state.visibleAdd}
              closeModal={this.closeModal}
            />
          </div>
          <div className="modal-edit">
            <Modal
              visible={this.state.visibleEdit}
              title="Chỉnh sửa tác giả"
              onCancel={this.handleCancel}
              onOk={this.handleSaveEdit}
              cancelText="Hủy"
              okText="Lưu"
              width={800}
            >
              <Form layout="vertical">
                <FormItem label="Tên tác giả">
                  {getFieldDecorator('nameEdit', {
                    rules: [
                      {
                        required: true,
                        message: 'Tên tac giả không được để trống !',
                      },
                    ],
                  })(<Input />)}
                </FormItem>
                <FormItem label="Năm sinh">
                  {getFieldDecorator('birthDateEdit')(<Input />)}
                </FormItem>
                <FormItem label="Tiểu sử">
                  {getFieldDecorator('descriptionEdit')(<TextArea rows={5} />)}
                </FormItem>
              </Form>
            </Modal>
          </div>
        </div>
      </ManagerAuthorWrapper>
    );
  }
}
const WrappedAuthor = Form.create()(ManagerAuthor);
export default WrappedAuthor;
