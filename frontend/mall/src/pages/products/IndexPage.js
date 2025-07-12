import { Outlet,useNavigate } from "react-router-dom";
import BasicLayout from "../../layouts/BasicLayout";
import { useCallback } from "react";
const IndexPage=() => {

    const navigate=useNavigate()

    const handleClickList=useCallback(()=> {navigate({pathname:'list'})})

    const handleClickAdd=useCallback(()=> {
        navigate({pathname:'add'})
    })
    return(
        <BasicLayout>
            <div className="text-black font-extrabold mt-10">Products Menus</div>

            <div className="w-full flex m-2 p-2">
                <div className="text-xl m-1 p-2 w-20 font-extrabold text-center underline" onClick={handleClickList}>LIST</div>
                <div className="text-xl m-1 p-2 w-20 font-extrabold text-center underline" onClick={handleClickAdd}>ADD</div>
            </div>
            <div className="flex flex-wrap w-full">
                <Outlet/>
            </div>
        </BasicLayout>
    );
}
export default IndexPage;

// outlet은    path:"products",
        //element:<Suspense fallback={Loading}><ProductsIndex/></Suspense>,
        //children:productsRouter() 의 children의 자식 요소들을 전부 가져옴
        // 여기선 라우터를 outlet으로 설정하엿다, 즉, 라우터는 거쳐가는역할, 라우터의add,list 페이지에 접근하기 위함