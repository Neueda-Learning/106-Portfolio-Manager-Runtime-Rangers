import React  from 'react';
import { Bell, Search, UserCircle } from "lucide-react";

const Header = () =>{
    return(

        <header className = "bg-blue-600 h-20 shadow-lg">
            <div className = "max-w-7xl mx-auto h-full flex items-center justify-between px-6">
                <h1 className = "text-white text-2xl font-bold">Portfolio Manager</h1>

                <div className = "flex items-center gap-6 text-white">

                   <Search size = {20} className = "cursor-pointer"/>
                   <Bell size = {20} className = "cursor-pointer"/>
                   <UserCircle size = {20} className = "cursor-pointer"/>

                </div>


            </div>
        </header>
    );

}
export default Header;