import { useEffect, useState } from "react";
import axios from "axios";

const TopMovers = () => {

  const [gainers, setGainers] = useState([]);
  const [losers, setLosers] = useState([]);

  const topGainers = gainers.slice(0, 3);
const topLosers = losers.slice(0, 2);


  useEffect(() => {

    axios
      .get("http://localhost:8080/api/market/gainers")
      .then((response) => {
        console.log("Gainers:", response.data);
        setGainers(response.data);
      })
      .catch((error) => {
        console.error("Error fetching gainers:", error);
      });


    axios
      .get("http://localhost:8080/api/market/losers")
      .then((response) => {
        console.log("Losers:", response.data);
        setLosers(response.data);
      })
      .catch((error) => {
        console.error("Error fetching losers:", error);
      });


  }, []);



  return (
    <div className="grid grid-cols-2 gap-6">


      {/* Top Gainers */}

      <div className="bg-[#1D1826] border border-[#32293F] rounded-2xl p-6">

        <h2 className="text-lg font-semibold text-green-400 mb-4">
          Top Gainers
        </h2>


        {topGainers.map((stock)=>(
          <div
            key={stock.id}
            className="flex justify-between py-2"
          >

            <span className="text-white">
              {stock.symbol}
            </span>

            <span className="text-green-400">
              +{stock.changePercent}%
            </span>

          </div>
        ))}

      </div>



      {/* Top Losers */}

      <div className="bg-[#1D1826] border border-[#32293F] rounded-2xl p-6">

        <h2 className="text-lg font-semibold text-red-400 mb-4">
          Top Losers
        </h2>


        {topLosers.map((stock)=>(
          <div
            key={stock.id}
            className="flex justify-between py-2"
          >

            <span className="text-white">
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