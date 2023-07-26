import React, { useEffect, useState } from 'react';
import { Button, Table} from 'reactstrap';
import { useNavigate } from 'react-router-dom';

const AdminTraineeList = () => {
  const navigate = useNavigate();
  const [trainees, setTrainees] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const traineesPerPage = 5;
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('http://localhost:9080/trainee/get/all', {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`,
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setTrainees(data.Trainees || []); 
        setLoading(false); 
      })
      .catch((error) => {
        console.error('Error fetching trainees:', error);
        setLoading(false); // Set loading to false even on error
      });
  }, []);


  // Pagination logic
  const indexOfLastTrainee = currentPage * traineesPerPage;
  const indexOfFirstTrainee = indexOfLastTrainee - traineesPerPage;
  const currentTrainees = trainees.slice(indexOfFirstTrainee, indexOfLastTrainee);

  const handlePageChange = (pageNumber) => setCurrentPage(pageNumber);

  // Moved the renderTrainees variable outside the JSX
  const renderTrainees = trainees
    ? currentTrainees.map((trainee) => (
        <tr key={trainee.id}>
          <td>{trainee.fullName}</td>
          <td>{trainee.email}</td>
          <td>{trainee.gender}</td>
          <td>{trainee.domain}</td>
          <td>{trainee.contactNumber}</td>
          <td>{trainee.degreeName}</td>
          <td>{trainee.educationalInstitute}</td>
          <td>{trainee.cgpa}</td>
          <td>{trainee.passingYear}</td>
        </tr>
      ))
    : null;

  return (
    <div>
   
      <h3>Number of Trainees: {trainees.length}</h3>
    
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
              <th>Gender</th>
              <th>Domain</th>
              <th>Contact Number</th>
              <th>Degree Name</th>
              <th>Educational Institute</th>
              <th>CGPA</th>
              <th>Passing Year</th>
            </tr>
          </thead>
          {/* Table body */}
          <tbody>{renderTrainees}</tbody>
        </Table>
      )}

      {/* Pagination */}
      <div>
        {Array.from({ length: Math.ceil(trainees.length / traineesPerPage) }).map((_, index) => (
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

export default AdminTraineeList;
