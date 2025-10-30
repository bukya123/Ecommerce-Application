import { Elements } from "@stripe/react-stripe-js";
import { useDispatch, useSelector } from "react-redux";
import PaymentForm from "./PaymentForm";
import { loadStripe } from "@stripe/stripe-js";
import { useEffect } from "react";
import Loader from "../Loader";
import { createStripePaymentSecret } from "../../Store/Actions";



const stripePromise=loadStripe("pk_test_51SNSPo3evsejvhuwAIJ4aYlh48vrNNqJOaeQxXR1AcUeoPH19aoyUXuGlHTqzozst32oB0F5XFUcXyeiZR17QqE400QKrtnQVr");
function StripePayment(){
    const {clientSecret}=useSelector((state)=>state.auth)
    const{isLoading,errorMessage}=useSelector((state)=>state.error);
    const{cart,totalPrice}=useSelector((state)=>state.carts);
    const dispatch=useDispatch();


    useEffect(()=>{
        if(!clientSecret){
            const sendData={
                amount:totalPrice*100,
                currency:"inr"
            }

            dispatch(createStripePaymentSecret(sendData))
        }
    },[clientSecret])

    if (isLoading) {
    return (
      <div className='max-w-lg mx-auto'>
        <Loader />
      </div>
    )
  }

    return (
        <>
        { 
           clientSecret &&(
            <Elements stripe={stripePromise} options={{clientSecret}}>
                <PaymentForm clientSecret={clientSecret} totalPrice={totalPrice}/>
            </Elements>
           )
        }
        </>
    )

}
export default StripePayment;