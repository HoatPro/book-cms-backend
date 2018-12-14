import styled from 'styled-components';

const AddbookUIStyle = styled.div`
  margin-top: 10px;
  font-family: Arial;
  .ant-breadcrumb a {
    font-size: 18px;
    color: black;
    font-weight: 600;
  }
  h3 {
    margin-top: 10px;
  }
  h4 {
    margin-top: 10px;
  }
  .form_add_infobook {
    border: 1px solid #ddd;
    margin-top: 10px;
    width: 80%;
    height: 500px;
    padding-left: 90px;
    margin-left: 90px;
  }
  .ant-row.ant-form-item {
    float: left;
    width: 50%;
  }

  .ant-form-item-required {
    float: left;
    font-size: 16px;
  }
  /* .ant-select-selection {
    background-color: #40a9ff;
  } */
  .tooltip {
    font-size: 14px;
  }
  .add_chapter {
    margin-top: 10px;
    margin-left: 90px;
    height: 40px;
  }
  .upload_chapter {
    margin-left: 625px;
  }
  .upload_chapter button {
    width: 140px;
    background-color: #ffa500;
    height: 40px;
    color: white;
  }
  .upload_chapter button:hover {
    background-color: #ffbf48;
    border: none;
  }
  .dropdown-action {
    margin-left: 90px;
    margin-bottom: 20px;
    margin-top: 8px;
    height: 35px;
  }
  hr {
    width: 80%;
    margin-top: 10px;
    margin-left: 90px;
  }

  .ant-table-content {
    margin-left: 90px;
    margin-right: 140px;
  }

  .pick_time_puslish {
    width: 322px;
  }
  .number_page {
    width: 322px;
  }
  .ant-pagination.ant-table-pagination {
    margin-right: 200px;
  }
  .addbook {
    margin: 30px;
    width: 150px;
    height: 40px;
    margin-left: 800px;
  }
  .table-addchapter {
    margin-top: 15px;
    border: 1px solid #ddd;
    width: 80%;
    margin-left: 90px;
    padding-left: 20px;
  }
  .table-thead {
    background-color: #cfcdcf;
  }
  td,
  tr {
    height: 50px;
    padding-left: 10px;
  }
  .chapter-table {
    width: 70%;
  }
  .content-table {
    color: #40a9ff;
  }

  .select-content select {
    width: 10px;
  }

  .ant-form-item-label.ant-col-xs-24.ant-col-sm-19 label {
    float: left;
  }
`;

export { AddbookUIStyle };
