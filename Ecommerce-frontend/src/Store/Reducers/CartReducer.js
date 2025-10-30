const initialState={
    cart: [],
    totalPrice: 0,
    cartId: null,
}

function CartReducer(state=initialState,action){
    switch (action.type) {
        case "ADD_PRODUCT_TO_CART":
            const productToAdd=action.payload
            const existingProduct = state.cart.find(
                (item) => item.productId === productToAdd.productId
            );

            if(existingProduct) {
                const updatedCart = state.cart.map((item) => {
                    if (item.productId === productToAdd.productId) {
                        return productToAdd;
                    } else {
                        return item;
                    }
                });

                return {
                    ...state,
                    cart: updatedCart,
                };
            } else {
                const newCart = [...state.cart, productToAdd];
                return {
                    ...state,
                    cart: newCart,
                };
            }
         case "REMOVE_FROM_CART":
            return {
                ...state,
                cart: state.cart.filter(
                    (item) => item.productId !== action.payload.productId
                ),
            }
        case "GET_USER_CART_PRODUCTS":
            return {
                ...state,
                cart:action.payload,
                totalPrice:action.totalPrice,
                cartId:action.cartId
            }
        case "CLEAR_CART":
            return {
                ...state,
                cart:[],
                totalPrice:0,
                cartId:null
            }
        default:
            return state;
    }
}
export default CartReducer;