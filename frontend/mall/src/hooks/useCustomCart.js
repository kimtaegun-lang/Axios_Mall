import { useDispatch, useSelector } from "react-redux"
import { getCartItemsAsync, postChangeCartAsync } from "../slices/cartSlice"

const useCustomCart = () => {

    const cartItems = useSelector(state => state.cartSlice)

    const dispatch = useDispatch()

    const refreshCart = () => {

        dispatch(getCartItemsAsync()) // state가 바뀜 리턴하는게 아님

    }

    const changeCart = (param) => {

        dispatch(postChangeCartAsync(param))
    }

    return { cartItems, refreshCart, changeCart }

}

export default useCustomCart