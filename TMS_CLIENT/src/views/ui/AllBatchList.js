import React, { useEffect, useState } from 'react';
import { Button, Table } from 'reactstrap';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const AllBatchList = () => {
  const navigate = useNavigate();
  const [batches, setBatches] = useState([]);
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

  const handleRedirect = () => {
    navigate('/admin/register/batch');
  };

  const renderBatches = batches.map((batch) => (
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
      <div className="d-flex justify-content-end mb-3">
        <Button color="primary" onClick={handleRedirect}>
          Add Batch
        </Button>
      </div>

      <h3>Number of Batch: {batches.length}</h3>

      {/* Conditional rendering based on loading state */}
      {loading ? (
        <p>Loading...</p>
      ) : (
        <Table responsive striped bordered>
          {/* Table header */}
          <thead>
            <tr>
              <th>Batch Name</th>
              <th>Start Date</th>
              <th>End Date</th>
              <th>Number of Trainer</th>
              <th>Number of Trainee</th>
              <th>Number of Schedule Program</th>
            </tr>
          </thead>
          {/* Table body */}
          <tbody>{renderBatches}</tbody>
        </Table>
      )}
    </div>
  );
};

export default AllBatchList;
