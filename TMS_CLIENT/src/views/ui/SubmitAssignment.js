import React, { useEffect, useState } from "react";
import axios from "axios";
import { Container, Table, Button, Modal, ModalHeader, ModalBody, FormGroup, Label, Input } from "reactstrap";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const SubmitAssignment = () => {
  const [assignments, setAssignments] = useState([]);
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedAssignment, setSelectedAssignment] = useState(null);
  const [file, setFile] = useState(null);
  const [fileName, setFileName] = useState("");

   // State to store the user data
   const [userData, setUserData] = useState(null);
   const [classId, setClassId] = useState(null);
   const [loading, setLoading] = useState(true);
 
   // Fetch user data from localStorage when the component mounts
   useEffect(() => {
     const userDataString = localStorage.getItem("user");
     if (userDataString) {
       const userData = JSON.parse(userDataString);
       setUserData(userData);
     }
   }, []);
 
   // Make API call when userData is set or updated
   useEffect(() => {
    if (userData) {
      const { role, id } = userData;

      if (role === "TRAINEE") {
        axios
          .get(`http://localhost:9080/trainee/classroom/${id}`,{
            headers:{
              Authorization : `Bearer ${localStorage.getItem('token')}`,
              "Content-Type": "application/json"
            }
          })
          .then((response) => {
            const classId = response.data;
            setClassId(classId);
            // Fetch assignments based on the class ID
           axios
              .get("http://localhost:9080/assignment/all",{
                headers:{
                  Authorization : `Bearer ${localStorage.getItem('token')}`,
                  "Content-Type": "application/json"
                }
              })
              .then((response) => {
                const filteredAssignments = response.data.Assignments.filter((assignment) =>
                  assignment.batches.includes(classId)
                );
                setAssignments(filteredAssignments);
                setLoading(false);
              })
              .catch((error) => {
                console.error("Error fetching assignments:", error);
                setLoading(false);
              });
          })
          .catch((error) => {
            console.error("Error fetching Trainee data:", error);
            setLoading(false);
          });
      } else {
        console.error("Invalid role:", role);
        setLoading(false);
      }
    }
  }, [userData]);

  const handleOpenModal = (assignment) => {
    setSelectedAssignment(assignment);
    setModalOpen(true);
  };

  const handleFileChange = (event) => {
    const selectedFile = event.target.files[0];
    setFile(selectedFile);
    setFileName(selectedFile.name);
  };


  const handleSubmit = () => {
    const formData = new FormData();
    formData.append("traineeId", userData.id); 
    if (file) {
      formData.append("file", file);
    }

    // Make API call to submit assignment
    axios
      .post(`http://localhost:9080/submit-assignment/${selectedAssignment.id}`, formData,{
        headers:{
          Authorization : `Bearer ${localStorage.getItem('token')}`,
          'Content-Type': 'multipart/form-data',
        }
      })
      .then((response) => {
        // Handle success
        toast.success("Assignment submitted successfully!");
      })
      .catch((error) => {
        // Handle error
        toast.error("Error submitting assignment: " + error.message);
      });

    // Close the modal after submission
    setModalOpen(false);
  };

  const handleDownloadLinkClick = (assignmentId) => {
      axios
        .get(`http://localhost:9080/assignment/${assignmentId}/download`, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'multipart/form-data',
          }
        })
        .then((response) => {
          toast.success("File downloaded successfully!");
        })
        .catch((error) => {
          console.error("Error downloading file:", error);
          toast.error("Error downloading file: " + error.message);
        });
    } 
  

    return (
      <Container fluid>
        <h1>Assignment List</h1>
        <div className="table-responsive">
          <Table striped bordered hover>
            <thead>
              <tr>
                <th>Course Name</th>
                <th>Task Title</th>
                <th>Description</th>
                <th>Type</th>
                <th>Deadline</th>
                <th>File</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {assignments.map((assignment) => (
                <tr key={assignment.id}>
                   <td>{assignment.scheduleName}</td>
                  <td>{assignment.name}</td>
                  <td>{assignment.description}</td>
                  <td>{assignment.type}</td>
                  <td>{new Date(assignment.deadline).toLocaleDateString()}</td>
                  <td>
                         {assignment.fileUrl ? (
                        <a href={assignment.fileUrl} target="_blank" onClick={() => handleDownloadLinkClick(assignment.id)} rel="noopener noreferrer">
                      file
                            </a>
                          ) : (
                            "No file added"
                          )}
                        </td>
                  <td>
                    <Button color="primary" onClick={() => handleOpenModal(assignment)}>
                      Submit Assignment
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
    
        <Modal isOpen={modalOpen} toggle={() => setModalOpen(false)} size="lg">
          <ModalHeader toggle={() => setModalOpen(false)}>Submit Assignment - {selectedAssignment && selectedAssignment.name}</ModalHeader>
          <ModalBody>
            <Container>
              <FormGroup>
                <Label for="file">Upload File:</Label>
                <Input type="file" name="file" id="file" onChange={handleFileChange} />
              </FormGroup>
              <Button color="primary" onClick={handleSubmit}>
                Upload
              </Button>
            </Container>
          </ModalBody>
        </Modal>
    
        <ToastContainer position="bottom-right" autoClose={3000} hideProgressBar={true} />
      </Container>
    );    
};

export default SubmitAssignment;