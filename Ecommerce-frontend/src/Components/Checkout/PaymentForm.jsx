import { PaymentElement, useElements, useStripe } from "@stripe/react-stripe-js";
import Loader from "../Loader";
import { useState } from "react";


function PaymentForm({clientSecret,totalPrice}){

    const stripe=useStripe();
    const elements=useElements();
    
    const[errorMessage,setErrorMessage]=useState("");

    const handlePaymentSubmit=async(e)=>{
         e.preventDefault();
        if (!stripe || !elements) {
            return;
        }

        const { error: submitError } = await elements.submit();

        const { error } = await stripe.confirmPayment({
            elements,
            clientSecret,
            confirmParams: {
                return_url: `${import.meta.env.VITE_FRONTEND_URL}/order-confirm`,
            },
        });

        if (error) {
            setErrorMessage(error.message);
            return false;
        }

    }
     const paymentElementOptions = {
        layout: "tabs",
    }


    const isLoading=!clientSecret;
    return (
       <form onSubmit={handlePaymentSubmit} className="max-w-lg mx-auto p-4">
        <h2 className="text-2xl font-semibold">Payment Information</h2>
        {isLoading ? (
            <Loader/>
        ) : (
            <>
            {clientSecret && <PaymentElement  options={paymentElementOptions}/> }
            {errorMessage && (
                <div className='text-red-500 mt-2'>{errorMessage}</div>
            )}

            <button
                className='text-white w-full px-5 py-[10px] bg-black mt-2 rounded-md font-bold disabled:opacity-50 disabled:animate-pulse'
                disabled={!stripe || isLoading}>
                    {!isLoading ? `Pay INR ${Number(totalPrice).toFixed(2)}`
                            : "Processing"}
            </button>
            </>
        )}
       </form>
    )
}
export default PaymentForm;