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

import { getSectorAllocation } from "../../api/portfolioApi";

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

const SectorAllocation = () => {

  const [sectorData, setSectorData] = useState([]);


  useEffect(() => {

    getSectorAllocation()
      .then((response) => {

        console.log("Sector Data:", response.data);

        setSectorData(response.data);

      })
      .catch((error) => {

        console.error(
          "Error fetching sector allocation:",
          error
        );

      });

  }, []);



  return (
    <ChartCard title="Sector Allocation">

      <div className="h-72">

        <ResponsiveContainer width="100%" height="100%">

          <PieChart>

            <Pie
              data={sectorData}
              cx="50%"
              cy="50%"
              innerRadius={65}
              outerRadius={90}
              paddingAngle={4}
              dataKey="currentValue"
              nameKey="sector"
              isAnimationActive={true}
              animationBegin={0}
              animationDuration={1200}
              animationEasing="ease-out"
            >

              {sectorData.map((entry,index)=>(
                <Cell
                  key={`cell-${index}`}
                  fill={COLORS[index % COLORS.length]}
                />
              ))}

            </Pie>


            <Tooltip
              formatter={(value)=>`₹${value.toLocaleString()}`}
            />


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


export default SectorAllocation;