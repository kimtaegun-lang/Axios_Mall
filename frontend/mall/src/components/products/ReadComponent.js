import { useEffect, useState } from "react"
import { getOne } from "../../api/productsApi"
import { API_SERVER_HOST } from "../../api/todoApi"
import useCustomMove from "../../hooks/useCustomMove"
import FetchingModal from "../common/FetchingModal"

const initState = { pno: 0, pname: '', pdesc: '', price: 0, uploadFileNames: [] }
const host = API_SERVER_HOST

const ReadComponent = ({ pno }) => {
    const [product, setProduct] = useState(initState)
    const { moveToList, moveToModify } = useCustomMove()
    const [fetching, setFetching] = useState(false)

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
            {fetching ? <FetchingModal /> : <></>}
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