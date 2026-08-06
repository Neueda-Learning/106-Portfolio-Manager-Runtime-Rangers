import { useState , useEffect } from "react";
import Header from "../components/layout/Header";
import Navbar from "../components/layout/Navbar";
import SearchBar from "../components/market/SearchBar";
import TopMovers from "../components/market/TopMovers";
import MarketTable from "../components/market/MarketTable";
import AIMarketScanner from "../components/market/AIMarketScanner";
import { getMarketStocks } from "../api/marketApi";

const Market = ({ theme, onToggleTheme }) => {
  const [marketData,setMarketData] = useState([]);
  const [search, setSearch] = useState("");
useEffect(()=>{


getMarketStocks()
.then(res=>{

setMarketData(res.data);

})
.catch(error=>{

console.log(error);

});


},[]);
  return (
    <div className="page-shell min-h-screen bg-[#121018] text-white">
      <Header
        theme={theme}
        onToggleTheme={onToggleTheme}
      />

      <div className="page-content max-w-7xl mx-auto px-6 py-8">
        <Navbar />

        <h1 className="text-3xl font-bold mt-8">
          Market
        </h1>

        <p className="page-subtitle text-[#A8A4B3] mt-2">
          Explore stocks and manage your investments.
        </p>

        <SearchBar
          search={search}
          setSearch={setSearch}
        />

        <TopMovers />
        <AIMarketScanner
 marketData={marketData}
/>


        <MarketTable search={search} />
      </div>
    </div>
  );
};

export default Market;