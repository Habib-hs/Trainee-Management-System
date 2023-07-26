import React from "react";
import { Link, useLocation } from "react-router-dom";
import { Button, Nav, NavItem } from "reactstrap";
import Logo from "../assets/images/logos/bjit.png";

const navigation = [
  {
    title: "Dashboard",
    href: "/admin/dashboard",
    icon: "bi bi-speedometer2",
  },
  {
    title: "Trainer",
    href: "/admin/trainer-list",
    icon: "bi bi-people",
  },
  {
    title: "Trainee",
    href: "/admin/trainee-list",
    icon: "bi bi-people",
  },
  {
    title: "Batch",
    href: "/admin/batch-list",
    icon: "bi bi-card-text",
  },
  {
    title: "Assign Trainee",
    href: "/admin/assign/trainee",
    icon: "bi bi-bell",
  },
  {
    title: "Assign Trainer",
    href: "/admin/assign/trainer",
    icon: "bi bi-patch-check",
  },
  {
    title: "Course",
    href: "/admin/courses",
    icon: "bi bi-card-text",
  },
  {
    title: "Schedule Batch",
    href: "/admin/scheduled-programs",
    icon: "bi bi-layout-split",
  },
  {
    title: "Classroom",
    href: "/admin/classroom",
    icon: "bi bi-hdd-stack",
  },
];


const AdminSideBar = () => {
  const showMobilemenu = () => {
    document.getElementById("sidebarArea").classList.toggle("showSidebar");
  };
  let location = useLocation();

  return (
    <div className="sticky-top" style={{ top: "0" }}>
      <div className="p-3">
        <div className="d-flex align-items-center">
                    <div className="d-none d-lg-block" style={{ flex: 1, marginLeft: "60px" }}>
              <div className="d-flex justify-content-start align-items-center" style={{ height: "100%" }}>
                <img
                  src={Logo}
                  alt="Logo"
                  style={{ maxWidth: "25%", height: "auto" }}
                />
              </div>
            </div>
          <span className="ms-auto d-lg-none">
            <Button
              close
              size="sm"
              className="ms-auto d-lg-none"
              onClick={() => showMobilemenu()}
            ></Button>
          </span>
        </div>
        <div className="pt-4 mt-2">
          <Nav vertical className="sidebarNav">
            {navigation.map((navi, index) => (
              <NavItem key={index} className="sidenav-bg">
                <Link
                  to={navi.href}
                  className={
                    location.pathname === navi.href
                      ? "text-primary nav-link py-3"
                      : "nav-link text-secondary py-3"
                  }
                >
                  <i className={navi.icon}></i>
                  <span className="ms-3 d-inline-block">{navi.title}</span>
                </Link>
              </NavItem>
            ))}
          </Nav>
        </div>
      </div>
    </div>
  );
};



export default AdminSideBar;

