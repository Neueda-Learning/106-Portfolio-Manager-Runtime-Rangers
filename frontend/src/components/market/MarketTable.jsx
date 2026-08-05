
import { useEffect, useState } from "react";
import axios from "axios";

const MarketTable = ({search}) => {
    const [marketData, setMarketData] = useState([]);

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/market")
      .then((response) => {
        console.log(response.data);
        setMarketData(response.data);
      })
      .catch((error) => {
        console.error("Error fetching market data:", error);
      });
  }, []);

  const filteredStocks = marketData.filter((stock) =>
  stock.companyName.toLowerCase().includes(search.toLowerCase()) ||
  stock.symbol.toLowerCase().includes(search.toLowerCase())
);
 return (
    <div className="bg-[#1D1826] border border-[#32293F] rounded-2xl mt-8 overflow-hidden">

      <div className="flex items-center justify-between px-6 py-5 border-b border-[#32293F]">
        <h2 className="text-xl font-semibold text-white">
          Market Overview
        </h2>

        <p className="text-sm text-gray-400">
          {filteredStocks.length} Stocks
        </p>
      </div>

      <table className="w-full">

        <thead className="bg-[#241C30] text-[#A8A4B3]">
          <tr>
            <th className="px-6 py-4 text-left">Company</th>
            <th className="text-left">Symbol</th>
            <th className="text-left">Sector</th>
            <th className="text-left">Current Price</th>
            <th className="text-left">Change</th>
            <th className="text-left">Exchange</th>
            <th className="text-center">Actions</th>
          </tr>
        </thead>

        <tbody>

         {filteredStocks.map((stock)=> (

            <tr
              key={stock.id}
              className="border-b border-[#32293F] hover:bg-[#262033] transition"
            >

              <td className="px-6 py-5">
                <h3 className="font-semibold text-white">
                  {stock.companyName}
                </h3>
              </td>

              <td>{stock.symbol}</td>

              <td>{stock.sector}</td>

              <td>₹{stock.currentPrice.toLocaleString()}</td>

              <td
                className={
                  stock.changePercent >= 0
                    ? "text-green-400 font-medium"
                    : "text-red-400 font-medium"
                }
              >
                {stock.changePercent >= 0 ? "+" : ""}
                {stock.changePercent}%
              </td>

              <td>{stock.exchange}</td>

              <td>
                <div className="flex justify-center gap-2">

                  <button className="bg-green-600 hover:bg-green-700 px-3 py-2 rounded-lg text-sm transition">
                    Buy
                  </button>

                  <button className="bg-red-600 hover:bg-red-700 px-3 py-2 rounded-lg text-sm transition">
                    Sell
                  </button>

                </div>
              </td>

            </tr>

          ))}

        </tbody>

      </table>

    </div>
  );
};

export default MarketTable;