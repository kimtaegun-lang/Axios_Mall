import { useParams } from "react-router-dom";
import ReadComponent from "../../components/products/ReadComponent"
const ReadPage=()=>
{
    const {pno}=useParams() // URL에 포함된 파라미터 값
    return (
        <div className="p-4 w-full bg-white">
            {/* padding: 1rem (16px) — 모든 방향(상하좌우)에 16px 패딩 적용
            text-3xl	폰트 사이즈: 1.875rem (30px) — 큰 제목용 텍스트
font-extrabold	폰트 굵기: extrabold (800) — 매우 두꺼운 글꼴 */}
            <div className="text-3xl font-extrabold">
                Products Read Page
            </div>
            <ReadComponent pno={pno}></ReadComponent>
        </div>
    );
}

export default ReadPage;