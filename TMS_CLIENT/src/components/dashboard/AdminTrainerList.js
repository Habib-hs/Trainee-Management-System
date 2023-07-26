import React, { useEffect, useState } from 'react';
import { Button, Table } from 'reactstrap';
import { useNavigate } from 'react-router-dom';

const AdminTrainerList = () => {
  const navigate = useNavigate();
  const [trainers, setTrainers] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const trainersPerPage = 5;
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchTrainers();
  }, []);


  const fetchTrainers = () => {
    fetch('http://localhost:9080/trainer/get/all', {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`,
         'Content-Type': 'application/json',
        
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setTrainers(data.Trainer || []);
        setLoading(false);
      })
      .catch((error) => {
        console.log(error)
        console.error('Error fetching trainers:', error);
        setLoading(false);
      });
  };


  // Pagination logic
  const indexOfLastTrainer = currentPage * trainersPerPage;
  const indexOfFirstTrainer = indexOfLastTrainer - trainersPerPage;
  const currentTrainers = trainers.slice(indexOfFirstTrainer, indexOfLastTrainer);

  const handlePageChange = (pageNumber) => setCurrentPage(pageNumber);

  const renderTrainers = trainers
    ? currentTrainers.map((trainer) => (
        <tr key={trainer.id}>
          <td>{trainer.fullName}</td>
          <td>{trainer.email}</td>
          <td>{trainer.designation}</td>
          <td>{trainer.joiningDate}</td>
          <td>{trainer.yearsOfExperience}</td>
          <td>{trainer.expertises}</td>
          <td>{trainer.contactNumber}</td>
          <td>{trainer.presentAddress}</td>
        </tr>
      ))
    : null;

  return (
    <div>
      

      <h3>Numebr of Trainers : {trainers.length}</h3>

      {/* Conditional rendering based on loading state */}
      {loading ? (
        <p>Loading...</p>
      ) : (
        <Table responsive striped bordered>
          {/* Table header */}
          <thead>
            <tr>
              <th>Full Name</th>
              <th>Email</th>
              <th>Designation</th>
              <th>Joining Date</th>
              <th>Years of Experience</th>
              <th>Expertises</th>
              <th>Contact Number</th>
              <th>Present Address</th>
            </tr>
          </thead>
          {/* Table body */}
          <tbody>{renderTrainers}</tbody>
        </Table>
      )}

      {/* Pagination */}
      <div>
        {Array.from({ length: Math.ceil(trainers.length / trainersPerPage) }).map((_, index) => (
          <Button
            key={index}
            color={currentPage === index + 1 ? 'primary' : 'secondary'}
            onClick={() => handlePageChange(index + 1)}
            className="mr-2"
          >
            {index + 1}
          </Button>
        ))}
      </div>
    </div>
  );
};

export default AdminTrainerList;
