import React, { useState } from 'react';
import { Container, Row, Col, Form, FormGroup, Label, Input, Button } from 'reactstrap';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const TrainerRegister = () => {
  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    password: '',
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevFormData) => ({
      ...prevFormData,
      [name]: value,
    }));
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post("http://localhost:9080/trainer/register", formData, {
        crossDomain: true,
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
        },
      });

      if (response.status === 201) {
        // Display success toast notification
        toast.success("Registration successful!", { autoClose: 3000 });
        // Clear form fields
        setFormData({
          fullName: '',
          email: '',
          password: '',
        });
      } else {
        // Display error toast notification
        toast.error(response.data.error, { autoClose: 3000 });
      }
    } catch (error) {
      // Display error toast notification
      toast.error("Error registering trainer. Please try again.", { autoClose: 3000 });
      
    }
  };

  return (
    <div style={{ backgroundColor: '#f8f9fa', minHeight: '30vh' }}>
      <Container>
        <Row className="justify-content-center align-items-center" style={{ minHeight: '55vh' }}>
          <Col md={6}>
            <Form onSubmit={handleFormSubmit}>
              <h2 className="text-center mb-4">Trainer Registration</h2>

              <FormGroup>
                <Label for="fullName">Full Name</Label>
                <Input
                  type="text"
                  id="fullName"
                  name="fullName"
                  placeholder="Enter full name"
                  value={formData.fullName}
                  onChange={handleInputChange}
                  required
                />
              </FormGroup>

              <FormGroup>
                <Label for="email">Email Address</Label>
                <Input
                  type="email"
                  id="email"
                  name="email"
                  placeholder="Enter email"
                  value={formData.email}
                  onChange={handleInputChange}
                  required
                />
              </FormGroup>

              <FormGroup>
                <Label for="password">Password</Label>
                <Input
                  type="password"
                  id="password"
                  name="password"
                  placeholder="Enter password"
                  value={formData.password}
                  onChange={handleInputChange}
                  required
                />
              </FormGroup>

              <Button color="primary" type="submit" block>
                Register
              </Button>
            </Form>
          </Col>
        </Row>
      </Container>
      <ToastContainer />
    </div>
  );
};

export default TrainerRegister;
