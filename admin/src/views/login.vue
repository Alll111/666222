<template>
  <div>
        <canvas id="canvas"  style="background:linear-gradient(135deg, #081f4d 0%, #1357c5 45%, #5fb3ff 100%)"></canvas>
        <div class="container loginIn">

      <div :class="2 == 1 ? 'left' : 2 == 2 ? 'left center' : 'left right'">
        <el-form class="login-form" label-position="left" :label-width="1 == 3 || 1 == 2 ? '30px': '0px'">
          <div class="title-container"><h3 class="title">校友社交系统登录</h3></div>
          <el-form-item :style='{"padding":"0","boxShadow":"0 0 6px rgba(0,0,0,0)","margin":"0px 0px 6px 40px","borderColor":"rgba(0,0,0,0)","backgroundColor":"rgba(255, 140, 0, 0)","borderRadius":"0","borderWidth":"0","width":"420px","borderStyle":"solid","height":"auto"}' :label="1 == 3 ? '用户名' : ''" :class="'style'+1">
            <span v-if="1 != 3" class="svg-container" style="
			color:rgba(30, 25, 0, 1);
			line-height:30px;
			font-size:14px;
			width:30px;
			padding:0;
			margin:12px 0px 0px 40px;
			radius:0;
			border-width:0;
			border-style:solid;
			border-color:rgba(0,0,0,0);
			background-color:rgba(0,0,0,0);
			box-shadow:0 0 6px rgba(0,0,0,0);
			}"><svg-icon icon-class="user" /></span>
            <el-input placeholder="请输入用户名" name="username" type="text" v-model="rulesForm.username" />
          </el-form-item>
          <el-form-item :style='{"padding":"0","boxShadow":"0 0 6px rgba(0,0,0,0)","margin":"0px 0px 6px 40px","borderColor":"rgba(0,0,0,0)","backgroundColor":"rgba(255, 140, 0, 0)","borderRadius":"0","borderWidth":"0","width":"420px","borderStyle":"solid","height":"auto"}' :label="1 == 3 ? '密码' : ''" :class="'style'+1">
            <span v-if="1 != 3" class="svg-container" style="color:rgba(30, 25, 0, 1);
			line-height:30px;
			font-size:14px;
			width:30px;
			padding:0;
			margin:12px 0px 0px 40px;
			radius:0;
			border-width:0;
			border-style:solid;
			border-color:rgba(0,0,0,0);
			background-color:rgba(0,0,0,0);
			box-shadow:0 0 6px rgba(0,0,0,0);"><svg-icon icon-class="password" /></span>
            <el-input placeholder="请输入密码" name="password" type="password" v-model="rulesForm.password" />
          </el-form-item>
          <el-form-item v-if="roles.length>1" label="角色" prop="loginInRole" class="role" style="display: flex;align-items: center;">
            <el-radio
              v-for="item in roles"
              v-bind:key="item.roleName"
              v-model="rulesForm.role"
              :label="item.roleName"
            >{{item.roleName}}</el-radio>
          </el-form-item>
          <el-form-item v-if="roles.length==1" label=" " prop="loginInRole" class="role" style="display: flex;align-items: center;">
          </el-form-item>
          <el-button type="primary" @click="login()" class="loginInBt">{{'1' == '1' ? '登录' : 'login'}}</el-button>
          <div class="register-action">
            <span class="register-text">没有账号？</span>
            <button type="button" class="register-link" @click="register()">
              立即注册
            </button>
          </div>
          <el-form-item class="setting">
          </el-form-item>
        </el-form>
      </div>

    </div>
  </div>
</template>
<script>

import menu from "@/utils/menu";
import canvasBg from "@/assets/js/canvas-bg-2.js";
import "@/assets/css/canvas-bg-2.css"

