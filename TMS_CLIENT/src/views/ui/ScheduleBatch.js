import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Form, FormGroup, Label, Input, Button } from 'reactstrap';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const ScheduleBatch = () => {
  const [formData, setFormData] = useState({
    courseName: '',
    courseType: 'Common',
    startDate: '',
    endDate: '',
    courseId: '',
    batchId: '', // Store the selected batch ID or name
  });
  const [courseList, setCourseList] = useState([]);
  const [batchList, setBatchList] = useState([]); // New state for the list of batches

  useEffect(() => {
    fetchCourses();
    fetchBatches(); // Fetch batches and populate the batchList state
  }, []);

  const fetchCourses = async () => {
    try {
      const response = await axios.get('http://localhost:9080/course/get/all',{
        headers:{
                  Authorization: `Bearer ${localStorage.getItem('token')}`,
                  "Content-Type": "application/json"
        },
      });
      const courses = response.data.Courses.map((course) => ({
        id: course.id.toString(),
        name: course.name,
        courseType: course.courseType,
      }));
      setCourseList(courses);
    } catch (error) {
      console.error('Error fetching courses:', error);
    }
  };

  const fetchBatches = async () => {
    try {
      const response = await axios.get('http://localhost:9080/batch/get/allName',{
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
          "Content-Type": "application/json"
        },
      });
      const batches = response.data.Batches.map((batch) => ({
        id: batch.id.toString(),
        name: batch.name,
      }));
      setBatchList(batches);
    } catch (error) {
      if (error.response && error.response.data) {
        const errorMessage = error.response.data;
        console.log(errorMessage);
        // toast.error(errorMessage, { autoClose: 3000 });
      }
    }
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    try {
      const scheduleBatchData = {
        courseName: formData.courseName,
        courseType: formData.courseType,
        startDate: new Date(formData.startDate).toISOString(),
        endDate: new Date(formData.endDate).toISOString(),
        courseId: formData.courseId,
        batchesIds: formData.batchId ? [parseInt(formData.batchId, 10)] : [],
      };

      console.log(scheduleBatchData)
      const response = await axios.post('http://localhost:9080/schedule-batch', scheduleBatchData,{
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
          "Content-Type": "application/json"
        },
      });
      toast.success(response.data);
    } catch (error) {
      if (error.response && error.response.data) {
        toast.error(error.response.data);
      } else {
        toast.error('Error scheduling batch');
      }
      console.error('Error scheduling batch:', error);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevFormData) => ({
      ...prevFormData,
      [name]: value,
    }));
  };

  const handleCourseChange = async (e) => {
    const selectedCourse = courseList.find((course) => course.name === e.target.value);
    setFormData((prevFormData) => ({
      ...prevFormData,
      courseName: e.target.value,
      courseId: selectedCourse ? selectedCourse.id : '',
      batchId: '', // Reset the batchId when a new course is selected
    }));
  };

  return (
    <div style={{ backgroundColor: '#f8f9fa', minHeight: '30vh' }}>
      <Container>
        <Row className="justify-content-center align-items-center" style={{ minHeight: '65vh' }}>
          <Col md={5}>
            <Form onSubmit={handleFormSubmit}>
              <h2 className="text-center mb-4">Schedule Batch</h2>
              <FormGroup>
                <div className="d-flex">
                  <Label for="courseName" style={{ minWidth: '120px' }}>
                    Course Name
                  </Label>
                  <Input
                    type="select"
                    id="courseName"
                    name="courseName"
                    value={formData.courseName}
                    onChange={handleCourseChange}
                    required
                  >
                    <option value="">Select a course</option>
                    {courseList.map((course) => (
                      <option key={course.id} value={course.name}>
                        {course.name}
                      </option>
                    ))}
                  </Input>
                </div>
              </FormGroup>

              <FormGroup>
                <div className="d-flex">
                  <Label for="courseType" style={{ minWidth: '120px' }}>
                    Course Type
                  </Label>
                  <Input
                    type="select"
                    id="courseType"
                    name="courseType"
                    value={formData.courseType}
                    onChange={handleInputChange}
                  >
                    <option value="Common">Common</option>
                    <option value="Domain Specific">Domain Specific</option>
                  </Input>
                </div>
              </FormGroup>

              <FormGroup>
                <div className="d-flex">
                  <Label for="startDate" style={{ minWidth: '120px' }}>
                    Start Date
                  </Label>
                  <Input
                    type="date"
                    id="startDate"
                    name="startDate"
                    value={formData.startDate}
                    onChange={handleInputChange}
                  />
                </div>
              </FormGroup>

              <FormGroup>
                <div className="d-flex">
                  <Label for="endDate" style={{ minWidth: '120px' }}>
                    End Date
                  </Label>
                  <Input
                    type="date"
                    id="endDate"
                    name="endDate"
                    value={formData.endDate}
                    onChange={handleInputChange}
                  />
                </div>
              </FormGroup>

              {formData.courseType === 'Domain Specific' && (
                <FormGroup>
                  <div className="d-flex">
                    <Label for="batchId" style={{ minWidth: '120px' }}>
                      Batch Name
                    </Label>
                    <Input
                      type="select" // Changed the input type to select
                      id="batchId"
                      name="batchId"
                      value={formData.batchId}
                      onChange={handleInputChange}
                    >
                      <option value="">Select a batch</option>
                      {batchList.map((batch) => (
                        <option key={batch.id} value={batch.id}>
                          {batch.name}
                        </option>
                      ))}
                    </Input>
                  </div>
                </FormGroup>
              )}

              <Button color="primary" type="submit" block>
                Schedule
              </Button>
            </Form>
          </Col>
        </Row>
      </Container>
      <ToastContainer />
    </div>
  );
};

export default ScheduleBatch;
