import React from 'react';
import { Table, Button, Tag } from 'antd';
const columns = [
  {
    title: '#',
    dataIndex: 'index',
    key: 'index',
    width: '5%',
  },
  {
    title: 'Giọng',
    dataIndex: 'speak',
    key: 'speak',
  },
  {
    title: 'Trạng thái',
    dataIndex: 'status',
    key: 'status',
  },
  {
    title: 'BitRate',
    key: 'bitrate',
    dataIndex: 'bitrate',
  },
  {
    title: 'Rate',
    key: 'rate',
    dataIndex: 'rate',
  },
  {
    title: 'Tổng hợp',
    key: 'synthesis',
    render: record => <Button type="primary">Tổng hợp</Button>,
  },
];

const data = [
  {
    key: '1',
    index: '1',
    speak: 'Nam HN',
    status: '0/7',
    bitrate: ['developer'],
    rate: '',
  },
  {
    key: '2',
    index: '2',
    speak: 'Nữ HN',
    status: '7/7',
    bitrate: ['loser'],
    rate: '',
  },
  {
    key: '3',
    index: '3',
    speak: 'Nữ SG',
    status: '5/7',
    bitrate: ['teacher'],
    rate: '',
  },
];
class TableSysthesis extends React.Component {
  render() {
    return <Table columns={columns} dataSource={data} bordered />;
  }
}
export default TableSysthesis;
