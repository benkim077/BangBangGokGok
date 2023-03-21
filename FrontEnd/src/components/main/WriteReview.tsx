import React, { useState, useEffect } from "react";
import Box from "@mui/material/Box";
import Modal from "@mui/material/Modal";
import styled from "styled-components";
import Line from "@/assets/common/Line.png";
import { theme } from "@/styles/theme";
import { Rating, RatingProps } from "@mui/material";
import ToggleButton from "@mui/material/ToggleButton";
import { styled as mstyled } from "@mui/material/styles";
import ToggleButtonGroup from "@mui/material/ToggleButtonGroup";
import InputBase from "@mui/material/InputBase";

interface IRatingData {
  rating: number;
  difficulty: number;
  fear: number;
  activity: number;
}
interface ITimeData {
  hour: number;
  min: number;
  sec: number;
}
interface IProps {
  childOpen: boolean;
  handleClose: () => void;
  data: IDetailData;
  themeId: number;
}
export default function WriteReview({
  childOpen,
  handleClose,
  data,
  themeId,
}: IProps) {
  const [isSuccess, setIsSuccess] = useState<string>("false");
  const [content, setContent] = useState<string>("");
  const [rate, setRate] = useState<IRatingData>({
    rating: 0.0,
    difficulty: 0.0,
    fear: 0.0,
    activity: 0.0,
  });
  const [time, setTime] = useState<ITimeData>({ hour: 0, min: 0, sec: 0 });

  const [postdata, setPostdata] = useState<IPostData>(initData);

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    switch (name) {
      case "hour":
        setTime({ ...time, hour: parseInt(value) });
        break;
      case "minute":
        setTime({ ...time, min: parseInt(value) });
        break;
      case "second":
        setTime({ ...time, sec: parseInt(value) });
    }
    console.log(time);
  };
  const handleTextareaChange = (
    event: React.ChangeEvent<HTMLTextAreaElement>
  ) => {
    setContent(event.target.value);
    console.log(content);
  };
  const handleValueChange = (
    event: React.MouseEvent<HTMLElement>,
    newValue: string | null
  ) => {
    if (newValue !== null) {
      setIsSuccess(newValue);
    }
  };
  const handleRatingChange = (
    e: React.SyntheticEvent<Element, Event>,
    value: number
  ) => {
    const { name } = e.target as HTMLButtonElement;
    setRate((prevData) => ({
      ...prevData,
      [name]: value ?? 0,
    }));
  };

  const handleSubmit = async (e: React.SyntheticEvent) => {
    e.preventDefault();
    try {
      await setPostdata(() => ({
        themeId: themeId,
        content: content,
        rating: rate.rating,
        activity: rate.activity,
        fear: rate.fear,
        difficulty: rate.difficulty,
        isSuccess: isSuccess,
      }));
      console.log(postdata);
    } catch (err) {
      console.log(err);
    }
    handleClose();
  };

  const ratings: RatingProps[] = [
    { name: "rating", emptyLabelText: "후기 평점", value: rate["rating"] ?? 0 },
    {
      name: "difficulty",
      emptyLabelText: "체감 난이도",
      value: rate["difficulty"] ?? 0,
    },
    {
      name: "fear",
      emptyLabelText: "체감 공포도",
      value: rate["fear"] ?? 0,
    },
    {
      name: "activity",
      emptyLabelText: "체감 활동성",
      value: rate["activity"] ?? 0,
    },
  ];
  return (
    <React.Fragment>
      <Modal open={childOpen} onClose={handleClose}>
        <Box sx={{ ...style }}>
          <Header>
            후기 작성하기
            <img
              src={Line}
              alt="line"
              style={{ width: "100%", margin: "1rem auto" }}
            />
          </Header>
          <InfoBox>
            테마명 :&nbsp; <div className="title">{data.title}</div>
          </InfoBox>
          <InfoBox>
            <div className="info">
              성공 여부 &nbsp;&nbsp;
              <ToggleButtonGroup
                value={isSuccess}
                exclusive
                onChange={handleValueChange}
              >
                <CustomToggleButton value="success">성공</CustomToggleButton>
                <CustomToggleButton value="fail">실패</CustomToggleButton>
              </ToggleButtonGroup>
            </div>
            <div className="info">
              남은 시간 &nbsp;{" "}
              <div className="title">
                <CustomIput
                  name="hour"
                  onChange={handleChange}
                  value={time.hour}
                />
                시
                <CustomIput
                  name="minute"
                  onChange={handleChange}
                  value={time.min}
                />
                분
                <CustomIput
                  name="second"
                  onChange={handleChange}
                  value={time.sec}
                />
                초
              </div>
            </div>
          </InfoBox>

          <ReviewBox>
            <RatingWrapper>
              {ratings.map(({ emptyLabelText, name, value }) => (
                <div className="ratingItem" key={name}>
                  <div className="title">
                    {emptyLabelText}
                    <Rating
                      sx={{
                        "& .MuiRating-iconEmpty": {
                          stroke: "white",
                        },
                        fontSize: "1.8rem",
                        marginLeft: "1rem",
                      }}
                      precision={0.5}
                      name={name}
                      value={value}
                      onChange={handleRatingChange}
                    />
                    <div />
                  </div>
                </div>
              ))}
            </RatingWrapper>
            <form>
              <CustomText value={content} onChange={handleTextareaChange} />
            </form>
          </ReviewBox>
          <ButtonWrapper>
            <CancelButton onClick={handleClose}>취소</CancelButton>
            <WriteButton onClick={handleSubmit}>등록</WriteButton>
          </ButtonWrapper>
        </Box>
      </Modal>
    </React.Fragment>
  );
}

