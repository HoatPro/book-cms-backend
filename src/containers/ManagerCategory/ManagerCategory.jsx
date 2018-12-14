import React from 'react';
import {
  Table,
  Menu,
  Button,
  Input,
  Dropdown,
  Icon,
  Form,
  Modal,
  message,
} from 'antd';
import reqwest from 'reqwest';
import axios from 'axios';
import { ManagerCategoryWrapper } from './ManagerCategory.style';
import ModalAddCategory from './ModalAddCategory';

const FormItem = Form.Item;
const { TextArea } = Input;
const Search = Input.Search;

class ManagerCategory extends React.Component {
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
  handleTableChange = (pagination, filter) => {
    const pager = { ...this.state.pagination };
    pager.current = pagination.current;
    this.setState({
      pagination: pager,
    });

    this.callApi({
      results: pagination.pageSize,
      page: pagination.current - 1,
      keyword: this.state.keyword,
      ...filter,
    });
  };
  //API get data the first
  async componentDidMount() {
    axios({
      method: 'GET',
      url: `http://localhost:8080/api/v1/categories?size=10`,
      withCredentials: true,
    }).then(res => {
      const pagination = { ...this.state.pagination };
      pagination.total = 10 * res.data.results.totalPages;
      const dataCategory = res.data.results.items;
      const dataObj = {};
      const dataTable = dataCategory.map((data, index) => {
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
    });
  }
  //API get data table
  callApi = (params = {}) => {
    this.setState({ loading: true });
    reqwest({
      url: `http://localhost:8080/api/v1/categories?size=10`,
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
      const dataCategory = items;
      const dataObj = {};
      const dataTable = dataCategory.map((data, index) => {
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
    });
  };
  //Function normalization keyword
  standardized = string => {
    let str = string.replace(
      /[^0-9a-zàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ\s]/gi,
      '',
    );
    return str.charAt(0).toUpperCase() + str.slice(1);
  };
  //Function search category
  onSearch = keyword => {
    let keywordUp = this.standardized(keyword);
    this.setState({ keyword: keywordUp });
    axios({
      method: 'GET',
      url: `http://localhost:8080/api/v1/categories?keyword=${keywordUp}&size=10`,
      withCredentials: true,
    }).then(res => {
      console.log(res);
      if (res.status) {
        const pagination = { ...this.state.pagination };
        const {
          results: { totalPages, items },
        } = res.data;
        pagination.total = 10 * totalPages;
        const dataCategory = items;
        const dataObj = {};
        const dataTable = dataCategory.map((data, index) => {
          dataObj[data.id] = data;
          return {
            ...data,
            key: data.id,
            index: index + 1,
          };
        });
        this.setState({ dataTable: dataTable, pagination, dataObj });
      }
    });
  };

  //Add category
  showModalAdd = () => {
    this.setState({ visibleAdd: true });
  };
  closeModal = visible => {
    this.setState({ visibleAdd: visible });
  };
  //Edit category
  handleEdit = key => {
    const { dataObj } = this.state;
    this.props.form.setFieldsValue({
      nameEdit: dataObj[key].name,
      descriptionEdit: dataObj[key].description,
    });
    this.setState({
      visibleEdit: true,
      keyEdit: key,
    });
  };
  handleSaveEdit = () => {
    const { keyEdit } = this.state;
    const categoryId = keyEdit;
    this.props.form.validateFields((err, fieldsValue) => {
      if (!err) {
        axios({
          method: 'PUT',
          url: `http://localhost:8080/api/v1/categories/${categoryId}`,
          headers: {
            'Content-Type': 'application/json',
          },
          withCredentials: true,
          data: {
            name: fieldsValue['nameEdit'],
            description: fieldsValue['descriptionEdit'],
          },
        }).then(res => {
          if (res.status) {
            const { dataTable } = this.state;
            message.success('Sửa thể loại thành công !');
            this.setState(
              {
                visibleEdit: false,
                dataTable: dataTable.map(item => {
                  if (item.id !== keyEdit) return item;
                  return {
                    ...item,
                    name: fieldsValue['nameEdit'],
                    description: fieldsValue['descriptionEdit'],
                  };
                }),
              },
              function() {
                this.updateDataObj();
              },
            );
          }
        });
      }
    });
  };
  //Function update data  table convert
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
  //Function delete category
  handleDelete = key => {
    const { dataObj } = this.state;
    const categoryId = dataObj[key].id;
    axios({
      method: 'DELETE',
      url: `http://localhost:8080/api/v1/categories/${categoryId}`,
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
        message.success('Xóa thể loại thành công');
      } else {
        message.warning('Xóa bị lỗi!!');
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
        title: 'Tên thể loại',
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
            <Menu
              onClick={() => this.handleDelete(record.key)}
              style={{ width: '40' }}
            >
              <Menu.Item>Xóa</Menu.Item>
            </Menu>
          );
          return (
            <Dropdown.Button
              overlay={menu}
              trigger={['click']}
              placement="bottomCenter"
            >
              <a onClick={() => this.handleEdit(record.key)}>Chỉnh sửa</a>
            </Dropdown.Button>
          );
        },
      },
    ];
    return (
      <ManagerCategoryWrapper>
        <h3>Quản lý thể loại</h3>
        <hr />
        <div className="manager-category">
          <div className="action">
            <Button type="primary" onClick={this.showModalAdd}>
              <Icon type="plus" />
              Thêm
            </Button>
            <Search
              className="search"
              placeholder="Tìm kiếm thể loại..."
              onSearch={keyword => this.onSearch(keyword)}
            />
          </div>
          <div className="table-category">
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
          {/* Modal add category */}
          <div className="modal-add">
            <ModalAddCategory
              visible={this.state.visibleAdd}
              closeModal={this.closeModal}
            />
          </div>
          {/* Modal edit category */}
          <div className="modal-edit">
            <Modal
              visible={this.state.visibleEdit}
              title="Chỉnh sửa thể loại"
              onCancel={this.handleCancel}
              onOk={this.handleSaveEdit}
              cancelText="Hủy"
              okText="Lưu"
              width={800}
            >
              <Form layout="vertical">
                <FormItem label="Tên thể loại">
                  {getFieldDecorator('nameEdit', {
                    rules: [
                      {
                        required: true,
                        message: 'Tên thể loại không được để trống !',
                      },
                    ],
                  })(<Input />)}
                </FormItem>
                <FormItem label="Mô tả">
                  {getFieldDecorator('descriptionEdit')(<TextArea rows={5} />)}
                </FormItem>
              </Form>
            </Modal>
          </div>
        </div>
      </ManagerCategoryWrapper>
    );
  }
}
const WrappedCategory = Form.create()(ManagerCategory);
export default WrappedCategory;
