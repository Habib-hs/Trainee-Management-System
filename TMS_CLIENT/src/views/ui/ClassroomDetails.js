import React, { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";
import { Container, Card, CardImg, CardImgOverlay, CardTitle, Row, Col } from "reactstrap";
import placeholderImage from "../../assets/images/bg/bg3.jpg";
import NoticeComponent from "../../components/NoticeComponent";
import axios from "axios";
import TrainerMessage from "../../components/TrainerMessage";
import Notice from "./Notice";

const ClassroomDetails = () => {
  const { classroomName } = useParams();
  const [classId, setClassId] = useState(null);
  const [loading, setLoading] = useState(true);
  
  useEffect(() => {
    axios
      .get(`http://localhost:9080/classroom/${classroomName}`, {
        headers:{
              Authorization: `Bearer ${localStorage.getItem('token')}`
        },
      })
      .then((response) => { 
        setClassId(response.data);
        setLoading(false);
      })
      .catch((error) => {
      //  console.error("Error fetching classroomId:", error);
        setLoading(false);
      });
  }, [classroomName]);
  return (
    <Container>
      <div>
        <Card inverse>
          <CardImg alt="Card image cap" src={placeholderImage} style={{ height: 170 }} width="100%" />
          <CardImgOverlay className="overlay">
            <CardTitle tag="h5">{classroomName}</CardTitle>
          </CardImgOverlay>
        </Card>
      </div>
      <div>
      { classId !== null && <Notice classroomId={classId} />}
      </div>
      <Row className="mt-5">
        <Col md={8}>
        <div> <h3>Posts</h3></div>
        { classId !== null && <TrainerMessage classroomId={classId} />}
        </Col>
        <Col md={4}>
        <div> <h3>Notice</h3></div>
          {classId !== null && <NoticeComponent classroomId={classId} />}
        </Col>
      </Row>
    </Container>
  );
};

export default ClassroomDetails;
