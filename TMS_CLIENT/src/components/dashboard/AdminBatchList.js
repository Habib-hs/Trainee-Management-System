import React, { useEffect, useState } from 'react';
import { Button, Table } from 'reactstrap';
import axios from 'axios';

const AdminBatchList = () => {
  const [batches, setBatches] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const batchesPerPage = 5;
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchBatches();
  }, []);

  const fetchBatches = () => {
    axios
      .get('http://localhost:9080/batch/get/all', {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      })
      .then((response) => {
        setBatches(response.data || []);
        setLoading(false);
      })
      .catch((error) => {
        console.error('Error fetching batches:', error);
        setLoading(false);
      });
  };

  // Pagination logic
  const indexOfLastBatch = currentPage * batchesPerPage;
  const indexOfFirstBatch = indexOfLastBatch - batchesPerPage;
  const currentBatches = batches.slice(indexOfFirstBatch, indexOfLastBatch);

  const handlePageChange = (pageNumber) => setCurrentPage(pageNumber);

  // Moved the renderBatches variable outside the JSX
  const renderBatches = currentBatches.map((batch) => (
    <tr key={batch.Id}>
      <td>{batch.BatchName}</td>
      <td>{new Date(batch.StartDate).toLocaleDateString()}</td>
      <td>{new Date(batch.EndDate).toLocaleDateString()}</td>
      <td>{batch.TrainerCount}</td>
      <td>{batch.TraineeCount}</td>
      <td>{batch.ScheduleProgramCount}</td>
    </tr>
  ));

  return (
    <div>
      <h3>Number of Batches: {batches.length}</h3>
      {/* Conditional rendering based on loading state */}
      {loading ? (
        <p>Loading...</p>
      ) : (
        <div>
          <Table responsive striped bordered>
            {/* Table header */}
            <thead>
              <tr>
                <th>Batch Name</th>
                <th>Start Date</th>
                <th>End Date</th>
                <th>Number of Trainers</th>
                <th>Number of Trainees</th>
                <th>Number of Schedule Programs</th>
              </tr>
            </thead>
            {/* Table body */}
            <tbody>{renderBatches}</tbody>
          </Table>

          {/* Pagination */}
          <div>
            {Array.from({ length: Math.ceil(batches.length / batchesPerPage) }).map((_, index) => (
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

export default AdminBatchList;
