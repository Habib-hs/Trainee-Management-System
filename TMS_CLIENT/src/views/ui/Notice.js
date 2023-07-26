import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Form, FormGroup, Input, Button, Alert } from 'reactstrap';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const Notice = ({ classroomId }) => {
  const [formData, setFormData] = useState({
    type: 'Message',
    title: '',
    file: null,
  });

  const [user, setUser] = useState(null);

  useEffect(() => {
    const userDataString = localStorage.getItem('user');
    if (userDataString) {
      const userData = JSON.parse(userDataString);
      setUser(userData);
    }
  }, []);

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

      formDataToSend.append('classroomId', classroomId);
      if (formData.type === 'Message'){
        formDataToSend.append('trainerId', user.id);
      }else{
        formDataToSend.append('senderEmail', user.userEmail);
      }
    
      console.log(formDataToSend);

      const endpoint = formData.type === 'Message' ? 'posts' : 'notice';
      const response = await axios.post(`http://localhost:9080/${endpoint}`, formDataToSend, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
          'Content-Type': 'multipart/form-data',
        },
      });

      toast.success(response.data);
      setFormData({
        type: 'Message',
        title: '',
        file: null,
      });
    } catch (error) {
      console.log(error);
      if (error.response && error.response.data) {
        toast.error(error.response.data);
      } else {
        toast.error('Failed to create Notice');
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

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    setFormData((prevFormData) => ({
      ...prevFormData,
      file: file || null,
    }));
  };

  return (
    <div>
      <Row className="justify-content-center align-items-center ">
        <Col md={12}>
          <Form onSubmit={handleFormSubmit}>
            <Row>
              <Col md={2}>
                <FormGroup>
                  <Input
                    type="select"
                    name="type"
                    value={formData.type}
                    onChange={handleInputChange}
                  >
                    <option value="Message">Message</option>
                    <option value="Notice">Notice</option>
                  </Input>
                </FormGroup>
              </Col>
              <Col md={6}>
                <FormGroup>
                  <Input
                    type="text"
                    name="title"
                    value={formData.title}
                    onChange={handleInputChange}
                    placeholder="Announce something to your class"
                    required
                  />
                </FormGroup>
              </Col>
              <Col md={4}>
                <FormGroup>
                  <Input type="file" name="file" onChange={handleFileChange} />
                </FormGroup>
              </Col>
            </Row>
            <Row className="justify-content-end">
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

export default Notice;
