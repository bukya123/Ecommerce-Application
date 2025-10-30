
import toast from "react-hot-toast";
import  {api} from "../../Api/api";

export const fetchProducts=(queryString)=>async(dispatch)=>{
    try{
        dispatch({
            type:"LOADING"
        })
        const {data}=await api.get(`/public/products?${queryString}`);
        dispatch({
            type:"FETCH_PRODUCTS",
            payload:data.content,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalElements: data.totalElements,
            totalPages: data.totalPages,
            lastPage: data.lastPage
        })
        dispatch({
            type:"SUCCESS"
        })

    }catch(error){
        dispatch({
            type:"ERROR",
            payload:error?.response?.data?.message || "failed to fetch Products"
        })

    }
    
}

export const fetchCategories=()=>async(dispatch)=>{
    try{
        dispatch({
            type:"CATEGORIES_LOADING"
        })
        const {data}=await api.get(`/public/categories`);
        dispatch({
            type:"FETCH_CATEGORIES",
            payload:data.content,
            pageNumber: data.pageNumber,
            pageSize: data.pageSize,
            totalElements: data.totalElements,
            totalPages: data.totalPages,
            lastPage: data.lastPage

        })
        dispatch({
            type:"CATEGORIES_SUCCESS"
        })
    }catch(error){
        dispatch({
            type:"CATEGORIES_ERROR",
            payload:error?.response?.data?.message || "failed to fetch categories"
        })
    }
}

export const addToCart=(data,qnty=1,toast)=>
    (dispatch,getState)=>{
        
         const { products } = getState().products;
        const getProduct = products.find(
            (item) => item.productId === data.productId
        );

        // Check for stocks
        const isQuantityExist = getProduct.quantity >= qnty;

        // If in stock -> add
        if (isQuantityExist) {
            dispatch({ type: "ADD_PRODUCT_TO_CART", payload: {...data, quantity: qnty}});
            toast.success(`${data?.productName} added to the cart`);
            localStorage.setItem("cartItems", JSON.stringify(getState().carts.cart));
        } else {
            // error
            toast.error("Out of stock");
        }
        
}



export const increaseCartQuantity=(data,toast,quantity,setQuantity)=>
    (dispatch,getState)=>{
        const { products } = getState().products;
        
        const getProduct =  products.find(
            (item) => item.productId === data.productId
        );

        const isQuantityExist = getProduct.quantity >= quantity + 1;

        if (isQuantityExist) {
            const newQuantity = quantity + 1;
            setQuantity(newQuantity);

            dispatch({
                type: "ADD_PRODUCT_TO_CART",
                payload: {...data, quantity: newQuantity},
            });
            localStorage.setItem("cartItems", JSON.stringify(getState().carts.cart));
        } else {
            toast.error("Quantity Reached to Limit");
        }

}

export const decreaseQuantity=(data,newQuantity)=>
    (dispatch,getState)=>{
        dispatch({
            type: "ADD_PRODUCT_TO_CART",
            payload: {...data, quantity: newQuantity},
        });
        localStorage.setItem("cartItems", JSON.stringify(getState().carts.cart));
    }

export const deleteFromCart=(data,toast)=>
    (dispatch,getState)=>{

        dispatch({
            type:"REMOVE_FROM_CART",
            payload:data
        })
        toast.success(`${data.productName} removed from cart`);
        localStorage.setItem("cartItems",JSON.stringify(getState().carts.cart));
    }

export const authenticateSignInUser=(sendData,toast,reset, navigate, setLoader)=>async(dispatch)=>{
    try{
        setLoader(true);
        const{data}=await api.post("/auth/SignIn",sendData);
         dispatch({ type: "LOGIN_USER", payload: data });
        localStorage.setItem("auth", JSON.stringify(data));
        reset();
            toast.success("Login Success");
            navigate("/");
        } catch (error) {
            console.log(error);
            toast.error(error?.response?.data?.message || "Internal Server Error");
        } finally {
            setLoader(false);
        }
}

export const SignUpUser=(sendData,toast,reset,navigate,setLoader)=>async(dispatch)=>{
    try{
        setLoader(true);
        const{data}=await api.post("/auth/SignUp",sendData);
        reset();
        toast.success("User Registered Successfully");
        navigate("/login");
    }catch(error){
        console.log(error);
        toast.error(error?.response?.data?.message || "Internal Servor Error");
    }finally{
        setLoader(false);
    }
}
export const Logoutuser=(navigate)=>(dispatch)=>{
        dispatch({
            type:"LOGOUT_USER"
        })
        localStorage.removeItem("auth")
        navigate("/login");
        toast.success("You have successfully loggedOut");
   
}

