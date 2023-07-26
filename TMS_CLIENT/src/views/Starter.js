import React from "react";
import { Col, Row } from "reactstrap";
import AllTraineeList from "./ui/AllTraineeList";
import AdminTraineeList from "../components/dashboard/AdminTraineeList";
import AdminBatchList from "../components/dashboard/AdminBatchList";
import AdminCourseList from "../components/dashboard/AdminCourseList";
import AdminScheduledPrograms from "../components/dashboard/AdminSchedulePrograms";
import AdminTrainerList from "../components/dashboard/AdminTrainerList";

const Starter = () => {
  return (
    <div>
      {/* Welcome Message */}
      <Row className="mb-3">
        <Col>
          <h1>Welcome, Admin!</h1>
          <p>
            Here, you can view and manage all the batches, trainers, trainees, courses, and more.
          </p>
        </Col>
      </Row>

      {/* AdminBatchList */}
      <Row className="mb-3">
        <Col>
          <AdminBatchList />
        </Col>
      </Row>

      {/* AdminCourseList */}
      <Row className="mb-3">
        <Col>
          <AdminCourseList />
        </Col>
      </Row>

      {/* AdminScheduledPrograms */}
      <Row className="mb-3">
        <Col>
          <AdminScheduledPrograms />
        </Col>
      </Row>

      {/* AdminTrainerList */}
      <Row className="mb-3">
        <Col>
          <AdminTrainerList />
        </Col>
      </Row>

      {/* AdminTraineeList */}
      <Row className="mb-3">
        <Col>
          <AdminTraineeList />
        </Col>
      </Row>
    </div>
  );
};

export default Starter;
