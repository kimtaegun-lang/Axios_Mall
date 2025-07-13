import { useEffect, useMemo } from "react";
import useCustomLogin from "../../hooks/useCustomLogin";
import useCustomCart from "../../hooks/useCustomCart";
import CartItemComponent from "../cart/CartItemComponent";
import { getCartItemsAsync } from "../../slices/cartSlice";
import { useDispatch, useSelector } from "react-redux";
const CartComponent = () => {
    const { isLogin, loginState } = useCustomLogin();
    const { refreshCart, cartItems, changeCart } = useCustomCart()
    const dispatch = useDispatch()


    const total = useMemo(() => { // cartItems가 바뀔 때만 total을 다시 계산하고, 안 바뀌면 이전 계산값을 재사용하겠다.
        // useMemo: 랜더링때 실행
        let total = 0
        for (const item of cartItems) {
            total += item.price * item.qty
        }
        return total
    }, [cartItems])

    // const cartItems = useSelector(state => state.cartSlice)
    useEffect(() => { // 값을 리턴하지 않음. 대신 내부에서 뭔가 **"행동"**을 함 (실행시점은 의존성배열의 변경, 단, 부수적인 효과 기대(useMemo와 비교하여,))
        // useEffect: 랜더링 이후 실행,
        if (isLogin) {
            //dispatch(getCartItemsAsync())
            refreshCart()
        }
    }, [isLogin])
    return (
        <div className="w-full">
            {isLogin ?
                <div className="flex flex-col">
                    <div className="w-full flex">
                        <div className="font-extrabold text-2xl w-4/5"> {loginState.nickname}'s Cart </div>
                        <div className="bg-orange-600 text-center text-white font-bold w-1/5 rounded-full m-1">
                            {cartItems.length}
                        </div>
                    </div>

                    <div>
                        <div className="text-2xl text-right font-extrabold">
                            TOTAL: {total}
                        </div>
                        <ul> {cartItems.map(item => <CartItemComponent {...item} key={item.cino} changeCart={changeCart} email={loginState.email} />)}</ul>
                    </div>
                </div>
                :
                <></>
            }
        </div>
    );
}

export default CartComponent;