<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getNoteDetail, createNote, updateNote, deleteNote } from '@/api/note'
import MarkdownPreview from '@/components/MarkdownPreview.vue'

const route = useRoute()
const router = useRouter()

// 编辑模式下 id 存在；新建模式 id 为 null
const noteId = computed<string | null>(() => {
  const id = route.params.id as string | undefined
  return id && id !== 'new' ? id : null
})

const loading = ref(false)
const saving = ref(false)

const form = reactive({
  title: '',
  content: '',
  tags: ''
})

// 预览与编辑分栏开关（小屏幕可切换）
const previewVisible = ref(true)

// 字数统计
const wordCount = computed(() => form.content.length)

async function loadDetail() {
  if (!noteId.value) return
  loading.value = true
  try {
    const detail = await getNoteDetail(noteId.value)
    form.title = detail.title
    form.content = detail.content
    form.tags = detail.tags || ''
  } finally {
    loading.value = false
  }
}

async function handleSave() {
  if (!form.title.trim()) {
    ElMessage.warning('请填写标题')
    return
  }
  saving.value = true
  try {
    if (noteId.value) {
      await updateNote(noteId.value, {
        title: form.title,
        content: form.content,
        tags: form.tags
      })
      ElMessage.success('保存成功')
    } else {
      const created = await createNote({
        title: form.title,
        content: form.content,
        tags: form.tags
      })
      ElMessage.success('创建成功')
      // 新建成功后跳到编辑模式，避免再点保存变成"又新建一条"
      router.replace(`/notes/${created.id}`)
    }
  } finally {
    saving.value = false
  }
}

async function handleDelete() {
  if (!noteId.value) return
  try {
    await ElMessageBox.confirm('确定删除这篇笔记吗？', '删除确认', { type: 'warning' })
  } catch {
    return
  }
  await deleteNote(noteId.value)
  ElMessage.success('已删除')
  router.push('/notes')
}

function handleBack() {
  router.push('/notes')
}

onMounted(loadDetail)
</script>

<template>
  <div class="editor-page" v-loading="loading">
    <div class="editor-toolbar">
      <el-input
        v-model="form.title"
        placeholder="笔记标题"
        class="title-input"
        maxlength="100"
        show-word-limit
      />
      <el-input
        v-model="form.tags"
        placeholder="标签，用英文逗号分隔，如: java,集合"
        class="tags-input"
      />
      <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
      <el-button @click="handleBack">返回列表</el-button>
      <el-button v-if="noteId" type="danger" @click="handleDelete">删除</el-button>
      <el-button link @click="previewVisible = !previewVisible">
        {{ previewVisible ? '隐藏预览' : '显示预览' }}
      </el-button>
    </div>

    <div class="editor-body">
      <div v-if="previewVisible" class="editor-half">
        <el-input
          v-model="form.content"
          type="textarea"
          class="editor-textarea"
          placeholder="支持 Markdown：# 标题、**加粗**、```代码块``` 等"
        />
      </div>
      <div v-if="previewVisible" class="preview-half">
        <MarkdownPreview :content="form.content" />
      </div>
      <div v-else class="editor-full">
        <el-input
          v-model="form.content"
          type="textarea"
          class="editor-textarea"
          placeholder="支持 Markdown"
        />
      </div>
    </div>

    <div class="editor-footer">
      字数：{{ wordCount }}
    </div>
  </div>
</template>

<style scoped>
.editor-page {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 60px - 40px);
}
.editor-toolbar {
  display: flex;
  gap: 10px;
  margin-bottom: 12px;
  flex-wrap: wrap;
}
.title-input {
  width: 280px;
}
.tags-input {
  width: 260px;
}
.editor-body {
  flex: 1;
  display: flex;
  gap: 12px;
  min-height: 0;
}
.editor-half {
  width: 50%;
  min-width: 0;
}
.editor-full {
  width: 100%;
}
.editor-textarea :deep(.el-textarea__inner) {
  height: 100%;
  font-family: 'Consolas', 'Courier New', monospace;
  line-height: 1.6;
}
.editor-footer {
  padding-top: 8px;
  color: #999;
  font-size: 12px;
}
</style>
