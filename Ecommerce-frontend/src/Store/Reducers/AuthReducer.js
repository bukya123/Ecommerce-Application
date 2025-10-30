const initislState={
    user:null,
    address:[],
    clientSecret:null,
    selectedCheckOutAddress:null
}

function AuthReducer(state=initislState,action){
    switch (action.type) {
        case "LOGIN_USER":
            return {
                ...state,
                user:action.payload
            }
        case "LOGOUT_USER":
            return{
                user:null,
                address:null
            }
        case "FETCH_ADDRESS":
            return {
                ...state,
                address:action.payload
            }
        case "SELECT_ADDRESS":
            return {
                ...state,
                selectedCheckOutAddress:action.payload
            }
        case "REMOVE_CHECKOUT_ADDRESS":
            return {
                ...state,
                selectedCheckOutAddress:null
            }
        case "CLIENT_SECRET":
            return {
                ...state,
                clientSecret:action.payload
            }
        case "REMOVE_CLIENT_SECRET_ADDRESS":
            return {
                ...state,
                clientSecret:null
            }
        
        default:
            return state;
    }
    
}
export default AuthReducer;