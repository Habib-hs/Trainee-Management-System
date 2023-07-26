import React, { useState } from "react";
import { Link, useLocation } from "react-router-dom";
import { Button, Nav, NavItem } from "reactstrap";
import Logo from "../assets/images/logos/bjit.png";

const navigation = [
  {
    title: "Classroom",
    href: "/trainer/classroom",
    icon: "bi bi-columns",
  },
  {
    title: "Assignment",
    href: "/trainer/assignment-list",
    icon: "bi bi-hdd-stack",
  }
];

const TrainerSidebar = () => {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const location = useLocation();

  const toggleSidebar = () => {
    setSidebarOpen(!sidebarOpen);
  };

  return (
    <div className={`sticky-top ${sidebarOpen ? "sidebarOpen" : ""}`}>
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
            <Button close size="sm" className="ms-auto d-lg-none" onClick={toggleSidebar}></Button>
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

export default TrainerSidebar;
