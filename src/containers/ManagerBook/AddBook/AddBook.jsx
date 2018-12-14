import React from 'react';
import { AddbookStyle } from './AddBook.style';
import { Link } from 'react-router-dom';
import { Breadcrumb, Button, Icon, Upload, message, notification } from 'antd';

const Dragger = Upload.Dragger;

const openNotification = () => {
  notification.open({
    duration: '10',
    message: 'Bạn đang upload...',
    description: 'Bạn chỉ được upload  tối đa 10 file trong 1 folder',
  });
};
class AddBook extends React.Component {
  render() {
    const props = {
      name: 'file',
      multiple: true,
      accept: '.XLSX',
      disabled: false,
      showUploadList: false,
      withCredentials: true,
      action: 'http://localhost:8080/api/v1/books/upload-info',
      onChange: info => {
        const { status } = info.file;
        if (status === 'done') {
          const { response } = info.file;
          if (response.status === 1) {
            message.success(`File ${info.file.name} upload thành công !`);
          } else {
            message.warning('Upload không đúng định dạng file!!');
          }
        } else if (status === 'error') {
          message.error(`File ${info.file.name}  upload thất bại !!`);
        }
      },
    };
    const propsUp = {
      name: 'file',
      multiple: true,
      accept: '.XLSX',
      disabled: false,
      showUploadList: false,
      withCredentials: true,
      action:
        'http://localhost:8080/api/v1/books//upload-multiple-book-content',
      onChange: info => {
        const { status } = info.file;
        if (status === 'done') {
          const { response } = info.file;
          if (response.status === 1) {
            message.success(`File ${info.file.name} upload thành công !`);
          } else {
            message.warning('Upload không đúng định dạng file!!');
          }
        } else if (status === 'error') {
          message.error(`File ${info.file.name}  upload thất bại !!`);
        }
      },
    };
    return (
      <AddbookStyle>
        <Breadcrumb>
          <Breadcrumb.Item>
            <Link to="/manager-book">Quản lý sách</Link>
          </Breadcrumb.Item>
          <Breadcrumb.Item>
            <Link to="/addbook">Thêm sách</Link>
          </Breadcrumb.Item>
        </Breadcrumb>
        <h3>Thêm một quyển sách</h3>
        <hr />
        <div className="box1">
          <h4>Thêm sách qua giao diện</h4>
          <Link to="/addbookui">
            <Button type="primary">
              <Icon type="plus-circle" theme="outlined" />
              Thêm
            </Button>
          </Link>
        </div>

        <h3>Thêm nhiều quyển sách</h3>
        <hr />
        <div className="box21">
          <h4>Thêm thông tin sách (.xlsx)</h4>
          <Dragger {...props} showUploadList={false}>
            <p className="ant-upload-drag-icon">
              <Icon type="inbox" />
            </p>
            Kéo hoặc Click
          </Dragger>
        </div>
        <div className="box22">
          <h4>
            Thêm nội dung sách
            <p>(chỉ áp dụng với những sách được thêm thông tin)</p>
          </h4>
          <Upload {...propsUp} directory>
            <Button type="primary" onClick={openNotification}>
              <Icon type="plus-circle" /> Thêm file hoặc folder
            </Button>
          </Upload>
        </div>
      </AddbookStyle>
    );
  }
}

export default AddBook;
