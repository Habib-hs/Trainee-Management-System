import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import { Container, Card, CardImg, CardImgOverlay, CardTitle, Row, Col } from "reactstrap";
import placeholderImage from "../../assets/images/bg/bg3.jpg";
import TraineeMessage from "../../components/TraineeMessage";
import NoticeComponent from "../../components/NoticeComponent";


const TraineeClassroom = () => {
  const { classroomName } = useParams();

  // State to store the user data
  const [userData, setUserData] = useState(null);
  const [classId, setClassId] = useState(null);
  const [loading, setLoading] = useState(true);

  // Fetch user data from localStorage when the component mounts
  useEffect(() => {
    const userDataString = localStorage.getItem("user");
    if (userDataString) {
      const userData = JSON.parse(userDataString);
      setUserData(userData);
    }
  }, []);

  // Make API call when userData is set or updated
  useEffect(() => {
    if (userData) {
      const { role, id } = userData;

      if (role === "TRAINEE") {
        axios
          .get(`http://localhost:9080/trainee/classroom/${id}`,{
            headers:{
              Authorization : `Bearer ${localStorage.getItem('token')}`,
              'Content-Type' : 'application/json'
            }
          })
          .then((response) => {
            setClassId(response.data);
            setLoading(false);
          })
          .catch((error) => {
            console.error("Error fetching Trainee data:", error);
            setLoading(false);
          });
      } 
    }
  }, [userData]);

  return (
    <Container>
      <div>
        {/* Render the loading message or placeholder if loading is true */}
        {loading ? (
          <div>Loading...</div>
        ) : (
          <Card inverse>
            <CardImg alt="Card image cap" src={placeholderImage} style={{ height: 170 }} width="100%" />
            <CardImgOverlay className="overlay">
              <CardTitle tag="h5">{classroomName}</CardTitle>
            </CardImgOverlay>
          </Card>
        )}
      </div>
      <Row className="mt-5">
        <Col md={8}>
        <div> <h3>Posts</h3></div>
          <TraineeMessage classroomId={classId} />
        </Col>
        <Col md={4}>
        <div> <h3>Notice</h3></div>
          <NoticeComponent classroomId={classId} />
        </Col>
      </Row>
    </Container>
  );
};

export default TraineeClassroom;
