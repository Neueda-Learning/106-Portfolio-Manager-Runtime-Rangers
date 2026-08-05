import { holdings } from "../../mock/dashboard";

const HoldingsTable = () => {
  return (
    <div className="bg-[#1D1826] border border-[#32293F] rounded-2xl mt-8 overflow-hidden">

      <div className="flex justify-between items-center px-6 py-5 border-b border-[#32293F]">
        <h2 className="text-xl font-semibold text-white">
          Your Holdings
        </h2>

        <button className="text-sm text-[#B784F7] hover:text-white transition">
          View All
        </button>
      </div>

      <table className="w-full">

        <thead className="bg-[#241C30] text-[#A8A4B3]">

          <tr>

            <th className="text-left px-6 py-4">Company</th>

            <th className="text-left">Qty</th>

            <th className="text-left">Avg Price</th>

            <th className="text-left">Current</th>

            <th className="text-left">Value</th>

            <th className="text-left">P&L</th>

            <th className="text-center">Actions</th>

          </tr>

        </thead>

        <tbody>

          {holdings.map((stock) => {

            const pnl =
              (stock.currentPrice - stock.avgPrice) *
              stock.quantity;

            const value =
              stock.currentPrice *
              stock.quantity;

            return (

              <tr
                key={stock.symbol}
                className="border-b border-[#32293F] hover:bg-[#262033] transition"
              >

                <td className="px-6 py-5">

                  <h3 className="font-semibold text-white">
                    {stock.company}
                  </h3>

                  <p className="text-sm text-[#A8A4B3]">
                    {stock.symbol}
                  </p>

                </td>

                <td>{stock.quantity}</td>

                <td>₹{stock.avgPrice}</td>

                <td>₹{stock.currentPrice}</td>

                <td>₹{value}</td>

                <td
                  className={`font-semibold ${
                    pnl >= 0
                      ? "text-green-400"
                      : "text-red-400"
                  }`}
                >
                  ₹{pnl}
                </td>

                <td className="text-center">
  <div className="flex justify-center gap-2">

    <button
      className="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-lg text-sm font-medium transition"
    >
      Buy
    </button>

    <button
      className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-lg text-sm font-medium transition"
    >
      Sell
    </button>

  </div>
</td>

              </tr>

            );

          })}

        </tbody>

      </table>

    </div>
  );
};

export default HoldingsTable;