import React from "react";
import Header from '../components/layout/Header';
import Navbar from '../components/layout/Navbar';
import SummaryCard from "../components/dashboard/SummaryCard";
import PortfolioAllocation from "../components/dashboard/PortfolioAllocation";
import SectorAllocation from "../components/dashboard/SectorAllocation";
import HoldingsTable from "../components/dashboard/HoldingTable";
import  { useEffect, useState } from "react";
import { getPortfolioSummary } from "../api/portfolioApi";

const Dashboard = () => {
  const [summary, setSummary] = useState(null);
 
const fetchSummary = () => {

  getPortfolioSummary()
    .then((response) => {
      console.log(response.data);
      setSummary(response.data);
    })
    .catch((error) => {
      console.error(
        "Error fetching portfolio summary:",
        error
      );
    });

};


useEffect(() => {

  fetchSummary();


  window.addEventListener(
    "portfolioUpdated",
    fetchSummary
  );


  return () => {

    window.removeEventListener(
      "portfolioUpdated",
      fetchSummary
    );

  };


}, []);
 
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
  value={
    summary
      ? `₹${summary.totalInvestedValue.toLocaleString()}`
      : "Loading..."
  }
  change="+8%"
  color="text-green-500"
/>

<SummaryCard
  title="Portfolio Value"
  value={
    summary
      ? `₹${summary.totalCurrentValue.toLocaleString()}`
      : "Loading..."
  }
  change="+13%"
  color="text-green-500"
/>

<SummaryCard
  title="Profit"
  value={
    summary
      ? `₹${summary.totalGainLoss.toLocaleString()}`
      : "Loading..."
  }
  change="+₹120"
  color={summary && summary.totalGainLoss >= 0 ? "text-green-500" : "text-red-500"}
/>

<SummaryCard
  title="Return"
  value={
    summary
      ? `${summary.growthPercentage.toFixed(2)}%`
      : "Loading..."
  }
  change={
    summary && summary.growthPercentage >= 0
      ? `+${summary.growthPercentage.toFixed(2)}%`
      : `${summary?.growthPercentage.toFixed(2)}%`
  }
  color={
    summary && summary.growthPercentage >= 0
      ? "text-green-500"
      : "text-red-500"
  }
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