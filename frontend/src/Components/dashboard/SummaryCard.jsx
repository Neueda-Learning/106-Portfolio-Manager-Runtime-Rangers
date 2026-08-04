import React from 'react';

const SummaryCard = ({
    title, value,change,color
}) => {
    return (
        <div className='bg-white rounded-xl shadow-md p-6'>
            <h3 className = "text-gray-500 text-sm">{title}</h3>
             <h2 className="text-3xl font-bold mt-3">

                {value}

            </h2>
            <p className={`mt-4 font-semibold ${color}`}>

                {change}

            </p>
        </div>
    );
};
export default SummaryCard;