import api from "./axiosInstance";


export const getPortfolioSummary = () => {
  return api.get("/portfolio/summary");
};


export const getPortfolioAllocation = () => {
  return api.get("/portfolio/allocation");
};


export const getSectorAllocation = () => {
  return api.get("/portfolio/sectors");
};