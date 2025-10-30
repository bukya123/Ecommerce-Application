import { RotatingLines } from "react-loader-spinner";


function Loader({text}){
    return(
        <div className="flex justify-center items-center w-full h-[450px]">
            <div>
                <RotatingLines
                    visible={true}
                    height="96"
                    width="96"
                    color="grey"
                    strokeWidth="5"
                    animationDuration="0.75"
                    ariaLabel="rotating-lines-loading"
                    wrapperStyle={{}}
                    wrapperClass=""
                    />
                <p>
                    {text?text:"Please wait...."}
                </p>
            </div>
              
        </div>
    )
}
export default Loader;