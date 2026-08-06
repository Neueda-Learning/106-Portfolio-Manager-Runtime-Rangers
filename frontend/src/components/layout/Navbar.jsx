import { NavLink } from "react-router-dom";

const Navbar = () => {
  const navClass = ({ isActive }) =>
    `nav-link h-full flex items-center px-6 transition border-b-2 ${
      isActive
        ? "nav-link-active border-[#B784F7] text-[#B784F7] font-semibold"
        : "border-transparent text-[#A8A4B3] hover:text-white"
    }`;

  return (
    <div className="app-navbar mt-2 bg-[#1D1826] border border-[#32293F] rounded-2xl shadow-lg">
      <div className="flex items-center h-16">

        <NavLink to="/" end className={navClass}>
          Dashboard
        </NavLink>

        <NavLink to="/market" className={navClass}>
          Market
        </NavLink>

      </div>
    </div>
  );
};

export default Navbar;