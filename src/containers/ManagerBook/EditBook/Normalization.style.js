import styled from 'styled-components';

const NormalizationWrapper = styled.div`
  margin-top: 10px;
  font-weight: 700;
  .context {
    font-weight: normal;
  }
  .editable-cell {
    position: relative;
  }
  .editable-cell-value-wrap {
    padding: 5px 12px;
    cursor: pointer;
  }
  .editable-row:hover .editable-cell-value-wrap {
    border: 1px solid #d9d9d9;
    border-radius: 4px;
    padding: 4px 11px;
  }
  .table-expand{
    background-color:red;
  }
`;

export { NormalizationWrapper };
