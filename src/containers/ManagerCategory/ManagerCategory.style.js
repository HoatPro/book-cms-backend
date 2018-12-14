import styled from 'styled-components';

const ManagerCategoryWrapper = styled.div`
  margin-top: 10px;
  font-size: 2rem;
  .manager-category {
    /* height: 100vh; */
    display: block;
    overflow: auto;
  }
  .action {
    margin-bottom: 30px;
  }
  .search {
    width: 40%;
    margin-left: 20%;
  }
  .table-detail {
    float: left;
    width: 95%;
  }
  .table-category {
    margin-bottom:10%;

  }
`;

export { ManagerCategoryWrapper };
