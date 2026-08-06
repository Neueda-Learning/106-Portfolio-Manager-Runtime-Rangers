import { useEffect, useState } from "react";
import {
  getHoldings,
  updateHolding,
  deleteHolding
} from "../../api/holdingApi";
import toast from "react-hot-toast";


const HoldingsTable = () => {

  const [holdings, setHoldings] = useState([]);
  const [sellModal, setSellModal] = useState(null);
  const [sellQuantity, setSellQuantity] = useState(1);
  const [buyQuantity, setBuyQuantity] = useState(1);
  const [buyModal, setBuyModal] = useState(null);

  useEffect(() => {

    getHoldings()
      .then(res => {
        setHoldings(res.data);
      })
      .catch(error => {
        console.error(
          "Fetching holdings failed:",
          error
        );
      });

  }, []);



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


const updatedHolding = {

  id: buyModal.holdingId,

  marketId: buyModal.marketId,

  quantity: buyModal.quantity + buyQty,

  purchasePrice: buyModal.purchasePrice,

  purchaseDate: buyModal.purchaseDate

};



try{


await updateHolding(
  buyModal.holdingId,
  updatedHolding
);



const response = await getHoldings();

setHoldings(response.data);



window.dispatchEvent(
 new Event("portfolioUpdated")
);



toast.success(
`${buyQty} ${buyModal.symbol} bought successfully`
);



setBuyModal(null);

setBuyQuantity(1);


}

catch(error){

console.error(
"Buy failed:",
error.response?.data || error
);


toast.error(
"Buy failed"
);


}


};



  const openSellModal = (stock) => {

    setSellModal(stock);
    setSellQuantity(1);

  };



  const handleSellConfirm = async()=>{

  const sellQty = Number(sellQuantity);


if(
  sellQty <= 0 ||
  sellQty > sellModal.quantity
){

toast.error(
`You have to sell minimum 1 stock and maximum ${sellModal.quantity} stocks`
);

return;

}



  const remainingQuantity =
    sellModal.quantity - sellQty;


  try{


  
    if(remainingQuantity === 0){


      await deleteHolding(
        sellModal.holdingId
      );
       toast.success(
        `${sellModal.symbol} removed from portfolio`
      );


    }


    
    else{


      const updatedHolding = {

        id: sellModal.holdingId,

        marketId: sellModal.marketId,

        quantity: remainingQuantity,

        purchasePrice: sellModal.purchasePrice,

        purchaseDate: sellModal.purchaseDate

      };


      await updateHolding(
        sellModal.holdingId,
        updatedHolding
      );


    }



 

    const response = await getHoldings();

    setHoldings(response.data);



    

    window.dispatchEvent(
      new Event("portfolioUpdated")
    );



    toast.success(
  `${sellQty} ${sellModal.symbol} sold successfully`
);


    setSellModal(null);

    setSellQuantity(1);


  }
  catch(error){

    console.error(
      "Sell failed:",
      error.response?.data || error
    );

  }

};



  return (

    <>


    <div className="
      theme-card holdings-table
      bg-[#1D1826]
      border border-[#32293F]
      rounded-2xl
      mt-8
      overflow-hidden
    ">


      <table className="w-full">


      <thead className="theme-table-head bg-[#241C30] text-[#A8A4B3] text-sm uppercase tracking-wider">

  <tr className="border-b border-[#32293F]">

    <th className="px-6 py-4 text-left font-medium">
      Company
    </th>

    <th className="px-4 py-4 text-left font-medium">
      Qty
    </th>

    <th className="px-4 py-4 text-left font-medium">
      Current Price
    </th>

    <th className="px-4 py-4 text-left font-medium">
      Current Value
    </th>

    <th className="px-4 py-4 text-left font-medium">
      P&L
    </th>

    <th className="px-6 py-4 text-center font-medium">
      Actions
    </th>

  </tr>

</thead>



        <tbody>

        {
          holdings.map((stock)=>(

            <tr
              key={stock.holdingId}
              className="theme-table-row border-b border-[#32293F]"
            >


              <td className="px-6 py-5">

                <h3 className="theme-primary-text font-semibold text-white">
                  {stock.companyName}
                </h3>

                <p className="theme-muted text-sm text-[#A8A4B3]">
                  {stock.symbol}
                </p>

              </td>



              <td  className="px-6 py-5">
                {stock.quantity}
              </td>



              <td  className="px-8 py-5">
                ₹{stock.currentPrice.toLocaleString()}
              </td>



              <td  className="px-10 py-5">
                ₹{stock.currentValue.toLocaleString()}
              </td>



              <td
                className={
                  stock.gainLoss >= 0
                  ? "text-green-400"
                  : "text-red-400"
                }
              >
                ₹{stock.gainLoss.toLocaleString()}
              </td>



              <td className="px-6 py-5">

                <div className="flex justify-center gap-3">


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
                    + Buy
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
                    − Sell
                  </button>


                </div>

              </td>


            </tr>

          ))
        }

        </tbody>


      </table>


    </div>




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

    </>

  );

};


export default HoldingsTable;