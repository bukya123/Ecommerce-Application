import { FaShoppingCart } from "react-icons/fa";
import { MdCurrencyRupee } from "react-icons/md";
import ProductViewModal from "./ProductViewModal";
import { useState } from "react";
import truncateText from "./truncateText";
import { useDispatch } from "react-redux";
import { addToCart } from "../Store/Actions";
import toast from "react-hot-toast";
function ProductCard({item,about}){

    const [openProductViewModal,setOpenProductViewModal]=useState(false);
    const[selectedViewProduct,setSelectedViewProduct]=useState("");
    const btnLoader=false;
    const isAvailable=item.quantity && item.quantity>0;

    const disptach=useDispatch();
    

    function handleView(item){
         if(!about){
            setSelectedViewProduct(item);
            setOpenProductViewModal(true);
         } 
    }
    function addProduct(){
        disptach(addToCart(item,1,toast))
    }

    return (
        <div className="shadow-lg  m-2 border rounded-lg overflow-hidden">
            <div onClick={()=>{handleView(item)}}>
                <img src={item.image} className="w-full h-full cursor-pointer transition-transform duration-300 transform hover:scale-105  "></img>
            </div>
            <div className="p-2">
                <h2 className="text-xl semi-bold mb-2 cursor-pointer">{truncateText( item.productName)}</h2>
                <p className="text-sm mb-5">{truncateText (item.productDescription)}</p>
            </div>

            {!about && 
            <div className="flex justify-between">
                <div>
                     {
                    item.specialPrice ?(
                        <div className="flex flex-col">
                            <div className="flex"><MdCurrencyRupee className="mt-1.5"/>{Number(item.specialPrice).toFixed(2)}</div>
                            <div className="line-through text-gray-400 flex"> <MdCurrencyRupee className="mt-1.5" />{Number(item.price).toFixed(2)}</div>
                        </div>  
                    ):(
                        <div className="flex"><MdCurrencyRupee className="mt-1.5"/>{Number(item.price).toFixed(2)}</div>
                    )
                    }

                </div>
                   <div>
                    <button className="bg-violet-500 hover:bg-violet-800 w-30 rounded-sm ">
                        {isAvailable?(
                            <div className="flex">
                                <div><FaShoppingCart className="m-2"/></div>
                                <div 
                                
                                    onClick={addProduct}
                                
                                className="mt-1"> Add to cart</div>
                            </div>
                            
                        ):(
                            <div className="flex">
                                <div><FaShoppingCart className="m-2"/></div>
                                <div className="mt-1"> Stock out</div>
                            </div>
                            
                        )}
                    </button>
                    
                </div>
            </div>
              }
            
            <ProductViewModal open={openProductViewModal} setOpen={setOpenProductViewModal} item={item} isAvailable={isAvailable}/>
        </div>
    )

}
export default ProductCard;