import React from "react";
import Header from '../components/layout/Header';
import Navbar from '../components/layout/Navbar';
import SummaryCard from "../components/dashboard/SummaryCard";
import PortfolioAllocation from "../components/dashboard/PortfolioAllocation";
import SectorAllocation from "../components/dashboard/SectorAllocation";
import HoldingsTable from "../components/dashboard/HoldingTable";

const Dashboard = () => {
  return (
    <div className="min-h-screen bg-[#121018] text-white">
      <Header />

      <div className="max-w-7xl mx-auto px-6 py-8">
        <Navbar />

        <h1 className="text-3xl font-bold mt-8">
          Investment Overview
        </h1>

        <p className="text-[#A8A4B3] mt-2">
          Track your portfolio performance and market insights.
        </p>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mt-8">
          <SummaryCard
            title="Total Investment"
            value="₹5,00,000"
            change="+8%"
            color="text-green-500"
          />

          <SummaryCard
            title="Portfolio Value"
            value="₹5,65,000"
            change="+13%"
            color="text-green-500"
          />

          <SummaryCard
            title="Profit"
            value="₹65,000"
            change="+₹12,000"
            color="text-green-500"
          />

          <SummaryCard
            title="Return"
            value="13%"
            change="+3%"
            color="text-green-500"
          />

        
          </div>
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mt-8">
            <PortfolioAllocation />
            <SectorAllocation />
            </div>
            <HoldingsTable />
      </div>
    </div>
  );
};

export default Dashboard;