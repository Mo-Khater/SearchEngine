import React from 'react';
import "./Urlpage.css";
import { useState, useEffect } from 'react';




export default function Urlpage({ urls }) {

  const [newurls,setnewurls] = useState(urls);


  const test = [
    {name:"Github" , url:"https://github.com" , describtion:"sadasd sadas wdas dwdasdwadasw sad w asdsa das dwads "},
    {name:"Youtube" , url:"https://www.youtube.com" , describtion:"sadasd sadas wdas dwdasdwadasw sad w"},
    {name:"Leetcode" , url:"https://leetcode.com/problemset" , describtion:"sadasd sadas wdas dwdasdwadasw sad w"}
  ];

  const [query, setQuery] = useState('');

  const handleChange = (event) => {
    if (event.key === 'Enter') {
      setQuery(event.target.value);
      setnewurls(test);
    }
  };




  return (
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
        <a className='name' href={card.url}>{card.name}</a>
        <a className='url' href={card.url}>{card.url}</a>
        <div className='describtion'>
          <span>description :</span> 
          {card.describtion}</div>
      </div>
    ))}
    </div>
    </div>
  )
}
