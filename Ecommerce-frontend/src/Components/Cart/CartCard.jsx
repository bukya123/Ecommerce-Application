import Button from "@mui/material/Button";
import { useState } from "react";
import { CiSquarePlus } from "react-icons/ci";
import { FaQq } from "react-icons/fa";
import { HiOutlineTrash } from "react-icons/hi";
import { useDispatch } from "react-redux";
import { addToCart, decreaseQuantity, deleteFromCart, increaseCartQuantity } from "../../Store/Actions";
import toast from "react-hot-toast";
import { formatPrice } from "../../Utils/formatPrice";


function CartCard({item}){
    const[quantity,setQuantity]=useState(item.quantity);
    
    const dispatch=useDispatch();
    
    function handleIncQuantityChange(){
        dispatch(increaseCartQuantity(item,toast,quantity,setQuantity))
    }
    function handleDescQuantityChange(){
        if (quantity > 1) {
            const newQuantity = quantity - 1;
            setQuantity(newQuantity);
            
            dispatch(decreaseQuantity(item,newQuantity))
        }
        
    }
    function handleRemoveFromCart(){
        dispatch(deleteFromCart(item,toast));
    }
    return (

        <div className="grid md:grid-cols-5 grid-cols-4 gap-4 my-5 py-2 px-10 font-semibold  border-2 border-gray-300 mx-5">
            <div className="w-30 col-span-2">
                <div className="pb-2">
                    {item.productName}
                </div>
                <div className="pb-2">
                    <img src={item.image} className="rounded-lg"/>
                </div>
                <div>
                    <button onClick={handleRemoveFromCart}
                        className="flex items-center font-semibold space-x-2 px-4 py-1 text-xs border border-rose-600 text-rose-600 rounded-md hover:bg-red-50 transition-colors duration-200">
                        <HiOutlineTrash size={16} className="text-rose-600"/>
                        Remove
                    </button>
                </div>  
            </div>

            
            <div className="justify-center place-self-center">
                {formatPrice(Number(item.specialPrice))}
            </div>
            <div className="flex gap-2 h-10 place-self-center" >
                <Button variant="outlined" color="error" size="small" onClick={handleDescQuantityChange} disabled={item.quantity<=1}>-</Button>
                <div>{item.quantity}</div>
                <Button variant="outlined" color="success" size="small" onClick={handleIncQuantityChange}>+</Button>
            </div>
            <div className="place-self-center">
                {formatPrice(Number(item.quantity*item.price))}
            </div>
            
            
        </div>
    )
}
export default CartCard;