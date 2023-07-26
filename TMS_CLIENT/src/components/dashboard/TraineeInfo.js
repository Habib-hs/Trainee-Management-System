import React, { useState, useEffect } from 'react';
import { Media } from 'reactstrap';
import axios from 'axios';


const TraineeInfo = () => {
  const [trainee, setTrainee] = useState(null);

  useEffect(() => {
    const fetchTraineeData = async () => {
      const userData = JSON.parse(localStorage.getItem("user"));
      try {
        const response = await axios.get(`http://localhost:9080/trainee/${userData.id}`, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json'
          }
        }); 
        setTrainee(response.data);
      } catch (error) {
        console.log(error);
      }
    };
  
    fetchTraineeData();
  }, []);
  
  if (!trainee) {
    // Add loading state or return null while fetching the data
    return null;
  }

  const { fullName, profilePicture, gender, dateOfBirth, contactNumber, degreeName, educationalInstitute, cgpa, passingYear, presentAddress } = trainee;

  return (
    <div className="profile-circle">
     
      <div className="profile-info">
        <h3>{fullName}</h3>
        <p>Gender: {gender}</p>
        <p>Date of Birth: {dateOfBirth}</p>
        <p>Contact Number: {contactNumber}</p>
        <p>Degree Name: {degreeName}</p>
        <p>Educational Institute: {educationalInstitute}</p>
        <p>CGPA: {cgpa}</p>
        <p>Passing Year: {passingYear}</p>
        <p>Present Address: {presentAddress}</p>
      </div>
    </div>
  );
};

export default TraineeInfo;
