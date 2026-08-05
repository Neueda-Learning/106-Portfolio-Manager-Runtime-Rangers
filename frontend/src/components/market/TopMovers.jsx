const TopMovers = () => {
  return (
    <div className="grid grid-cols-2 gap-6">

      <div className="bg-[#1D1826] border border-[#32293F] rounded-2xl p-6">

        <h2 className="text-lg font-semibold text-green-400 mb-4">
          Top Gainers
        </h2>

        <p>NVDA +4.5%</p>
        <p>AAPL +2.4%</p>
        <p>MSFT +0.8%</p>

      </div>

      <div className="bg-[#1D1826] border border-[#32293F] rounded-2xl p-6">

        <h2 className="text-lg font-semibold text-red-400 mb-4">
          Top Losers
        </h2>

        <p>TSLA -1.3%</p>
        <p>INTC -1.0%</p>
        <p>META -0.7%</p>

      </div>

    </div>
  );
};

export default TopMovers;