<template>
  <div>
    <canvas
      id="canvas"
      style="background:linear-gradient(135deg, #081f4d 0%, #1357c5 45%, #5fb3ff 100%)"
    ></canvas>
    <div class="register-page">
      <div class="register-card">
        <div class="title-container">
          <h1 class="title">校友社交系统注册</h1>
          <p class="subtitle">填写以下信息即可创建普通用户账号</p>
        </div>
        <el-form
          ref="registerForm"
          class="register-form"
          :model="ruleForm"
          @submit.native.prevent
        >
          <el-form-item v-if="tableName === 'yonghu'" label="账号" class="form-item">
            <el-input
              v-model="ruleForm.zhanghao"
              autocomplete="off"
              placeholder="请输入账号"
            />
          </el-form-item>
          <el-form-item v-if="tableName === 'yonghu'" label="密码" class="form-item">
            <el-input
              v-model="ruleForm.mima"
              autocomplete="off"
              placeholder="请输入密码"
              show-password
              type="password"
            />
          </el-form-item>
          <el-form-item v-if="tableName === 'yonghu'" label="确认密码" class="form-item">
            <el-input
              v-model="ruleForm.mima2"
              autocomplete="off"
              placeholder="请再次输入密码"
              show-password
              type="password"
            />
          </el-form-item>
          <el-form-item v-if="tableName === 'yonghu'" label="姓名" class="form-item">
            <el-input
              v-model="ruleForm.xingming"
              autocomplete="off"
              placeholder="请输入姓名"
            />
          </el-form-item>
          <el-form-item v-if="tableName === 'yonghu'" label="年龄" class="form-item">
            <el-input
              v-model="ruleForm.nianling"
              autocomplete="off"
              placeholder="请输入年龄"
            />
          </el-form-item>
          <el-form-item v-if="tableName === 'yonghu'" label="手机" class="form-item">
            <el-input
              v-model="ruleForm.shouji"
              autocomplete="off"
              placeholder="请输入手机号"
            />
          </el-form-item>
          <div class="action-row">
            <el-button class="primary-btn" type="primary" @click="submitRegister">
              注册
            </el-button>
            <el-button class="secondary-btn" @click="goLogin">
              返回登录
            </el-button>
          </div>
          <div class="login-link-row">
            <span class="login-tip">已有账号？</span>
            <button type="button" class="login-link" @click="goLogin">
              立即登录
            </button>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script>
import canvasBg from "@/assets/js/canvas-bg-2.js";
import "@/assets/css/canvas-bg-2.css";

