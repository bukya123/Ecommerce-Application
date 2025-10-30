import { FaAddressBook } from "react-icons/fa";
import InputField from "../../Utils/InputFiled";
import Spinners from "../../Utils/Spinners";
import { useForm } from "react-hook-form";
import { useDispatch } from "react-redux";
import { SaveUserAddress } from "../../Store/Actions";
import toast from "react-hot-toast";
import { useEffect, useState } from "react";



function AddressForm({setOpen,selectedAddress}){
    const [loader,setLoader]=useState(false);
    const dispatch=useDispatch();
     const {
            register,
            handleSubmit,
            reset,
            setValue,
            formState: {errors},
        } = useForm({
            mode: "onTouched",
        }); 

    const saveAddress = async (data) => {
        dispatch(SaveUserAddress(
            data,
            toast,
            reset,
            setOpen,
            setLoader,
            selectedAddress?.addressId
        ));
    };
    useEffect(() => {
            if (selectedAddress?.addressId) {
                setValue("buildingName", selectedAddress?.buildingName);
                setValue("city", selectedAddress?.city);
                setValue("street", selectedAddress?.street);
                setValue("state", selectedAddress?.state);
                setValue("pincode", selectedAddress?.pincode);
                setValue("country", selectedAddress?.country);
            }
        }, [selectedAddress]);

    return (
        <div>
            <form
                onSubmit={handleSubmit(saveAddress)}
                className="">
                    <div className="flex flex-col items-center justify-center space-y-4">
                        <FaAddressBook className="text-slate-800 text-3xl"/>
                       {!selectedAddress?.addressId ? 
                        "Add Address" :
                        "Edit Address"
                        }
                        
                    </div>
            <hr className="mt-2 mb-5 text-black" />
            <div className="flex flex-col gap-3">
                <InputField
                    label="Building Name"
                    required
                    id="buildingName"
                    type="text"
                    message="*Building Name is required"
                    placeholder="Enter your Building Name"
                    register={register}
                    errors={errors}
                    />

                <InputField
                    label="Street"
                    required
                    id="street"
                    type="text"
                    message="*Street is required"
                    placeholder="Enter your street"
                    register={register}
                    errors={errors}
                    />
                <InputField
                    label="City"
                    required
                    id="city"
                    //id is picked as key value in requestbody
                    type="text"
                    message="*City is required"
                    placeholder="Enter your city"
                    register={register}
                    errors={errors}
                    />
                <InputField
                    label="State"
                    required
                    id="state"
                    type="text"
                    message="*State is required"
                    placeholder="Enter your State"
                    register={register}
                    errors={errors}
                    />
                <InputField
                    label="Country"
                    required
                    id="country"
                    type="text"
                    message="*Country is required"
                    placeholder="Enter your country"
                    register={register}
                    errors={errors}
                    />
                <InputField
                    label="Pincode"
                    required
                    id="pincode"
                    type="text"
                    message="*Pincode is required"
                    placeholder="Enter your pincode"
                    register={register}
                    errors={errors}
                    />
            </div>

            <button
                disabled={loader}
                className="bg-blue-500 flex gap-2 items-center justify-center font-semibold text-white w-full py-2 hover:text-slate-400 transition-colors duration-100 rounded-xs my-3"
                type="submit">
                {loader ? (
                    <>
                    <Spinners /> Loading...
                    </>
                ) : (
                    <>Save</>
                )}
            </button>

            </form>
        </div>
    )
}
export default AddressForm;