const initialState={
    isLoading:false,
    errorMessage:null,
}

function ErrorReducer(state=initialState,action){
        switch (action.type) {
            case "LOADING":
                return{
                    ...state,
                    isLoading:true,
                    errorMessage:null
                }
                
            case "SUCCESS":
                return{
                    ...state,
                    isLoading:false,
                    errorMessage:null
                }
            case "ERROR":
                return{
                    ...state,
                    isLoading:false,
                    errorMessage:action.payload
                }
           
       
            default:
                break;
        }
        return state;

}
export default ErrorReducer;