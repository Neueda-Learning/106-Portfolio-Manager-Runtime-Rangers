
import { useEffect, useState } from "react";
import axios from "axios";
import { getMarketStocks } from "../../api/marketApi";
import {
  buyStock,
  getHoldings,
  updateHolding,
  deleteHolding
} from "../../api/holdingApi";
import toast from "react-hot-toast";

const MarketTable = ({search}) => {
    const [marketData, setMarketData] = useState([]);

  useEffect(()=>{

 getMarketStocks()
 .then((response)=>{
    setMarketData(response.data);
 })
 .catch((error)=>{
    console.log(error);
 });

},[]);

const handleBuy = async(stock)=>{


try{


  

  const response = await getHoldings();

  const holdings = response.data;


  const existingStock = holdings.find(
    item => item.marketId === stock.id
  );



  if(existingStock){


   

    const updatedHolding = {

      id : 1,
      marketId: existingStock.marketId,

      quantity: existingStock.quantity + 1,

      purchasePrice: existingStock.purchasePrice,

      purchaseDate: existingStock.purchaseDate

    };



    await updateHolding(
      existingStock.holdingId,
      updatedHolding
    );


    toast.success(
  `${stock.symbol} quantity increased successfully`
);

  }

  else{


   


    const holding = {
      id: 1,

      marketId: stock.id,

      quantity: 1,

      purchasePrice: stock.currentPrice,

      purchaseDate:
        new Date()
        .toISOString()
        .split("T")[0]

    };



    await buyStock(holding);


  toast.success(
  `${stock.symbol} bought successfully`
);

  }




  window.dispatchEvent(
    new Event("portfolioUpdated")
  );


}

catch(error){
toast.error(
  "Buy failed. Please try again."
);

console.error(
  error
);
}


};
const handleSell = async(stock)=>{

  try{


    const response = await getHoldings();

    const holdings = response.data;


    const existingStock = holdings.find(
      item => item.marketId === stock.id
    );


    
    if(!existingStock){

      toast.error(
  `You don't own ${stock.symbol}`
);

      return;
    }



    const remainingQuantity =
      existingStock.quantity - 1;



    
    if(remainingQuantity === 0){


      await deleteHolding(
        existingStock.holdingId
      );


    }

    
    else{


      const updatedHolding = {

        id: existingStock.holdingId,

        marketId: existingStock.marketId,

        quantity: remainingQuantity,

        purchasePrice:
          existingStock.purchasePrice,

        purchaseDate:
          existingStock.purchaseDate

      };


      await updateHolding(
        existingStock.holdingId,
        updatedHolding
      );


    }



    window.dispatchEvent(
      new Event("portfolioUpdated")
    );


  toast.success(
 `${stock.symbol} sold successfully`
);


  }
  catch(error){
 toast.error(
    "Sell failed. Please try again."
  );

  console.error(
    "Sell failed:",
    error
  );

  }

};

  const filteredStocks = marketData.filter((stock) =>
  stock.companyName.toLowerCase().includes(search.toLowerCase()) ||
  stock.symbol.toLowerCase().includes(search.toLowerCase())
);
 return (
    <div className="bg-[#1D1826] border border-[#32293F] rounded-2xl mt-8 overflow-hidden">

      <div className="flex items-center justify-between px-6 py-5 border-b border-[#32293F]">
        <h2 className="text-xl font-semibold text-white">
          Market Overview
        </h2>

        <p className="text-sm text-gray-400">
          {filteredStocks.length} Stocks
        </p>
      </div>

      <table className="w-full">

        <thead className="bg-[#241C30] text-[#A8A4B3]">
          <tr>
            <th className="px-6 py-4 text-left">Company</th>
            <th className="text-left">Symbol</th>
            <th className="text-left">Sector</th>
            <th className="text-left">Current Price</th>
            <th className="text-left">Change</th>
            <th className="text-left">Exchange</th>
            <th className="text-center">Actions</th>
          </tr>
        </thead>

        <tbody>

         {filteredStocks.map((stock)=> (

            <tr
              key={stock.id}
              className="border-b border-[#32293F] hover:bg-[#262033] transition"
            >

              <td className="px-6 py-5">
                <h3 className="font-semibold text-white">
                  {stock.companyName}
                </h3>
              </td>

              <td>{stock.symbol}</td>

              <td>{stock.sector}</td>

              <td>₹{stock.currentPrice.toLocaleString()}</td>

              <td
                className={
                  stock.changePercent >= 0
                    ? "text-green-400 font-medium"
                    : "text-red-400 font-medium"
                }
              >
                {stock.changePercent >= 0 ? "+" : ""}
                {stock.changePercent}%
              </td>

              <td>{stock.exchange}</td>

              <td>
                <div className="flex justify-center gap-2">

                <button
 onClick={()=>handleBuy(stock)}
 className="
                    bg-green-500/10
                    border border-green-500/30
                    text-green-400
                    hover:bg-green-500
                    hover:text-white
                    px-4 py-2
                    rounded-xl
                    text-sm
                    font-medium
                    transition
                    "
>
 Buy
</button>

                  <button

onClick={()=>handleSell(stock)}

className="
bg-red-500/10
border border-red-500/30
text-red-400
hover:bg-red-500
hover:text-white
px-4 py-2
rounded-xl
text-sm
font-medium
transition
"

>
 Sell
</button>

                </div>
              </td>

            </tr>

          ))}

        </tbody>

      </table>

    </div>
  );
};

export default MarketTable;