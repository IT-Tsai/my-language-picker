import { PayloadAction, createSlice } from '@reduxjs/toolkit'
import type { RootState } from '../../store'
import Account from '@/models/Account'

const initialState: Account = {
  id: Infinity,
  username: "",
  password: "",
  email: "",
  name: "",
  languages: [],
}

export const accountSlice = createSlice({
  name: 'account',
  initialState,
  reducers: {
    updateAccount: (state, action: PayloadAction<Account>) => {
      state = { ...state, ...action.payload }
      return state;
    }
  }
})

export const { updateAccount } = accountSlice.actions

// Other code such as selectors can use the imported `RootState` type
export const accountId = (state: RootState) => state.account.id;

export default accountSlice.reducer