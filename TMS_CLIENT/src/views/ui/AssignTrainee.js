import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Form, FormGroup, Label, Input, Button } from 'reactstrap';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const AssignTrainee = () => {
  const [batchId, setBatchId] = useState('');
  const [traineeList, setTraineeList] = useState([]);
  const [selectedTrainees, setSelectedTrainees] = useState([]);
  const [batchList, setBatchList] = useState([]);

  useEffect(() => {
    fetchBatchNames();
    fetchTrainees();
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

  const fetchTrainees = async () => {
    try {
      const response = await axios.get('http://localhost:9080/trainee/get/unassigned-trainee', {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      });
      const assignedTrainees = response.data.Trainees.filter((trainee) => trainee.batchId !== null);
      const unassignedTrainees = assignedTrainees.map((trainee) => ({
        id: trainee.id,
        name: trainee.fullName,
      }));
      setTraineeList(unassignedTrainees);
    } catch (error) {
      console.error('Error fetching trainees:', error);
    }
  };

  const handleBatchChange = (e) => {
    const selectedBatchId = e.target.value;
    setBatchId(selectedBatchId);
  };

  const handleTraineeCheckboxChange = (traineeId) => {
    const isSelected = selectedTrainees.includes(traineeId);
    if (isSelected) {
      setSelectedTrainees(selectedTrainees.filter((id) => id !== traineeId));
    } else {
      setSelectedTrainees([...selectedTrainees, traineeId]);
    }
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    // Prepare the request body
    const requestBody = {
      batchId: batchId,
      traineeIds: selectedTrainees,
    };

    try {
      // Send the POST request to the backend API
      const response = await axios.post('http://localhost:9080/assign-trainee', requestBody, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
          'Content-Type': 'application/json',
        },
      });
      toast.success('Trainees assigned successfully');
      console.log('Response:', response.data);

      // Reset the form fields to their initial values
      setBatchId('');
      setSelectedTrainees([]);

      // Fetch updated trainees
      fetchTrainees();
    } catch (error) {
      toast.error('Error assigning trainees');
      console.error('Error assigning trainees:', error);
    }
  };

  return (
    <div style={{ backgroundColor: '#f8f9fa', minHeight: '20vh', marginBottom: '150px' }}>
      <Container>
        <Row className="justify-content-center align-items-center" style={{ minHeight: '55vh' }}>
          <Col md={6}>
            <Form onSubmit={handleFormSubmit}>
              <h2 className="text-center mb-4">Assign Trainees to Batch</h2>
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
                <Label for="traineeList">Trainee List</Label>
                <div id="traineeList">
                  {traineeList.map((trainee) => (
                    <div key={trainee.id}>
                      <input
                        type="checkbox"
                        id={`trainee-${trainee.id}`}
                        value={trainee.id}
                        checked={selectedTrainees.includes(trainee.id)}
                        onChange={() => handleTraineeCheckboxChange(trainee.id)}
                      />
                      <label htmlFor={`trainee-${trainee.id}`}>{trainee.name}</label>
                    </div>
                  ))}
                </div>
              </FormGroup>

              <Button color="primary" type="submit" block>
                Assign Trainees
              </Button>
            </Form>
          </Col>
        </Row>
      </Container>
      <ToastContainer />
    </div>
  );
};

export default AssignTrainee;
