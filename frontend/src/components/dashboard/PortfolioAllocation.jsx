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
  "#3B82F6", 
  "#22C55E", 
  "#F59E0B", 
  "#EF4444", 
  "#A855F7", 
];


const PortfolioAllocation = () => {

  const [portfolioData, setPortfolioData] = useState([]);


  useEffect(() => {

    axios
      .get("http://localhost:8082/api/portfolio/allocation")
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