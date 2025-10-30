import Pagination from "@mui/material/Pagination";
import { useLocation, useNavigate, useSearchParams } from "react-router-dom";

function Paginations({numOfPages,totalProducts}){
    const[searchParams]=useSearchParams();
    const params=new URLSearchParams(searchParams);
    const pathname=useLocation().pathname;
    const navigate=useNavigate();
     const paramValue = searchParams.get("pageNumber")
                ? Number(searchParams.get("pageNumber"))
                : 1;

    function handlePageChange(e,value){
        params.set("pageNumber",value.toString());
        navigate(`${pathname}?${params}`);
    }
    return (
        <Pagination count={numOfPages} 
        page={paramValue}
        defaultPage={1} 
        siblingCount={0} 
        onChange={handlePageChange}/>
    )

}
export default Paginations;