import React from 'react';
import {
  Table,
  Form,
  Dropdown,
  Menu,
  Modal,
  Input,
  Select,
  message,
} from 'antd';
import axios from 'axios';
import { EditBookWapper } from './EditBook.style';
import ModalNormalization from './ModalNormalization';

const { TextArea } = Input;
const Option = Select.Option;
const FormItem = Form.Item;

//get data selected element table
const dataSelected = [];
const rowSelection = {
  onChange: selectedRows => {
    return dataSelected.push(selectedRows);
  },
};

class TableBook extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      title: '',
      content: '',
      visibleAdd: false,
      visibleEdit: false,
      dataTable: [],
      objChapters: {},
      chapters: [],
      keyModal: null,
      loading: false,
    };
  }
  //API get data chapters
  async componentDidMount() {
    const { bookId } = this.props;
    axios({
      method: 'GET',
      url: `http://localhost:8080/api/v1/books/${bookId}/chapters`,
      withCredentials: true,
    }).then(response => {
      if (response.status) {
        const chapters = response.data.results;
        const dataTable = chapters.map(chapter => {
          return {
            key: `${chapter.id}`,
            title: `${chapter.title}`,
            content: `${chapter.content}`,
            orderNo: chapter.orderNo,
          };
        });

        this.setState({
          dataTable: dataTable,
          chapters: chapters },
          function() {
          this.convertChapters();
        });
      }
    });
  }
  //Function convert data chapter
  convertChapters = () => {
    const { chapters } = this.state;
    const objChapters = {};
    chapters.forEach(chapter => {
      objChapters[chapter.id] = chapter;
    });
    this.setState({ objChapters });
  };
  //Function Delete one chapter
  handleDelete = key => {
    //key is chapterID
    //const {chapterId}=key
    const { bookId } = this.props;
    axios({
      method: 'DELETE',
      url: `http://localhost:8080/api/v1/books/${bookId}/chapters/${key}`,
      headers: {
        'Content-Type': 'application/json',
      },
      withCredentials: true,
    }).then(res => {
      if (res.status) {
        const dataTable = [...this.state.dataTable];
        this.setState({
          dataTable: dataTable.filter(item => item.key !== key),
        });
        message.success('Xóa chương thành công!');
      }
    });
  };
  //Function delete chapters
  handleDeleteChapters = () => {
    const { dataTable } = this.state;
    const dataNew = [...dataTable];
    const { bookId } = this.props;
    const lastIndex = dataSelected.length - 1;
    const listSelected = dataSelected[lastIndex];
    const arrayIndex = [];
    dataTable.map((data, index) => {
      listSelected.map(list => {
        if (data.key === list) {
          arrayIndex.push(index);
        }
      });
    });
    for (let i = arrayIndex.length - 1; i >= 0; i--) {
      dataNew.splice(arrayIndex[i], 1);
    }
    axios({
      method: 'DELETE',
      url: `http://localhost:8080/api/v1/books/${bookId}/chapters/`,
      headers: {
        'Content-Type': 'application/json',
      },
      withCredentials: true,
      data: listSelected,
    }).then(res => {
      if (res.status) {
        this.setState({
          dataTable: dataNew,
        });
        message.success('Xóa chương thành công!');
      }
    });
  };
  // Function cancel
  handleCancel = () => {
    this.setState({
      visibleEdit: false,
      loading: false,
      visibleAdd: false,
    });
  };
  //Function add chapter
  handleAddChapter = visibleAdd => {
    this.props.form.setFieldsValue({
      modalTitleAdd: '',
      modalContentAdd: '',
    });
    this.setState({
      visibleAdd,
    });
  };
  handleSaveAddChapter = () => {
    const { bookId } = this.props;
    const { dataTable } = this.state;
    //Case table have data
    if (dataTable.length > 0) {
      const lastIndex = dataTable.length - 1;
      const chapterOrderNo = dataTable[lastIndex].orderNo;
      this.setState({ loading: true });
      this.props.form.validateFields((err, fieldsValue) => {
        if (!err) {
          axios({
            method: 'POST',
            url: `http://localhost:8080/api/v1/books/${bookId}/chapters`,
            headers: {
              'Content-Type': 'application/json',
            },
            withCredentials: true,
            data: {
              title: fieldsValue['modalTitleAdd'],
              content: fieldsValue['modalContentAdd'],
              orderNo: chapterOrderNo + 1,
            },
          }).then(res => {
            if (res.data.status === 0) {
              message.error('Thêm chương có orderNo đã tồn tại!!');
            } else {
              this.setState({
                visibleAdd: false,
                loading: false,
              });
              message.success('Thêm chương thành công !');
              window.location.reload();
            }
          });
        }
      });
    }
    //Case add new chapter
    else {
      this.setState({ loading: true });
      this.props.form.validateFields((err, fieldsValue) => {
        if (!err) {
          axios({
            method: 'POST',
            url: `http://localhost:8080/api/v1/books/${bookId}/chapters`,
            headers: {
              'Content-Type': 'application/json',
            },
            withCredentials: true,
            data: {
              title: fieldsValue['modalTitleAdd'],
              content: fieldsValue['modalContentAdd'],
              orderNo: 1,
            },
          }).then(res => {
            if (res.data.status === 0) {
              message.error('Thêm chương có orderNo đã tồn tại!!');
            } else {
              this.setState({
                visibleAdd: false,
                loading: false,
              });
              message.success('Thêm chương thành công !');
              window.location.reload();
            }
          });
        }
      });
    }
  };

  // handleSynthensis = () => {
  //   console.log('Quản lý tổng hợp');
  // };
  //Function edit chapter
  handleEdit = key => {
    const { objChapters } = this.state;
    this.props.form.setFieldsValue({
      modalTitleEdit: objChapters[key].title,
      modalContentEdit: objChapters[key].content,
    });
    this.setState({
      visibleEdit: true,
      keyModal: key,
      loading: true,
    });
  };
  handleSave = () => {
    const { keyModal } = this.state;
    const { bookId } = this.props;
    //keyModal is chapterId
    let chapterId = keyModal;
    this.props.form.validateFields((err, fieldsValue) => {
      if (!err) {
        axios({
          method: 'PUT',
          url: `http://localhost:8080/api/v1/books/${bookId}/chapters/${chapterId}`,
          headers: {
            'Content-Type': 'application/json',
          },
          withCredentials: true,
          data: {
            title: fieldsValue['modalTitleEdit'],
            content: fieldsValue['modalContentEdit'],
          },
        }).then(res => {
          if (res.status) {
            const { dataTable, chapters } = this.state;
            message.success('Sửa chương thành công !!');
            this.setState(
              {
                visibleEdit: false,
                dataTable: dataTable.map(item => {
                  if (item.key !== keyModal) return item;
                  return {
                    ...item,
                    title: fieldsValue['modalTitleEdit'],
                    content: fieldsValue['modalContentEdit'],
                  };
                }),
                loading: false,
                chapters: chapters.map(chapter => {
                  if (chapter.id !== keyModal) return chapter;
                  return {
                    ...chapter,
                    title: fieldsValue['modalTitleEdit'],
                    content: fieldsValue['modalContentEdit'],
                  };
                }),
              },
              function() {
                this.updateObjChapters();
              },
            );
          }
        });
      }
    });
  };
  //Function update data chapter convert
  updateObjChapters = () => {
    const { chapters } = this.state;
    const objChapters = {};
    chapters.forEach(chapter => {
      objChapters[chapter.id] = chapter;
    });
    this.setState({ objChapters: objChapters });
  };
  // Modal Normalization
  setNormalizationVisible = key => {
    const { objChapters } = this.state;
    this.setState({
      dataChapterNormalization: objChapters[key].normalizationValue,
      dataNormalization: objChapters[key],
      visibleNormalization: true,
    });
  };
  closeModal = visible => {
    this.setState({
      visibleNormalization: visible,
    });
  };
  render() {
    const columns = [
      { title: 'Chương', dataIndex: 'title', key: 'title', width: '80%' },
      {
        title: 'Lựa chọn',
        key: 'choice',
        width: '20%',
        render: record => {
          if (this.state.dataTable.length >= 1) {
            const menu = (
              <Menu>
                <Menu.Item>
                  <a>Tổng hợp</a>
                </Menu.Item>
                <Menu.Item>
                  <a>Chuẩn hóa</a>
                </Menu.Item>
                <Menu.Item onClick={() => this.handleDelete(record.key)}>
                  <a>Xóa</a>
                </Menu.Item>
                <Menu.Item
                  onClick={() => this.setNormalizationVisible(record.key)}
                >
                  <a>Kiểm tra</a>
                </Menu.Item>
              </Menu>
            );
            return (
              <Dropdown.Button
                style={{ color: '#505659' }}
                onClick={() => this.handleEdit(record.key)}
                overlay={menu}
              >
                Chỉnh sửa
              </Dropdown.Button>
            );
          } else {
            return;
          }
        },
      },
    ];
    const { getFieldDecorator } = this.props.form;
    return (
      <EditBookWapper>
        <Select
          className="selectact"
          showSearch
          style={{ width: 120 }}
          placeholder="Hành động"
        >
          <Option
            value="addchapter"
            onClick={() => this.handleAddChapter(true)}
          >
            Thêm chương
          </Option>
          <Option value="synthesis">Tổng hợp</Option>
          <Option value="normalization">Chuẩn hóa</Option>
          <Option value="delete" onClick={this.handleDeleteChapters}>
            Xóa
          </Option>
        </Select>
        <div className="table-chapter">
          <Table
            bordered
            columns={columns}
            rowSelection={rowSelection}
            dataSource={this.state.dataTable}
            loading={this.state.loading}
            rowKey={record => record.key}
          />
        </div>
        {/* Modal Add  chapter */}
        <div className="modal-add-chapter">
          <Modal
            visible={this.state.visibleAdd}
            title="Thêm chương"
            okText="Thêm"
            cancelText="Hủy"
            onCancel={this.handleCancel}
            width={900}
            onOk={this.handleSaveAddChapter}
          >
            <Form layout="vertical">
              <FormItem label="Tiêu đề">
                {getFieldDecorator('modalTitleAdd')(<Input />)}
              </FormItem>
              <FormItem label="Nội dung">
                {getFieldDecorator('modalContentAdd')(<TextArea rows={16} />)}
              </FormItem>
            </Form>
          </Modal>
        </div>
        {/* Modal Edit chapter */}
        <Modal
          visible={this.state.visibleEdit}
          title="Sửa chương"
          onOk={this.handleSave}
          okText="Lưu"
          onCancel={this.handleCancel}
          cancelText="Hủy"
          width={900}
        >
          <Form layout="vertical">
            <FormItem label="Tiêu đề">
              {getFieldDecorator('modalTitleEdit')(<Input />)}
            </FormItem>
            <FormItem label="Nội dung">
              {getFieldDecorator('modalContentEdit')(<TextArea rows={16} />)}
            </FormItem>
          </Form>
        </Modal>
        {/* Modal normalization content */}
        <div className="modal-normalization">
          <ModalNormalization
            visible={this.state.visibleNormalization}
            dataChapterNormalization={this.state.dataChapterNormalization}
            dataNormalization={this.state.dataNormalization}
            closeModal={this.closeModal}
          />
        </div>
      </EditBookWapper>
    );
  }
}

const TableChapter = Form.create()(TableBook);

export default TableChapter;
