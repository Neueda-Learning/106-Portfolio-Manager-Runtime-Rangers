import axios from "axios";


const GROQ_KEY = import.meta.env.VITE_GROQ_KEY;


export const analyzePortfolio = async (holdings)=>{


const cleanData = holdings.map(stock => ({

    company: stock.companyName,
    symbol: stock.symbol,
    quantity: stock.quantity,
    buyPrice: stock.purchasePrice,
    currentPrice: stock.currentPrice,
    profitLoss: stock.gainLoss

}));



const prompt = `

You are an AI portfolio advisor for a stock investment dashboard.

Analyze this portfolio:

${JSON.stringify(cleanData)}


Give a SHORT professional summary.

Format exactly:

## 🟢 BUY
- Stock: Reason (one line)


## 🔴 SELL
- Stock: Reason (one line)


## 🟡 HOLD
- Stock: Reason (one line)


## ⚠️ Risk
One short sentence.


Rules:
- Maximum 2 stocks per category
- Keep each reason under 15 words
- No long explanations
- Be concise

`;



const response = await axios.post(

"https://api.groq.com/openai/v1/chat/completions",

{

model:"llama-3.3-70b-versatile",

messages:[

{
role:"system",
content:"You are a professional investment analysis assistant."
},

{
role:"user",
content:prompt
}

],

temperature:0.5

},

{

headers:{

Authorization:`Bearer ${GROQ_KEY}`,

"Content-Type":"application/json"

}

}

);



return response.data.choices[0].message.content;


};

export const analyzeMarket = async (marketData) => {


const cleanData = marketData.map(stock => ({

    company: stock.companyName,
    symbol: stock.symbol,
    sector: stock.sector,
    currentPrice: stock.currentPrice,
    changePercent: stock.changePercent

}));



const prompt = `

You are an AI stock market analyst.

Analyze these available market stocks:

${JSON.stringify(cleanData)}


Give a SHORT professional market recommendation.

Format exactly:

## 🟢 BUY
- Stock: Reason (one line)


## 🟡 WATCH
- Stock: Reason (one line)


## 🔴 AVOID
- Stock: Reason (one line)


## 📊 Market Insight
One short sentence.


Rules:
- Maximum 3 stocks per category
- Keep each reason under 15 words
- No long explanations
- Be concise

`;



const response = await axios.post(

"https://api.groq.com/openai/v1/chat/completions",

{

model:"llama-3.3-70b-versatile",

messages:[

{
role:"system",
content:"You are a professional stock market analyst."
},

{
role:"user",
content:prompt
}

],

temperature:0.5

},

{

headers:{

Authorization:`Bearer ${GROQ_KEY}`,

"Content-Type":"application/json"

}

}

);



return response.data.choices[0].message.content;


};