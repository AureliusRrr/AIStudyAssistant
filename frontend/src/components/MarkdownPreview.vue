<script setup lang="ts">
import { computed } from 'vue'
import { renderMarkdown } from '@/utils/markdown'

const props = defineProps<{
  content: string
}>()

const html = computed(() => renderMarkdown(props.content))
</script>

<template>
  <div class="markdown-preview" v-html="html" />
</template>

<style scoped>
.markdown-preview {
  padding: 16px;
  overflow-y: auto;
  height: 100%;
  line-height: 1.7;
}
/* 简单覆盖 markdown 标题、代码块样式（可按需扩展） */
.markdown-preview :deep(h1) {
  font-size: 24px;
  border-bottom: 1px solid #eee;
  padding-bottom: 8px;
}
.markdown-preview :deep(pre) {
  background: #f6f8fa;
  padding: 12px;
  border-radius: 6px;
  overflow-x: auto;
}
.markdown-preview :deep(code) {
  background: #f6f8fa;
  padding: 2px 4px;
  border-radius: 4px;
}
</style>
