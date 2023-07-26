import React, { useState, useEffect } from "react";
import {  Link } from "react-router-dom";
import { Container, Card, CardImg, CardImgOverlay, CardTitle, Row, Col } from "reactstrap";
import placeholderImage from "../../assets/images/bg/bg3.jpg";
import AdminNotice from "../../components/AdminNotice";
import AdminMessage from "../../components/AdminMessage";
import AdminNoticeComponent from "../../components/AdminNoticeComponent";
import AdminPostComponent from "../../components/AdminPostComponent";

const AdminClassroom = () => {
  const [classId, setClassId] = useState(null);
  const [loading, setLoading] = useState(true);
  const [classroom, setClassroom] = useState('1');
  

  return (
    <Container>
      <div>
        <Card inverse>
          <CardImg alt="Card image cap" src={placeholderImage} style={{ height: 170 }} width="100%" />
          <CardImgOverlay className="overlay">
          </CardImgOverlay>
        </Card>
      </div>
      <div>
         <div> <AdminNotice/></div>
      </div>
      <Row className="mt-5">
        <Col md={8}>
          <div> <h3>Posts</h3></div>
       {<AdminPostComponent/>}
        </Col>
        <Col md={4}>
        <div> <h3>Notice</h3></div>
      {<AdminNoticeComponent/>}
        </Col>
      </Row>
    </Container>
  );
};

export default  AdminClassroom;
