import axios from "axios"

export const API_SERVER_HOST='http://localhost:8090'
const prefix=`${API_SERVER_HOST}/api/todo`
// 이 함수들을 사용하면 url의 page,size를 가져와 백 컨트롤러에 넘겨주는 역할을 한다, 그리고 호출한 쪽에서 serverdata로 값을 받아올수잇다. (json형태로 ) 아마 컨트롤러에서는 requestBody로 값을 리턴할것임..
export const getOne=async(tno) => {
    const res=await axios.get(`${prefix}/${tno}`)
    return res.data
}

export const getList=async(pageParam) => {
    const {page,size}=pageParam
    const res=await axios.get(`${prefix}/list`,{params:{page:page,size:size}}) 
    return res.data
}

export const postAdd=async(todoObj) =>{
    const res=await axios.post(`${prefix}/`,todoObj)
    return res.data
}


export const deleteOne = async(tno)=> {
    const res=await axios.delete(`${prefix}/${tno}`)

    return res.data
}

export const putOne=async(todo) => {
    const res=await axios.put(`${prefix}/${todo.tno}`,todo) 
    return res.data
}