import React from 'react';
import "./Urlpage.css";
import { useState, useEffect } from 'react';
import axiosInstance from '../apiClient';



export default function Urlpage({ urls,time }) {

  // const test =[
  //   ["asdsad20" , "sadasdasdweqewtrgdfgfd"],
  //   ["asdsad21" , "sadasdasdweqewtrgdfgfd"],
  //   ["asdsad22" , "sadasdasdweqewtrgdfgfd"],
  //   ["asdsad23" , "sadasdasdweqewtrgdfgfd"],
  //   ["asdsad24" , "sadasdasdweqewtrgdfgfd"],
  //   ["asdsad25" , "sadasdasdweqewtrgdfgfd"],
  //   ["asdsad26" , "sadasdasdweqewtrgdfgfd"],
  //   ["asdsad27" , "sadasdasdweqewtrgdfgfd"],
  //   ["asdsad28" , "sadasdasdweqewtrgdfgfd"],
  //   ["asdsad29" , "sadasdasdweqewtrgdfgfd"],
  //   ["asdsad30" , "sadasdasdweqewtrgdfgfd"],
  //   ["asdsad31" , "sadasdasdweqewtrgdfgfd"],
  //   ["asdsad32" , "sadasdasdweqewtrgdfgfd"]
  // ];

  // const [newurls,setnewurls] = useState([
  //   ["asdsad1" , "sadasdasdweqewtrgdfgfd"],
  //   ["asdsad2" , "sadasdasdweqewtrgdfgfd"],
  //   ["asdsad3" , "sadasdasdweqewtrgdfgfd"],
  //   ["asdsad4" , "sadasdasdweqewtrgdfgfd"],
  //   ["asdsad5" , "sadasdasdweqewtrgdfgfd"],
  //   ["asdsad6" , "sadasdasdweqewtrgdfgfd"],
  //   ["asdsad7" , "sadasdasdweqewtrgdfgfd"],
  //   ["asdsad8" , "sadasdasdweqewtrgdfgfd"],
  //   ["asdsad9" , "sadasdasdweqewtrgdfgfd"]
  // ]);

  const [newurls,setnewurls] = useState(urls);

  const [query, setQuery] = useState();
  const [timetaken, settimetaken] = useState(time);



  const [startIndex, setStartIndex] = useState(0);
  const [nextavailable, setnextavailable] = useState();
  const [prevavailable, setprevavailable] = useState();
  const [ispending ,setispending] = useState(false);

  const showNextArrays = () => {
    setStartIndex(prevIndex => prevIndex + 10);
    setprevavailable(true);
    if(newurls.length - (startIndex) >= 20 ) setnextavailable(true);
    else{setnextavailable(false);}
  };

  const showprevArrays = () => {
    setStartIndex(prevIndex => prevIndex - 10);
    if(startIndex === 10) setprevavailable(false);
    setnextavailable(true);
  };


  const handleChange = (event) => {
    if (event.key === 'Enter') {
      setQuery(event.target.value);
    }
  };

  useEffect(() => {
    if(query){
      // setispending(true);
      // setTimeout(() => {
      //   setnewurls(test);
      //   setispending(false);
      //  }, 2000);


    setispending(true);
    const startTime = performance.now(); // Start measuring time
  
    axiosInstance.get('/search', {
      params: {
        keyword: query
      }
    }).then(response => {
      setnewurls(response.data);
      setispending(false);
      const endTime = performance.now(); // Stop measuring time
      const elapsedTime = endTime - startTime; // Calculate elapsed time in milliseconds
      console.log('Time taken:', elapsedTime, 'ms');
      settimetaken(elapsedTime/1000);
    }).catch(error => {
      console.error('Error searching', error);
    });
    }
  },[query]);

  useEffect(() => {
    setprevavailable(false);
    setnextavailable((newurls.length > 10) ? true:false);
  },[newurls]);

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
        {newurls.slice(startIndex, startIndex + Math.min(10,newurls.length - startIndex)).map((card) => (
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
      {nextavailable && <button className="next10" onClick={showNextArrays}></button>}
      {prevavailable && <button className="prev10" onClick={showprevArrays}></button>}
      {ispending && <div className="loading"></div>}
    </div>
  )
}
