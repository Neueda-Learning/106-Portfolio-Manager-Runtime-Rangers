import { useEffect, useState } from "react";
import axios from "axios";
import {
 getTopGainers,
 getTopLosers
} from "../../api/marketApi";

const TopMovers = () => {

  const [gainers, setGainers] = useState([]);
  const [losers, setLosers] = useState([]);

  const topGainers = gainers.slice(0, 3);
const topLosers = losers.slice(0, 2);

useEffect(()=>{

 getTopGainers()
 .then(res=>{
   setGainers(res.data);
 });


 getTopLosers()
 .then(res=>{
   setLosers(res.data);
 });


},[]);


  return (
    <div className="grid grid-cols-2 gap-6">


      {/* Top Gainers */}

      <div className="theme-card top-movers-card bg-[#1D1826] border border-[#32293F] rounded-2xl p-6">

        <h2 className="text-lg font-semibold text-green-400 mb-4">
          Top Gainers
        </h2>


        {topGainers.map((stock)=>(
          <div
            key={stock.id}
            className="flex justify-between py-2"
          >

            <span className="theme-primary-text text-white">
              {stock.symbol}
            </span>

            <span className="text-green-400">
              +{stock.changePercent}%
            </span>

          </div>
        ))}

      </div>



      {/* Top Losers */}

      <div className="theme-card top-movers-card bg-[#1D1826] border border-[#32293F] rounded-2xl p-6">

        <h2 className="text-lg font-semibold text-red-400 mb-4">
          Top Losers
        </h2>


        {topLosers.map((stock)=>(
          <div
            key={stock.id}
            className="flex justify-between py-2"
          >

            <span className="theme-primary-text text-white">
              {stock.symbol}
            </span>

            <span className="text-red-400">
              {stock.changePercent}%
            </span>

          </div>
        ))}

      </div>


    </div>
  );
};

export default TopMovers;