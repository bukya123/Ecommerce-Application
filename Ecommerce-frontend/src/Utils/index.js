import { FaBoxOpen, FaHome, FaShoppingCart, FaStore, FaThList } from "react-icons/fa";
import { bannerImageOne, bannerImageThree, bannerImageTwo } from "./constant";
import Badge from '@mui/material/Badge';
import { styled } from "@mui/material/styles";
export const bannerLists = [
    {
        id: 1,
        image: bannerImageOne,
        title: "Home Comfort",
        subtitle: "Living Room",
        description: "Upgrade your space with cozy and stylish sofas",
      },
      {
        id: 2,
        image: bannerImageTwo,
        title: "Entertainment Hub",
        subtitle: "Smart TV",
        description: "Experience the latest in home entertainment",
      },
      {
        id: 3,
        image: bannerImageThree,
        title: "Playful Picks",
        subtitle: "Kids' Clothing",
        description: "Bright and fun styles for kids, up to 20% off",
    }
];

export const StyledBadge = styled(Badge)(({ theme }) => ({
  '& .MuiBadge-badge': {
    right: -3,
    top: 13,
    border: `2px solid ${(theme.vars ?? theme).palette.background.paper}`,
    padding: '0 4px',
  },
}));