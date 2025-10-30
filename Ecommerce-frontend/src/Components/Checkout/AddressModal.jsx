import { Dialog, DialogPanel, DialogTitle } from "@headlessui/react";
import { FaCross } from "react-icons/fa";
import { MdOutlineCancel } from "react-icons/md";



function AddressModal({open,setOpen,children}){
    return (
        <div>

            <Dialog open={open} onClose={() => setOpen(false)} className="relative z-50">
                <div className="fixed inset-0 flex w-screen items-center justify-center p-4 mt-25">
                <DialogPanel className="relative w-full max-w-md mx-auto transform overflow-hidden bg-white rounded-lg shadow-xl transition-all">
                    <div className="py-6 px-6">
                        {children}
                    </div>
                    <button className="absolute justify-end right-4 top-2" onClick={()=>setOpen(false)}>
                        <MdOutlineCancel size={25}/>
                    </button>
                </DialogPanel>
                </div>
            </Dialog>

        </div>
        
        
    )
}
export default AddressModal;