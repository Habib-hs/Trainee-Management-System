import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Form, FormGroup, Label, Input, Button } from 'reactstrap';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const Assignment = () => {
  const [formData, setFormData] = useState({
    scheduleId: '',
    name: '',
    type: '',
    deadline: '',
    trainerId: '',
    description: '',
    file: null,
  });
  const [scheduleList, setScheduleList] = useState([]);
  const [userData, setUserData] = useState(null);

  useEffect(() => {
    const userDataString = localStorage.getItem('user');
    if (userDataString) {
      const userData = JSON.parse(userDataString);
      setUserData(userData);
      fetchSchedules(userData.id);
    }
  }, []);
  
  const fetchSchedules = async (userId) => {
    try {
      const response = await axios.get('http://localhost:9080/schedule-batch',{
        headers:{
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        }
      });
      const schedules = response.data.Schedules.filter((schedule) => schedule.trainerId === userId)
        .map((schedule) => ({
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
    try {
      const formDataToSend = new FormData();
      formDataToSend.append('scheduleId', formData.scheduleId);
      formDataToSend.append('name', formData.name);
      formDataToSend.append('type', formData.type);
      formDataToSend.append('deadline', formData.deadline);
      formDataToSend.append('trainerId', userData.id )
      formDataToSend.append('description', formData.description);
      if (formData.file !== null) {
        formDataToSend.append('file', formData.file);
      }
  
      const response = await axios.post('http://localhost:9080/assignment', formDataToSend, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
          'Content-Type': 'multipart/form-data',
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
              <h2 className="text-center mb-4">Create Assignment</h2>
              <FormGroup row>
                <Label for="scheduleId" sm={4}>
                  Course Name
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
                    <option value="">Select a Course</option>
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
                <Label for="description" sm={4}>
                  Description
                </Label>
                <Col sm={8}>
                  <Input
                    type="textarea"
                    id="description"
                    name="description"
                    value={formData.description}
                    onChange={handleInputChange}
                    required
                  />
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
                Create Assignment
              </Button>
            </Form>
          </Col>
        </Row>
      </Container>
      <ToastContainer />
    </div>
  );
};

export default Assignment;
