
function Status({bgColor,icon:Icon,content,textColor}){
    return(
        <button className={`${bgColor} ${textColor} w-full flex justify-evenly w-36 rounded-sm  align-bottom px-2 py-2`}>
            {content}
            <Icon className="mt-1"/>
        </button>
    )
}
export default Status;