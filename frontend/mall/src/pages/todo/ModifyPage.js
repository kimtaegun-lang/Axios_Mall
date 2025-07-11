import { useNavigate,useParams } from "react-router-dom";
import ModifyComponent from "../../components/todo/ModifyComponent";

const ModifyPage=()=> { // 여기 props 변수로 tno넣엇엇음.
    const{tno}=useParams() // 이거 없고 // 이건 브라우저 주소(URL)에서 :tno에 해당하는 값을 꺼내오는 거예요.
    const navigate=useNavigate()

    /*
const moveToRead=()=> {
    navigate({pathname:`/todo/read/${tno}`})
}

const moveToList=()=> {
    navigate({pathname:`/todo/list`})
}
    return (
        <div className="text-3xl font-extrabold">
            Today Modify Page
        </div>
    ); */

    return (
        <div className="p-4 w-full bg-white">
            <div className="text-3xl font-extrabold">
                Todo Modify Page
            </div>
            <ModifyComponent tno={tno}/>
        </div>
    );
}

export default ModifyPage;