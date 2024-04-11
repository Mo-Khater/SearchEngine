import { BrowserRouter, Route, Routes } from "react-router-dom";
import HomePage from "./HomePage/HomePage";
import Footer from "./Footer/Footer";
import Urlpage from "./UrlPage/Urlpage";
import { useState } from "react";


function App() {

  const [urls,seturls] = useState([]);

  return (
    <BrowserRouter>
      <Routes>
        <Route exact path="/" element={<HomePage seturls={seturls} />} />
        <Route exact path="/urlpage" element={<Urlpage urls={urls} />} />
      </Routes>
      <Footer />
    </BrowserRouter>
  );
}

export default App;
