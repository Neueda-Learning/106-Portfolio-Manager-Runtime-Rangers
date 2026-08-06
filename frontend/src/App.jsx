import { useEffect, useState } from 'react'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import Dashboard from './Pages/Dashboard'
import Market from "./Pages/Market";
import './App.css'


function App() {
  const [theme, setTheme] = useState(() => {
    const savedTheme = window.localStorage.getItem("theme");
    return savedTheme === "light" ? "light" : "dark";
  });

  useEffect(() => {
    document.documentElement.setAttribute("data-theme", theme);
    window.localStorage.setItem("theme", theme);
  }, [theme]);

  const toggleTheme = () => {
    setTheme((currentTheme) =>
      currentTheme === "dark" ? "light" : "dark"
    );
  };

  return (
    <BrowserRouter>
    <Routes>
      <Route
        path="/"
        element={
          <Dashboard
            theme={theme}
            onToggleTheme={toggleTheme}
          />
        }
      />
      <Route
        path="/market"
        element={
          <Market
            theme={theme}
            onToggleTheme={toggleTheme}
          />
        }
      />
    </Routes>
    </BrowserRouter>
  )
}

export default App;
