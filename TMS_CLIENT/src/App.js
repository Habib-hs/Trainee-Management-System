import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { lazy, Suspense } from 'react';
import AdminLayout from './layouts/AdminLayout'
import TraineeLayout from './layouts/TraineeLayout';
import Signin from './components/Signin';
import AdminRoute from './routes/AuteRoutes/AdminRoute';
import TraineeRoute from './routes/AuteRoutes/TraineeRoute';
import Batch from "./views/ui/Batch"
import AssignTrainee from './views/ui/AssignTrainee';
import AssignTrainer from './views/ui/AssignTrainer';
import ScheduleBatch from './views/ui/ScheduleBatch';
import TrainerRoute from './routes/AuteRoutes/TrainerRoute';
import TrainerLayout from './layouts/TrainerLayout';
import TrainerRegister from './views/ui/TrainerRegister';
import TraineeRegister from './views/ui/TraineeRegister';
import Course from './views/ui/Course';
import Post from './views/ui/Post';
import Classroom from './views/ui/Classroom';
import ClassroomDetails from './views/ui/ClassroomDetails';
import TraineeClassroom from './views/ui/TraineeClassroom';
import SubmitAssignment from './views/ui/SubmitAssignment';
import Assignment from './views/ui/Assignment';
import AllAssignmentList from './views/ui/AllAssignmentList';
import AssignmentUpdate from './views/ui/AssignmentUpdate';
import AllTraineeList from './views/ui/AllTraineeList';
import AllTrainerList from './views/ui/AllTrainerList';
import AllBatchList from './views/ui/AllBatchList';
import AllCourseList from './views/ui/AllCourseList';
import AllScheduledPrograms from './views/ui/AllScheduledPrograms';
import TraineeUpdateProfile from './components/TraineeUpdateProfile';
import TraineeInfo from './components/dashboard/TraineeInfo';
import AdminClassroom from './views/ui/AdminClassroom';
import Starter from './views/Starter';



function App() {
  return (
    <div className="App">
        <Routes>
          <Route path="/signin" element={<Signin />} />
          
          <Route element={<AdminRoute />}>
          <Route path="admin" element={<AdminLayout />}>
            <Route path="/admin/dashboard" element={<Starter />} />
            <Route path="/admin/trainee-list" element={<AllTraineeList/>} />
            <Route path="/admin/register/trainee" element={<TraineeRegister />} />
            <Route path="/admin/trainer-list" element={<AllTrainerList/>} />
            <Route path="/admin/trainer/register" element={<TrainerRegister />} />
            <Route path="/admin/batch-list" element={<AllBatchList/>} />
            <Route path="/admin/register/batch" element={<Batch />} />
            <Route path="/admin/assign/trainee" element={<AssignTrainee />} />
            <Route path="/admin/assign/trainer" element={<AssignTrainer />} />
            <Route path="/admin/courses" element={<AllCourseList/>} />
            <Route path="/admin/create/course" element={<Course/>} />
            <Route path="/admin/scheduled-programs" element={<AllScheduledPrograms/>} />
            <Route path="/admin/schedule/batch" element={<ScheduleBatch />} />
            <Route path="/admin/classroom" element={<AdminClassroom/>} />
          </Route>
        </Route>

          <Route element={<TraineeRoute />}>
            <Route path="trainee" element={<TraineeLayout />}>
              <Route path="/trainee/classsroom" element={<TraineeClassroom/>} />
              <Route path ="/trainee/submit-assignment" element={<SubmitAssignment/>} />
              <Route path ="/trainee/update-profile" element={<TraineeUpdateProfile/>} />
              <Route path ="/trainee/personal-info" element={<TraineeInfo/>} />
            </Route>
          </Route>

          <Route element={<TrainerRoute />}>
            <Route path="trainer" element={<TrainerLayout />}>
              <Route path="/trainer/classroom" element ={<Classroom/>}/>
              <Route path="/trainer/classroom/:classroomName" element ={<ClassroomDetails/>}/>
              <Route path="/trainer/assignment-list" element ={<AllAssignmentList/>}/>
              <Route path="/trainer/create/assignment" element={<Assignment/>} />
              <Route path="/trainer/view/submitted_assignment" element={<SubmitAssignment/>} />
              <Route path="/trainer/update/assignment/:assignmentId" element={<AssignmentUpdate/>} />
              <Route path="/trainer/create/post" element={<Post/>} />
            </Route>
          </Route>

          <Route path="/" element={<Navigate to="/signin" />} />
        </Routes>
    </div>
  );
}

export default App;
