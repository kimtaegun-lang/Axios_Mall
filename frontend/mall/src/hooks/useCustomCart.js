import { useDispatch, useSelector } from "react-redux"
import { getCartItemsAsync, postChangeCartAsync } from "../slices/cartSlice"
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { getCartItems, postChangeCart } from "../api/cartApi" 
import { useRecoilState } from "recoil"
import { cartState } from "../atoms/cartState"
import { useEffect } from "react"

const useCustomCart = () => {
    /*
        const cartItems = useSelector(state => state.cartSlice)
    
        const dispatch = useDispatch()
    
        const refreshCart = () => {
    
            dispatch(getCartItemsAsync()) // state가 바뀜 리턴하는게 아님
    
        }
    
        const changeCart = (param) => {
    
            dispatch(postChangeCartAsync(param))
        }
    
        return { cartItems, refreshCart, changeCart }
    */
    const [cartItems, setCartItems] = useRecoilState(cartState)
    const queryClient = useQueryClient()
    
    const changeMutation = useMutation({
        mutationFn: (param) => postChangeCart(param),
        onSuccess: (result) => {
            setCartItems(result);
        }
    })
    // 1 hour
    const query = useQuery({ queryKey: ["cart"], getCartItems, queryFn: { staleTime: 1000 * 60 * 60 } })
    useEffect(() => {
        if (query.isSuccess) {
            queryClient.invalidateQueries("cart")
            setCartItems(query.data)
        }
    }, [query.isSuccess, query.data]) 

    const changeCart = (param) => {
        changeMutation.mutate(param)
    }

    return { cartItems, changeCart }


}

export default useCustomCart