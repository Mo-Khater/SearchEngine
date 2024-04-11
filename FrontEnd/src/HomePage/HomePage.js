import React, { useEffect } from 'react'
import "./HomePage.css";
import { useState } from 'react';
import BG from "../Bg/BG";
import { useNavigate } from 'react-router-dom';


export default function HomePage({ seturls }) {

    const navigate = useNavigate();

    const [query, setQuery] = useState('');

    const test = [
      {name:"Wikipedia" , url:"https://www.wikipedia.org" , describtion:"sadasd sadas wdas dwdasdwadasw sad w"},
      {name:"Codeforces" , url:"https://codeforces.com" , describtion:"sadasd sadas wdas dwdasdwadasw sad w"}
    ];

    const handleChange = (event) => {
      if (event.key === 'Enter') {
        setQuery(event.target.value);
        seturls(test);
        navigate('/urlpage');
      }
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
        placeholder="Type your url"
        onKeyPress={handleChange}
      />
    </div>
  )
}
