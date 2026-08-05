import React from 'react';

const ChartCard =({title, children}) =>{
    return (
    <div className="w-full min-h-77.5 bg-[#1D1826] border border-[#32293F] rounded-2xl shadow-lg p-6">
      <h2 className="text-xl font-semibold text-white mb-6">
        {title}
      </h2>

      {children}
    </div>
  );
};
export default ChartCard;
