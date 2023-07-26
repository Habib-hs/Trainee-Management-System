import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Form, FormGroup, Label, Input, Button } from 'reactstrap';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const CreateCourse = () => {
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    assignedTrainerId: '',
    trainerName: '',
  });
  const [trainerList, setTrainerList] = useState([]);

  useEffect(() => {
    fetchTrainers();
  }, []);

  const fetchTrainers = async () => {
    try {
      const response = await axios.get('http://localhost:9080/trainer/get/allName',{
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      })
      const trainers = response.data.Trainers.map((trainer) => ({
        id: trainer.id,
        name: trainer.name,
      }));
      setTrainerList(trainers);
    } catch (error) {
      console.error('Error fetching trainers:', error);
    }
  };
  
  

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    try {
      console.log(formData);
      const response = await axios.post('http://localhost:9080/course', formData, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
          'Content-Type': 'application/json',
          
        },
      });
      toast.success(response.data);
      setFormData({
        name: '',
        description: '',
        assignedTrainerId: '',
        trainerName: '',
      });
    } catch (error) {
      if (error.response && error.response.data) {
        toast.error(error.response.data);
      } else {
        toast.error('Failed to create Assignment');
      }
    }
  };
  

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevFormData) => ({
      ...prevFormData,
      [name]: value,
    }));
  };

  const handleTrainerChange = (e) => {
    const selectedTrainer = trainerList.find((trainer) => trainer.name === e.target.value);
    setFormData((prevFormData) => ({
      ...prevFormData,
      trainerName: e.target.value,
      assignedTrainerId: selectedTrainer ? selectedTrainer.id : '',
    }));
  };

  return (
    <div style={{ backgroundColor: '#f8f9fa', minHeight: '30vh' }}>
      <Container>
        <Row className="justify-content-center align-items-center" style={{ minHeight: '55vh' }}>
          <Col md={6}>
            <Form onSubmit={handleFormSubmit}>
              <h2 className="text-center mb-4">Create Course</h2>
              <FormGroup>
                <Label for="name">Course Name</Label>
                <Input
                  type="text"
                  id="name"
                  name="name"
                  value={formData.name}
                  onChange={handleInputChange}
                  required
                />
              </FormGroup>

              <FormGroup>
                <Label for="description">Description</Label>
                <Input
                  type="textarea"
                  id="description"
                  name="description"
                  value={formData.description}
                  onChange={handleInputChange}
                  required
                />
              </FormGroup>

              <FormGroup>
                <Label for="trainerName">Trainer Name</Label>
                <Input
                  type="select"
                  id="trainerName"
                  name="trainerName"
                  value={formData.trainerName}
                  onChange={handleTrainerChange}
                  required
                >
                  <option value="">Select a trainer</option>
                  {trainerList.map((trainer) => (
                    <option key={trainer.id} value={trainer.name}>
                      {trainer.name}
                    </option>
                  ))}
                </Input>
              </FormGroup>

              <FormGroup>
                <Label for="assignedTrainerId">Assigned Trainer ID</Label>
                <Input
                  type="number"
                  id="assignedTrainerId"
                  name="assignedTrainerId"
                  value={formData.assignedTrainerId}
                  onChange={handleInputChange}
                  disabled
                />
              </FormGroup>

              <Button color="primary" type="submit" block>
                Create Course
              </Button>
            </Form>
          </Col>
        </Row>
      </Container>
      <ToastContainer />
    </div>
  );
};

export default CreateCourse;
