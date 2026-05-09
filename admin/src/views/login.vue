<template>
  <div class="auth-page">
    <div class="auth-card">
      <div class="title-wrap">
        <h1 class="title">校友社交系统登录</h1>
      </div>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        class="auth-form"
        label-width="74px"
        @submit.prevent="handleLogin"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="form.username"
            :prefix-icon="User"
            placeholder="请输入用户名"
            clearable
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model="form.password"
            :prefix-icon="Lock"
            type="password"
            show-password
            placeholder="请输入密码"
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-form-item label="角色" prop="role">
          <el-radio-group v-model="form.role" class="role-group">
            <el-radio
              v-for="item in roles"
              :key="item.roleName"
              :label="item.roleName"
              :value="item.roleName"
            >
              {{ item.roleName }}
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item class="submit-item">
          <el-button
            type="primary"
            class="submit-button"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>

      <div class="link-row">
        <span class="link-text">没有账号？</span>
        <button type="button" class="link-button" @click="goRegister">
          立即注册
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Lock, User } from '@element-plus/icons-vue'
import http from '@/utils/http'
import menu from '@/utils/menu'
import storage from '@/utils/storage'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const form = ref({
  username: '',
  password: '',
  role: '用户'
})

const roles = ref(menu.list().filter(item => item.hasBackLogin === '是'))

if (!roles.value.some(item => item.roleName === form.value.role) && roles.value.length > 0) {
  form.value.role = roles.value[0].roleName
}

const roleMap = computed(() => {
  return roles.value.reduce((result, item) => {
    result[item.roleName] = item.tableName
    return result
  }, {})
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 位', trigger: 'blur' }
  ],
  role: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ]
}

const getRedirectPath = (role, tableName) => {
  return tableName === 'users' || role === '管理员' || role === 'admin' ? '/index' : '/front/home'
}

const goRegister = () => {
  storage.set('loginTable', 'yonghu')
  router.push('/register')
}

const handleLogin = async () => {
  if (!formRef.value || loading.value) {
    return
  }

  try {
    await formRef.value.validate()
  } catch (error) {
    ElMessage.error('请完整填写登录信息')
    return
  }

  const tableName = roleMap.value[form.value.role]
  if (!tableName) {
    ElMessage.error('当前角色暂不支持登录')
    return
  }

  loading.value = true

  try {
    const { data } = await http({
      url: '/login',
      method: 'post',
      params: {
        username: form.value.username,
        password: form.value.password,
        role: form.value.role,
        tableName
      }
    })

    const token = data && data.data ? data.data.token : ''
    if (data && data.code === 200 && token) {
      const role = form.value.role
      const username = form.value.username

      storage.set('Token', token)
      localStorage.setItem('token', token)
      localStorage.setItem('Token', token)

      storage.set('role', role)
      localStorage.setItem('role', role)

      storage.set('sessionTable', tableName)
      localStorage.setItem('sessionTable', tableName)

      storage.set('adminName', username)
      localStorage.setItem('adminName', username)

      storage.set('loginRole', role)
      localStorage.setItem('loginRole', role)

      storage.set('loginTable', tableName)
      localStorage.setItem('loginTable', tableName)

      ElMessage.success('登录成功')
      router.replace({ path: getRedirectPath(role, tableName) })
      return
    }

    ElMessage.error((data && (data.message || data.msg)) || '登录失败')
  } catch (error) {
    ElMessage.error((error && error.message) || '登录请求失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px 20px;
  box-sizing: border-box;
  background: linear-gradient(135deg, #0a3a80 0%, #4a9eff 100%);
}

.auth-card {
  width: 100%;
  max-width: 430px;
  padding: 40px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.9);
  box-shadow: 0 20px 48px rgba(8, 38, 89, 0.22);
  backdrop-filter: blur(10px);
  box-sizing: border-box;
}

.title-wrap {
  margin-bottom: 26px;
  text-align: center;
}

.title {
  margin: 0;
  color: #0a3a80;
  font-size: 28px;
  font-weight: 700;
  line-height: 1.4;
}

.auth-form :deep(.el-form-item) {
  margin-bottom: 20px;
  padding-bottom: 4px;
}

.auth-form :deep(.el-form-item__label) {
  color: #173b73;
  font-size: 15px;
  font-weight: 600;
  line-height: 44px;
}

.auth-form :deep(.el-form-item__content) {
  line-height: 44px;
  min-height: 44px;
}

.auth-form :deep(.el-input) {
  width: 100%;
  --el-input-height: 44px;
}

.auth-form :deep(.el-input__wrapper) {
  min-height: 44px;
  padding: 0 12px;
  border: 1px solid #d4e2fb;
  border-radius: 12px;
  background: #ffffff;
  box-shadow: none;
}

.auth-form :deep(.el-input__wrapper.is-focus) {
  border-color: #4a9eff;
  box-shadow: 0 0 0 2px rgba(74, 158, 255, 0.12);
}

.auth-form :deep(.el-input__inner) {
  height: 100%;
  color: #173b73;
  border: none !important;
  background: transparent !important;
  box-shadow: none !important;
}

.auth-form :deep(.el-input__inner:hover),
.auth-form :deep(.el-input__inner:focus) {
  border: none !important;
  box-shadow: none !important;
}

.auth-form :deep(.el-input__prefix-inner) {
  color: #6b8cc7;
}

.auth-form :deep(.el-form-item.is-error .el-input__wrapper) {
  border-color: #f56c6c;
  box-shadow: none;
}

.auth-form :deep(.el-form-item__error) {
  position: absolute;
  top: 100%;
  padding-top: 6px;
  line-height: 1.2;
}

.role-group {
  display: flex;
  flex-wrap: nowrap;
  gap: 18px;
  width: 100%;
  min-height: 44px;
  align-items: center;
}

.role-group :deep(.el-radio) {
  margin-right: 0;
}

.role-group :deep(.el-radio__label) {
  color: #173b73;
}

.submit-item {
  margin-top: 10px;
  margin-bottom: 12px;
}

.submit-item :deep(.el-form-item__content) {
  margin-left: 0 !important;
}

.submit-button {
  width: 100%;
  height: 46px;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #1f6fff 0%, #4a9eff 100%);
  box-shadow: 0 14px 28px rgba(31, 111, 255, 0.26);
}

.submit-button:hover,
.submit-button:focus {
  filter: brightness(1.03);
}

.link-row {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  margin-top: 18px;
  font-size: 14px;
}

.link-text {
  color: #6d7f9c;
}

.link-button {
  padding: 0;
  border: none;
  background: transparent;
  color: #1f6fff;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
}

.link-button:hover {
  color: #0a56d6;
}

@media (max-width: 768px) {
  .auth-card {
    max-width: 100%;
    padding: 32px 24px;
  }

  .title {
    font-size: 24px;
  }

  .auth-form :deep(.el-form-item__label) {
    width: 68px;
  }

  .role-group {
    flex-wrap: wrap;
    gap: 12px 18px;
  }
}
</style>
