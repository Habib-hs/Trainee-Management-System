import React, { useState } from 'react';
import { Container, Row, Col, Form, FormGroup, Label, Input, Button } from 'reactstrap';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const TraineeRegister = () => {
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [domain, setDomain] = useState('');

  const handleFormSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post("http://localhost:9080/trainee/register", {
        fullName,
        email,
        password,
        domain,
      }, {
        crossDomain: true,
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
          "Content-Type": "application/json",
        },
      });

      if (response.status === 201) {
        // Display success toast notification
        toast.success("Registration successful!", { autoClose: 3000 });
        // Clear form fields
        setFullName('');
        setEmail('');
        setPassword('');
        setDomain('');
      } else {
        // Display error toast notification
        toast.error("Registration failed. Please try again.", { autoClose: 3000 });
      }
    } catch (error) {
      // Display error toast notification
      toast.error("Error registering trainee. Please try again.", { autoClose: 3000 });
      console.log("Error registering trainee:", error);
    }
  };

  return (
    <div style={{ backgroundColor: '#f8f9fa', minHeight: '30vh' }}>
      <Container>
        <Row className="justify-content-center align-items-center" style={{ minHeight: '55vh' }}>
          <Col md={6}>
            <Form onSubmit={handleFormSubmit}>
              <h2 className="text-center mb-4">Trainee Registration</h2>

              <FormGroup>
                <Label for="fullName">Full Name</Label>
                <Input
                  type="text"
                  id="fullName"
                  placeholder="Enter full name"
                  value={fullName}
                  onChange={(e) => setFullName(e.target.value)}
                  required
                />
              </FormGroup>

              <FormGroup>
                <Label for="email">Email Address</Label>
                <Input
                  type="email"
                  id="email"
                  placeholder="Enter email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
              </FormGroup>

              <FormGroup>
                <Label for="password">Password</Label>
                <Input
                  type="password"
                  id="password"
                  placeholder="Enter password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </FormGroup>

              <FormGroup>
                <Label for="domain">Domain</Label>
                <Input
                  type="text"
                  id="domain"
                  placeholder="Enter domain"
                  value={domain}
                  onChange={(e) => setDomain(e.target.value)}
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

export default TraineeRegister;