export default {
  data() {
    return {
      ruleForm: {
        zhanghao: "",
        mima: "",
        mima2: "",
        xingming: "",
        nianling: "",
        shouji: ""
      },
      tableName: "yonghu"
    };
  },
  mounted() {
    const table = this.$storage.get("loginTable") || "yonghu";
    this.tableName = table;
    this.$storage.set("loginTable", table);
    this.$nextTick(() => {
      canvasBg();
    });
  },
  methods: {
    goLogin() {
      this.$router.push({ path: "/login" });
    },
    submitRegister() {
      const url = this.tableName + "/register";
      if ((!this.ruleForm.zhanghao) && this.tableName === "yonghu") {
        this.$message.error("账号不能为空");
        return;
      }
      if ((!this.ruleForm.mima) && this.tableName === "yonghu") {
        this.$message.error("密码不能为空");
        return;
      }
      if ((this.ruleForm.mima !== this.ruleForm.mima2) && this.tableName === "yonghu") {
        this.$message.error("两次密码输入不一致");
        return;
      }
      if ((!this.ruleForm.xingming) && this.tableName === "yonghu") {
        this.$message.error("姓名不能为空");
        return;
      }
      if (this.tableName === "yonghu" && this.ruleForm.nianling && (!this.$validate.isNumber(this.ruleForm.nianling))) {
        this.$message.error("年龄应输入数字");
        return;
      }
      if (this.tableName === "yonghu" && this.ruleForm.shouji && (!this.$validate.isMobile(this.ruleForm.shouji))) {
        this.$message.error("手机应输入手机格式");
        return;
      }
      this.$http({
        url: url,
        method: "post",
        data: this.ruleForm
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message({
            message: "注册成功，请登录",
            type: "success",
            duration: 1500,
            onClose: () => {
              this.$router.replace({ path: "/login" });
            }
          });
        } else {
          this.$message.error((data && data.msg) || "注册失败");
        }
      }).catch((error) => {
        this.$message.error((error && error.message) || "注册请求失败");
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.register-page {
  min-height: 100vh;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px 20px;
  box-sizing: border-box;
  overflow: hidden;

  &::before {
    content: "";
    position: absolute;
    inset: 0;
    background:
      radial-gradient(circle at 18% 20%, rgba(255, 255, 255, 0.32), transparent 24%),
      radial-gradient(circle at 82% 18%, rgba(163, 214, 255, 0.28), transparent 18%),
      linear-gradient(135deg, rgba(4, 24, 70, 0.12), rgba(17, 93, 211, 0.08));
    pointer-events: none;
  }
}

.register-card {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 520px;
  padding: 40px 40px 32px;
  border: 1px solid rgba(255, 255, 255, 0.35);
  border-radius: 28px;
  background: rgba(255, 255, 255, 0.9);
  box-shadow: 0 28px 80px rgba(4, 22, 66, 0.28);
  backdrop-filter: blur(12px);
  box-sizing: border-box;
}

.title-container {
  margin-bottom: 24px;
  text-align: center;
}

.title {
  margin: 0;
  color: #0f2f6c;
  font-size: 28px;
  font-weight: 700;
  line-height: 1.4;
}

.subtitle {
  margin: 10px 0 0;
  color: #6f83a8;
  font-size: 14px;
}

.register-form {
  width: 100%;
}

.form-item {
  width: 100%;
  margin-bottom: 18px;

  & ::v-deep .el-form-item__label {
    width: 92px;
    padding-right: 14px;
    color: #1f2d3d;
    font-size: 15px;
    font-weight: 600;
    line-height: 46px;
  }

  & ::v-deep .el-form-item__content {
    display: flex;
  }

  & ::v-deep .el-input__inner {
    height: 46px;
    line-height: 46px;
    border-radius: 12px;
    border: 1px solid rgba(174, 198, 245, 0.9);
    background: linear-gradient(180deg, #f9fbff 0%, #f3f8ff 100%);
    color: #102a56;
    box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.85);
    transition: border-color 0.2s ease, box-shadow 0.2s ease, background 0.2s ease;
  }

  & ::v-deep .el-input__inner:focus {
    border-color: #1f6fff;
    box-shadow: 0 0 0 4px rgba(31, 111, 255, 0.12);
    background: #fff;
  }
}

.action-row {
  display: flex;
  justify-content: center;
  gap: 14px;
  margin-top: 8px;
}

.primary-btn,
.secondary-btn {
  min-width: 120px;
  height: 46px;
  border-radius: 12px;
  font-size: 15px;
  font-weight: 600;
  transition: transform 0.2s ease, box-shadow 0.2s ease, filter 0.2s ease;
}

.primary-btn {
  border: 0;
  background: linear-gradient(135deg, #2c7dff 0%, #1f6fff 100%);
  box-shadow: 0 16px 28px rgba(31, 111, 255, 0.28);

  &:hover,
  &:focus {
    transform: translateY(-2px);
    filter: brightness(1.02);
    box-shadow: 0 20px 34px rgba(31, 111, 255, 0.32);
  }
}

.secondary-btn {
  border: 1px solid rgba(174, 198, 245, 0.9);
  background: rgba(244, 248, 255, 0.92);
  color: #2d5ca8;

  &:hover,
  &:focus {
    transform: translateY(-2px);
    border-color: #8db7ff;
    box-shadow: 0 12px 24px rgba(78, 123, 206, 0.16);
  }
}

.login-link-row {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 4px;
  margin-top: 18px;
  font-size: 14px;
}

.login-tip {
  color: #6c82aa;
}

.login-link {
  padding: 0;
  border: 0;
  background: transparent;
  color: #3f8cff;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: color 0.2s ease, text-shadow 0.2s ease;

  &:hover {
    color: #1f6fff;
    text-shadow: 0 0 12px rgba(31, 111, 255, 0.2);
  }
}

@media (max-width: 768px) {
  .register-card {
    padding: 32px 22px 26px;
  }

  .form-item {
    & ::v-deep .el-form-item__label {
      width: 78px;
      padding-right: 10px;
    }
  }

  .action-row {
    flex-direction: column;
    align-items: stretch;
  }

  .primary-btn,
  .secondary-btn {
    width: 100%;
  }
}
</style>
