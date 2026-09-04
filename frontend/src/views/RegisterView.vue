<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { register } from '@/api/auth'

const router = useRouter()

const formRef = ref<FormInstance>()
const loading = ref(false)

const form = ref({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
})

const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度 3~20', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少 6 位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        if (value !== form.value.password) {
          callback(new Error('两次密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

async function handleRegister() {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
  } catch {
    // 校验不通过:表单内联提示,不发请求
    return
  }

  loading.value = true
  try {
    // confirmPassword 只用于前端二次校验,后端 RegisterRequest 没有该字段会忽略它
    await register(form.value)
    ElMessage.success('注册成功,请登录')
    router.push('/login')
  } catch {
    // 请求失败提示已由 axios 响应拦截器统一弹出
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="auth-page">
    <el-card class="auth-card" shadow="always">
      <h2>注册</h2>
      <p class="auth-tip">创建账号,开启 AI 学习之旅</p>

      <el-form ref="formRef" :model="form" :rules="rules" label-width="0">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名(3~20位)" clearable />
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model="form.email" placeholder="邮箱" clearable />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码(至少 6 位)" show-password />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="form.confirmPassword"
            type="password"
            placeholder="确认密码"
            show-password
            @keyup.enter="handleRegister"
          />
        </el-form-item>
        <el-form-item>
          <el-button class="auth-submit" type="primary" :loading="loading" @click="handleRegister">
            注册
          </el-button>
        </el-form-item>
      </el-form>

      <p class="auth-switch">
        已有账号?
        <router-link to="/login">去登录</router-link>
      </p>
    </el-card>
  </div>
</template>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
  background: linear-gradient(135deg, #eef2ff 0%, #f0fdf9 100%);
}

.auth-card {
  width: 400px;
  border-radius: 12px;
}

.auth-card :deep(.el-card__body) {
  padding: 36px 32px 28px;
}

.auth-card h2 {
  margin: 0 0 8px;
  text-align: center;
  font-size: 22px;
  color: var(--el-text-color-primary);
}

.auth-tip {
  margin: 0 0 24px;
  text-align: center;
  font-size: 13px;
  color: var(--el-text-color-secondary);
}

.auth-submit {
  width: 100%;
  letter-spacing: 4px;
}

.auth-switch {
  margin: 8px 0 0;
  text-align: center;
  font-size: 13px;
  color: var(--el-text-color-regular);
}

.auth-switch a {
  color: var(--el-color-primary);
  text-decoration: none;
}
</style>
