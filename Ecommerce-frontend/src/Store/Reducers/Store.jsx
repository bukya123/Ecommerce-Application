
import { configureStore } from "@reduxjs/toolkit";
import ProductReducer from "./ProductReducer";
import ErrorReducer from "./ErrorReducer";
import CartReducer from "./CartReducer";
import AuthReducer from "./AuthReducer";
import PaymentMethodReducer from "./PaymentMethodReducer";

const user = localStorage.getItem("auth")
    ? JSON.parse(localStorage.getItem("auth"))
    : null;

const cartItems=localStorage.getItem("cartItems") ? JSON.parse(localStorage.getItem("cartItems")) : [];
const selectedCheckOutAddress=localStorage.getItem("address")?JSON.parse(localStorage.getItem("address")): null;
const initialCart={
     auth: { user: user ,selectedCheckOutAddress:selectedCheckOutAddress},
    carts:{cart:cartItems},
}
const Store=configureStore({
    reducer:{
        products:ProductReducer,
        error:ErrorReducer,
        categories:ProductReducer,
        carts:CartReducer,
        auth:AuthReducer,
        payment:PaymentMethodReducer,
    },
    preloadedState:initialCart,
})
export default Store;