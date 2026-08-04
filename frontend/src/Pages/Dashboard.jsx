import React from "react";
import Header from "../components/layout/Header";
import SummaryCard from "../components/dashboard/SummaryCard";

const Dashboard = () => {
  const summaryCards = [
    {
      title: "Total Portfolio Value",
      value: "$125,430",
      change: "+2.4%",
      color: "text-green-500",
    },
    {
      title: "Daily Gain/Loss",
      value: "+$1,240",
      change: "+1.0%",
      color: "text-green-500",
    },
    {
      title: "Top Performer",
      value: "AAPL",
      change: "+3.2%",
      color: "text-green-500",
    },
    {
      title: "Worst Performer",
      value: "TSLA",
      change: "-1.8%",
      color: "text-red-500",
    },
  ];


  return (
    <div className="min-h-screen bg-gray-100">
      <Header />

      <div className="max-w-7xl mx-auto px-6 py-6">
        <h1 className="text-3xl font-bold">
          Investment Overview
        </h1>

        <p className="text-gray-500 mt-2">
          Track your portfolio performance and market insights.
        </p>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mt-8 color">
          {summaryCards.map((card) => (
            <SummaryCard
              key={card.title}
              title={card.title}
              value={card.value}
              change={card.change}
              color={card.color}
            />
          ))}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;