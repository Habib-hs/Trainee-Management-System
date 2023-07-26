import React from 'react';
import { Container, Row, Col, Button } from 'reactstrap';
import { Link, useSearchParams } from 'react-router-dom';

const ViewSubmittedAssignmentPage = () => {
    const [searchParams] = useSearchParams();
    const assignment = JSON.parse(searchParams.get('assignment'));

  // Handle the data from the 'assignment' prop and render the details as needed

  return (
    <div style={{ backgroundColor: '#f8f9fa', minHeight: '100vh' }}>
      <Container>
        <Row>
          <Col>
            <h2 className="mt-4">View Submitted Assignment</h2>
            <div>
              <h3>Assignment Details:</h3>
              <p>Schedule Name: {assignment.scheduleName}</p>
              <p>Assignment Name: {assignment.name}</p>
              <p>Assignment Type: {assignment.type}</p>
              <p>Deadline: {new Date(assignment.deadline).toLocaleDateString()}</p>
            </div>
            <div>
              <h3>Submitted Assignments:</h3>
              {assignment.submittedAssignment.length === 0 ? (
                <p>No submissions yet.</p>
              ) : (
                <ul>
                  {assignment.submittedAssignment.map((submission) => (
                    <li key={submission.id}>
                      Trainee ID: {submission.traineeId} (Trainee Name: {submission.traineeName})
                      <br />
                      Submit File URL: {submission.submitFileUrl}
                      <br />
                      Submitted Time: {new Date(submission.createdTime).toLocaleString()}
                    </li>
                  ))}
                </ul>
              )}
            </div>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default ViewSubmittedAssignmentPage;