export const SaveUserAddress=(sendData,toast,reset,setOpen,setLoader,selectedAddress)=>async(dispatch)=>{
    try{
        setLoader(true)
        if(!selectedAddress){
            const{data}=await api.post("/addresses",sendData);
        }else{
            const{data}=await api.put(`/addresses/${selectedAddress}`,sendData);
            dispatch(selectedUserAddress(sendData));
        }
        dispatch(fetchAddressFromBackend());
        reset();
        toast.success("Address saved successfully");
        setOpen(false);
    } catch (error) {
            console.log(error);
            toast.error(error?.response?.data?.message || "Internal Server Error");
    }finally{
        setLoader(false);
    }
}

export const fetchAddressFromBackend=()=>async(dispatch)=>{
    try{
        dispatch({type:"LOADING"})
        const {data}=await api.get("/addresses");
        dispatch({
            type:"FETCH_ADDRESS",
            payload:data,
        })
        dispatch({type:"SUCCESS"})
    }catch(error){
        dispatch({
            type:"IS_ERROR",
            payload:error?.response?.data?.message || "failed to fetch Address"
        })
    }
}

export const selectedUserAddress=(item)=>(dispatch)=>{
    localStorage.setItem("address",JSON.stringify(item));
    dispatch({
        type:"SELECT_ADDRESS",
        payload:item
    })
}

export const deletedUserAddress=(item,toast)=>async(dispatch)=>{
    try{
        const {data}=await api.delete(`/addresses/${item.addressId}`);
    dispatch(fetchAddressFromBackend());
    dispatch(removeCheckOutAddress());
    toast.success("Address removed successfully")
    }catch(error){
         dispatch({
            type:"IS_ERROR",
            payload:error?.response?.data?.message || "failed to remove Address"
        })
    }
    
    
}

export const removeCheckOutAddress=()=>(dispatch)=>{
    dispatch({
        type:"REMOVE_CHECKOUT_ADDRESS"
    })
    localStorage.removeItem("address");
}

export const addPaymentMethod=(method)=>{
    return {
        type:"ADD_PAYMENT_METHOD",
        payload:method
    }
}

export const createUserCart=(sendCartItems)=> async(dispatch)=>{
    try{
        await api.post("/cart/create",sendCartItems);
        await dispatch(getUserCart());
    }catch(error){
         dispatch({
            type:"ERROR",
            payload:error?.response?.data?.message || "failed to create  cart"
        })
    }
}

export const getUserCart=()=>async(dispatch,getState)=>{

    const{data}=await api.get("/carts/users/cart");
    try{
        dispatch({
        type: "GET_USER_CART_PRODUCTS",
        payload: data.products,
        totalPrice: data.totalPrice,
        cartId: data.cartId
        })
        localStorage.setItem("cartItems", JSON.stringify(getState().carts.cart));
        dispatch({ type: "SUCCESS" });

    }catch(error){
        dispatch({
            type:"ERROR",
            payload:error?.response?.data?.message || "failed to fetch cart"
        })
    }
     
}

export const createStripePaymentSecret 
    = (sendData) => async (dispatch, getState) => {
        try {
            dispatch({ type: "LOADING" });
            const { data } = await api.post("/order/stripe-client-secret", sendData);
            dispatch({ type: "CLIENT_SECRET", payload: data });
            localStorage.setItem("client-secret", JSON.stringify(data));
            dispatch({ type: "SUCCESS" });
        } catch (error) {
            console.log(error);
            toast.error(error?.response?.data?.message || "Failed to create client secret");
        }
};

export const stripePaymentConfirmation = (sendData, setErrorMesssage, setLoadng, toast) => async (dispatch, getState) => {
        try {
            const response  = await api.post("/order/users/payments/online", sendData);
            if (response.data) {
                localStorage.removeItem("address");
                localStorage.removeItem("cartItems");
                localStorage.removeItem("client-secret");
                dispatch({ type: "REMOVE_CLIENT_SECRET_ADDRESS"});
                dispatch({ type: "CLEAR_CART"});
                toast.success("Order Accepted");
              } else {
                setErrorMesssage("Payment Failed. Please try again.");
              }
        } catch (error) {
            setErrorMesssage("Payment Failed. Please try again.");
        }
};

