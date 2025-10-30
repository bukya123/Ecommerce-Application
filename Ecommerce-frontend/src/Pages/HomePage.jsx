import { useDispatch, useSelector } from "react-redux";
import Banner from "../Components/Banner";
import { useEffect } from "react";
import { fetchProducts } from "../Store/Actions";
import ProductCard from "../Components/ProductCard";
import Loader from "../Components/Loader";
import Navbar from "../Components/Navbar";

function HomePage(){
    const disptach=useDispatch();
    const{isLoading,errorMessage}=useSelector(
            (state)=>state.error
        )
    const {products}=useSelector(
        (state)=>(state.products)
    )
    useEffect(()=>{
        disptach(fetchProducts())
    },[disptach])

    return (
        <div>
            <Banner/>
            {
            isLoading ?(
                <Loader/>
                )
                :errorMessage ?(
                    <div>{errorMessage}</div>
                ):(
                    
                    <div className="m-10  grid 2xl:grid-cols-6 lg:grid-cols-5 sm:grid-cols-3 gap-y-6 gap-x-6">
                    {products && 
                        products.map((item) => <ProductCard item={item} />
                        )}
                    </div>
                    
                )
            }
               
        </div>
        
    )
}
export default HomePage;