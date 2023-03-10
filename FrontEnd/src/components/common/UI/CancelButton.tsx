import React from "react";
import styled from "styled-components";
import { theme } from "@/styles/theme";

export default function CancelButton({ children }) {
  return <Button value="cancel">{children}</Button>;
}

const Button = styled.button`
  color: ${theme.colors.pink};
  background-color: ${theme.colors.white};
  border-radius: 10px;
  border: none;
  box-shadow: 2px 2px 4px rgba(0, 0, 0, 0.25);
  padding: 10px;
  transition: background-color 0.3s ease-in-out;
  cursor: pointer;
  &:hover {
    opacity: 0.9;
  }
  width: 100%;
`;
