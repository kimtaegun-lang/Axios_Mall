import axios from "axios";
import { getCookie, setCookie } from "./cookieUtil";
import { API_SERVER_HOST } from "../api/todoApi";
const jwtAxios = axios.create()

//before request
// jwt와 cookie의 연관성? jwt토큰을 로컬 스토리지에 저장하기 위해?
const beforeReq = (config) => {
    console.log("before request.............")
    const memberInfo = getCookie("member")
    if (!memberInfo) {
        console.log("Member NOT FOUND")
        return Promise.reject( // 요청을 강제로 실패시킵니다.
            {
                response:
                {
                    data:
                        { error: "REQUIRE_LOGIN" } // 나중에 예외처리시에 사용할듯
                }
            }
        )
    }
    const { accessToken } = memberInfo
    // Authorization 헤더 처리
    config.headers.Authorization = `Bearer ${accessToken}`
    return config
}
//fail request
const requestFail = (err) => {
    console.log("request error............")
    return Promise.reject(err) // Promise는 자바스크립트에서 비동기 작업을 다루기 위한 객체예요.
    /* 이건 "나는 이 비동기 작업을 실패로 처리할 거야"라고 알려주는 것.
→ 이후 .catch()에서 이 에러를 받을 수 있어요.*/
}
//before return response
const beforeRes = async (res) => { // 토큰 리프레시 가지고 토큰 재발급 받아오기
    /* 서버가 "ERROR_ACCESS_TOKEN" 에러를 보내면
     → refreshToken을 이용해 accessToken을 자동으로 재발급 → 쿠키에 저장.*/
    console.log("before return response...........")
    console.log(res)
    const data = res.data

    if (data && data.error === 'ERROR_ACCESS_TOKEN') {
        const memberCookieValue = getCookie("member")
        const result = await refreshJWT(memberCookieValue.accessToken, memberCookieValue.refreshToken)
        // 백엔드에 새로운 accessToken과 refreshToken을 요청하는 핵심 코드입니다.
        console.log("refreshJWT RESULT", result)

        memberCookieValue.accessToken = result.accessToken
        memberCookieValue.refreshToken = result.refreshToken

        setCookie("member", JSON.stringify(memberCookieValue), 1)

        const originalRequest = res.config 
        originalRequest.headers.Authorization = `Bearer ${result.accessToken}` 
        return await axios(originalRequest)
    }
    return res
}
//fail response
const responseFail = (err) => {
    console.log("response fail error.............")
    return Promise.reject(err);
}

const refreshJWT = async (accessToken, refreshToken) => { // acess 토큰의 유효기간이 지낫을 경우 서버 호출

    const host = API_SERVER_HOST
    const header = { headers: { "Authorization": `Bearer ${accessToken}` } }
    const res = await axios.get(`${host}/api/member/refresh?refreshToken=${refreshToken}`, header)
    console.log("----------------------")
    console.log(res.data)
    return res.data
}

jwtAxios.interceptors.request.use(beforeReq, requestFail) // 요청을 보내기 직전 실행 예: 토큰 붙이기, 로그인 여부 확인
jwtAxios.interceptors.response.use(beforeRes, responseFail) //응답을 받은 직후 실행 예: accessToken 만료되었는지 확인하고 자동 재발급 처리
export default jwtAxios