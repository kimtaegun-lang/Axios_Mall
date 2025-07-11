import { useCallback,useState } from "react";
import { createSearchParams,useNavigate,useSearchParams } from "react-router-dom"; 

const getNum=(param,defaultValue)=> {
    if(!param)
    {
        return defaultValue
    }
    return parseInt(param)
}

const useCustomMove=()=> { 
    const navigate=useNavigate()
    const [refresh,setRefresh]=useState(false) // 동일 페이지 클릭 시, 페이지 새로고침이 되게하는 기능

    const [queryParams]=useSearchParams() // useSearchParams: 현재 URL의 ?page=1&size=10 같은 쿼리 파라미터를 가져옴
    const page=getNum(queryParams.get('page'),1)
    const size=getNum(queryParams.get('size'),10) // 요건 url 디폴트값 설정

    const queryDefault=createSearchParams({page,size}).toString() // url 형식의 문자열 생성

    const moveToList=(pageParam) => { // url뒤에 붙는 searchparam 붙이는 코드 ex) ?page=1&size=1
    let queryStr=""
    if(pageParam) {
        const pageNum=getNum(pageParam.page,1) 
        const sizeNum=getNum(pageParam.size,10)

        queryStr=createSearchParams({page:pageNum,size:sizeNum}).toString()
    }
    else {
        queryStr=queryDefault
    }

    navigate({
        pathname:`../list`,
        search:queryStr
    })
    setRefresh(!refresh)
    }


    const moveToRead =(num) => {
        console.log(queryDefault)
        navigate({
            pathname:`../read/${num}`,
            search:queryDefault
        })
    }

    const moveToModify=useCallback((num)=> {
    console.log(queryDefault)
    navigate({pathname:`../modify/${num}`,search:queryDefault})
})
return {moveToList,moveToModify,moveToRead,page,size,refresh}
}
export default useCustomMove 
