import axios from "axios"
import { API_SERVER_HOST } from "./todoApi"// 기존꺼 재사용
const host = `${API_SERVER_HOST}/api/products` 
export const postAdd = async (product) => {

//const header = {headers: {"Content-Type": "multipart/form-data"}}
// 파일이 포함된 FormData 객체 전송 시 필요
//multipart/form-data로 FormData를 보낼 때는 Content-Type 헤더를 직접 지정할 필요가 없다네
const res = await axios.post(`${host}/`, product)

return res.data

}
export const getList = async ( pageParam ) => { 
const {page,size} = pageParam
const res = await axios.get(`${host}/list`, {params:
{page:page,size:size }})

return res.data

}
export const getOne=async(pno) => {
    const res=await axios.get(`${host}/${pno}`)
    return res.data
}

export const putOne=async(pno,product) => {
    const header={headers:{"Content-Type":"multipart/form-data"}}
    const res=await axios.put(`${host}/${pno}`,product,header)
    return res.data
}

export const deleteOne=async(pno)=>{
    const res=await axios.delete(`${host}/${pno}`)
    return res.data
}
