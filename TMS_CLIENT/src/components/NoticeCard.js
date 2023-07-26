import React from 'react';
import { Card, CardBody, CardText, Badge } from 'reactstrap';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import Modal from 'react-modal';
import axios from 'axios';

const NoticeCard = ({ post }) => {
  const { senderName, title, createdTime, fileUrl, id } = post;
  const [expanded, setExpanded] = React.useState(false);
  const [showModal, setShowModal] = React.useState(false);

  const randomColor = () => {
    const colors = ['#f0f7da', '#e0edd5', '#d0e4d1', '#f5d0d0', '#f5e3e6'];
    return colors[Math.floor(Math.random() * colors.length)];
  };

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
  const defaultSenderName = post.senderName || 'ADMIN';

  const handleFileDownload = () => {
    axios.get(`http://localhost:9080/notice/${id}/download`,{
      headers:{
        Authorization: `Bearer ${localStorage.getItem('token')}`,
        'Content-Type': 'multipart/form-data',
      }
    })
      .then((response) => {
      toast.success('File downloaded');
      })
      .catch(() => {
    //    toast.error('Error downloading file');
      });
  }

  const handleModalOpen = () => {
    setShowModal(true);
  };

  const handleModalClose = () => {
    setShowModal(false);
  };

  const renderTitle = () => {
    const maxWords = 10;
    const words = (title ?? '').split(' ');

    if (words.length <= maxWords || expanded) {
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
  
  return (
    <>
      <ToastContainer />
      <Card className="mb-3" style={{ fontSize: '13px', padding: '8px', backgroundColor: randomColor(), display: 'flex', flexDirection: 'column' }}>
        <CardBody className="p-2 d-flex justify-content-between align-items-center">
         <div className="text-muted">Sender: {defaultSenderName}</div>
          <Badge color="info" className="font-weight-bold">
            {formatTimestamp(createdTime)}
          </Badge>
        </CardBody>
        <CardBody className="p-2">
          <div className="d-flex justify-content-between">
            <CardText className="font-weight-bold" onClick={handleModalOpen} style={{ cursor: 'pointer' }}>
              {renderTitle()}
            </CardText>
            {fileUrl && (
              <div>
                <a href={fileUrl} target="_blank" rel="noopener noreferrer" onClick={handleFileDownload} className="font-weight-bold">
                  File
                </a>
              </div>
            )}
          </div>
        </CardBody>
      </Card>
      <Modal isOpen={showModal} onRequestClose={handleModalClose} style={{
        overlay: {
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          backgroundColor: 'rgba(0, 0, 0, 0.5)',
        },
        content: {
          width: '300px',
          height: '150px',
          margin: 'auto',
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'center',
          alignItems: 'center',
          backgroundColor: '#fff',
        }
      }}>
        <div>
          <h5 className="font-weight-bold mb-3">Notice Title</h5>
          <p>{title}</p>
          <button className="btn btn-secondary" onClick={handleModalClose}>Close</button>
        </div>
      </Modal>
    </>
  );
};

export default NoticeCard;