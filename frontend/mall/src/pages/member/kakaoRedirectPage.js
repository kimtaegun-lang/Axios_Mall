import { useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { getAccessToken,getMemberWithAccessToken } from "../../api/kakaoApi";
import { useDispatch } from "react-redux"; // Redux 스토어에 **액션(action)**을 보내는 함수입니다.
import { login } from "../../slices/loginSlice";
import useCustomLogin from "../../hooks/useCustomLogin";
const KakaoRedirectPage = () => {

    const [searchParams] = useSearchParams()
    const {moveToPath}=useCustomLogin()
    const dispatch=useDispatch()
    const authCode = searchParams.get("code") // 카카오 인가코드 
    // OAuth 2.0 표준에서 인가 코드는 code라는 이름으로 전달됨

    useEffect(() => {
        getAccessToken(authCode).then(accessToken => {
            getMemberWithAccessToken(accessToken).then(memberInfo=>{
                dispatch(login(memberInfo))
                if(memberInfo&&!memberInfo.social) { // 쇼셜 계정이 아닐경우,
                    moveToPath("/")
                }
                else { // 소셜계정일 경우,
                    moveToPath("/member/modify")
                }
            })
        })
    }, [authCode])

    return (
        <div>
            <div>Kakao Login Redirect</div>
            <div>{authCode}</div>
        </div>
    )
}

export default KakaoRedirectPage;