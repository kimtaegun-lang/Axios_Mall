import { Link } from "react-router-dom";
import { getKakaoLoginLink } from "../../api/kakaoApi";

const KakaoLoginComponent = () => {
    const link = getKakaoLoginLink() 
    return (
        <div className="flex flex-col">
            <div className="text-center text-blue-500">로그인시에 자동 가입처리 됩니다</div>
            <div className="flex justify-center	w-full">
                <div
                    className="text-3xl text-center m-6 text-white font-extrabold w-3/4 bg-yellow-500 shadow-sm rounded p-2">
                    <Link to={link}>KAKAO LOGIN</Link> {/*로그인 후에는 Redirect Uri 설정 경로로 이동하게 된다. 이때 쿼리스트링으로 인가 코드가 전달된다. */}
                </div>
            </div>
        </div>
    )
}
export default KakaoLoginComponent;