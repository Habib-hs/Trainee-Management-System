import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Form, FormGroup, Label, Input, Button } from 'reactstrap';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import { useParams } from 'react-router-dom';
import 'react-toastify/dist/ReactToastify.css';

const AssignmentUpdate = () => {
 const { assignmentNumber } = useParams();
  const [formData, setFormData] = useState({
    scheduleId: '',
    name: '',
    type: '',
    deadline: '',
    file: null,
  });
  const [scheduleList, setScheduleList] = useState([]);

  useEffect(() => {
    fetchSchedules();
  }, []);

  const fetchSchedules = async () => {
    try {
      const response = await axios.get('http://localhost:9080/schedule-batch', {
        headers:{
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        }
      });
      const schedules = response.data.Schedules.map((schedule) => ({
        id: schedule.id,
        name: schedule.name,
      }));
      setScheduleList(schedules);
    } catch (error) {
      console.error('Error fetching schedules:', error);
    }
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    if (!assignmentNumber) {
    // Assignment number is not available yet, return early or show loading
    console.log('Assignment number is not available yet.');
    return;
  }
    try {
      const formDataToSend = new FormData();
      formDataToSend.append('scheduleId', formData.scheduleId);
      formDataToSend.append('name', formData.name);
      formDataToSend.append('type', formData.type);
      formDataToSend.append('deadline', formData.deadline);
      if (formData.file !== null) {
        formDataToSend.append('file', formData.file);
      }
  
    //  console.log(formDataToSend)
    console.log(assignmentNumber)
    const response = await axios.put(`http://localhost:9080/assignment/update/${assignmentNumber}`, formDataToSend, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
          'Content-Type': 'multipart/form-data'
        },
      });

      toast.success(response.data);

      setFormData({
        scheduleId: '',
        name: '',
        type: '',
        deadline: '',
        file: null,
      });
    } catch (error) {
      toast.error('Error creating assignment');
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevFormData) => ({
      ...prevFormData,
      [name]: value,
    }));
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    setFormData((prevFormData) => ({
      ...prevFormData,
      file: file || null,
    }));
  };

  return (
    <div style={{ backgroundColor: '#f8f9fa', minHeight: '50vh' }}>
      <Container>
        <Row className="justify-content-center align-items-center mt-2" style={{ minHeight: '75vh' }}>
          <Col md={6}>
            <Form onSubmit={handleFormSubmit}>
              <h2 className="text-center mb-4">Update Assignment</h2>
              <FormGroup row>
                <Label for="scheduleId" sm={4}>
                  Schedule Name
                </Label>
                <Col sm={8}>
                  <Input
                    type="select"
                    id="scheduleId"
                    name="scheduleId"
                    value={formData.scheduleId}
                    onChange={handleInputChange}
                    required
                  >
                    <option value="">Select a schedule</option>
                    {scheduleList.map((schedule) => (
                      <option key={schedule.id} value={schedule.id}>
                        {schedule.name}
                      </option>
                    ))}
                  </Input>
                </Col>
              </FormGroup>

              <FormGroup row>
                <Label for="name" sm={4}>
                  Assignment Name
                </Label>
                <Col sm={8}>
                  <Input
                    type="text"
                    id="name"
                    name="name"
                    value={formData.name}
                    onChange={handleInputChange}
                    required
                  />
                </Col>
              </FormGroup>

              <FormGroup row>
                <Label for="type" sm={4}>
                  Type
                </Label>
                <Col sm={8}>
                  <Input
                    type="select"
                    id="type"
                    name="type"
                    value={formData.type}
                    onChange={handleInputChange}
                    required
                  >
                    <option value="">Select a type</option>
                    <option value="Daily Task">Daily Task</option>
                    <option value="Assignment">Assignment</option>
                    <option value="Mini Project">Mid Term Mini Project</option>
                    <option value="Final Project">Final Project</option>
                  </Input>
                </Col>
              </FormGroup>

              <FormGroup row>
                <Label for="deadline" sm={4}>
                  Deadline
                </Label>
                <Col sm={8}>
                  <Input
                    type="date"
                    id="deadline"
                    name="deadline"
                    value={formData.deadline}
                    onChange={handleInputChange}
                    required
                  />
                </Col>
              </FormGroup>

              <FormGroup row>
                <Label for="file" sm={4}>
                  File
                </Label>
                <Col sm={8}>
                  <Input type="file" id="file" name="file" onChange={handleFileChange} />
                </Col>
              </FormGroup>

              <Button color="primary" type="submit" block>
                Update Assignment
              </Button>
            </Form>
          </Col>
        </Row>
      </Container>
      <ToastContainer />
    </div>
  );
};

export default AssignmentUpdate;
