import { Suspense, lazy } from "react";
import { Navigate } from "react-router-dom";

const productsRouter = () => {

    const Loading = <div>Loading...</div>
    const ProductsList = lazy(() => import("../pages/products/ListPage"))
    const ProductsAdd = lazy(() => import("../pages/products/AddPage"))
    const ProductRead = lazy(() => import("../pages/products/ReadPage"))
    const ProductModify=lazy(()=> import("../pages/products/ModifyPage"))
    // 실제로 이 컴포넌트가 필요할 때(렌더링 시점에) 불러옵니다.
    return [
        {
            path: "list",
            element: <Suspense fallback={Loading}><ProductsList /></Suspense>
        }, {
            path: "",
            element: <Navigate replace to="/products/list" />
        },
        {
            path: "add",
            element: <Suspense fallback={Loading}><ProductsAdd /></Suspense>
        },
        {
            path:"read/:pno",
            element:<Suspense fallback={Loading}><ProductRead/></Suspense>
        },
        {
            path:"modify/:pno",
            element:<Suspense fallback={Loading}><ProductModify/></Suspense>
        }
    ]
}


export default productsRouter;