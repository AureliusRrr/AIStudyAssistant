import request from './request'
import type {NoteSummary, NoteDetail, NoteRequest} from '@/types/note';

export function getNoteList(params:{keyword?:string;tag?:string}){
  // 和第 7 周 auth.ts 同理:拦截器运行时已把 Result.data 解包,
  // 第二个泛型 R 才是 Promise 真正 resolve 的类型
  return request.get<NoteSummary[], NoteSummary[]>('/note/list', {params})

}

export function getNoteDetail(id:number | string){
  return request.get<NoteDetail,NoteDetail>(`/note/${id}`)
}

export function createNote(data:NoteRequest){
  return request.post<NoteDetail,NoteDetail>('/note', data)
}

export function updateNote(id:number | string, data:NoteRequest){
  return request.put<NoteDetail,NoteDetail>(`/note/${id}`, data)
}

// id 统一收 number | string:列表页传的是 note.id(number),
// 编辑页传的是路由参数(string),与 getNoteDetail / updateNote 保持一致
export function deleteNote(id: number | string){
  return request.delete<string, string>(`/note/${id}`)
}
