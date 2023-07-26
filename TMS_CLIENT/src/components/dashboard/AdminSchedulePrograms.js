import React, { useEffect, useState } from 'react';
import { Button, Table } from 'reactstrap';
import axios from 'axios';

const AdminScheduledPrograms = () => {
  const [schedules, setSchedules] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const schedulesPerPage = 5;
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchSchedules();
  }, []);

  const fetchSchedules = () => {
    axios
      .get('http://localhost:9080/schedule-batch', {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      })
      .then((response) => {
        setSchedules(response.data.Schedules);
        setLoading(false);
      })
      .catch((error) => {
        console.error('Error fetching schedule data:', error);
        setLoading(false);
      });
  };

  // Pagination logic
  const indexOfLastSchedule = currentPage * schedulesPerPage;
  const indexOfFirstSchedule = indexOfLastSchedule - schedulesPerPage;
  const currentSchedules = schedules.slice(indexOfFirstSchedule, indexOfLastSchedule);

  const handlePageChange = (pageNumber) => setCurrentPage(pageNumber);

  // Moved the renderSchedules variable outside the JSX
  const renderSchedules = currentSchedules.map((schedule) => (
    <tr key={schedule.id}>
      <td>{schedule.id}</td>
      <td>{schedule.name}</td>
      <td>{new Date(schedule.startingDate).toLocaleDateString()}</td>
      <td>{new Date(schedule.endingDate).toLocaleDateString()}</td>
      <td>{schedule.courseType}</td>
      <td>{schedule.batchNames.join('\n')}</td>
    </tr>
  ));

  return (
    <div >
      <h2>Number of Schedule Courses: {schedules.length}</h2>
      {/* Conditional rendering based on loading state */}
      {loading ? (
        <p>Loading...</p>
      ) : (
        <div>
          <Table striped>
            {/* Table header */}
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Starting Date</th>
                <th>Ending Date</th>
                <th>Course Type</th>
                <th>Batch Names</th>
              </tr>
            </thead>
            {/* Table body */}
            <tbody>{renderSchedules}</tbody>
          </Table>

          {/* Pagination */}
          <div>
            {Array.from({ length: Math.ceil(schedules.length / schedulesPerPage) }).map((_, index) => (
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
      )}
    </div>
  );
};

export default AdminScheduledPrograms;