export default {
  data() {
    return {
      rulesForm: {
        username: "",
        password: "",
        role: "",
        code: '',
      },
      menus: [],
      roles: [],
      tableName: "",
      codes: [{
        num: 1,
        color: '#000',
        rotate: '10deg',
        size: '16px'
      },{
        num: 2,
        color: '#000',
        rotate: '10deg',
        size: '16px'
      },{
        num: 3,
        color: '#000',
        rotate: '10deg',
        size: '16px'
      },{
        num: 4,
        color: '#000',
        rotate: '10deg',
        size: '16px'
      }],
    };
  },
  mounted() {
	let menus = menu.list();
	this.menus = menus;
    for (let i = 0; i < this.menus.length; i++) {
      if (this.menus[i].hasBackLogin=='是') {
            this.roles.push(this.menus[i])
      }
    }
	this.$nextTick(() => {
	  canvasBg()
	})
  },
  created() {
    this.getRandCode()

  },
  methods: {
    getRedirectPath(role, tableName) {
      const roleName = role || this.rulesForm.role
      const table = tableName || this.tableName
      return table === 'users' || roleName === '管理员' || roleName === 'admin' ? '/index' : '/front'
    },
    register(tableName = 'yonghu'){
      this.$storage.set("loginTable", tableName);
      this.$router.push({path:'/register'})
    },
    login() {
      if (!this.rulesForm.username) {
         this.$message.error("请输入用户名");
        return;
      }
      if (!this.rulesForm.password) {
         this.$message.error("请输入密码");
        return;
      }
      if(this.roles.length>1) {
          if (!this.rulesForm.role) {
             this.$message.error("请选择角色");
            return;
          }
          let menus = this.menus;
          for (let i = 0; i < menus.length; i++) {
            if (menus[i].roleName == this.rulesForm.role) {
              this.tableName = menus[i].tableName;
            }
          }
      } else {
          this.tableName = this.roles[0].tableName;
          this.rulesForm.role = this.roles[0].roleName;
      }

      this.$http({
        url: '/login',
        method: "post",
        params: {
          username: this.rulesForm.username,
          password: this.rulesForm.password,
          role: this.rulesForm.role,
          tableName: this.tableName
        }
      }).then(({ data }) => {
        const token = data && data.data ? data.data.token : ''
        if (data && data.code === 200 && token) {
          const role = this.rulesForm.role
          const sessionTable = this.tableName
          this.$storage.set("Token", token);
          localStorage.setItem("token", token);
          localStorage.setItem("Token", token);
          this.$storage.set("role", role);
          localStorage.setItem("role", role);
          this.$storage.set("sessionTable", sessionTable);
          localStorage.setItem("sessionTable", sessionTable);
          this.$storage.set("adminName", this.rulesForm.username);
          localStorage.setItem("adminName", this.rulesForm.username);
          this.$storage.set("loginRole", role);
          localStorage.setItem("loginRole", role);
          this.$storage.set("loginTable", sessionTable);
          localStorage.setItem("loginTable", sessionTable);
          this.$router.replace({ path: this.getRedirectPath(role, sessionTable) });
        } else {
          this.$message.error((data && (data.message || data.msg)) || "登录失败");
        }
      }).catch((error) => {
        this.$message.error((error && error.message) || "登录请求失败");
      });
    },
    getRandCode(len = 4){
      this.randomString(len)
    },
    randomString(len = 4) {
      let chars = [
          "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
          "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
          "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G",
          "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
          "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2",
          "3", "4", "5", "6", "7", "8", "9"
      ]
      let colors = ["0", "1", "2","3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"]
      let sizes = ['14', '15', '16', '17', '18']

      let output = [];
      for (let i = 0; i < len; i++) {
        let key = Math.floor(Math.random()*chars.length)
        this.codes[i].num = chars[key]
        let code = '#'
        for (let j = 0; j < 6; j++) {
          let key = Math.floor(Math.random()*colors.length)
          code += colors[key]
        }
        this.codes[i].color = code
        let rotate = Math.floor(Math.random()*60)
        let plus = Math.floor(Math.random()*2)
        if(plus == 1) rotate = '-'+rotate
        this.codes[i].rotate = 'rotate('+rotate+'deg)'
        let size = Math.floor(Math.random()*sizes.length)
        this.codes[i].size = sizes[size]+'px'
      }
    },
  }
};
</script>
<style lang="scss" scoped>
.loginIn {
  min-height: 100vh;
  position: relative;
  background-repeat: no-repeat;
  background-position: center center;
  background-size: cover;
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

  .loginInBt {
    width: 100% !important;
    height: 46px !important;
    line-height: 46px !important;
    margin: 12px 0 0 !important;
    padding: 0 !important;
    color: #fff !important;
    font-size: 16px !important;
    font-weight: 600;
    border-radius: 12px !important;
    border: 0 !important;
    background: linear-gradient(135deg, #2c7dff 0%, #1f6fff 100%) !important;
    box-shadow: 0 16px 28px rgba(31, 111, 255, 0.28) !important;
    transition: transform 0.2s ease, box-shadow 0.2s ease, filter 0.2s ease;

    &:hover {
      transform: translateY(-2px);
      filter: brightness(1.02);
      box-shadow: 0 20px 34px rgba(31, 111, 255, 0.32) !important;
    }
  }
  .register {
    width: 90px;
    height: 25px;
    line-height: 25px;
    margin: 10px 0px 0px 0px;
    padding: 0px 0px 0px 20px;
    color: rgba(0, 0, 0, 1);
    font-size: 14px;
    border-radius: 0;
    border-width: 0;
    border-style: solid;
    border-color: rgba(64, 158, 255, 1);
    background-color: rgba(144, 238, 144, 0);
    box-shadow: 0 0 6px rgba(255,0,0,0);
	cursor: pointer;
  }
  .register-action {
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 16px 0 4px;
    font-size: 14px;
    line-height: 1;
    gap: 4px;
  }

  .register-text {
    color: #6c82aa;
  }

  .register-link {
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
  .reset {
    width: auto;
    height: 24px;
    line-height: 24px;
    margin: 0;
    padding: 0;
    color: rgba(153, 153, 153, 1);
    font-size: 12px;
    border-radius: 0;
    border-width: 0;
    border-style: solid;
    border-color: rgba(64, 158, 255, 1);
    background-color: rgba(255, 255, 255, 0);
    box-shadow: 0 0 6px rgba(255,0,0,0);
  }


  .left {
    position: absolute;
    left: 0;
    top: 0;
	box-sizing: border-box;
	width: 480px;
	min-height: 520px;
	margin: 0;
	padding: 44px 42px 38px;
	border-radius: 28px;
	border: 1px solid rgba(255, 255, 255, 0.35);
	background: rgba(255, 255, 255, 0.88);
	box-shadow: 0 28px 80px rgba(4, 22, 66, 0.28);
	backdrop-filter: blur(12px);

    .login-form {
      background-color: transparent;
      width: 100%;
      right: inherit;
      padding: 0;
      box-sizing: border-box;
      display: flex;
	  position: initial;
      justify-content: center;
      flex-direction: column;
    }

    .title-container {
      text-align: center;
      font-size: 24px;
      margin-bottom: 26px;

      .title {
        width: 100%;
        line-height: 1.4;
        margin: 0;
        padding: 0;
        color: #0f2f6c;
        font-size: 28px;
        font-weight: 700;
        letter-spacing: 1px;
        border: 0;
        background: transparent;
        box-shadow: none;
      }
    }

    .el-form-item {
      position: relative;
      width: 100% !important;
      margin: 0 0 18px !important;
      padding: 0 !important;
      background: transparent !important;
      border: 0 !important;
      box-shadow: none !important;

      & ::v-deep .el-form-item__content {
        line-height: initial;
      }

	  & ::v-deep .el-radio__label {
	    width: auto;
	    height: 14px;
	    line-height: 14px;
	    margin: 0;
	    padding: 0 0 0 10px;
	    color: rgba(3, 0, 0, 1);
	    font-size: 15px;
	    border-radius: 0;
	    border-width: 0;
	    border-style: solid;
	    border-color: rgba(255, 255, 255, 0);
	    background-color: rgba(255, 255, 255, 0);
	    box-shadow: 0 0 6px rgba(255,0,0,0);
	    text-align: left;
	  }
	  & ::v-deep .el-radio.is-checked .el-radio__label {
	    width: auto;
	    height: 14px;
	    line-height: 14px;
	    margin: 0;
	    padding: 0 0 0 10px;
	    color: rgba(64, 158, 255, 1);
	    font-size: 14px;
	    border-radius: 0;
	    border-width: 0;
	    border-style: solid;
	    border-color: rgba(255, 255, 255, 0);
	    background-color: rgba(255, 255, 255, 0);
	    box-shadow: 0 0 6px rgba(255,0,0,0);
	    text-align: left;
	  }
	  & ::v-deep .el-radio__inner {
	    width: 14px;
	    height: 14px;
	    margin: 0;
	    padding: 0;
	    border-radius: 100%;
	    border-width: 1px;
	    border-style: solid;
	    border-color: #dcdfe6;
	    background-color: rgba(255, 255, 255, 1);
	    box-shadow: 0 0 6px rgba(255,0,0,0);
	  }
	  & ::v-deep .el-radio.is-checked .el-radio__inner {
	    width: 14px;
	    height: 14px;
	    margin: 0;
	    padding: 0;
	    border-radius: 100%;
	    border-width: 1px;
	    border-style: solid;
	    border-color: rgba(64, 158, 255, 1);
	    background-color: rgba(64, 158, 255, 1);
	    box-shadow: 0 0 6px rgba(255,0,0,0);
	  }

      .svg-container {
        color: #5b7ab8 !important;
        vertical-align: middle;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        position: absolute;
        left: 16px;
        top: 50%;
        transform: translateY(-50%);
        z-index: 1;
        padding: 0 !important;
        margin: 0 !important;
        line-height: 1 !important;
        width: 18px !important;
        height: 18px;
        text-align: center;
      }

      .el-input {
        display: inline-block;
        width: 100%;

        & ::v-deep input {
          -webkit-appearance: none;
          width: 100% !important;
          height: 46px !important;
          line-height: 46px !important;
          margin: 0 !important;
          padding: 0 16px 0 46px !important;
          color: #102a56 !important;
          font-size: 15px !important;
          border-radius: 12px !important;
          border: 1px solid rgba(174, 198, 245, 0.9) !important;
          background: linear-gradient(180deg, #f9fbff 0%, #f3f8ff 100%) !important;
          box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.85) !important;

          &::placeholder {
            color: #8aa0c6;
          }

          &:focus {
            border-color: #1f6fff !important;
            box-shadow: 0 0 0 4px rgba(31, 111, 255, 0.12) !important;
            background: #fff !important;
          }
        }
      }

    }


  }

  .center {
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate3d(-50%,-50%,0);
  }

  .right {
    position: absolute;
    left: inherit;
    right: 0;
    top: 0;
  }

  .code {
    .el-form-item__content {
      position: relative;

      .getCodeBt {
        position: absolute;
        right: 0;
        top: 50%;
        transform: translate3d(0, -50%, 0);
        line-height: 40px;
        width: 100px;
        background-color: rgba(51,51,51,0.4);
        color: #fff;
        text-align: center;
        border-radius: 0 4px 4px 0;
        height: 40px;
        overflow: hidden;
		padding: 0;
		margin: 0;
		width: 100px;
        height: 30px;
        line-height: 30px;
        border-radius: 0;
        border-width: 0;
        border-style: solid;
        border-color: rgba(64, 158, 255, 1);
        background-color: rgba(51, 51, 51, 0.4);
        box-shadow: 0 0 6px rgba(255,0,0,0);

        span {
          padding: 0 5px;
          display: inline-block;
          font-size: 16px;
          font-weight: 600;
        }
      }

      .el-input {
        & ::v-deep input {
          padding: 0 130px 0 30px;
        }
      }
    }
  }

  .setting {
    margin-bottom: 0 !important;

    & ::v-deep .el-form-item__content {
      box-sizing: border-box;
      line-height: 32px;
      height: 32px;
      font-size: 14px;
      color: #999;
      margin: 0 !important;
	  display: flex;

      .register {
      }

      .reset {
        float: right;
        width: 50%;
        text-align: right;
      }
    }
  }

  .style2 {
    padding-left: 30px;

    .svg-container {
      left: -30px !important;
    }

    .el-input {
      & ::v-deep input {
        padding: 0 15px !important;
      }
    }
  }

  .code.style2, .code.style3 {
    .el-input {
      & ::v-deep input {
        padding: 0 115px 0 15px;
      }
    }
  }

  .style3 {
    & ::v-deep .el-form-item__label {
      padding-right: 6px;
      height: 36px;
      line-height: 36px;
    }

    .el-input {
      & ::v-deep input {
        padding: 0 15px !important;
      }
    }
  }

  & ::v-deep .el-form-item__label {
	width: 30px;
	height: 30px;
	line-height: 30px;
	margin: 0;
	padding: 0;
	color: rgba(30, 25, 0, 1);
	font-size: 14px;
	border-radius: 0;
	border-width: 0;
	border-style: solid;
	border-color: rgba(0,0,0,0);
	background-color: rgba(0,0,0,0);
	box-shadow: 0 0 6px rgba(0,0,0,0);
  }

  .role {
    margin: 2px 0 8px !important;

    & ::v-deep .el-form-item__label {
      width: 56px !important;
      height: 38px;
      line-height: 38px;
      margin: 0;
      padding: 0;
      color: #1f2d3d;
      font-size: 15px;
      font-weight: 600;
      border: 0;
      background-color: transparent;
      box-shadow: none;
      text-align: left;
    }

    & ::v-deep .el-radio {
      margin-right: 16px;
      margin-bottom: 8px;
    }
  }
}

@media (max-width: 768px) {
  .loginIn {
    padding: 20px;

    .left {
      width: 100%;
      max-width: 420px;
      min-height: auto;
      padding: 34px 24px 28px;
    }

    .title-container .title {
      font-size: 24px;
    }
  }
}
</style>
