import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { Card, CardBody, CardText, Badge } from 'reactstrap';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import Modal from 'react-modal';

const TraineeMessageCard = ({ post }) => {
  const { id, trainerId,trainerName, classroomId, title, createdTime, fileUrl } = post;
  const [expanded, setExpanded] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [comment, setComment] = useState('');
  const [user, setUser] = useState(null); 

  // Fetch user data from localStorage when the component mounts
  useEffect(() => {
    const userData = JSON.parse(localStorage.getItem("user"));
    setUser(userData);
  }, []);
  

  const handleAddComment = () => {
    // Prepare the comment data
    const commentData = {
      comment,
      postId: id,
      time: new Date(),
      traineeId: user.id,
    };

    // Make API call to add the comment
    axios.post(`http://localhost:9080/comments/${id}`, commentData,{
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`,
        "Content-Type" : 'application/json'
      }
    })
      .then((response) => {
        setShowModal(false);
        setComment('');
        toast.success('Comment added successfully!');
      })
      .catch((error) => {
        setShowModal(false);
        setComment('');
        toast.error('Failed to add comment');
      });
  };

  const handleFileDownload = () => {
    axios.get(`http://localhost:9080/posts/${id}/download`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`,
        "Content-Type" : 'multipart/form-data'
      }
    })
      .then((response) => {
      toast.success('File downloaded');
      })
      .catch(() => {
      toast.error('Error downloading file');
      });
  }

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
     <ToastContainer/>
      <Card className="mb-3" style={{ fontSize: '13px', padding: '8px' }}>
        <CardBody className="p-2 d-flex justify-content-between align-items-center">
          <div className="text-muted">Author: {trainerName}</div>
          <Badge color="info" className="font-weight-bold">{formatTimestamp(createdTime)}</Badge>
        </CardBody>
        <CardBody className="p-2">
          <CardText className="font-weight-bold">{renderTitle()}</CardText>
        </CardBody>
        <CardBody className="p-2">
          <div className="d-flex justify-content-between align-items-center">
            <div>
              <Badge color="secondary">
                  <button
                    className="btn btn-link p-0 small no-bg"
                    style={{ textDecoration: 'none', fontSize: '8px' }}
                    onClick={() => setShowModal(true)}
                  >
                    Add Comment
                  </button>
                
              </Badge>
            </div>
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
      <Modal
        isOpen={showModal}
        onRequestClose={() => setShowModal(false)}
        style={{
          overlay: {
            backgroundColor: 'rgba(0, 0, 0, 0.5)',
          },
          content: {
            top: '50%',
            left: '50%',
            right: 'auto',
            bottom: 'auto',
            marginRight: '-50%',
            transform: 'translate(-50%, -50%)',
            borderRadius: '5px',
            boxShadow: '0 2px 10px rgba(0, 0, 0, 0.2)',
            border: '1px solid #ccc',
          },
        }}
      >
        <h4>Add Comment</h4>
        <div>
          <textarea
            value={comment}
            onChange={(e) => setComment(e.target.value)}
            placeholder="Enter your comment..."
            style={{ width: '100%', height: '100px', padding: '5px' }}
          />
        </div>
        <div style={{ textAlign: 'right', marginTop: '10px' }}>
          <button onClick={handleAddComment} className="btn btn-primary">Submit</button>
          <button onClick={() => setShowModal(false)} className="btn btn-secondary" style={{ marginLeft: '5px' }}>Cancel</button>
        </div>
      </Modal>
    </>
  );
};

export default TraineeMessageCard;