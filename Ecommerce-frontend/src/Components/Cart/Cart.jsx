import { useDispatch, useSelector } from "react-redux";
import CartCard from "./CartCard";
import { fetchProducts } from "../../Store/Actions";
import { MdArrowBack, MdShoppingCart } from "react-icons/md";
import { Link } from "react-router-dom";
import CartEmpty from "./CartEmpty";
import { formatPrice } from "../../Utils/formatPrice";


function Cart(){
   const { cart } = useSelector((state) => state.carts);
    const cartItems=cart;
    const newCart = { ...cart };

    newCart.totalPrice = cart?.reduce(
        (acc, cur) => acc + Number(cur?.specialPrice) * Number(cur?.quantity), 0
    );

    if(cartItems.length==0){
        return (
            <CartEmpty/>
        )
    }
    return (
        <div>
            <div className="flex justify-center font-bold p-5"> Your Cart ( {cartItems.length} ) items</div>
                <div className="grid md:grid-cols-5 grid-cols-4 gap-4 m-5 py-2 px-10 font-semibold">
                    <div className="w-30 col-span-2">
                        Product
                    </div>
                    <div className="place-self-center">
                        Price
                    </div>
                    <div className="place-self-center">
                        Quantity
                    </div>
                    <div className="place-self-center">
                        NetPrice
                    </div>
                </div>
            {
                (cartItems && cartItems.map((item)=>{
                    
                    return <CartCard item={item}/>
                }) )

            }
            
            
                <div className="flex-col justify-items-end m-4">
                    <div >
                        <span>Subtotal</span>
                        
                        <span>{formatPrice(newCart?.totalPrice)}</span>
                    </div>

                    <p className="text-slate-500">
                        Taxes and shipping calculated at checkout
                    </p>

                    <Link  className="flex justify-items-end" to="/checkout">
                    <button
                        onClick={() => {}}
                        className="font-semibold w-[300px] py-2 px-4 rounded-xs bg-custom-blue text-white flex items-center justify-center gap-2 hover:text-gray-300 transition duration-500">
                        <MdShoppingCart size={20} />
                        Checkout
                    </button>
                    </Link>

                    <Link  className="flex justify-items-end" to="/products">
                        <MdArrowBack />
                        <span>Continue Shopping</span>
                    </Link>
            </div>

        </div>
    )
}
export default Cart;