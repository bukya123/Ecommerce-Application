import { FaBuilding, FaCity, FaStreetView } from "react-icons/fa";
import { MdPinDrop, MdPublic } from "react-icons/md";
import { RiDeleteBin6Line } from "react-icons/ri";
import { useDispatch, useSelector } from "react-redux";
import { deletedUserAddress, selectedUserAddress } from "../../Store/Actions";
import toast from "react-hot-toast";
import DeleteModal from "./DeleteModal";


function AddressCard({item,setSelectedAddress,setOpenDeleteModal,setOpenAddressModal}){
    const {selectedCheckOutAddress}=useSelector((state)=>state.auth);
    const dispatch=useDispatch();
    
    const handleDeleteAddress=()=>{
        setOpenDeleteModal(true);
    }
    const handleEditUserAddress=()=>{
        setSelectedAddress(item);
        setOpenAddressModal(true);
    }
    const handleSelectedAddress=()=>{ 
        dispatch(selectedUserAddress(item))
    }

    return (
        
        <div 
        key={item.addressId}
        onClick={handleSelectedAddress}
        className={`border-2 rounded-2xl p-5  cursor-pointer ${
            ( selectedCheckOutAddress?.addressId===item.addressId)? "bg-green-100 border-black" :"bg-white"
        }`} >
            <div className="flex justify-end">
                <button onClick={handleDeleteAddress}>
                    <RiDeleteBin6Line size={20} />
                </button>
            </div>
            <div>
                <div className="flex items-center gap-1">
                    <FaBuilding/>
                    {item.buildingName}
                </div>
                <div className="flex items-center gap-1">
                    <FaStreetView/>
                    {item.street}
                </div>
                <div className="flex items-center gap-1">
                    <FaCity/>
                    <p>{item.city}, {item.state}</p>
                </div>
                <div className="flex items-center gap-1">
                    <MdPublic/>
                    {item.country}
                </div>
                <div className="flex items-center gap-1">
                    <MdPinDrop/>
                    {item.pincode}
                </div>
            </div>
            <button className="w-full overflow-hidden border-2 mt-3 text-slate-700 hover:text-slate-950" onClick={handleEditUserAddress}>Edit</button>
            
            
        </div>
    )
}
export default AddressCard;