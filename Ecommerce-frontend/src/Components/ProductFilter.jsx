import { useSearchParams } from "react-router-dom";
import { fetchProducts } from "../Store/Actions";
import { useEffect } from "react";
import { useDispatch } from "react-redux";


function ProductFilter(){
    const[searchParams]=useSearchParams();
    const dispatch=useDispatch();
    useEffect(()=>{

        const params=new URLSearchParams();
        const sortOrder = searchParams.get("sortBy") || "asc";
        const categoryParams = searchParams.get("category") || null;
        const keyword = searchParams.get("keyword") || null;
        params.set("sortBy","price");
        params.set("sortOrder", sortOrder);
        const currentPage = searchParams.get("pageNumber")
            ? Number(searchParams.get("pageNumber"))
            : 1;

        params.set("pageNumber", currentPage - 1);

        if (categoryParams) {
            params.set("category", categoryParams);
        }

        if (keyword) {
            params.set("keyword", keyword);
        }
        

        const queryString=params.toString();
         console.log("QUERY STRING", queryString);
        dispatch(fetchProducts(queryString));
    },[searchParams])
    
}
export default ProductFilter;