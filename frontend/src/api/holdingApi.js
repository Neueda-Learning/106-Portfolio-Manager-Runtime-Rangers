import api from "./axiosInstance";


// Get holdings
export const getHoldings = () => {
  return api.get("/portfolio/allocation");
};


// Buy new stock
export const buyStock = (holding) => {
  return api.post("/holdings", holding);
};


// Update holding quantity 
export const updateHolding = (id, holding) => {
  return api.put(`/holdings/${id}`, holding);
};


// Delete complete holding (only when quantity becomes 0)
export const deleteHolding = (id) => {
  return api.delete(`/holdings/${id}`);
};