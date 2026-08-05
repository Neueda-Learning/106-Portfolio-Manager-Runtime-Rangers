import React from 'react';
import ChartCard from "./ChartCard";
import {portfolioData} from '../../mock/dashboard';
import {PieChart,Pie,Cell,Tooltip,ResponsiveContainer,Legend,} from "recharts";

const COLORS = [
  "#8B5CF6", 
  "#06B6D4", 
  "#22C55E", 
  "#F59E0B", 
];
const PortfolioAllocation = () => {
  return (
    <ChartCard title="Portfolio Allocation">
      <div className="h-72 flex items-center justify-center text-[#A8A4B3]">
     <ResponsiveContainer width="100%" height="100%">
      
      <PieChart>

     <Pie 
     data = {portfolioData}
        cx="50%"
        cy="50%"
        innerRadius={65}
              outerRadius={90}
              paddingAngle={4}
              dataKey="value">
   
     {portfolioData.map((entry, index) => (
                <Cell
                  key={`cell-${index}`}
      fill={COLORS[index % COLORS.length]}
                />
              ))}


              </Pie>
              <Tooltip />
                <Legend
              verticalAlign="bottom"
              iconType="circle"
            />



      </PieChart>

     </ResponsiveContainer>
      </div>
    </ChartCard>
  );
};

export default PortfolioAllocation;

