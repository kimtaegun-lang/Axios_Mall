import { useRef, useState } from "react";
import { postAdd } from "../../api/productsApi";
import FetchingModal from "../common/FetchingModal";
import ResultModal from "../common/ResultModal";
import useCustomMove from "../../hooks/useCustomMove";
import { useMutation,useQueryClient } from "@tanstack/react-query";

const initState = { pname: '', pdesc: '', price: 0, files: [] }
const AddComponent = () => {
    const [product, setProduct] = useState({ ...initState })
    const [fetching, setFetching] = useState(false)
    const uploadRef = useRef() // 값 저장, 변경해도 렌더링 안 됨
    const [result, setResult] = useState(null)
    const { moveToList } = useCustomMove()
    const handleChangeProduct = (e) => { // state를 통해 변경시마다 랜더링
        product[e.target.name] = e.target.value;
        setProduct({ ...product })
    }
    
    const addMutation = useMutation({
        mutationFn: (product) => postAdd(product)
    });

    const handleClickAdd = (e) => {
        const files = uploadRef.current.files

        const formData = new FormData() // ajax를 통해 보내기 위해

        for (let i = 0; i < files.length; i++) {
            formData.append("files", files[i]);
        }


        formData.append("pname", product.pname);
        formData.append("pdesc", product.pdesc);
        formData.append("price", product.price) // formData객체에 append

        addMutation.mutate(formData)
        // console.log(formData);
        /*
        setFetching(true)
        postAdd(formData).then(data => { // 폼데이터 전송시 로딩때 로디화면 출력
            setFetching(false) // 다끝난후 상태값을 변경해서 로딩창 없앰
            setResult(data.result);
        })*/

    }

    const queryClient=useQueryClient() 

    const closeModal = () => {
       // setResult(null)
       queryClient.invalidateQueries("products/list")
        moveToList({ page: 1 })
    }




    return (
        <div className="border-2 border-sky-200 mt-10 m-2 p-4">
            {/*  {fetching ? <FetchingModal /> : <></>}
            {result? <ResultModal title={'Product Add Result'}
            content={`${result}번 등록 완료`}
            callbackFn={closeModal}/>:<></>} */}

            {addMutation.isLoading ? <FetchingModal /> : <></>}

            {addMutation.isSuccess ?
                <ResultModal
                    title={'Add Result'}
                    content={`Add Success ${addMutation.data.result}`}
                    callbackFn={closeModal}
                /> : <></>
            }


            <div className="border-2 border-sky-200 mt-10 m-2 p-4">
                <div className="flex justify-center">
                    <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                        <div className="w-1/5 p-6 text-right font-bold">Product Name</div>
                        <input className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md" name="pname" type={'text'} value={product.pname} onChange={handleChangeProduct} >
                        </input>
                    </div>
                </div>

                <div className="flex justify-center">
                    <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                        <div className="w-1/5 p-6 text-right font-bold">Desc</div>
                        <textarea
                            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md resize-y" name="pdesc" rows="4" onChange={handleChangeProduct} value={product.pdesc}>

                        </textarea>
                    </div>
                </div>
                <div className="flex justify-center">
                    <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                        <div className="w-1/5 p-6 text-right font-bold">Price</div>
                        <input
                            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md"
                            name="price" type={'number'} value={product.price} onChange={handleChangeProduct}>
                        </input>
                    </div>
                </div>

                <div className="flex justify-center">
                    <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                        <div className="w-1/5 p-6 text-right font-bold">Files</div>
                        <input
                            ref={uploadRef}
                            className="w-4/5 p-6 rounded-r border border-solid border-neutral-300 shadow-md" type={'file'} multiple={true}>
                        </input>
                    </div>
                </div>
                <div className="flex justify-end">
                    <div className="relative mb-4 flex p-4 flex-wrap items-stretch">
                        <button type="button"
                            className="rounded p-4 w-36 bg-blue-500 text-xl	text-white "
                            onClick={handleClickAdd} >
                            ADD
                        </button>
                    </div>
                </div>

            </div>
        </div>
    );
}

export default AddComponent;