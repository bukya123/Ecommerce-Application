const initialState={
    paymentMethod:null
}

function PaymentMethodReducer(state=initialState,action){
    switch (action.type) {
        case "ADD_PAYMENT_METHOD":
            return {
                ...state,
                paymentMethod:action.payload
            }
    }
    return state;
}

export default PaymentMethodReducer;