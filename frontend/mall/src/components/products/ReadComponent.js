import { useEffect, useState } from "react"
import { getOne } from "../../api/productsApi"
import { API_SERVER_HOST } from "../../api/todoApi"
import useCustomMove from "../../hooks/useCustomMove"
import FetchingModal from "../common/FetchingModal"
import useCustomCart from "../../hooks/useCustomCart"
import useCustomLogin from "../../hooks/useCustomLogin"
import { useQuery } from "@tanstack/react-query"

const initState = { pno: 0, pname: '', pdesc: '', price: 0, uploadFileNames: [] }
const host = API_SERVER_HOST

const ReadComponent = ({ pno }) => {
    const [product, setProduct] = useState(initState)
    const { moveToList, moveToModify } = useCustomMove()
    const [fetching, setFetching] = useState(false)
    const { changeCart, cartItems } = useCustomCart()
    const { loginState } = useCustomLogin()

    const {isFetching,data}=useQuery({ // 서버 데이터를 비동기적으로 가져오고, 로딩 상태, 오류, 캐싱 등 여러 상태를 자동으로 관리해주는 훅입니다.
        queryKey:['product',pno], // 이 쿼리를 구분하는 고유한 키입니다
        queryFn:()=>getOne(pno), // 실제로 서버에서 데이터를 가져오는 비동기 함수입니다.
        staleTime:1000*10, // 10초 동안은 React Query가 캐시된 데이터를 재사용하고, 서버 요청을 다시 하지 않습니다.
        retry:1 // 요청 실패 시 재시도 횟수입니다.
    });
// useQuery()는 특정 데이터를 조회하고 통신 상태나 결과 혹은 에러 데이터 등을 한 번에 처리할 수 있게 도와준다.
    


const handleClickAddCart = () => {
        let qty = 1
        const addedItem = cartItems.filter(item => item.pno === parseInt(pno))[0]

        if (addedItem) {
            if (window.confirm("이미 추가된 상품입니다. 추가하시겠습니까?") === false) {
                return
            }
            qty = addedItem.qty + 1
        }
        changeCart({ email: loginState.email, pno: pno, qty: qty })
    }


    useEffect(() => {
        setFetching(true)
        getOne(pno).then(data => {
            setProduct(data)
            setFetching(false)
        })
    }, [pno])
    return (
        <div className="border-2 border-sky-200 mt-10 m-2 p-4"> {/*border-2	두께 2의 테두리(border) 생성
                border-sky-200	테두리 색상을 sky-200 (밝은 하늘색)으로 설정
                    mt-10	상단 마진(margin-top) 2.5rem (40px)
                 m-2	모든 방향 마진 0.5rem (8px)
                 p-4	안쪽 여백(padding) 1rem (16px)*/ }
            {isFetching ? <FetchingModal /> : <></>}
            <div class="flex justify-center mt-10">
                <div class="relative mb-4 flex w-full flex-wrap items-stretch">
                    <div class="w-1/5 p-6 text-right font-bold">PNO</div>
                    <div class="w-4/5 p-6 rounded-r border border-solid shadow-md">{product.pno}</div>
                </div>
            </div>

            <div class="flex justify-center">
                <div class="relative mb-4 flex w-full flex-wrap items-stretch">
                    <div class="w-1/5 p-6 text-right font-bold">PNAME</div>
                    <div class="w-4/5 p-6 rounded-r border border-solid shadow-md">{product.pname}</div>
                </div>
            </div>
            <div class="flex justify-center">
                <div class="relative mb-4 flex w-full flex-wrap items-stretch">
                    <div class="w-1/5 p-6 text-right font-bold">PRICE</div>
                    <div class="w-4/5 p-6 rounded-r border border-solid shadow-md">{product.price}</div>
                </div>
            </div>

            <div class="flex justify-center">
                <div class="relative mb-4 flex w-full flex-wrap items-stretch">
                    <div class="w-1/5 p-6 text-right font-bold">PDESC</div>
                    <div class="w-4/5 p-6 rounded-r border border-solid shadow-md">{product.pdesc}</div>
                </div>
            </div>

            <div class="w-full justify-center flex flex-col m-auto items-center">
                {product.uploadFileNames.map((imgFile, i) =>
                    <img alt="product" key={i} className="p-4 w-1/2" src={`${host}/api/products/view/${imgFile}`} />
                )}
            </div>


            <div class="flex justify-end p-4">
                <button type="button"
                    className="inline-block rounded p-4 m-2 text-xl w-32	text-white bg-green-500"
                    onClick={handleClickAddCart} > Add Cart </button>

                <button type="button"
                    class="inline-block rounded p-4 m-2 text-xl w-32 text-white bg-red-500"
                    onClick={() => moveToModify(pno)}>
                    Modify
                </button>
                <button type="button" class="rounded p-4 m-2 text-xl w-32 text-white bg-blue-500" onClick={moveToList}>
                    List
                </button>
            </div>
        </div>
    )

}
export default ReadComponent;