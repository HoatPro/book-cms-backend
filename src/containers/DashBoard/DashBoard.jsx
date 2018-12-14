import React from 'react';
import { List, Card, Table } from 'antd';
const data = [
  {
    title: 'Title 1',
  },
  {
    title: 'Title 2',
  },
  {
    title: 'Title 3',
  },
  {
    title: 'Title 4',
  },
  {
    title: 'Title 5',
  },
  {
    title: 'Title 6',
  },
];

class DashBoard extends React.Component {
  render() {
    const data1 = [
      {
        key: '1',
        countBook: '15 quyển',
        countAuthor: 18,
        countCategory: 20,
      },
      // {
      //   key: '2',
      //   name: 'John',
      //   age: 42,
      //   address: '10 Downing Street',
      // },
    ];

    const columns = [
      {
        title: 'Tổng số sách',
        dataIndex: 'countBook',
        key: 'countBook',
      },
      {
        title: 'Tổng số tác giả',
        dataIndex: 'countAuthor',
        key: 'countAuthor',
      },
      {
        title: 'Tổng số thể loại',
        dataIndex: 'countCategory',
        key: 'countCategory',
      },
    ];
    return (
      <div>
        <h2>Đây là trang tổng quan</h2>
        <List
          grid={{ gutter: 16, xs: 1, sm: 2, md: 4, lg: 4, xl: 6, xxl: 3 }}
          dataSource={data}
          renderItem={item => (
            <List.Item>
              <Card title={item.title}>Card content</Card>
            </List.Item>
          )}
        />
        <h2>Bảng thông tin </h2>
        <hr />
        <Table columns={columns} dataSource={data1} />
        <h2>Đây là trang tổng quan</h2>
      </div>
    );
  }
}
export default DashBoard;
