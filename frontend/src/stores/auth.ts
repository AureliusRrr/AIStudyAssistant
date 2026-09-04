import {defineStore} from 'pinia'
import {ref} from 'vue'
import {login as loginApi,getMe,type LoginParams,type UserInfo} from '@/api/auth'

export const useAuthStore = defineStore('auth',()=>{
  const token = ref(localStorage.getItem('token')||'')
  const user = ref<UserInfo | null>(null)

  async function login(params:LoginParams){
    const res = await loginApi(params)
    token.value = res.token
    localStorage.setItem('token',res.token)
    await fetchMe()
  }

  async function fetchMe(){
    user.value = await getMe()
  }

  function logout(){
    token.value = ''
    user.value = null
    localStorage.removeItem('token')
  }

  return {token,user,login,fetchMe,logout}
})
