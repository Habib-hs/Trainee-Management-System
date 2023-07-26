import React, { useState, useEffect } from 'react';
import { Button, Table } from 'reactstrap';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const AllCourseList = () => {
  const navigate = useNavigate();
  const [courses, setCourses] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const coursesPerPage = 5;
  const [totalCourses, setTotalCourses] = useState(0);

  useEffect(() => {
    fetchCourses();
  }, []);

  const fetchCourses = async () => {
    try {
      const response = await axios.get('http://localhost:9080/course/get/all',{
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      })
      setCourses(response.data.Courses || []);
      setTotalCourses(response.data['Total Course'] || 0);
    } catch (error) {
      console.error('Error fetching courses:', error);
      toast.error('Error fetching courses. Please try again.');
    }
  };

  const handleAddCourse = () => {
    navigate('/admin/create/course');
  };

  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  // Pagination logic
  const indexOfLastCourse = currentPage * coursesPerPage;
  const indexOfFirstCourse = indexOfLastCourse - coursesPerPage;
  const currentCourses = courses.slice(indexOfFirstCourse, indexOfLastCourse);

  return (
    <div>
      <div className="d-flex justify-content-end mb-3">
        <Button color="primary" onClick={handleAddCourse}>
          Add Course
        </Button>
      </div>

      <h3>Number of Courses: {totalCourses}</h3>

      <Table responsive striped bordered>
        {/* Table header */}
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Description</th>
            <th>Assigned Trainer ID</th>
            <th>Trainer Name</th>
          </tr>
        </thead>
        {/* Table body */}
        <tbody>
          {currentCourses.map((course) => (
            <tr key={course.id}>
              <td>{course.id}</td>
              <td>{course.name}</td>
              <td>{course.description}</td>
              <td>{course.assignedTrainerId}</td>
              <td>{course.trainerName}</td>
            </tr>
          ))}
        </tbody>
      </Table>

      {/* Pagination */}
      <div>
        {Array.from({ length: Math.ceil(courses.length / coursesPerPage) }).map((_, index) => (
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

      {/* Add ToastContainer at the end */}
      <ToastContainer />
    </div>
  );
};

export default AllCourseList;
