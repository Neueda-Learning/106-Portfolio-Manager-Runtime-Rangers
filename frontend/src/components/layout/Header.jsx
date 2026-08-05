import React from "react";
import { Bell, Search, UserCircle } from "lucide-react";

const Header = () => {
  return (
    <header className="bg-[#16111D] border-b border-[#32293F] h-20">
      <div className="max-w-7xl mx-auto h-full flex items-center justify-between px-8">

        <h1 className="text-white text-2xl font-bold tracking-wide">
          Portfolio Manager
        </h1>

        <div className="flex items-center gap-6">

          <Search
            size={20}
            className="text-[#A8A4B3] hover:text-[#B784F7] cursor-pointer transition duration-300"
          />

          <Bell
            size={20}
            className="text-[#A8A4B3] hover:text-[#B784F7] cursor-pointer transition duration-300"
          />

          <UserCircle
            size={24}
            className="text-[#B784F7] hover:text-white cursor-pointer transition duration-300"
          />

        </div>

      </div>
    </header>
  );
};

export default Header;