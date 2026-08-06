import React from "react";
import { TrendingUp, TrendingDown } from "lucide-react";

const SummaryCard = ({ title, value, change, color }) => {
  return (
    <div className="theme-card summary-card bg-[#1D1826] border border-[#32293F] rounded-2xl shadow-lg p-6 hover:bg-[#262033] transition-all duration-300">

      <h3 className="theme-muted text-[#A8A4B3] text-sm font-medium">
        {title}
      </h3>

      <h2 className="theme-card-value text-3xl font-bold text-white mt-4">
        {value}
      </h2>

     <div className="mt-5 flex items-center justify-between">
  <p className={`font-semibold ${color}`}>
    {change}
  </p>

  <div
    className={`w-10 h-10 rounded-full flex items-center justify-center ${
      color.includes("green")
        ? "bg-green-500/10"
        : "bg-red-500/10"
    }`}
  >
    {color.includes("green") ? (
      <TrendingUp className="text-green-400" size={22} />
    ) : (
      <TrendingDown className="text-red-400" size={22} />
    )}
  </div>
</div>
</div>
  );
};

export default SummaryCard;