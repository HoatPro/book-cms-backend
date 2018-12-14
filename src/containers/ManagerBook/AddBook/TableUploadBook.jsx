import React from 'react';
import { Table, Button, Dropdown, Menu, Icon } from 'antd';
class TableUploadBook extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      data: [],
    };
  }
  handleDelete = orderNo => {
    const chaptersUpload = [...this.state.data];
    this.setState({
      data: chaptersUpload.filter(item => item.orderNo !== orderNo),
    });
  };
  componentWillReceiveProps(nextProps) {
    const data = chaptersUpload.map(chapterUpload => {
      return {
        key: `${chapterUpload.orderNo}`,
        title: `Chương ${chapterUpload.orderNo} : ${chapterUpload.title}`,
        content: `${chapterUpload.content}`,
      };
    });
    this.setState({
      data,
      disableButton: nextProps.disableButton,
      disableButton: false,
    });
  }
  // handleFooter = () => {
  //   return (
  //     <Button type="primary" onClick={() => this.handleConfirm()}>
  //       Xác nhận
  //     </Button>
  //   );
  // };
  handleConfirm = () => {
    this.setState({ disableButton: false });
  };

  getColumns = () => {
    const columns = [
      {
        title: 'Chương',
        dataIndex: 'title',
        key: 'title',
        width: '80%',
      },

      {
        title: 'Lựa chọn',
        key: 'operation',
        width: '20%',
        render: (text, record) => {
          return this.props.chaptersUpload.length >= 1 ? (
            <div>
              <Dropdown
                overlay={
                  <Menu>
                    <Menu.Item onClick={() => this.handleEdit(record.orderNo)}>
                      <a>Chỉnh sửa</a>
                    </Menu.Item>
                    <Menu.Item
                      onClick={() => this.handleDelete(record.orderNo)}
                    >
                      <a>Xóa</a>
                    </Menu.Item>
                  </Menu>
                }
                placement="bottomLeft"
              >
                <Button>
                  <Icon type="down" theme="outlined" />
                  Hành động
                </Button>
              </Dropdown>
            </div>
          ) : null;
        },
      },
    ];
    return columns;
  };

  render() {
    const { chaptersUpload, disableButton } = this.props;

    return (
      <Table
        dataSource={this.state.data}
        columns={this.getColumns()}
        bordered={true}
        pagination={false}
        scroll={{ y: 450 }}
        rowKey={record => record.key}
        // footer={this.handleFooter}
        disableButton={disableButton}
      />
    );
  }
}

export default TableUploadBook;
