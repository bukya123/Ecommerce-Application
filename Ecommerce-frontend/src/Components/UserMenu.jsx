import * as React from 'react';
import Button from '@mui/material/Button';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import { FaRegUser, FaShoppingCart, FaUser } from 'react-icons/fa';
import { Link, useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { IoLogOut } from "react-icons/io5";
import BackDrop from './BackDrop';
import { Logoutuser } from '../Store/Actions';

function UserMenu(){
    const [anchorEl, setAnchorEl] = React.useState(null);
    const open = Boolean(anchorEl);
    const {user}=useSelector((state)=>state.auth);
    const dispatch=useDispatch();
    const navigate=useNavigate();
  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleLogoutUser=()=>{
    dispatch(Logoutuser(navigate))
  }

  return (
    <div>
      
      <FaUser aria-controls={open ? 'basic-menu' : undefined} aria-haspopup="true"  aria-expanded={open ? 'true' : undefined} onClick={handleClick} size={25}/>
      <Menu
        id="basic-menu"
        anchorEl={anchorEl}
        open={open}
        onClose={handleClose}
        slotProps={{
          list: {
            'aria-labelledby': 'basic-button',
          },
        }}
      >
        <Link to="/profile">
            <MenuItem onClick={handleClose} className='flex gap-2 items-center'>
                <FaRegUser size={15}/>
                <span>{user.username}</span> 
            </MenuItem>
        </Link>

        <Link to="/profile/order">
        <MenuItem onClick={handleClose} className='flex gap-2 items-center'>
         <FaShoppingCart size={15} />
         <span>Order</span>
        </MenuItem>
        </Link>

        
        <MenuItem onClick={handleLogoutUser} className='flex gap-2 items-center'>
        <IoLogOut size={20}/>
        <span>Logout</span>
        </MenuItem>
        
      </Menu>
        {open && <BackDrop data={open}/>}
    </div>

  );
}
export default UserMenu;