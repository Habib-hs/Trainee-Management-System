import React, { useEffect, useState } from "react";
import ClassroomCard from "../../components/ClassroomCard";
import bg1 from '../../assets/images/bg/bg1.jpg';
import bg2 from '../../assets/images/bg/bg2.jpg';
import axios from "axios";

const Classroom = () => {
  const [classrooms, setClassrooms] = useState([]);
  const [userData, setUserData] = useState(null);
  const [classId, setClassId] = useState(null);
  const [classIds, setClassIds] = useState([])
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
    const fetchClassIds = async () => {
      if (userData) {
        const { role, id } = userData;

        try {
          if (role === "TRAINEE") {
            const response = await axios.get(`http://localhost:9080/trainee/classroom/${id}`,{
              headers:{
                Authorization: `Bearer ${localStorage.getItem('token')}`
              }
            });
            setClassId(response.data); // Set classIds with the array data received from the API
          } else if (role === "TRAINER") {
            const response = await axios.get(`http://localhost:9080/trainer/classroom/${id}`,{
              headers:{
                Authorization: `Bearer ${localStorage.getItem('token')}`
              }
            });
            setClassIds(response.data); // Set classIds with the array data received from the API
          } else {
            console.log("failed to fetch")
          }
        } catch (error) {
          console.log("failed to fetch")
        } finally {
          setLoading(false);
        }
      }
    };

    fetchClassIds();
  }, [userData]);

  useEffect(() => {
    const fetchClassrooms = async () => {
      try {
        const response = await fetch("http://localhost:9080/classroom/get/all", {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
            "Content-Type": "application/json",
          },
        });
        const data = await response.json();

        // Map the classroom data and set the image based on classroom ID
        const mappedClassrooms = data.Classroom.map((classroom) => ({
          id: classroom.id,
          name: classroom.name,
          image: classroom.id === 1 ? bg1 : bg2,
        }));

        // Filter classrooms based on matching IDs in classIds state
        const filteredClassrooms = mappedClassrooms.filter((classroom) =>
          classIds.includes(classroom.id)
        );

        const classroomWithIds = filteredClassrooms.map((classroom) => ({
          ...classroom,
          classroomId: classroom.id,
        }));

        setClassrooms(filteredClassrooms);
      } catch (error) {
        
      }
    };

    // Fetch classrooms only when classIds state is updated (dependency)
    fetchClassrooms();
  }, [classIds]); 

  return (
    <div>
      <h1>Classrooms</h1>
      <ClassroomCard classrooms={classrooms}/>
    </div>
  );
};

export default Classroom;
