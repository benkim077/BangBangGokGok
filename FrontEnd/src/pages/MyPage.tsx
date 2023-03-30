import React from "react";
import Header from "@components/common/Header";
import LeftNavBar from "@components/mypage/LeftSection/LeftNavBar";
import RightContent from "@components/mypage/RightSection/RightContent";
import styled from "styled-components";
import { theme } from "@/styles/theme";

export default function MyPage() {
  return (
    <>
      <Header />
      <BackGround>
        <ContentWrapper>
          <LeftNavBar />
          <RightContent />
        </ContentWrapper>
      </BackGround>
    </>
  );
}

const BackGround = styled.div`
  height: 90vh;
  background-color: ${theme.colors.background};
`;

const ContentWrapper = styled.div`
  width: 90%;
  box-sizing: border-box;
  height: 95%;
  margin: 0 auto;
  padding: 3rem;
  @media (max-height: 800px) {
    padding: 2rem;
  }
  gap: 1rem;
  display: flex;
  flex-direction: row;
  align-items: center;
  align-items: flex-start;
  background-color: ${theme.colors.containerLight};
  border-radius: 1.5rem;
`;
