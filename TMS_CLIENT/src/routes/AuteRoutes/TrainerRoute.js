import React from 'react'
import {Navigate, Outlet} from 'react-router-dom'

function TrainerRoute() {
  // Retrieve user role from your authentication logic
  const user = JSON.parse(localStorage.getItem("user"));
  const isTrainer = user && user.role === "TRAINER";
  return isTrainer ? <Outlet /> : <Navigate to="/signin" />;

}

export default TrainerRoute;