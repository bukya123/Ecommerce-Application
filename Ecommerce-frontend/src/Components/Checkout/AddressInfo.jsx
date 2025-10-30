import { FaAddressBook } from "react-icons/fa";
import Loader from "../Loader";
import { useState } from "react";
import AddressModal from "./AddressModal";
import AddressForm from "./AddressForm";
import { useDispatch, useSelector } from "react-redux";
import { deletedUserAddress, fetchAddressFromBackend } from "../../Store/Actions";
import AddressCard from "./AddressCard";
import DeleteModal from "./DeleteModal";
import toast from "react-hot-toast";

function AddressInfo(){
    const {address}=useSelector((state)=>state.auth);
    const isLoading=false;
    const[openAddressModal,setOpenAddressModal]=useState(false);
     const [selectedAddress, setSelectedAddress] = useState("");
     const [openDeleteModal, setOpenDeleteModal] = useState(false);
     const dispatch=useDispatch();

    const handleAddressModal=()=>{
        setSelectedAddress("");
        setOpenAddressModal(true);
    }
    const handleDeleteAddress=()=>{
        dispatch(deletedUserAddress(selectedAddress,toast))
    }
    return (
        <div>
            {
                address==null ?(
                    <div className="flex flex-col items-center mt-10">
                        <div className="flex gap-2 items-center mb-2">
                            <FaAddressBook size={22}/>
                            <h1 className="text-slate-900 font-semibold text-2xl">No Address Exist</h1>
                        </div>
                        <p className="text-slate-800 mb-4">Please add address to complete your purchase</p>
                        <button className="px-2 py-2 bg-blue-500 text-white rounded hover:bg-blue-700 transition-all" onClick={handleAddressModal}>Add Address</button>
                    </div>
                ):(
                    isLoading?(
                            <Loader/>
                    ):(
                        <div className="">
                            <div className="flex flex-col items-center m-10">
                                <div className="flex gap-2 items-center">
                                    <FaAddressBook size={22}/>
                                <h2 className="text-slate-900 font-semibold text-2xl">Select Address</h2>
                                </div>
                            </div>

                            <div className="pb-6 pt-14 m-10 grid lg:grid-cols-3 gap-y-4 gap-x-4">
                            {
                                address.map((item)=>{
                                    return (<AddressCard item={item} setSelectedAddress={setSelectedAddress} setOpenDeleteModal={setOpenDeleteModal} setOpenAddressModal={setOpenAddressModal}/>)
                                })
                            }
                            </div>

                            <div className="flex justify-center">
                                <button className="bg-blue-500 px-2 py-2 rounded" 
                                onClick={
                                    handleAddressModal
                                }>
                                Add more</button>
                            </div>
                        </div>

                        
                        
                    )

                )
            }

            <AddressModal open={openAddressModal} setOpen={setOpenAddressModal}>
                <AddressForm setOpen={setOpenAddressModal} selectedAddress={selectedAddress}/>
            </AddressModal>

            <DeleteModal openDeleteModal={openDeleteModal} setOpenDeleteModal={setOpenDeleteModal} title="Delete Address" onDeleteHandler={handleDeleteAddress} />
        </div>
    )

}
export default AddressInfo;