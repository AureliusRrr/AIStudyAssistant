<template>
  <div>
    <!-- 工具栏 -->
    <el-card>
      <div class="toolbar">
        <el-input
          v-model="keyword"
          placeholder="搜索笔记标题或内容"
          clearable
          style="width: 280px"
          @input="onKeywordInput"
          @clear="loadNotes"
        />
        <el-select
          v-model="tag"
          placeholder="按标签过滤"
          clearable
          style="width: 180px"
          @change="loadNotes"
        >
          <el-option v-for="t in allTags" :key="t" :label="t" :value="t" />
        </el-select>
        <el-button type="primary" @click="router.push('/notes/new')">
          + 新建笔记
        </el-button>
      </div>
    </el-card>

    <!-- 笔记列表 -->
    <el-card style="margin-top: 16px">
      <el-empty v-if="notes.length === 0" description="还没有笔记，点右上角新建一篇吧" />
      <div v-else>
        <div
          v-for="note in notes"
          :key="note.id"
          class="note-item"
          @click="router.push(`/notes/${note.id}`)"
        >
          <div class="note-title">
            <el-tag v-if="note.isPinned === 1" type="warning" size="small">置顶</el-tag>
            {{ note.title }}
          </div>
          <div class="note-summary">{{ note.summary || '（无摘要）' }}</div>
          <div class="note-meta">
            <el-tag v-for="t in splitTags(note.tags)" :key="t" size="small" type="info" class="note-tag">
              {{ t }}
            </el-tag>
            <span>阅读 {{ note.viewCount }}</span>
            <span>更新于 {{ formatTime(note.updateTime) }}</span>
            <el-button
              type="danger"
              link
              @click.stop="handleDelete(note)"
            >
              删除
            </el-button>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { deleteNote, getNoteList } from '@/api/note'
import type { NoteSummary } from '@/types/note'

const router = useRouter()

const notes = ref<NoteSummary[]>([])
const keyword = ref('')
const tag = ref('')
const allTags = ref<string[]>(['java', 'spring', 'database', 'network'])
const loading = ref(false)

let timer: ReturnType<typeof setTimeout> | undefined

// 输入防抖：停止输入 400ms 后再请求
function onKeywordInput() {
  clearTimeout(timer)
  timer = setTimeout(loadNotes, 400)
}

async function loadNotes() {
  loading.value = true
  try {
    notes.value = await getNoteList({
      keyword: keyword.value || undefined,
      tag: tag.value || undefined
    })
  } finally {
    loading.value = false
  }
}

async function handleDelete(note: NoteSummary) {
  try {
    await ElMessageBox.confirm(`确定删除笔记「${note.title}」吗？`, '提示', {
      type: 'warning'
    })
  } catch {
    return // 用户点了取消
  }
  await deleteNote(note.id)
  ElMessage.success('删除成功')
  loadNotes()
}

function splitTags(tags: string): string[] {
  return tags ? tags.split(',').filter(Boolean) : []
}

function formatTime(value: string): string {
  if (!value) return ''
  return value.replace('T', ' ').substring(0, 19)
}

onMounted(loadNotes)
</script>

<style scoped>
.toolbar {
  display: flex;
  gap: 12px;
  align-items: center;
}
.note-item {
  padding: 14px 8px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
}
.note-item:hover {
  background: #f5f7fa;
}
.note-title {
  font-size: 16px;
  font-weight: 600;
  display: flex;
  gap: 8px;
  align-items: center;
}
.note-summary {
  color: #606266;
  margin: 6px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.note-meta {
  display: flex;
  gap: 12px;
  align-items: center;
  color: #909399;
  font-size: 13px;
}
.note-tag {
  margin-right: 4px;
}
</style>
