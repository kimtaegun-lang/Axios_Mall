import axios from "axios";
import { API_SERVER_HOST } from "./todoApi";
const rest_api_key = `66f69b0cdd873415432d03c7290855b0` //자신의 REST키값 

const redirect_uri = `http://localhost:3000/member/kakao`

const auth_code_path = `https://kauth.kakao.com/oauth/authorize`
const access_token_url = `https://kauth.kakao.com/oauth/token`
export const getKakaoLoginLink = () => {

    const kakaoURL = `${auth_code_path}?client_id=${rest_api_key}&redirect_uri=${redirect_uri}&response_type=code`;

    return kakaoURL

}
export const getAccessToken = async (authCode) => {
    const header = {
        headers: { // 카카오에 요청 보낼 때는 Content-Type을 반드시 폼 전송 방식(x-www-form-urlencoded)으로 지정
            "Content-Type": "application/x-www-form-urlencoded",
        }
    }
    const params = {
        grant_type: "authorization_code", // 고정값: 인증 방식
        client_id: rest_api_key,  // 내 카카오 앱의 REST API 키
        redirect_uri: redirect_uri,    // 카카오 개발자 콘솔에 등록한 redirect 주소
        code: authCode   // 인가 코드
    }
    const res = await axios.post(access_token_url, params, header) // 카카오의 토큰 발급 서버로 POST 요청을 보냄
    const accessToken = res.data.access_token
    /* 요청시 이런 데이터가 들어옴
    {
  "access_token": "ABC1234567abcdef...",
  "token_type": "bearer",
  "refresh_token": "XYZ9876543ghijk...",
  "expires_in": 21599,
  "scope": "profile_image profile_nickname",
  "refresh_token_expires_in": 5183999
}
  OAuth 액세스 토큰 (OAuth Access Token)" 이라고함

    */
    return accessToken
}
export const getMemberWithAccessToken = async (accessToken) => {

    const res = await axios.get(`${API_SERVER_HOST}/api/member/kakao?accessToken=${accessToken}`)

    return res.data

}

