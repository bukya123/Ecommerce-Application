import { Button, Dialog, DialogPanel, DialogTitle } from '@headlessui/react'
import { useState } from 'react'
import { FaShoppingCart } from 'react-icons/fa';
import Status from './Status';
import { MdCurrencyRupee } from 'react-icons/md';
import { MdClose, MdDone } from 'react-icons/md';

function ProductViewModal({open,setOpen,item,isAvailable}) {
  return (
    <>
      <Dialog open={open} as="div" className="relative z-10 focus:outline-none" onClose={close}>
        <div className="fixed inset-0 z-10 w-screen overflow-y-auto">
          <div className="flex  items-center justify-center  p-4">
            <DialogPanel
              transition
              className="w-full h-full max-w-md  overflow-hidden rounded-lg bg-white shadow-lg "
            > 
            <div className="w-full">
                <img src={item.image}></img>
            </div>
            
            <div className='m-2'>
                <DialogTitle as="h3" className="text-xl font-medium mb-2">
                    {item.productName}
                </DialogTitle>
                <p className="mt-2 text-sm/6 mb-5">
                    {item.productDescription}
                </p>
                 
                 <div className="flex justify-between">
                        <div>
                            {
                            item.specialPrice ?(
                                <div className='flex'>
                                    <div className="flex"><MdCurrencyRupee className="mt-1.5"/>{Number(item.specialPrice).toFixed(2)}</div>
                                    <div className="line-through text-gray-400 mr-2 flex"><MdCurrencyRupee className="mt-1.5"/>{Number(item.price).toFixed(2)}</div>
                                </div>  
                            ):(
                                <div className="flex"><MdCurrencyRupee className="mt-1.5"/>{Number(item.price).toFixed(2)}</div>
                            )
                            }
                 
                        </div>
                        <div>
                            <button className="bg-violet-500 hover:bg-violet-800 w-30 rounded-sm ">
                                {isAvailable?(
                                    <Status bgColor="bg-teal-200" icon={MdDone} content="InStock" textColor="text-teal-700" />
                                    
                                ):(
                                    <Status bgColor="bg-rose-200" icon={MdClose} content="Out of Stock" textColor="text-rose-900"/>
                                    
                                )}
                            </button>            
                        </div>
                </div>
            </div>
              
              <div className="m-2 flex justify-center">
                <Button
                  className="inline-flex items-center gap-2 rounded-md bg-gray-700 px-3 py-1.5 text-sm/6 font-semibold text-white shadow-inner shadow-white/10 focus:not-data-focus:outline-none data-focus:outline data-focus:outline-white data-hover:bg-gray-600 data-open:bg-gray-700"
                  onClick={() => setOpen(false)}
                >
                  Close
                </Button>
              </div>

            </DialogPanel>
          </div>
        </div>
      </Dialog>
    </>
  )
}

export default ProductViewModal;