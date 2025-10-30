import Step from "@mui/material/Step";
import StepLabel from "@mui/material/StepLabel";
import Stepper from "@mui/material/Stepper";
import { useEffect, useState } from "react";
import AddressInfo from "./AddressInfo";
import { fetchAddressFromBackend } from "../../Store/Actions";
import { useDispatch, useSelector } from "react-redux";
import Button from "@mui/material/Button";
import Loader from "../Loader";
import ErrorPage from "../ErrorPage";
import PaymentMethod from "./PaymentMethod";
import OrderSummary from "./OrderSummary";
import StripePayment from "./StripePayment";


function Checkout(){
    const[activeStep,setActiveStep]=useState(0);
    const dispatch=useDispatch();
     const { selectedCheckOutAddress } = useSelector(
        (state) => state.auth
    )
    const {isLoading,errorMessage}=useSelector((state)=>state.error)
    const { paymentMethod } = useSelector((state)=>state.payment)
    const{cart,totalPrice}=useSelector((state)=>state.carts);

    const steps = [
    'Address',
    'Payment Mode',
    'Order Summary',
    'Payment'
    ];

    useEffect(()=>{
        dispatch(fetchAddressFromBackend())
    },[dispatch])
    // as soon as dispatch is created .i want to dispatch it once.

    const handleBackButton=()=>{
        setActiveStep(activeStep-1);
    }
    const handleNextButton=()=>{
        if(activeStep === 0 && !selectedCheckOutAddress) {
            toast.error("Please select checkout address before proceeding.");
            return;
        }

        if(activeStep === 1 && (!selectedCheckOutAddress || !paymentMethod)) {
            toast.error("Please select payment address before proceeding.");
            return;
        }
        setActiveStep(activeStep+1);
    }
    return (
        <div className="m-4">
            <Stepper activeStep={activeStep} alternativeLabel>
                {steps.map((label) => (
                <Step key={label}>
                    <StepLabel>{label}</StepLabel>
                </Step>
                ))}
            </Stepper>

            {
                isLoading?(
                    <Loader/>
                ):(
                    <div>
                        {activeStep===0 &&(<AddressInfo/>)}
                        {activeStep===1 && (<PaymentMethod/>)}
                        {activeStep==2 && (<OrderSummary totalPrice={totalPrice} cart={cart} address={selectedCheckOutAddress} paymentMethod={paymentMethod} />)}
                        {activeStep==3 && (<StripePayment/>)}
                    </div>
                
                )
            }

             <div className="flex justify-between items-center px-4 fixed z-50 h-24 bottom-0 bg-white left-0 w-full py-4 border-slate-200">
                <Button
                 variant='outlined'
                
                disabled={activeStep===0}
                onClick={handleBackButton}
                >Back</Button>

                {activeStep !== steps.length - 1 && (
                    <button
                        disabled={
                            errorMessage || (
                                (activeStep === 0 ? !selectedCheckOutAddress
                                    : activeStep === 1 ? !paymentMethod
                                    : false
                                )
                            )
                        }
                        className={`bg-custom-blue font-semibold px-6 h-10 rounded-md text-white
                        ${
                            errorMessage ||
                            (activeStep === 0 && !selectedCheckOutAddress) ||
                            (activeStep === 1 && !paymentMethod)
                            ? "opacity-60"
                            : ""
                        }`}
                        onClick={handleNextButton}>
                        Proceed
                    </button>
                )}
             </div>

             {errorMessage && <ErrorPage message={errorMessage} />}
        </div>

        
       
    )
}
export default Checkout;