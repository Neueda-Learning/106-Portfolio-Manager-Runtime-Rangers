import React from "react";
import { Bell, Moon, Search, Sun, UserCircle } from "lucide-react";

const Header = ({ theme = "dark", onToggleTheme }) => {
  return (
    <header className="app-header bg-[#16111D] border-b border-[#32293F] h-20">
      <div className="max-w-7xl mx-auto h-full flex items-center justify-between px-8">

        <h1 className="app-brand text-white text-2xl font-bold tracking-wide">
          EquityFlow
        </h1>

        <div className="flex items-center gap-4">

          <button
            type="button"
            onClick={onToggleTheme}
            className="theme-toggle"
            aria-label="Toggle light and dark theme"
            title="Toggle theme"
          >
            {theme === "dark" ? (
              <>
                <Sun size={16} />
                <span>Light</span>
              </>
            ) : (
              <>
                <Moon size={16} />
                <span>Dark</span>
              </>
            )}
          </button>

          <Search
            size={20}
            className="header-icon text-[#A8A4B3] hover:text-[#B784F7] cursor-pointer transition duration-300"
          />

          <Bell
            size={20}
            className="header-icon text-[#A8A4B3] hover:text-[#B784F7] cursor-pointer transition duration-300"
          />

          <UserCircle
            size={24}
            className="header-user text-[#B784F7] hover:text-white cursor-pointer transition duration-300"
          />

        </div>

      </div>
    </header>
  );
};

export default Header;