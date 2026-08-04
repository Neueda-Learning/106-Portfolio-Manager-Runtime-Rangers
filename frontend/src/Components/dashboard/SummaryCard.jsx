import React from "react";
import { TrendingDown, TrendingUp } from "lucide-react";

const SummaryCard = ({
  title = "Untitled metric",
  value = "--",
  change = "No change",
  color = "text-slate-500",
}) => {
  return (
    <div className="bg-white rounded-2xl shadow-sm border border-slate-200 p-5">
      <p className="text-sm text-slate-500">{title}</p>
      <p className="text-2xl font-bold text-slate-900 mt-1">{value}</p>

      <div className="mt-4 flex items-center justify-between">
        <p className={`font-semibold ${color}`}>{change}</p>

        <div
          className={`w-10 h-10 rounded-full flex items-center justify-center ${
            color.includes("green") ? "bg-green-500/10" : "bg-red-500/10"
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