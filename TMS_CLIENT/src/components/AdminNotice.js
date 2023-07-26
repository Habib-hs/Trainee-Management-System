import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Label, Form, FormGroup, Input, Button, Alert } from 'reactstrap';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const AdminNotice = () => {
  const [formData, setFormData] = useState({
    title: '',
    file: null,
    classroomId: '', // Use classroomId as the state variable
  });

  const [user, setUser] = useState(null);
  const [batchList, setBatchList] = useState([]);

  useEffect(() => {
    fetchBatchNames();

    const userDataString = localStorage.getItem('user');
    if (userDataString) {
      const userData = JSON.parse(userDataString);
      setUser(userData);
    }
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

  // Update handleSelectOption to set classroomId
  const handleSelectOption = (e) => {
    const selectedClassroomId = e.target.value;
    setFormData((prevFormData) => ({
      ...prevFormData,
      classroomId: selectedClassroomId,
    }));
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();

    try {
      const formDataToSend = new FormData();

      if (formData.title) {
        formDataToSend.append('title', formData.title);
      }

      if (formData.file !== null) {
        formDataToSend.append('file', formData.file);
      }

      formDataToSend.append('classroomId', formData.classroomId);
      formDataToSend.append('senderEmail', user.userEmail);

      console.log(formDataToSend);

      const response = await axios.post('http://localhost:9080/notice', formDataToSend, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
          'Content-Type': 'multipart/form-data',
        },
      });

      toast.success(response.data);
      setFormData({
        title: '',
        file: null,
        classroomId: '', // Reset the classroomId field after form submission
      });
    } catch (error) {
      console.log(error);
      toast.error('Failed to create Notice');
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
    <div>
      <Row className="justify-content-center align-items-center">
        <Col md={12}>
          <Form onSubmit={handleFormSubmit}>
            <Row>
              <Col md={2}>
                <FormGroup>
                  <Input
                    type="select"
                    id="batch"
                    value={formData.classroomId} 
                    onChange={handleSelectOption} 
                    required
                  >
                    <option value="">Batch</option>
                    {batchList.map((batch) => (
                      <option key={batch.id} value={batch.id}>
                        {batch.name}
                      </option>
                    ))}
                  </Input>
                </FormGroup>
              </Col>
              <Col md={4}>
                <FormGroup>
                  <Input
                    type="text"
                    name="title"
                    value={formData.title}
                    onChange={handleInputChange}
                    placeholder="Send notice to the classrooms..."
                    required
                  />
                </FormGroup>
              </Col>
              <Col md={4}>
                <FormGroup>
                  <Input type="file" name="file" onChange={handleFileChange} />
                </FormGroup>
              </Col>
              <Col md={2}>
                <Button color="primary" type="submit" block>
                  Post
                </Button>
              </Col>
            </Row>
          </Form>
        </Col>
      </Row>
      <ToastContainer />
    </div>
  );
};

export default AdminNotice;
