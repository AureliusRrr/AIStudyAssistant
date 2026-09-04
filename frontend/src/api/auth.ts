import request from './request'

export interface LoginParams{
  username:string
  password:string
}

export interface RegisterParams{
  username:string
  password:string
  email:string
}

export interface LoginResult{
  token:string
  userId:number
  username:string
}

export interface UserInfo{
  id:number
  username:string
  email:string
  avatar?:string
  role:string
}

// 注意:响应拦截器在运行时已经把 Result 的 data 解包出来了,
// 所以这里要同时传第二个泛型 R(返回值类型),否则 TS 仍会认为
// 返回的是 Promise<AxiosResponse<T>>,和真实返回值不一致
export function login(data: LoginParams): Promise<LoginResult> {
  return request.post<LoginResult, LoginResult>('/user/login', data)
}

export function register(data: RegisterParams): Promise<UserInfo> {
  return request.post<UserInfo, UserInfo>('/user/register', data)
}

export function getMe(): Promise<UserInfo> {
  return request.get<UserInfo, UserInfo>('/user/me')
}


