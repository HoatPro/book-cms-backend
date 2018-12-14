import styled from 'styled-components';

const LayoutWrapper = styled.div`
  display: flex;
  flex-direction: column;
  .wrapper {
    width: 1360px;
    height: 100%;
  }
  .header {
    width: 100%;
    height: 50px;
    background-color: #c8c8c8;
  }
  .main_wrapper {
    width: 100%;
    height: 100%;
    border: 1px solid #ddd;
  }
  .sidebar {
    background-color: white;
    width: 15%;
    height: 100%;
    float: left;
  }

  .content {
    width: 85%;
    height: 100%;
    margin-left: 204px;
    border: 1px solid #ddd;
    border-bottom-color: white;
    border-top-color: white;
    border-right-color: white;
  }
  .footer {
    width: 100%;
    height: 10%;
    border: 1px solid #ddd;
    border-top: none;
    float: right;
  }
`;
const HeaderWrapper = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;
  .header-wrapper h3 {
    margin-left: 20px;
    margin-top: 10px;
    color: white;
    float: left;
  }
  .info-wrapper {
   margin-left: 96%;
   margin-top:10px;
  }
`;
const SiderWrapper = styled.div`
  display: flex;
  flex-direction: column;
  font-family: Arial;
  font-size: 16px;
  .ant-menu.ant-menu-light.ant-menu-root.ant-menu-vertical{
    border:none;
  }
  padding-left:10px;
`;
const ContentWrapper = styled.div`
    background-color: white;
    padding-left: 20px;
  hr {
    border: 1px solid #ddd;
  }
`;
const FooterWrapper = styled.div`
  display: flex;
  flex-direction: column;
  padding-left:24px;
`;

export { LayoutWrapper, HeaderWrapper, SiderWrapper, FooterWrapper, ContentWrapper};
