import { useEffect,useState } from "react";
import { getList } from "../../api/todoApi";
import useCustomMove from "../../hooks/useCustomMove";
import PageComponent from "../common/PageComponent";

const initState={
    dtoList:[],pageNumList:[],pageRequestDTO:null,prev:false,
    next:false,totalCount:0,prevPage:0,nextPage:0,
    totalPage:0,current:0
}

const ListComponent=()=> {
    const {page,size,refresh,moveToList,moveToRead}=useCustomMove()
    const [serverData,setServerData]=useState(initState)

    useEffect(()=> {
        getList({page,size}).then(data=> { //page, size 값을 가지고 백엔드(Spring 컨트롤러)에 요청
            console.log(data) //백엔드는 그 값을 받아 DB에서 데이터를 조회하고, JSON으로 응답
            setServerData(data) //→ 여기서 setServerData(data)는
                                    // 받은 JSON 응답을 React 상태(serverData)에 저장하는 역할이야.


        })
    },[page,size,refresh])
// key는 오로지 React가 리스트의 항목을 추적하기 위한 용도일 뿐이에요.
    return (
        <div className="border-2 border-blue-100 mt-10 mr-2 ml-2">
            <div className="flex flex-wrap mx-auto justify-center p-6">
                {serverData.dtoList.map(todo=>
                    <div key={todo.tno} className="w-full min-w-[400px] p-2 m-2 rounded shadow-md" onClick={()=>moveToRead(todo.tno)}>
                        <div className="flex">
                        <div class Name="font-extrabold text-2xl p-2 w-1/12">{todo.tno}</div>
                        <div className="text-1xl m-1 p-2 w-8/12 font-extrabold">{todo.title}</div>
                        <div className="text-1xl m-1 p-2 w-2/10 font-medium">
                        {todo.dueDate}
                        </div>          
                        </div>
                    </div>
                )}
            </div>
            <PageComponent serverData={serverData} movePage={moveToList}></PageComponent>
        </div>
    );
}
export default ListComponent;
