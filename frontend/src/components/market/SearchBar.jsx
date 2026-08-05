
import { Search } from "lucide-react";

const SearchBar = ({ search, setSearch }) => {
  return (
    <div className="flex items-center gap-4 mt-8 mb-6">

      <div className="relative w-full">

        <Search
          size={20}
          className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400"
        />

        <input
          type="text"
          placeholder="Search by company or symbol..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="w-full bg-[#1D1826] border border-[#32293F] rounded-xl py-3 pl-12 pr-4 outline-none focus:border-[#8B5CF6]"
        />

      </div>

    </div>
  );
};

export default SearchBar;