import React, { useState, useEffect } from 'react';
import { Table } from 'reactstrap';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const AllScheduledPrograms = () => {
  const [schedules, setSchedules] = useState([]);

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
      })
      .catch((error) => {
        console.error('Error fetching schedule data:', error);
        toast.error('Failed to fetch schedule data');
      });
  };

  const formatDate = (date) => {
    const formattedDate = new Date(date);
    const year = formattedDate.getFullYear();
    const month = padZero(formattedDate.getMonth() + 1);
    const day = padZero(formattedDate.getDate());
    return `${year}-${month}-${day}`;
  };
  

  const padZero = (value) => {
    return value.toString().padStart(2, '0');
  };

  return (
    <div style={{ padding: '20px' }}>
      <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: '20px' }}>
        <a href="/admin/schedule/batch" className="btn btn-primary">Add Schedule</a>
      </div>
      <h2>Number of Schedule Course: {schedules.length}</h2>
      <Table striped>
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
        <tbody>
          {schedules.map((schedule) => (
            <tr key={schedule.id}>
              <td style={{ borderRight: '1px solid #dddddd' }}>{schedule.id}</td>
              <td style={{ borderRight: '1px solid #dddddd' }}>{schedule.name}</td>
              <td style={{ borderRight: '1px solid #dddddd' }}>{formatDate(schedule.startingDate)}</td>
              <td style={{ borderRight: '1px solid #dddddd' }}>{formatDate(schedule.endingDate)}</td>
              <td style={{ borderRight: '1px solid #dddddd' }}>{schedule.courseType}</td>
              <td style={{ whiteSpace: 'pre-line' }}>{schedule.batchNames.join('\n')}</td>
            </tr>
          ))}
        </tbody>
      </Table>
      <ToastContainer />
    </div>
  );
};

export default AllScheduledPrograms;
