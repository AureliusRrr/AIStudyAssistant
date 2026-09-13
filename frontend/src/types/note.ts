export interface NoteSummary{
  id:number
  title:string
  summary:string
  tags:string
  isPinned:number
  viewCount:number
  createTime:string
  updateTime:string
}

export interface NoteDetail extends NoteSummary{
  content:string
}

export interface NoteRequest{
  title:string
  content:string
  tags?:string
  isPinned?:number
}
