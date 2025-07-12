import { useEffect, useState } from "react";
import { getList } from "../../api/productsApi";
import useCustomMove from "../../hooks/useCustomMove";
import FetchingModal from "../common/FetchingModal";
import { API_SERVER_HOST } from "../../api/todoApi";
import PageComponent from "../common/PageComponent";
const initState = { // 초기 데이터
    dtoList: [],
    pageNumList: [],
    pageRequestDTO: null,
    prev: false,
    next: false,
    totoalCount: 0,
    prevPage: 0,
    nextPage: 0,
    totalPage: 0,
    current: 0
}
const host=API_SERVER_HOST
const ListComponent = () => {

    const { page, size, refresh, moveToList, moveToRead } = useCustomMove()
    const [serverData, setServerData] = useState(initState)
    const [fetching, setFetching] = useState(false)

    useEffect(() => {
        setFetching(true)
        getList({ page, size }).then(data => {
            console.log(data)
            setServerData(data)
            setFetching(false) // 서버로부터 데이터 받아옴.
        })
    }, [page, size, refresh])
return (
  <div className="border-2 border-blue-100 mt-10 mr-2 ml-2">
    <h1>Products List Component</h1>
    {fetching ? <FetchingModal /> : <></>}

    <div className="flex flex-wrap mx-auto p-6">
      {serverData.dtoList.map(product => (
        <div
          key={product.pno} // 리스트 업데이트 시 성능 저하 예상치 못한 컴포넌트 재사용 혹은 재렌더링 버그 (사용안하면 경고창이 나옴)
          className="w-1/2 p-1 rounded shadow-md border-2"
          onClick={() => moveToRead(product.pno)} //  클릭 시 상세 창 이동기능
        >
          <div className="flex flex-col h-full">
            <div className="font-extrabold text-2xl p-2 w-full">
              {product.pno}
            </div>
            <div className="text-1xl m-1 p-2 w-full flex flex-col">
              <div className="w-full overflow-hidden">
                <img
                  alt="product"
                  className="m-auto rounded-md w-60"
                  src={`${host}/api/products/view/s_${product.uploadFileNames[0]}`}
                  // axios 사용 안해도 된다. 이런식으로 서버측에 요청하면 기본 get으로 전달되어 호출 방식을 지정안해도 된다.
                />
              </div>
              <div className="bottom-0 font-extrabold bg-white">
                <div className="text-center p-1">이름: {product.pname}</div>
                <div className="text-center p-1">가격: {product.price}</div>
              </div>
            </div>
          </div>
        </div>
      ))}
    </div>
    <PageComponent serverData={serverData} movePage={moveToList}></PageComponent>
  </div>
);

}

export default ListComponent;