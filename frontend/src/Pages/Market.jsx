import { useState } from "react";
import Header from "../components/layout/Header";
import Navbar from "../components/layout/Navbar";
import SearchBar from "../components/market/SearchBar";
import TopMovers from "../components/market/TopMovers";
import MarketTable from "../components/market/MarketTable";

const Market = () => {
  const [search, setSearch] = useState("");

  return (
    <div className="min-h-screen bg-[#121018] text-white">
      <Header />

      <div className="max-w-7xl mx-auto px-6 py-8">
        <Navbar />

        <h1 className="text-3xl font-bold mt-8">
          Market
        </h1>

        <p className="text-[#A8A4B3] mt-2">
          Explore stocks and manage your investments.
        </p>

        <SearchBar
          search={search}
          setSearch={setSearch}
        />

        <TopMovers />

        <MarketTable search={search} />
      </div>
    </div>
  );
};

export default Market;