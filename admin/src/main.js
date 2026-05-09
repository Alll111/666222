import { createApp } from 'vue'
import App from '@/App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import '@/assets/css/element-variables.scss'
import '@/assets/css/style.scss'
import router from '@/router'
import BreadCrumbs from '@/components/common/BreadCrumbs'
import http from '@/utils/http.js'
import base from '@/utils/base'
import { isAuth, getCurDate, getCurDateTime } from '@/utils/utils'
import storage from '@/utils/storage'
import FileUpload from '@/components/common/FileUpload'
import Editor from '@/components/common/Editor'
import * as validate from '@/utils/validate.js'
import { ElMessage, ElMessageBox } from 'element-plus'
import { registerLegacyElIcons } from '@/icons/legacy-el-icons'

const app = createApp(App)
app.use(router)
app.use(ElementPlus, { size: 'default', zIndex: 3000 })

app.config.globalProperties.$validate = validate
app.config.globalProperties.$http = http
app.config.globalProperties.$base = base.get()
app.config.globalProperties.$project = base.getProjectName()
app.config.globalProperties.$storage = storage
app.config.globalProperties.isAuth = isAuth
app.config.globalProperties.getCurDateTime = getCurDateTime
app.config.globalProperties.getCurDate = getCurDate
app.config.globalProperties.$message = ElMessage
app.config.globalProperties.$alert = ElMessageBox.alert
app.config.globalProperties.$confirm = ElMessageBox.confirm
app.config.globalProperties.$prompt = ElMessageBox.prompt
app.config.globalProperties.$msgbox = ElMessageBox

app.component('bread-crumbs', BreadCrumbs)
app.component('file-upload', FileUpload)
app.component('editor', Editor)
registerLegacyElIcons(app)

app.mount('#app')
