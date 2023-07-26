import React, { useState } from 'react';
import { Card, CardBody, CardText, Badge, Button } from 'reactstrap';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';

const AdminMessageCard = ({ post }) => {
    console.log(post)
  const { senderName, createdTime, title, file, id } = post;
  const [expanded, setExpanded] = useState(false);
  const [showModal, setShowModal] = useState(false);

  const formatTimestamp = (timestamp) => {
    const currentTime = new Date().getTime();
    const diffInSeconds = Math.floor((currentTime - timestamp) / 1000);

    if (diffInSeconds < 0) {
      return 'Just now';
    } else if (diffInSeconds < 60) {
      return `${diffInSeconds} second${diffInSeconds === 1 ? '' : 's'} ago`;
    } else if (diffInSeconds < 3600) {
      const minutes = Math.floor(diffInSeconds / 60);
      return `${minutes} minute${minutes === 1 ? '' : 's'} ago`;
    } else if (diffInSeconds < 86400) {
      const hours = Math.floor(diffInSeconds / 3600);
      return `${hours} hour${hours === 1 ? '' : 's'} ago`;
    } else if (diffInSeconds < 2592000) {
      const days = Math.floor(diffInSeconds / 86400);
      return `${days} day${days === 1 ? '' : 's'} ago`;
    } else if (diffInSeconds < 31536000) {
      const months = Math.floor(diffInSeconds / 2592000);
      return `${months} month${months === 1 ? '' : 's'} ago`;
    } else {
      const years = Math.floor(diffInSeconds / 31536000);
      return `${years} year${years === 1 ? '' : 's'} ago`;
    }
  };

  const renderTitle = () => {
    const maxWords = 20;
    const words = (title ?? '').split(' ');

    if (words.length <= maxWords) {
      return title;
    } else {
      const truncatedTitle = words.slice(0, maxWords).join(' ');
      return (
        <>
          {truncatedTitle}{' '}
          <button className="btn btn-link p-0" onClick={() => setExpanded(true)}>
            Read More
          </button>
        </>
      );
    }
  };

  const toggleModal = () => {
    setShowModal(!showModal);
  };

  return (
    <>
      <ToastContainer />
      <Card className="mb-3" style={{ fontSize: '13px', padding: '8px' }}>
        <CardBody className="p-2 d-flex justify-content-between align-items-center">
          <div className="text-muted">Sender: {senderName}</div>
          <Badge color="info" className="font-weight-bold">
            {formatTimestamp(createdTime)}
          </Badge>
        </CardBody>
        <CardBody className="p-2">
          <CardText className="font-weight-bold">{renderTitle()}</CardText>
        </CardBody>
        <CardBody className="p-2">
          {/* Button to open the modal */}
          <div className="d-flex justify-content-between align-items-center">
            <div>
              {file && (
                <div>
                  <a href={file} target="_blank" rel="noopener noreferrer" className="font-weight-bold">
                    File
                  </a>
                </div>
              )}
            </div>
          </div>
        </CardBody>
      </Card>

      <Modal isOpen={showModal} toggle={toggleModal} centered>
        <ModalHeader toggle={toggleModal} style={{ border: 'none' }}>{title}</ModalHeader>
        <ModalBody style={{ padding: '20px', background: '#f8f9fa' }}>
          <div>Message ID: {id}</div>
          {/* Add more details about the message here */}
        </ModalBody>
        <ModalFooter style={{ border: 'none', justifyContent: 'center' }}>
          <Button color="secondary" onClick={toggleModal}>Close</Button>
        </ModalFooter>
      </Modal>
    </>
  );
};

export default AdminMessageCard;
