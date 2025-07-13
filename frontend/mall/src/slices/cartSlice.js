import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { getCartItems, postChangeCart } from "../api/cartApi";

export const getCartItemsAsync = createAsyncThunk('getCartItemsAsync', () => { // createAsyncThunk가 만들어주는 3가지 액션 타입의 접두사 역할을 함
    return getCartItems()
})

export const postChangeCartAsync = createAsyncThunk('postCartItemsAsync', (param) => {
    return postChangeCart(param)
})

const initState = []
const cartSlice = createSlice({
    name: 'cartSlice', 
    initialState: initState, 
    extraReducers: (builder) => {
        builder.addCase(
            getCartItemsAsync.fulfilled, (state, action) => { // 성공
                console.log("getCartItemsAsync fulfilled") 
                return action.payload
            }
        )
            .addCase(
                postChangeCartAsync.fulfilled, (state, action) => { // 성공
                    console.log("postCartItemsAsync fulfilled") 
                    return action.payload
                }
            )
    }
})
export default cartSlice.reducer