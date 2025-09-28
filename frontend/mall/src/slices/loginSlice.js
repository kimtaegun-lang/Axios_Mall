import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { loginPost } from "../api/memberApi";
import { setCookie, getCookie,removeCookie } from "../util/cookieUtil";

const initState = {
    email: ''
};
const loadMemberCookie = () => {
    const memberInfo = getCookie("member")
    //닉네임 처리
    if (memberInfo && memberInfo.nickname) { 
        // decodeURIComponent :URL에 사용된 인코딩(퍼센트 인코딩)을 다시 원래 문자열로 변환
        memberInfo.nickname = decodeURIComponent(memberInfo.nickname)
    }
    return memberInfo
}

// 서버에 로그인 요청을 보내는 비동기 thunk 함수
export const loginPostAsync = createAsyncThunk('loginPostAsync', (param) => {
    return loginPost(param); // createAsyncThunk()를 사용해서 비동기 통신을 호출하는 함수
});

// thunk란?: 비동기 작업(예: 서버 요청)을 Redux 안에서 처리할 수 있도록 도와주는 미들웨어
const loginSlice = createSlice({
    name: 'loginSlice',
    initialState: loadMemberCookie()||initState,
    reducers: {
        login: (state, action) => { //  dispatch(login(memberInfo)) 여기서 action 호출
            // action.payload로 데이터 받은 후 return 시 ,state에 값 저장됨. 여기선 추가로 쿠키에도 저장
            console.log("login.....");
            const payload = action.payload;

            setCookie("member",JSON.stringify(payload),1) // 1은 쿠키 만료시간
           // return { email: data.email };
           return payload;
        },
        logout: (state, action) => {
            console.log("logout....");
            removeCookie("member")
            return { ...initState };
        }
    },
    extraReducers: (builder) => { // 비동기 호출의 상태에 따라 동작하는 extraReducers를 추가
        // dispatch(loginPostAsync(loginParam)) 요 부분에서 실행됨
        builder
            .addCase(loginPostAsync.pending, (state, action) => {
                console.log("pending"); // 비동기 요청 시작됨 (ex: 로딩 중 UI 표시)
            })
            .addCase(loginPostAsync.fulfilled, (state, action) => {
                console.log("fulfilled"); // 요청 성공 (ex: 응답 받은 데이터 상태에 저장)

                const payload = action.payload;

                // 정상적인 로그인시에만 쿠키 저장
                if (!payload.error) {
                    setCookie('member', JSON.stringify(payload), 1); // 1일
                }

                return payload;
            })
            .addCase(loginPostAsync.rejected, (state, action) => {
                console.log("rejected"); // 요청 실패 (ex: 에러 메시지 표시 등)
            });
    }
});

export const { login, logout } = loginSlice.actions;
export default loginSlice.reducer;

/* 대략적인 흐름

사용자가 로그인 폼 제출

dispatch(loginPostAsync(loginParam)) 실행

thunk가 서버에 로그인 요청 (loginPost)

요청 상태에 따라:

pending: 콘솔에 pending 로그

fulfilled: 응답 데이터를 상태로 저장 (email 등) 및 쿠키 저장

rejected: 실패 로그만 남음

useSelector로 상태를 읽고 UI 반영 (로그인 상태 등)
*/
