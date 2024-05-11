import React from 'react';
import "./Urlpage.css";
import { useState, useEffect } from 'react';
import axiosInstance from '../apiClient';



export default function Urlpage({ urls,time }) {

  const [newurls,setnewurls] = useState(urls);


  const [query, setQuery] = useState();
  const [timetaken, settimetaken] = useState(time);

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
      settimetaken(elapsedTime/1000);
  
      setnewurls(response.data);
    }).catch(error => {
      console.error('Error searching', error);
    });
  }, [query]);


  return (
    <div className='urlpage_main'>
      <div  className='urlpage'>
        <input
          className='searchbar'
          type="text"
          placeholder="Type your url"
          onKeyPress={handleChange}
        />
        <div className='urls'>
        {newurls.map((card) => (
          <div className='element'>
            <a className='url' href={card[0]}>{card[0]}</a>
            <div className='describtion'>
              <span>snippet :</span> 
              {card[1]}</div>
          </div>
        ))}
        </div>
      </div>
      <div className='timetaken'>
        <h3>Timetaken : {timetaken}</h3>
      </div>
    </div>
  )
}
