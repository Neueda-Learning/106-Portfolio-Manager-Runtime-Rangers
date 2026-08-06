import api from "./axiosInstance";


export const getMarketStocks = () => {
  return api.get("/market");
};


export const getTopGainers = () => {
  return api.get("/market/gainers");
};


export const getTopLosers = () => {
  return api.get("/market/losers");
};