const CustomIput = mstyled(InputBase)`
width: 4rem;
border: solid 2px white;
border-radius: 0.5rem;
color: white;
height: 85%;
font-size: 1.4rem;
padding: 0 1rem;
margin: 0 0.5rem;
max-width: 4rem;
box-sizing: border-box;
`;

const CustomToggleButton = mstyled(ToggleButton)({
  padding: "0.5rem 1.5rem",
  fontSize: "1.5rem",
  borderRadius: "0.8rem",
  border: `solid 2px white`,
  fontFamily: "Pretendard",
  color: "white",
  "&.Mui-selected, &.Mui-selected:hover": {
    color: "white",
    backgroundColor: `${theme.colors.pink}`,
    fontWeight: "bold",
  },
});

const CustomText = styled.textarea`
  font-size: 1.4rem;
  padding: 2rem;
  width: 95%;
  padding: 1rem;
  height: 10rem;
  border: solid 2px white;
  color: white;
  border-radius: 1rem;
  background-color: transparent;
  margin-top: 2rem;
  resize: none;
`;
const Header = styled.div`
  font-size: 1.6rem;
  font-weight: bold;
`;

const InfoBox = styled.div`
  display: flex;
  font-size: 1.8rem;
  margin-top: 2rem;
  align-items: center;
  .info {
    align-items: center;

    width: 50%;
    display: flex;
  }
`;
const RatingWrapper = styled.div`
  display: flex;
  font-size: 1.4rem;
  justify-content: space-between;
`;
const ReviewBox = styled.div`
  margin-top: 2rem;
  display: flex;
  flex-direction: column;
  .title {
    font-size: 1.6rem;
    margin-right: 1.5rem;
  }
  .ratingItem {
    font-size: 1.4rem;
    font-weight: ${theme.fontWeight.medium};
    color: white;
    display: flex;
    align-items: center;
  }
`;
const style = {
  position: "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: "48%",
  height: "55%",
  bgcolor: "#3E2133",
  borderRadius: 10,
  boxShadow: 24,
  padding: "4rem 6rem",
  color: "white",
};

const ButtonWrapper = styled.div`
  display: flex;
  margin-top: 2rem;
`;
const CancelButton = styled.div`
  border: solid 2px white;
  color: white;
  background-color: ${theme.colors.container};
  font-size: 1.6rem;
  margin-left: auto;
  border-radius: 1rem;
  text-align: center;
  padding: 1rem 1.8rem;
  font-weight: bold;
  cursor: pointer;
  &:hover {
    color: ${theme.colors.container};
    background-color: white;
    border: solid 2px ${theme.colors.container};
  }
`;
const WriteButton = styled.div`
  margin-left: 2rem;
  border: solid 2px ${theme.colors.container};
  color: ${theme.colors.container};
  background-color: white;
  font-size: 1.6rem;
  border-radius: 1rem;
  text-align: center;
  padding: 1rem 1.8rem;
  font-weight: bold;
  cursor: pointer;
  &:hover {
    color: white;
    background-color: ${theme.colors.container};
    border: solid 2px white;
  }
`;

const initData: IPostData = {
  themeId: 1, // 테마 id
  content: "", // 리뷰 내용
  rating: 0.0, // 평점
  activity: 0.0, // 활동성
  fear: 0.0, // 공포도
  difficulty: 0.0, // 체감 난이도
  isSuccess: 1,
  recordHH: 1,
  recordSS: 24,
  recordMM: 33, // 성공 여부 (0:실패, 1:성공)
};
