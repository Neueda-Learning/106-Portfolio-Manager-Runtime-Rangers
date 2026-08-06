import React, { useEffect, useState } from "react";
import ChartCard from "./ChartCard";
import {
  PieChart,
  Pie,
  Cell,
  Tooltip,
  ResponsiveContainer,
  Legend,
} from "recharts";
import axios from "axios";


const COLORS = [
  "#8B5CF6", 
  "#06B6D4",
  "#22C55E", 
  "#F59E0B",
  "#EF4444", 
  "#EC4899", 
  "#14B8A6", 
  "#6366F1", 
  "#84CC16", 
  "#F97316", 
  "#A855F7", 
  "#0EA5E9", 
  "#64748B", 
  "#D946EF", 
  "#EAB308"  
];


const PortfolioAllocation = () => {

  const [portfolioData, setPortfolioData] = useState([]);


  useEffect(() => {

    axios
      .get("http://10.9.77.127:8082/api/portfolio/allocation")
      .then((response) => {
        console.log(response.data);
        setPortfolioData(response.data);
      })
      .catch((error) => {
        console.error(
          "Error fetching portfolio allocation:",
          error
        );
      });

  }, []);
const chartData = portfolioData.map(item => ({
  name: item.companyName,
  value: Number(item.currentValue),
}));

  return (
    <ChartCard title="Portfolio Allocation">

      <div className="h-72">

        <ResponsiveContainer width="100%" height="100%">

          <PieChart>
<Pie
  data={chartData}
  dataKey="value"
  nameKey="name"
  cx="50%"
  cy="50%"
  innerRadius={65}
  outerRadius={90}
  paddingAngle={4}
  dataKey="value"
  isAnimationActive={true}
  animationBegin={0}
  animationDuration={1200}
  animationEasing="ease-out"
>
  {
    chartData.map((entry,index)=>(
      <Cell
        key={index}
        fill={COLORS[index % COLORS.length]}
      />
    ))
  }
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