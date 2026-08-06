import { useState } from "react";
import ReactMarkdown from "react-markdown";
import { analyzeMarket } from "../../api/aiApi";
import toast from "react-hot-toast";


const AIMarketScanner = ({ marketData }) => {


const [analysis,setAnalysis] = useState("");
const [loading,setLoading] = useState(false);



const generateAnalysis = async()=>{


try{

setLoading(true);


const result = await analyzeMarket(
  marketData
);


setAnalysis(result);


}

catch(error){

console.error(error);

toast.error(
"AI market analysis failed"
);

}

finally{

setLoading(false);

}


};



return (

<div className="
bg-[#1D1826]
border border-[#32293F]
rounded-2xl
p-6
mt-8
">


<div className="
flex
justify-between
items-center
">


<h2 className="
text-xl
font-semibold
text-white
">

🤖 AI Market Scanner

</h2>



<button

onClick={generateAnalysis}

className="
bg-green-500
hover:bg-green-600
px-5
py-2
rounded-xl
text-white
font-medium
transition
"

>

{
loading
?
"Analyzing..."
:
"Analyze Market"
}

</button>


</div>



<div className="
mt-5
bg-[#241C30]
rounded-xl
p-5
text-gray-300
prose
prose-invert
max-w-none
">


{
analysis
?
<ReactMarkdown>
{analysis}
</ReactMarkdown>
:
"Click analyze to get AI market suggestions"
}


</div>


</div>

);

};


export default AIMarketScanner;