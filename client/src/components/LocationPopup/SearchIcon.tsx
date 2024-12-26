import { Check, CircleHelp, Search } from "lucide-react";

const SearchIcon = ({
  address,
  isValidAddress,
}: {
  address: string;
  isValidAddress: boolean;
}) => {
  if (address === "") return <Search size={20} color="#1ab66d" />;
  if (isValidAddress) return <Check size={20} color="#1ab66d" />;
  return <CircleHelp size={20} color="#ff008f" />;
};

export default SearchIcon;
