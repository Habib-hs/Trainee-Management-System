import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Pagination, PaginationItem, PaginationLink, Input } from 'reactstrap';
import TrainerMessageCard from './TrainerMessageCard';
import { contextType } from 'react-modal';

const TrainerMessage = ({ classroomId }) => {
  const [posts, setPosts] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [postsPerPage] = useState(5); // Number of posts to display per page
  const [searchTerm, setSearchTerm] = useState('');
  const [filteredPosts, setFilteredPosts] = useState([]);

  // Fetch data from the API
  useEffect(() => {
    axios
      .get(`http://localhost:9080/posts/classroom/${classroomId}`,{
        headers:{
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      })
      .then((response) => {
        const sortedPosts = response.data.Posts.sort((a, b) => b.createdTime - a.createdTime);
        setPosts(sortedPosts);
        setFilteredPosts(response.data.Posts);
      })
      .catch((error) => console.error('Error fetching data:', error));
  }, [classroomId]);

  // Function to handle pagination page change
  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  // Function to handle search input change
  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value);

    // Filter posts based on search term
    const filteredPosts = posts.filter((post) => {
      const titleMatch = post.title && post.title.toLowerCase().includes(e.target.value.toLowerCase());
      const authorMatch = post.trainerName && post.trainerName.toLowerCase().includes(e.target.value.toLowerCase());
      const fileMatch = e.target.value.toLowerCase() === 'file' ? post.fileUrl !== null : false;

      return titleMatch || authorMatch || fileMatch;
    });

    setFilteredPosts(filteredPosts);
  };

  // Get current posts based on pagination
  const indexOfLastPost = currentPage * postsPerPage;
  const indexOfFirstPost = indexOfLastPost - postsPerPage;
  const currentPosts = filteredPosts.slice(indexOfFirstPost, indexOfLastPost);

  return (
    <div>
      <Input
        type="text"
        placeholder="Search by title, author ..."
        value={searchTerm}
        onChange={handleSearchChange}
        className="mb-3"
      />
      {currentPosts.map((post) => (
        <TrainerMessageCard key={post.id} post={post} />
      ))}
      <Pagination className="mt-4">
        {Array.from({ length: Math.ceil(filteredPosts.length / postsPerPage) }).map((_, index) => (
          <PaginationItem key={index} active={currentPage === index + 1}>
            <PaginationLink onClick={() => handlePageChange(index + 1)}>{index + 1}</PaginationLink>
          </PaginationItem>
        ))}
      </Pagination>
    </div>
  );
};

export default TrainerMessage;
