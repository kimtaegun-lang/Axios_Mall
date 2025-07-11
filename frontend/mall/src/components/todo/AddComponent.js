import { useState } from "react";
import {postAdd} from "../../api/todoApi";
import ResultModal from "../common/ResultModal";
import useCustomMove from "../../hooks/useCustomMove";
const initState= {
    title:``,writer:``,dueDate:``
}
const AddComponent=() => {
 const [todo,setTodo]=useState({...initState}) 
 const[result,setResult]=useState(null)
 const {moveToList}=useCustomMove()

 const handleChangeTodo=(e)=> {
    todo[e.target.name]=e.target.value  // 사용자가 폼에 입력하면 해당하는 name의 valiue가 바뀐다 이거네 그리고 바뀌게 되면 input의 value에 해당 값을 넣는다 맞지
    setTodo({...todo}) // 객체나 배열의 값을 풀어서 복사하거나, 새로운 객체(또는 배열)를 만들기 위해 사용한다" (변화 감지를 위해!)
 }
 const handleClickAdd=()=> {
    postAdd(todo)
    .then(result=> {
        setResult(result.TNO)
        setTodo({...initState})
    }).catch(e=>{console.error(e)})
 }


 const closeModal=()=> {
    setResult(null)
    moveToList()
 }


 return (
    <div className="border-2 border-sky-200 mt-10 m-2 p-4">

        {result ? <ResultModal title={'Add Result'} content={`New ${result} Added`}
        callbackFn={closeModal}/>:<></>}

        <div className="flex jusity-center">
            <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                <div className="w-1/5 p-6 text-right font-bold">TITLE</div>
                <input className="w-4/5 p-6 rounded-r border border-solid border-neutral-500 shadow-md"
                name="title" type={'text'} value={todo.title} onChange={handleChangeTodo}></input>
            </div>
        </div>

        <div className="flex justify-center">
            <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                <div className="w-1/5 p-6 text-right font-bold">WRITER</div>
                <input className="w-4/5 p-6 rounded-r border border-solid border-neutral-500 shadow-md"
                name="writer" type={'text'} value={todo.writer} onChange={handleChangeTodo}></input>
            </div>
        </div>

        <div className="flex justify-center">
            <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                <div className="w-1/5 p-6 text-right font-bold">DUEDATE</div>
                <input className="w-4/5 p-6 rounded-r border border-solid border-neutral-500 shadow-md"
                name="dueDate" type={'date'} value={todo.dueDate}
                onChange={handleChangeTodo}></input>
            </div>
        </div>

        <div className="flex justify-end">
            <div className="relative mb-4 flex p-4 flex-wrap items-stretch">
                <button type="button" onClick={handleClickAdd}
                className="rounded p-4 w-36 bg-blue-500 text-xl text-white">ADD</button>
            </div>
        </div>
    </div>
 )
}
export default AddComponent;