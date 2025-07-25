import { Suspense, lazy } from "react"; const Loading = <div>Loading....</div>
const Login = lazy(() => import("../pages/member/LoginPage"))
const LogoutPage = lazy(() => import("../pages/member/LogoutPage"))
const KakaoRedirectPage=lazy(()=> import("../pages/member/kakaoRedirectPage"))
const MemberModify = lazy(() => import("../pages/member/ModifyPage"))
const memberRouter = () => {

    return [
        {
            path: "login",
            element: <Suspense fallback={Loading}><Login /></Suspense>
        },
        {
            path:"logout",
            element: <Suspense fallback={Loading}><LogoutPage/></Suspense>
        },
        {
            path:"kakao",
            element: <Suspense
            fallback={Loading}><KakaoRedirectPage/></Suspense>
        },
        {
            path:"modify",
            element: <Suspense
            fallback={Loading}><MemberModify/></Suspense>
        }
    ]

}

export default memberRouter