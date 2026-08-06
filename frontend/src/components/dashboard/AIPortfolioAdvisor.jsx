import {useState} from "react";
import {analyzePortfolio} from "../../api/aiApi";
import toast from "react-hot-toast";
import ReactMarkdown from "react-markdown";

const AIPortfolioAdvisor = ({holdings})=>{


const [analysis,setAnalysis]=useState("");

const [loading,setLoading]=useState(false);



const generateAnalysis = async()=>{


try{

setLoading(true);


const result =
await analyzePortfolio(holdings);


setAnalysis(result);


}

catch(error){

console.log(error);

toast.error(
"AI analysis failed"
);

}

finally{

setLoading(false);

}


};



return (

<div className="
theme-card
ai-panel
bg-[#1D1826]
border border-[#32293F]
rounded-2xl
p-6
mt-8
">


<div className="flex justify-between items-center">


<h2 className="text-xl font-semibold text-white">

🤖 AI Portfolio Advisor

</h2>


<button

onClick={generateAnalysis}

className="
bg-green-500
px-4 py-2
rounded-lg
text-white
"

>

{
loading
?
"Analyzing..."
:
"Generate"
}

</button>


</div>



<div
className="
ai-markdown
mt-5
text-gray-300
prose
prose-invert
max-w-none
"
>

{
analysis ? (
<ReactMarkdown>
{analysis}
</ReactMarkdown>
)
:
(
"Generate AI insights for your portfolio"
)
}

</div>


</div>

);

};


export default AIPortfolioAdvisor;