import React from 'react';
import { Card, CardBody, CardTitle, CardText, Badge } from 'reactstrap';

const BlogCard = ({ title, content, timestamp, commentCount }) => {
  // Function to format the timestamp

  // Function to format the timestamp
const formatTimestamp = (timestamp) => {
    // Convert the timestamp to milliseconds
    const timestampInMs = timestamp * 1000;
    const currentTime = Date.now();
  
    // Calculate the difference in milliseconds
    const diffInMs = currentTime - timestampInMs;
  
    // Calculate the difference in minutes
    const diffInMinutes = Math.floor(diffInMs / (1000 * 60));
  
    // Format the timestamp based on the difference in minutes
    if (diffInMinutes < 1) {
      return 'Just now';
    } else if (diffInMinutes < 60) {
      return `${diffInMinutes} minutes ago`;
    } else if (diffInMinutes < 1440) {
      const diffInHours = Math.floor(diffInMinutes / 60);
      return `${diffInHours} hours ago`;
    } else {
      const diffInDays = Math.floor(diffInMinutes / 1440);
      return `${diffInDays} days ago`;
    }
  };
  

  return (
    <Card>
      <CardBody>
        <div className="d-flex justify-content-between align-items-center mb-2">
          <div>
            <CardTitle tag="h5">{title}</CardTitle>
          </div>
          <div>
            <Badge color="info">{formatTimestamp(timestamp)}</Badge>
          </div>
        </div>
        <CardText>{content}</CardText>
        <div className="d-flex justify-content-between align-items-center">
          <div>
            <Badge color="secondary">{commentCount} comments</Badge>
          </div>
        </div>
      </CardBody>
    </Card>
  );
};

export default BlogCard;
