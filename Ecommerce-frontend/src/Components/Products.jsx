import { useDispatch, useSelector } from "react-redux";
import ProductCard from "./ProductCard";
import { useEffect } from "react";
import { fetchProducts } from "../Store/Actions";
import Filter from "./Filter";
import ProductFilter from "./ProductFilter";
import { RotatingLines } from "react-loader-spinner";
import Loader from "./Loader";
import Paginations from "./Paginations";

function Products(){
    const{isLoading,errorMessage}=useSelector(
        (state)=>state.error
    )
    const {products,categories,pagination}=useSelector(
        (state)=>state.products
    )
    const dispatch=useDispatch();
    ProductFilter();
    

    

    return(
        <div> 
            <Filter/>
            {
                isLoading ?(
                  <Loader/>
                )
                :errorMessage ?(
                    <div>{errorMessage}</div>
                ):(
                    <div className="min-h-[700px] m-10">
                    <div className="pb-6 pt-14   grid 2xl:grid-cols-6 lg:grid-cols-5 sm:grid-cols-3 gap-y-6 gap-x-6">
                       {products && 
                        products.map((item) => <ProductCard item={item} about={false}/>
                        )}
                    </div>
                    <div className="flex justify-center">
                        <Paginations numOfPages={pagination.totalPages} totalProducts = {pagination.totalElements}/>
                    </div>
                  </div> 
                )
            }
        </div>
    )
}
export default Products;