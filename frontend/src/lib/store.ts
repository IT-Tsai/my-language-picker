
import { configureStore } from '@reduxjs/toolkit';
import accReducer from './features/account/accountSlice';
export const makeStore = () => {
  return configureStore({
    reducer: {
      account: accReducer
    },
  })
}

// Infer the type of makeStore
export type AppStore = ReturnType<typeof makeStore>
// Infer the `RootState` and `AppDispatch` types from the store itself
export type RootState = ReturnType<AppStore['getState']>
export type AppDispatch = AppStore['dispatch']
