
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
    const [buyModal, setBuyModal] = useState(null);
const [buyQuantity, setBuyQuantity] = useState(1);

const [sellModal, setSellModal] = useState(null);
const [sellQuantity, setSellQuantity] = useState(1);

  useEffect(()=>{

 getMarketStocks()
 .then((response)=>{
    setMarketData(response.data);
 })
 .catch((error)=>{
    console.log(error);
 });

},[]);

const openBuyModal = (stock)=>{

setBuyModal(stock);
setBuyQuantity(1);

};



const handleBuyConfirm = async()=>{


const buyQty = Number(buyQuantity);

if(!buyQty || buyQty <= 0){

toast.error(
"Quantity must be greater than 0"
);

return;

}


try{


const response = await getHoldings();

const holdings = response.data;



const existingStock = holdings.find(
item => item.marketId === buyModal.id
);



if(existingStock){


const updatedHolding={

id: existingStock.holdingId,

marketId: existingStock.marketId,

quantity:
existingStock.quantity + buyQty,

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


else{


const holding={

marketId: buyModal.id,

quantity: buyQty,

purchasePrice:
buyModal.currentPrice,

purchaseDate:
new Date()
.toISOString()
.split("T")[0]

};


await buyStock(holding);


}



toast.success(
`${buyQty} ${buyModal.symbol} bought successfully`
);



window.dispatchEvent(
new Event("portfolioUpdated")
);



setBuyModal(null);


}

catch(error){

console.error(error);

toast.error(
"Buy failed"
);

}


};


const openSellModal=(stock)=>{


setSellModal(stock);
setSellQuantity(1);


};



const handleSellConfirm = async()=>{


const sellQty = Number(sellQuantity);


if(!sellQty || sellQty <= 0){

toast.error(
"Sell quantity must be greater than 0"
);

return;

}


try{


const response = await getHoldings();

const holdings = response.data;


const existingStock = holdings.find(
item => item.marketId === sellModal.id
);



if(!existingStock){

toast.error(
"You don't own this stock"
);

return;

}



if(sellQty > existingStock.quantity){

toast.error(
`Maximum quantity is ${existingStock.quantity}`
);

return;

}



const remaining =
existingStock.quantity - sellQty;



if(remaining === 0){

await deleteHolding(
existingStock.holdingId
);

}

else{


const updatedHolding = {

id: existingStock.holdingId,

marketId: existingStock.marketId,

quantity: remaining,

purchasePrice: existingStock.purchasePrice,

purchaseDate: existingStock.purchaseDate

};


await updateHolding(
existingStock.holdingId,
updatedHolding
);


}



toast.success(
`${sellQty} ${sellModal.symbol} sold successfully`
);



window.dispatchEvent(
new Event("portfolioUpdated")
);



setSellModal(null);


}

catch(error){

console.error(error);

toast.error(
"Sell failed"
);

}


};

  const filteredStocks = marketData.filter((stock) =>
  stock.companyName.toLowerCase().includes(search.toLowerCase()) ||
  stock.symbol.toLowerCase().includes(search.toLowerCase())
);
 return (
    <div className="theme-card market-table bg-[#1D1826] border border-[#32293F] rounded-2xl mt-8 overflow-hidden">

      <div className="theme-table-toolbar flex items-center justify-between px-6 py-5 border-b border-[#32293F]">
        <h2 className="theme-primary-text text-xl font-semibold text-white">
          Market Overview
        </h2>

        <p className="theme-muted text-sm text-gray-400">
          {filteredStocks.length} Stocks
        </p>
      </div>

      <table className="w-full">

        <thead className="theme-table-head bg-[#241C30] text-[#A8A4B3]">
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
              className="theme-table-row border-b border-[#32293F] hover:bg-[#262033] transition"
            >

              <td className="px-6 py-5">
                <h3 className="theme-primary-text font-semibold text-white">
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
 onClick={()=>openBuyModal(stock)}
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

onClick={()=>openSellModal(stock)}

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

       {
      sellModal && (

        <div className="
          theme-modal-overlay
          fixed inset-0
          bg-black/60
          flex items-center justify-center
          z-50
        ">


          <div className="
            theme-modal
            bg-[#1D1826]
            border border-[#32293F]
            rounded-2xl
            p-6
            w-96
          ">


            <h2 className="
              theme-primary-text
              text-xl
              font-semibold
              text-white
            ">
              Sell {sellModal.symbol}
            </h2>



            <p className="theme-muted text-[#A8A4B3] mt-3">

              Available Quantity:

              <span className="theme-primary-text text-white font-semibold ml-2">
                {sellModal.quantity}
              </span>

            </p>



            <input

              type="number"

              value={sellQuantity}

              min="1"

              max={sellModal.quantity}

              onChange={(e)=>setSellQuantity(e.target.value)}

              className="
              theme-input
              mt-5
              w-full
              bg-[#241C30]
              border border-[#32293F]
              rounded-xl
              px-4 py-3
              text-white
              "

            />



            <div className="
              flex
              justify-end
              gap-3
              mt-6
            ">


              <button

                onClick={()=>setSellModal(null)}

                className="
                theme-button-secondary
                px-4 py-2
                rounded-xl
                bg-[#32293F]
                text-white
                "

              >
                Cancel

              </button>



              <button

                onClick={handleSellConfirm}

                className="
                px-4 py-2
                rounded-xl
                bg-red-500
                text-white
                hover:bg-red-600
                "

              >
                Confirm Sell

              </button>


            </div>


          </div>


        </div>

      )
    }


     {
buyModal && (

<div className="
  theme-modal-overlay
  fixed inset-0
  bg-black/60
  flex items-center justify-center
  z-50
">


<div className="
  theme-modal
  bg-[#1D1826]
  border border-[#32293F]
  rounded-2xl
  p-6
  w-96
">


<h2 className="
  theme-primary-text
  text-xl
  font-semibold
  text-white
">

Buy {buyModal.symbol}

</h2>



<p className="theme-muted text-[#A8A4B3] mt-3">

Current Quantity:

<span className="
theme-primary-text
text-white
font-semibold
ml-2
">

{buyModal.quantity}

</span>

</p>



<input

type="number"

value={buyQuantity}

min="1"

onChange={(e)=>{

setBuyQuantity(e.target.value);

}}

className="
theme-input
mt-5
w-full
bg-[#241C30]
border border-[#32293F]
rounded-xl
px-4 py-3
text-white
"

/>



<div className="
flex
justify-end
gap-3
mt-6
">


<button

onClick={()=>setBuyModal(null)}

className="
theme-button-secondary
px-4 py-2
rounded-xl
bg-[#32293F]
text-white
"

>

Cancel

</button>



<button

onClick={handleBuyConfirm}

className="
px-4
py-2
rounded-xl
bg-green-500
text-white
hover:bg-green-600
"

>

Confirm Buy

</button>


</div>


</div>


</div>

)
}

    </div>
  );
};

export default MarketTable;