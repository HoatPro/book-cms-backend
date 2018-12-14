import React from 'react';
import { Form, Modal, Input, message } from 'antd';
import TextArea from 'antd/lib/input/TextArea';
import axios from 'axios';
const FormItem = Form.Item;
const ModalAddCategory = Form.create()(
  class extends React.Component {
    constructor(props) {
      super(props);
    }
    handleCancel = () => {
      const { visible } = this.props;
      this.props.closeModal(!visible);
    };
    handleCreate = () => {
      const { visible } = this.props;
      this.props.form.validateFields((err, values) => {
        if (!err) {
          axios({
            method: 'POST',
            url: `http://localhost:8080/api/v1/categories`,
            withCredentials: true,
            headers: {
              'Content-Type': 'application/json',
            },
            data: {
              name: values.name,
              description: values.description,
            },
          })
            .then(res => {
              if (res.status) {
                if (res.data.status === 0) {
                  message.warning(
                    'Tên thể loại đã có ,vui lòng thêm thể loại khác !',
                  );
                } else {
                  message.success('Thêm thể loại thành công');
                  window.location.reload();
                }
              }
            })
            .catch(err => {
              message.error('Thêm thể loại lỗi :' + err);
            });
          this.props.closeModal(!visible);
        }
      });
    };
    render() {
      const { visible } = this.props;
      const { getFieldDecorator } = this.props.form;
      return (
        <Modal
          visible={visible}
          title="Thêm thể loại"
          okText="Thêm"
          cancelText="Hủy"
          onCancel={this.handleCancel}
          onOk={this.handleCreate}
          width={800}
        >
          <Form layout="vertical">
            <FormItem label="Tên">
              {getFieldDecorator('name', {
                rules: [
                  {
                    required: true,
                    message: 'Tên thể loại không được để trống !',
                  },
                ],
              })(<Input />)}
            </FormItem>
            <FormItem label="Mô tả">
              {getFieldDecorator('description')(<TextArea rows={5} />)}
            </FormItem>
          </Form>
        </Modal>
      );
    }
  },
);
export default ModalAddCategory;
