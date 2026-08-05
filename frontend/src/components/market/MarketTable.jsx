import { marketData } from "../../mock/market";

const MarketTable = ({ marketData }) => {
  return (
    <div className="bg-[#1D1826] border border-[#32293F] rounded-2xl mt-8 overflow-hidden">

      <div className="flex items-center justify-between px-6 py-5 border-b border-[#32293F]">
        <h2 className="text-xl font-semibold text-white">
          Market Overview
        </h2>

        <p className="text-sm text-gray-400">
          {marketData.length} Stocks
        </p>
      </div>

      <table className="w-full">

        <thead className="bg-[#241C30]">
          <tr className="text-gray-400">
            <th className="px-6 py-4 text-left">Company</th>
            <th className="text-left">Symbol</th>
            <th className="text-left">Sector</th>
            <th className="text-left">Price</th>
            <th className="text-left">Change</th>
            <th className="text-left">Volume</th>
            <th className="text-center">Actions</th>
          </tr>
        </thead>

        <tbody>
          {marketData.map((stock) => (
            <tr
              key={stock.symbol}
              className="border-b border-[#32293F] hover:bg-[#262033]"
            >
              <td className="px-6 py-5 font-medium text-white">
                {stock.company}
              </td>

              <td>{stock.symbol}</td>

              <td>{stock.sector}</td>

              <td>₹{stock.price}</td>

              <td
                className={
                  stock.change > 0
                    ? "text-green-400 font-medium"
                    : "text-red-400 font-medium"
                }
              >
                {stock.change > 0 ? "+" : ""}
                {stock.change}%
              </td>

              <td>{stock.volume}</td>

              <td>
                <div className="flex justify-center gap-2">

                  <button className="bg-green-600 hover:bg-green-700 px-3 py-2 rounded-lg text-sm">
                    Buy
                  </button>

                  <button className="bg-red-600 hover:bg-red-700 px-3 py-2 rounded-lg text-sm">
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