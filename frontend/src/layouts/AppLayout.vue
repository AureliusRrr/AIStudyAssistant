<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

// 当前高亮菜单项：用路由 path 与菜单 path 匹配
const activeMenu = computed(() => route.path)

function handleCommand(command: string) {
  if (command === 'logout') {
    authStore.logout()
    ElMessage.success('已退出登录')
    router.push('/login')
  }
}
</script>

<template>
  <el-container class="app-layout">
    <!-- 顶栏 -->
    <el-header class="header">
      <div class="logo">AI Study Assistant</div>
      <el-dropdown @command="handleCommand">
        <span class="user-info">
          {{ authStore.user?.username || '未登录' }}
          <el-icon><ArrowDown /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </el-header>

    <el-container>
      <!-- 左侧导航 -->
      <el-aside width="200px" class="aside">
        <el-menu :default-active="activeMenu" router>
          <el-menu-item index="/dashboard">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/notes">
            <el-icon><Notebook /></el-icon>
            <span>我的笔记</span>
          </el-menu-item>
          <el-menu-item index="/documents">
            <el-icon><FolderOpened /></el-icon>
            <span>学习资料</span>
          </el-menu-item>
          <el-menu-item index="/ai">
            <el-icon><MagicStick /></el-icon>
            <span>AI 助手</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 内容区 -->
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.app-layout {
  height: 100vh;
}
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  border-bottom: 1px solid #eee;
}
.logo {
  font-size: 18px;
  font-weight: 600;
}
.user-info {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
}
.aside {
  border-right: 1px solid #eee;
}
.main {
  background: #f5f7fa;
}
</style>
