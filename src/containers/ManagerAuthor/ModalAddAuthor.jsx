import React from 'react';
import { Form, Modal, Input, InputNumber, message } from 'antd';
import TextArea from 'antd/lib/input/TextArea';
import axios from 'axios';
const FormItem = Form.Item;

const ModalAddAuthor = Form.create()(
  class extends React.Component {
    //Function add author and props data component parents
    handleCreate = () => {
      const { visible } = this.props;
      this.props.form.validateFields((err, values) => {
        if (!err) {
          axios({
            method: 'POST',
            url: `http://localhost:8080/api/v1/authors`,
            withCredentials: true,
            headers: {
              'Content-Type': 'application/json',
            },
            data: {
              name: values.name,
              birthDate: values.birthDate,
              description: values.description,
            },
          })
            .then(res => {
              if (res.status) {
                if (res.data.status === 0) {
                  message.warning('Tác giả đã có ,vui lòng thêm tác giả khác!');
                } else {
                  message.success('Thêm tác giả thành công');
                  window.location.reload();
                }
              }
            })
            .catch(err => {
              message.error('Lỗi :' + err);
            });
          this.props.closeModal(!visible);
        }
      });
    };
    handleCancel = () => {
      const { visible } = this.props;
      this.props.closeModal(!visible);
    };
    render() {
      const { visible } = this.props;
      const { getFieldDecorator } = this.props.form;
      return (
        <Modal
          visible={visible}
          title="Thêm tác giả"
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
                    message: 'Tên tac giả không được để trống !',
                  },
                ],
              })(<Input />)}
            </FormItem>
            <FormItem label="Năm sinh">
              {getFieldDecorator('birthDate')(
                <InputNumber style={{ width: '100%' }} />,
              )}
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
export default ModalAddAuthor;
