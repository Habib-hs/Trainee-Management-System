import React, { useState } from 'react';
import { Card, CardBody, CardTitle, CardText, Badge, Pagination, PaginationItem, PaginationLink } from 'reactstrap';

const Blog = () => {
  const [currentPage, setCurrentPage] = useState(1);
  const postsPerPage = 2;

  // Dummy data for blog posts
  const blogPosts = [
    {
      id: 1,
      title: 'Blog Post 1',
      content: 'This is the content of blog post 1.',
      timestamp: 1628640000, // Example timestamp (in seconds)
      commentCount: 5,
    },
    {
      id: 2,
      title: 'Blog Post 2',
      content: 'This is the content of blog post 2.',
      timestamp: 1628553600, // Example timestamp (in seconds)
      commentCount: 3,
    },
    {
      id: 3,
      title: 'Blog Post ',
      content: 'This is the content of blog post 1.',
      timestamp: 1628640000, // Example timestamp (in seconds)
      commentCount: 5,
    },
    {
      id: 4,
      title: 'Blog Post 4',
      content: 'This is the content of blog post 2.',
      timestamp: 1628553600, // Example timestamp (in seconds)
      commentCount: 3,
    }
    // Add more blog posts as needed
  ];

  // Calculate the index of the first and last post to be displayed on the current page
  const indexOfLastPost = currentPage * postsPerPage;
  const indexOfFirstPost = indexOfLastPost - postsPerPage;
  const currentPosts = blogPosts.slice(indexOfFirstPost, indexOfLastPost);

  // Function to handle pagination page change
  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  return (
    <div>
      {currentPosts.map((post) => (
        <Card key={post.id} className="mb-3">
          <CardBody>
            <div className="d-flex justify-content-between align-items-center mb-2">
              <div>
                <CardTitle tag="h5">{post.title}</CardTitle>
              </div>
              <div>
                <Badge color="info">{post.timestamp}</Badge>
              </div>
            </div>
            <CardText>{post.content}</CardText>
            <div className="d-flex justify-content-between align-items-center">
              <div>
                <Badge color="secondary">{post.commentCount} comments</Badge>
              </div>
            </div>
          </CardBody>
        </Card>
      ))}
      <Pagination className="mt-4">
        {Array.from({ length: Math.ceil(blogPosts.length / postsPerPage) }).map((_, index) => (
          <PaginationItem key={index} active={currentPage === index + 1}>
            <PaginationLink onClick={() => handlePageChange(index + 1)}>{index + 1}</PaginationLink>
          </PaginationItem>
        ))}
      </Pagination>
    </div>
  );
};

export default Blog;
