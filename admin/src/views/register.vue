<template>
  <div class="auth-page">
    <div class="auth-card">
      <div class="title-wrap">
        <h1 class="title">校友社交系统注册</h1>
        <p class="subtitle">填写以下信息即可创建普通用户账号</p>
      </div>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        class="auth-form"
        label-width="88px"
        @submit.prevent="handleRegister"
      >
        <el-form-item label="账号" prop="zhanghao">
          <el-input
            v-model="form.zhanghao"
            placeholder="请输入账号"
            clearable
          />
        </el-form-item>

        <el-form-item label="密码" prop="mima">
          <el-input
            v-model="form.mima"
            type="password"
            show-password
            placeholder="请输入密码"
          />
        </el-form-item>

        <el-form-item label="确认密码" prop="mima2">
          <el-input
            v-model="form.mima2"
            type="password"
            show-password
            placeholder="请再次输入密码"
          />
        </el-form-item>

        <el-form-item label="姓名" prop="xingming">
          <el-input
            v-model="form.xingming"
            placeholder="请输入姓名"
            clearable
          />
        </el-form-item>

        <el-form-item label="年龄" prop="nianling">
          <el-input
            v-model="form.nianling"
            type="number"
            placeholder="请输入年龄"
          />
        </el-form-item>

        <el-form-item label="手机" prop="shouji">
          <el-input
            v-model="form.shouji"
            maxlength="11"
            placeholder="请输入手机号"
            clearable
          />
        </el-form-item>

        <div class="action-row">
          <el-button
            type="primary"
            class="primary-button"
            :loading="loading"
            @click="handleRegister"
          >
            注册
          </el-button>
          <el-button
            class="secondary-button"
            :disabled="loading"
            @click="goLogin"
          >
            返回登录
          </el-button>
        </div>
      </el-form>

      <div class="link-row">
        <span class="link-text">已有账号？</span>
        <button type="button" class="link-button" @click="goLogin">
          立即登录
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import http from '@/utils/http'
import storage from '@/utils/storage'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const tableName = ref(storage.get('loginTable') || 'yonghu')
const form = ref({
  zhanghao: '',
  mima: '',
  mima2: '',
  xingming: '',
  nianling: '',
  shouji: ''
})

storage.set('loginTable', tableName.value)

const validateConfirmPassword = (_rule, value, callback) => {
  if (!value) {
    callback(new Error('请再次输入密码'))
    return
  }

  if (value !== form.value.mima) {
    callback(new Error('两次密码输入不一致'))
    return
  }

  callback()
}

const validateAge = (_rule, value, callback) => {
  if (!value && value !== 0) {
    callback(new Error('请输入年龄'))
    return
  }

  if (!/^\d+$/.test(String(value))) {
    callback(new Error('年龄应为数字'))
    return
  }

  callback()
}

const validateMobile = (_rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入手机号'))
    return
  }

  if (!/^1\d{10}$/.test(String(value))) {
    callback(new Error('请输入正确的手机号'))
    return
  }

  callback()
}

const rules = {
  zhanghao: [
    { required: true, message: '请输入账号', trigger: 'blur' }
  ],
  mima: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 位', trigger: 'blur' }
  ],
  mima2: [
    { validator: validateConfirmPassword, trigger: 'blur' }
  ],
  xingming: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  nianling: [
    { validator: validateAge, trigger: 'blur' }
  ],
  shouji: [
    { validator: validateMobile, trigger: 'blur' }
  ]
}

const goLogin = () => {
  router.push('/login')
}

const handleRegister = async () => {
  if (!formRef.value || loading.value) {
    return
  }

  try {
    await formRef.value.validate()
  } catch (error) {
    ElMessage.error('请完整并正确填写注册信息')
    return
  }

  loading.value = true

  try {
    const { data } = await http({
      url: `${tableName.value}/register`,
      method: 'post',
      data: {
        zhanghao: form.value.zhanghao,
        mima: form.value.mima,
        mima2: form.value.mima2,
        xingming: form.value.xingming,
        nianling: form.value.nianling,
        shouji: form.value.shouji
      }
    })

    if (data && data.code === 0) {
      ElMessage.success('注册成功，请登录')
      router.replace('/login')
      return
    }

    ElMessage.error((data && (data.msg || data.message)) || '注册失败')
  } catch (error) {
    ElMessage.error((error && error.message) || '注册请求失败')
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
  max-width: 440px;
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

.subtitle {
  margin: 8px 0 0;
  color: #6d7f9c;
  font-size: 14px;
  line-height: 1.6;
}

.auth-form :deep(.el-form-item) {
  margin-bottom: 18px;
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

.action-row {
  display: flex;
  justify-content: center;
  gap: 14px;
  margin-top: 12px;
}

.primary-button,
.secondary-button {
  flex: 1;
  width: 100%;
  height: 46px;
  border-radius: 12px;
  font-size: 15px;
  font-weight: 600;
}

.primary-button {
  border: none;
  background: linear-gradient(135deg, #1f6fff 0%, #4a9eff 100%);
  box-shadow: 0 14px 28px rgba(31, 111, 255, 0.26);
}

.primary-button:hover,
.primary-button:focus {
  filter: brightness(1.03);
}

.secondary-button {
  border: 1px solid #cfe0ff;
  background: #f6f9ff;
  color: #173b73;
}

.secondary-button:hover,
.secondary-button:focus {
  color: #0a56d6;
  border-color: #9fc1ff;
  background: #f1f6ff;
}

.link-row {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  margin-top: 20px;
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
    width: 78px;
  }

  .action-row {
    flex-direction: column;
    align-items: stretch;
  }

  .primary-button,
  .secondary-button {
    width: 100%;
  }
}
</style>
