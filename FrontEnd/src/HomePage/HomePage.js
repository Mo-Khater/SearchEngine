import React, { useEffect } from 'react'
import "./HomePage.css";
import { useState } from 'react';
import BG from "../Bg/BG";
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../apiClient';



export default function HomePage({ seturls, settime }) {

    const navigate = useNavigate();

    const [query, setQuery] = useState();

    const [ispending, setispending] = useState(false);

    // const[Suggestions, setSuggestions] = useState([
    //   ["iam good at play", 10],
    //   ["messi at barcelona", 20],
    //   ["play football", 15],
    //   ["play basketball", 18],
    //   ["coding is fun", 12]
    // ]);

    const[Suggestions, setSuggestions] = useState([]);

    const [suggestiontoshow, setsuggestiontoshow] = useState();

    const [showSuggestions, setShowSuggestions] = useState(false);

    const handleChange = (event) => {
      if (event.key === 'Enter') {
        setQuery(event.target.value);
        addStringToLocalStorage(event.target.value);
        //navigate('/urlpage');
      }
    };

    const handleInputChange = (event) => {
      const newQuery = event.target.value;
      getSuggestions(newQuery);
      setShowSuggestions(true);
    };

    const handleSuggestionClick = (suggestion) => {
      setQuery(suggestion);
    };

    const getSuggestions = (newQuery) => {
      const filteredSuggestions = Suggestions
      .filter(([suggestion, score]) => {
        const queryWords = newQuery.toLowerCase().split(' ');
        return queryWords.every(word => suggestion.toLowerCase().includes(word));
      })
      .map(([suggestion, score]) => suggestion);

      setsuggestiontoshow(filteredSuggestions);
      setShowSuggestions(true);
    };

    function addStringToLocalStorage(str) {
      let strings = [];
    
      // Get existing strings from localStorage
      const existingStrings = localStorage.getItem('myStrings');
      if (existingStrings) {
        strings = JSON.parse(existingStrings);
      }
    
      // Check if the string is already present
      if (!strings.includes(str)) {
        // Add the new string to the array
        strings.push(str);
    
      // Save the updated array back to localStorage
        localStorage.setItem('myStrings', JSON.stringify(strings));
      }
    };
    
    // Function to get all strings from localStorage as an array
    function getAllStringsFromLocalStorage() {
      const existingStrings = localStorage.getItem('myStrings');
      return existingStrings ? JSON.parse(existingStrings) : [];
    };

    useEffect( () =>{
      const allStrings = getAllStringsFromLocalStorage();
      setSuggestions(allStrings);
    },[]);

    useEffect(() => {
    if(query){
      // setispending(true);
      // setTimeout(() => {
      //   setispending(false);
      //   navigate('/urlpage');
      // }, 2000);

      setispending(true);
      const startTime = performance.now(); // Start measuring time

      axiosInstance.get('/search', {
        params: {
          keyword: query
        }
      }).then(response => {
        seturls(response.data);
        setispending(false);
        const endTime = performance.now(); // Stop measuring time
        const elapsedTime = endTime - startTime; // Calculate elapsed time in milliseconds
        console.log('Time taken:', elapsedTime, 'ms');
        settime(elapsedTime/1000);
        console.log(query);
        console.log(response.data);
        navigate('/urlpage');
      }).catch(error => {
        console.error('Error searching', error);
      });
     }
    }, [query]) ;

  
    // Function to handle input blur
    const handleInputBlur = () => {
      setShowSuggestions(false);
    };

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
        onBlur={handleInputBlur}
        placeholder="Type your url"
        onKeyPress={handleChange}
        onChange={handleInputChange}
      />
      {showSuggestions && (
        <ul>
          {suggestiontoshow.map((suggestion, index) => (
            <li key={index} onClick={() => handleSuggestionClick(suggestion)} >
              {suggestion}
            </li>
          ))}
        </ul>
      )}
      {ispending && <div className="loading"></div>}
    </div>
  )
}
