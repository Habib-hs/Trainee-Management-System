import React, { useState, useEffect } from 'react';
import { FormGroup, Label, Input, Button, Col, Container, Row } from 'reactstrap';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const TraineeUpdateProfile = () => {
  const [trainee, setTrainee] = useState({
    fullName: '',
    profilePicture: null,
    gender: '',
    dateOfBirth: '',
    contactNumber: '',
    degreeName: '',
    educationalInstitute: '',
    cgpa: 0,
    passingYear: 0,
    presentAddress: '',
  });

  const [userData, setUserData] = useState(null);

  // Fetch user data from localStorage when the component mounts
  useEffect(() => {
    const userDataString = localStorage.getItem("user");
    if (userDataString) {
      const userData = JSON.parse(userDataString);
      setUserData(userData);
     
    }
  }, []);


  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setTrainee({ ...trainee, [name]: value });
  };

  const handleProfilePictureChange = (e) => {
    setTrainee({ ...trainee, profilePicture: e.target.files[0] });
  };

  const handleUpdate = async () => {
    const formData = new FormData();
    Object.entries(trainee).forEach(([key, value]) => {
      if (key === 'profilePicture' && value !== null) {
        formData.append(key, value);
      } else {
        if (key === 'dateOfBirth') {
          formData.append(key, new Date(value));
        } else {
          formData.append(key, value);
        }
      }
    });

    try {
      const response = await axios.put(`http://localhost:9080/trainee/update/${userData.id}`, formData,{
        headers: {
          Authorization : `Bearer ${localStorage.getItem('token')}`,
          "Content-Type" : 'multipart/form-data'
        }
      });
     
      toast.success(response.data);
    } catch (error) {
        console.log(error.response)
      toast.error(error.response?.data || "An error occurred while updating the trainee");
    }
  };

  return (
    <Container className="d-flex justify-content-center">
      <div className="form-container">
      <h3 className="text-center">Update Profile</h3> 
        <FormGroup row>
          <Label for="fullName" sm={4}>Full Name</Label>
          <Col sm={8}>
            <Input type="text" name="fullName" id="fullName" value={trainee.fullName} onChange={handleInputChange} />
          </Col>
        </FormGroup>
        <FormGroup row>
          <Label for="gender" sm={4}>Gender</Label>
          <Col sm={8}>
            <Input type="select" name="gender" id="gender" value={trainee.gender} onChange={handleInputChange}>
              <option value="">Select Gender</option>
              <option value="Male">Male</option>
              <option value="Female">Female</option>
              <option value="Other">Other</option>
            </Input>
          </Col>
        </FormGroup>
        <FormGroup row>
          <Label for="dateOfBirth" sm={4}>Date of Birth</Label>
          <Col sm={8}>
            <Input type="date" name="dateOfBirth" id="dateOfBirth" value={trainee.dateOfBirth} onChange={handleInputChange} />
          </Col>
        </FormGroup>
        <FormGroup row>
          <Label for="contactNumber" sm={4}>Contact Number</Label>
          <Col sm={8}>
            <Input type="tel" name="contactNumber" id="contactNumber" value={trainee.contactNumber} onChange={handleInputChange} />
          </Col>
        </FormGroup>
        <FormGroup row>
          <Label for="degreeName" sm={4}>Degree Name</Label>
          <Col sm={8}>
            <Input type="text" name="degreeName" id="degreeName" value={trainee.degreeName} onChange={handleInputChange} />
          </Col>
        </FormGroup>
        <FormGroup row>
          <Label for="educationalInstitute" sm={4}>Educational Institute</Label>
          <Col sm={8}>
            <Input type="text" name="educationalInstitute" id="educationalInstitute" value={trainee.educationalInstitute} onChange={handleInputChange} />
          </Col>
        </FormGroup>
        <FormGroup row>
          <Label for="cgpa" sm={4}>CGPA</Label>
          <Col sm={8}>
            <Input type="number" name="cgpa" id="cgpa" value={trainee.cgpa} onChange={handleInputChange} />
          </Col>
        </FormGroup>
        <FormGroup row>
          <Label for="passingYear" sm={4}>Passing Year</Label>
          <Col sm={8}>
            <Input type="number" name="passingYear" id="passingYear" value={trainee.passingYear} onChange={handleInputChange} />
          </Col>
        </FormGroup>
        <FormGroup row>
          <Label for="presentAddress" sm={4}>Present Address</Label>
          <Col sm={8}>
            <Input type="textarea" name="presentAddress" id="presentAddress" value={trainee.presentAddress} onChange={handleInputChange} />
          </Col>
        </FormGroup>
        <FormGroup row>
          <Label for="profilePicture" sm={4}>Profile Picture</Label>
          <Col sm={8}>
            <div className="profile-picture-upload">
              <Input type="file" name="profilePicture" id="profilePicture" onChange={handleProfilePictureChange} />
              {!trainee.profilePicture ? (
                <span className="upload-message">Upload an image</span>
              ) : (
                <span className="upload-message">{trainee.profilePicture.name}</span>
              )}
            </div>
          </Col>
        </FormGroup>
        <Button color="primary" onClick={handleUpdate}>Update</Button>
        <ToastContainer />
      </div>
    </Container>
  );
};

export default TraineeUpdateProfile;