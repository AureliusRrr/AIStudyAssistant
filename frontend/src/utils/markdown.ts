import markdown from 'markdown-it'

//创建markdown-it实例,在此处统一配置
const md = markdown({
  html:false,
  linkify:true,
  breaks:true
})

export function renderMarkdown(content:string):string{
  return md.render(content || '')
}
