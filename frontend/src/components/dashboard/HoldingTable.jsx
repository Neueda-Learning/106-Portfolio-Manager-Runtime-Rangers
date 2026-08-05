import { useEffect, useState } from "react";
import {
  getHoldings,
  updateHolding,
  deleteHolding
} from "../../api/holdingApi";


const HoldingsTable = () => {

  const [holdings, setHoldings] = useState([]);
  const [sellModal, setSellModal] = useState(null);
  const [sellQuantity, setSellQuantity] = useState(1);


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



  const handleBuy = async(stock) => {

    const updatedHolding = {
          id: 1,
      marketId: stock.marketId,
      quantity: stock.quantity + 1,
      purchasePrice: stock.purchasePrice,
      purchaseDate: stock.purchaseDate

    };


    try {

      await updateHolding(
        stock.holdingId,
        updatedHolding
      );


      const response = await getHoldings();

      setHoldings(response.data);


      window.dispatchEvent(
        new Event("portfolioUpdated")
      );


      alert(
        `${stock.symbol} quantity increased`
      );


    }
    catch(error){

      console.error(
        "Buy failed:",
        error.response?.data || error
      );

    }

  };



  const openSellModal = (stock) => {

    setSellModal(stock);
    setSellQuantity(1);

  };



  const handleSellConfirm = async()=>{

  const sellQty = Number(sellQuantity);


  const remainingQuantity =
    sellModal.quantity - sellQty;


  try{


  
    if(remainingQuantity === 0){


      await deleteHolding(
        sellModal.holdingId
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



    alert(
      `${sellQty} shares sold`
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
      bg-[#1D1826]
      border border-[#32293F]
      rounded-2xl
      mt-8
      overflow-hidden
    ">


      <table className="w-full">


       <thead className="bg-[#241C30] text-[#A8A4B3] text-sm uppercase tracking-wider">

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
              className="border-b border-[#32293F]"
            >


              <td className="px-6 py-5">

                <h3 className="font-semibold text-white">
                  {stock.companyName}
                </h3>

                <p className="text-sm text-[#A8A4B3]">
                  {stock.symbol}
                </p>

              </td>



              <td td className="px-6 py-5">
                {stock.quantity}
              </td>



              <td td className="px-8 py-5">
                ₹{stock.currentPrice.toLocaleString()}
              </td>



              <td td className="px-10 py-5">
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
          fixed inset-0
          bg-black/60
          flex items-center justify-center
          z-50
        ">


          <div className="
            bg-[#1D1826]
            border border-[#32293F]
            rounded-2xl
            p-6
            w-96
          ">


            <h2 className="
              text-xl
              font-semibold
              text-white
            ">
              Sell {sellModal.symbol}
            </h2>



            <p className="text-[#A8A4B3] mt-3">

              Available Quantity:

              <span className="text-white font-semibold ml-2">
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


    </>

  );

};


export default HoldingsTable;