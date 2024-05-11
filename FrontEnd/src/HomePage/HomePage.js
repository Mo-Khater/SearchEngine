import React, { useEffect } from 'react'
import "./HomePage.css";
import { useState } from 'react';
import BG from "../Bg/BG";
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../apiClient';



export default function HomePage({ seturls, settime }) {

    const navigate = useNavigate();

    const [query, setQuery] = useState();


    const handleChange = (event) => {
      if (event.key === 'Enter') {
        setQuery(event.target.value);
      }
    };


    useEffect(() => {
      const startTime = performance.now(); // Start measuring time

      axiosInstance.get('/search', {
        params: {
          keyword: query
        }
      }).then(response => {
        const endTime = performance.now(); // Stop measuring time
        const elapsedTime = endTime - startTime; // Calculate elapsed time in milliseconds
        console.log('Time taken:', elapsedTime, 'ms');
        settime(elapsedTime/1000);
        console.log(query);
        seturls(response.data);
        console.log(response.data);
        navigate('/urlpage');
      }).catch(error => {
        console.error('Error searching', error);
      });
    }, [query]) ;


  return (
    <div className='homepage'>
        <div className='dark'/>
        <BG/>
        <div className='name'>
            Search Engine
        </div>
        <input
        className='searchbar'
        type="text"
        placeholder="Type your url"
        onKeyPress={handleChange}
      />
    </div>
  )
}
