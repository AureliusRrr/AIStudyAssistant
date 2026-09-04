import axios from "axios";
import {ElMessage} from "element-plus";
import router from "@/router";

const request = axios.create({
  baseURL:'/api',
  timeout:10000
})

//请求拦截器:自动携带Token
request.interceptors.request.use((config)=>{
  const token = localStorage.getItem('token')
  if(token){
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

//相应拦截器,统一处理Result和错误
request.interceptors.response.use(
  (response) => {
    //文件下载等二进制响应,没有code字段,直接返回Blob,不按Result解包
    if(response.config.responseType == 'blob'){
      return response.data
    }
    const res = response.data
    if(res.code != 200){
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message))
    }
    return res.data
  },
  (error) => {
    if(error.response?.status === 401){
      localStorage.removeItem('token')
      router.push('/login')
      ElMessage.error('登录已过期,请重新登录')
    }else{
      ElMessage.error(error.response?.data?.message || '网络异常')
    }
    return Promise.reject(error)
  }
)

export default request
