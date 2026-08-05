import { holdings } from "../../mock/dashboard";
import { useEffect, useState } from "react";
import axios from "axios";

const HoldingsTable = () => {
 const [holdings, setHoldings] = useState([]);

  useEffect(() => {
  axios
    .get("http://localhost:8080/api/portfolio/allocation")
    .then((response) => {
      console.log(response.data);
      setHoldings(response.data);
    })
    .catch((error) => {
      console.error("Error fetching holdings:", error);
    });
}, []);
  return (
     <div className="bg-[#1D1826] border border-[#32293F] rounded-2xl mt-8 overflow-hidden">

    <table className="w-full">
        <thead className="bg-[#241C30] text-[#A8A4B3]">
    <tr>
      <th className="text-left px-6 py-4">Company</th>
      <th className="text-left">Qty</th>
      <th className="text-left">Current Price</th>
      <th className="text-left">Current Value</th>
      <th className="text-left">P&L</th>
      <th className="text-center">Actions</th>
    </tr>
  </thead>
  {holdings.map((stock) => (
    <tr
      key={stock.holdingId}
      className="border-b border-[#32293F] hover:bg-[#262033] transition"
    >
      <td className="px-6 py-5">
        <h3 className="font-semibold text-white">
          {stock.companyName}
        </h3>

        <p className="text-sm text-[#A8A4B3]">
          {stock.symbol}
        </p>
      </td>

      <td>{stock.quantity}</td>

      <td>₹{stock.currentPrice.toLocaleString()}</td>

      <td>₹{stock.currentValue.toLocaleString()}</td>

      <td
        className={`font-semibold ${
          stock.gainLoss >= 0
            ? "text-green-400"
            : "text-red-400"
        }`}
      >
        ₹{stock.gainLoss.toLocaleString()}
      </td>

      <td className="text-center">
        <div className="flex justify-center gap-2">
          <button className="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-lg text-sm transition">
            Buy
          </button>

          <button className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-lg text-sm transition">
            Sell
          </button>
        </div>
      </td>
    </tr>
  ))}
</table>
    </div>
  );
};

export default HoldingsTable;