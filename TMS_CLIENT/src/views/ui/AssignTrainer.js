import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Form, FormGroup, Label, Input, Button } from 'reactstrap';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const AssignTrainer = () => {
  const [batchId, setBatchId] = useState('');
  const [trainerList, setTrainerList] = useState([]);
  const [selectedTrainers, setSelectedTrainers] = useState([]);
  const [batchList, setBatchList] = useState([]);

  useEffect(() => {
    fetchBatchNames();
    fetchTrainers();
  }, []);

  const fetchBatchNames = async () => {
    try {
      const response = await axios.get('http://localhost:9080/batch/get/allName', {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      });
      
      const batches = response.data.Batches.map((batch) => ({
        id: batch.id,
        name: batch.name,
      }));
      setBatchList(batches);
    } catch (error) {
      console.error('Error fetching batch names:', error);
    }
  };

  const fetchTrainers = async () => {
    try {
      const response = await axios.get('http://localhost:9080/trainer/get/all', {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      });
      console.log(response.data);
      const trainers = response.data.Trainer.map((trainer) => ({
        id: trainer.id,
        name: trainer.fullName,
      }));
      setTrainerList(trainers);
    } catch (error) {
      console.error('Error fetching trainers:', error);
    }
  };

  const handleBatchChange = (e) => {
    const selectedBatchId = e.target.value;
    setBatchId(selectedBatchId);
  };

  const handleTrainerCheckboxChange = (trainerId) => {
    const isSelected = selectedTrainers.includes(trainerId);
    setSelectedTrainers([...selectedTrainers, trainerId]);
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    // Prepare the request body
    const requestBody = {
      batchId: batchId,
      trainerIds: selectedTrainers,
    };

    try {
      // Send the POST request to the backend API
      const response = await axios.post('http://localhost:9080/assign_trainer', requestBody, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
          'Content-Type': 'application/json',
        },
      });

      console.log('Response:', response.data);
      toast.success('Trainers assigned successfully');
      console.log('Response:', response.data);

      // Reset the form fields to their initial values
      setBatchId('');
      setSelectedTrainers([]);

      // Fetch updated trainers
      fetchTrainers();
    } catch (error) {
      toast.error('Error assigning trainers');
      console.error('Error assigning trainers:', error);
    }
  };

  return (
    <div style={{ backgroundColor: '#f8f9fa', minHeight: '20vh', marginBottom: '150px' }}>
      <Container>
        <Row className="justify-content-center align-items-center" style={{ minHeight: '55vh' }}>
          <Col md={6}>
            <Form onSubmit={handleFormSubmit}>
              <h2 className="text-center mb-4">Assign Trainers to Batch</h2>
              <FormGroup>
                <Label for="batch">Batch</Label>
                <Input
                  type="select"
                  id="batch"
                  value={batchId}
                  onChange={handleBatchChange}
                  required
                >
                  <option value="">Select a batch</option>
                  {batchList.map((batch) => (
                    <option key={batch.id} value={batch.id}>
                      {batch.name}
                    </option>
                  ))}
                </Input>
              </FormGroup>

              <FormGroup>
                <Label for="trainerList">Trainer List</Label>
                <div id="trainerList">
                  {trainerList.map((trainer) => (
                    <div key={trainer.id}>
                      <input
                        type="checkbox"
                        id={`trainer-${trainer.id}`}
                        value={trainer.id}
                        checked={selectedTrainers.includes(trainer.id)}
                        onChange={() => handleTrainerCheckboxChange(trainer.id)}
                      />
                      <label htmlFor={`trainer-${trainer.id}`}>{trainer.name}</label>
                    </div>
                  ))}
                </div>
              </FormGroup>

              <Button color="primary" type="submit" block>
                Assign Trainers
              </Button>
            </Form>
          </Col>
        </Row>
      </Container>
      <ToastContainer />
    </div>
  );
};

export default AssignTrainer;
