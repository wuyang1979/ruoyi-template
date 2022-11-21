<template>
  <div class="login">
    <el-form
      v-show="loginTypeFlag===1"
      ref="loginDouForm"
      :model="loginDouForm"
      :rules="loginDouRules"
      class="login-form"
    >
      <h3 class="title">后台管理系统</h3>
      <el-form-item prop="username">
        <el-input v-model="loginDouForm.username" type="text" auto-complete="off" placeholder="账号">
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginDouForm.password"
          type="password"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter.native="handleDouLogin"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="smsCode">
        <el-input
          v-model="loginDouForm.smsCode"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter.native="handleDouLogin"
        >
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="login-code">
          <!-- <img :src="codeUrl" @click="getCode" class="login-code-img"/> -->
          <el-button
            @click="getMsDouCode"
            type="primary"
            v-if="this.showDouCode"
            style="height: 38px"
          >获取验证码</el-button>
          <el-button type="primary" disabled v-else style="height: 38px">{{ count }} s后重发</el-button>
        </div>
      </el-form-item>
      <el-checkbox v-model="loginDouForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>

      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleDouLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <div style="float: right;" v-if="register">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
        </div>
      </el-form-item>
    </el-form>

    <el-form
      v-show="loginTypeFlag===2"
      ref="loginForm"
      :model="loginForm"
      :rules="loginRules"
      class="login-form"
    >
      <h3 class="title">后台管理系统</h3>
      <el-form-item prop="username">
        <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="账号">
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="code" v-if="captchaEnabled">
        <el-input
          v-model="loginForm.code"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img" />
        </div>
      </el-form-item>
      <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>

      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <div style="float: right;" v-if="register">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
        </div>
      </el-form-item>
    </el-form>

    <el-form
      v-show="loginTypeFlag===3"
      ref="loginVerForm"
      :model="loginVerForm"
      :rules="loginVerRules"
      class="login-form"
    >
      <h3 class="title">后台管理系统</h3>
      <el-form-item prop="mobile">
        <el-input
          v-model="loginVerForm.mobile"
          type="text"
          auto-complete="off"
          placeholder="输入手机号"
          @keyup.enter.native="handleVerLogin"
        >
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="smsCode">
        <el-input
          v-model="loginVerForm.smsCode"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter.native="handleVerLogin"
        >
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="login-code">
          <!-- <img :src="codeUrl" @click="getCode" class="login-code-img"/> -->
          <el-button
            @click="getMsCode"
            type="primary"
            v-if="this.showCode"
            style="height: 38px"
          >获取验证码</el-button>
          <el-button type="primary" disabled v-else style="height: 38px">{{ count }} s后重发</el-button>
        </div>
      </el-form-item>

      <el-button
        :loading="loading"
        size="medium"
        type="primary"
        style="width:100%;margin:0px 0px 25px 0px;"
        @click.native.prevent="handleVerLogin"
      >
        <span v-if="!loading">登 录</span>
        <span v-else>登 录 中...</span>
      </el-button>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright {{Copyright}} © 2022 {{ICP}} {{police}}</span>
    </div>
  </div>
</template>

<script>
import {
  getCodeImg,
  loginType,
  smsCode,
  smsDouCode,
  getSysInfo,
} from '@/api/login'
import Cookies from 'js-cookie'
import { encrypt, decrypt } from '@/utils/jsencrypt'

export default {
  name: 'Login',
  data() {
    return {
      codeUrl: '',
      Copyright: '',
      ICP: '',
      police: '',
      loginDouForm: {
        username: '',
        password: '',
        rememberMe: false,
        smsCode: '',
        uuid: '',
      },
      loginForm: {
        username: '',
        password: '',
        rememberMe: false,
        code: '',
        uuid: '',
      },
      loginVerForm: {
        mobile: '',
        smsCode: '',
        uuid: '',
      },
      loginDouRules: {
        username: [
          { required: true, trigger: 'blur', message: '请输入您的账号' },
        ],
        password: [
          { required: true, trigger: 'blur', message: '请输入您的密码' },
        ],
        smsCode: [
          { required: true, trigger: 'change', message: '请输入验证码' },
        ],
      },
      loginRules: {
        username: [
          { required: true, trigger: 'blur', message: '请输入您的账号' },
        ],
        password: [
          { required: true, trigger: 'blur', message: '请输入您的密码' },
        ],
        code: [{ required: true, trigger: 'change', message: '请输入验证码' }],
      },
      loginVerRules: {
        mobile: [
          { required: true, trigger: 'blur', message: '请输入您的手机号' },
          {
            pattern:
              /^((1[3,5,8,7,9][0-9])|(14[5,7])|(17[0,6,7,8])|(19[7]))\d{8}$/,
            trigger: 'blur',
            message: '请输入正确的手机号',
          },
        ],
        smsCode: [
          { required: true, trigger: 'blur', message: '请输入您的验证码' },
        ],
      },
      loading: false,
      // 验证码开关
      captchaEnabled: true,
      // 注册开关
      register: false,
      redirect: undefined,
      loginTypeFlag: 1,
      showCode: true,
      showDouCode: true,
      count: '',
      timer: null,
    }
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true,
    },
  },
  created() {
    this.getCode()
    this.getLoginType()
    this.getCookie()
    this.getSysInfo()
  },
  methods: {
    getCode() {
      getCodeImg().then((res) => {
        this.captchaEnabled =
          res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          this.codeUrl = 'data:image/gif;base64,' + res.img
          this.loginForm.uuid = res.uuid
        }
      })
    },
    getSysInfo() {
      getSysInfo().then((res) => {
        this.Copyright = res.data[0].copyright
        this.ICP = res.data[0].ICP
        this.police = res.data[0].police
      })
    },
    getLoginType() {
      loginType().then((res) => {
        if (res.data[0].password === true && res.data[0].verifyCode === true) {
          this.loginTypeFlag = 1
        } else if (
          res.data[0].password === true &&
          res.data[0].verifyCode === false
        ) {
          this.loginTypeFlag = 2
        } else if (
          res.data[0].password === false &&
          res.data[0].verifyCode === true
        ) {
          this.loginTypeFlag = 3
        } else if (
          res.data[0].password === false &&
          res.data[0].verifyCode === false
        ) {
          this.loginTypeFlag = 1
        }
      })
    },
    getMsDouCode() {
      if (this.loginDouForm.username && this.loginDouForm.password) {
        let data = {
          username: this.loginDouForm.username,
          password: encrypt(this.loginDouForm.password),
        }
        smsDouCode(data).then((res) => {
          if (res.code === 200) {
            this.$modal.msgSuccess(res.msg)
            this.$set(this.loginDouForm, 'uuid', res.uuid)
            const TIME_COUNT = 60
            if (!this.timer) {
              this.count = TIME_COUNT
              this.showDouCode = false
              this.timer = setInterval(() => {
                if (this.count > 0 && this.count <= TIME_COUNT) {
                  this.count--
                } else {
                  this.showDouCode = true
                  clearInterval(this.timer)
                  this.timer = null
                }
              }, 1000)
            }
          } else {
            this.$modal.msgError(res.msg)
          }
        })
      } else {
        this.$modal.msgError('请填写用户名及密码！')
      }
    },
    getMsCode() {
      if (this.loginVerForm.mobile) {
        let data = {
          mobile: this.loginVerForm.mobile,
        }
        smsCode(data).then((res) => {
          if (res.code === 200) {
            this.$modal.msgSuccess(res.msg)
            this.$set(this.loginVerForm, 'smsCode', res.smsCode)
            this.$set(this.loginVerForm, 'uuid', res.uuid)
            const TIME_COUNT = 60
            if (!this.timer) {
              this.count = TIME_COUNT
              this.showCode = false
              this.timer = setInterval(() => {
                if (this.count > 0 && this.count <= TIME_COUNT) {
                  this.count--
                } else {
                  this.showCode = true
                  clearInterval(this.timer)
                  this.timer = null
                }
              }, 1000)
            }
          } else {
            this.$modal.msgError(res.msg)
          }
        })
      } else {
        this.$modal.msgError('请填写手机号！')
      }
    },
    getCookie() {
      const username = Cookies.get('username')
      const password = Cookies.get('password')
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password:
          password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
      }
    },
    handleDouLogin() {
      this.$refs.loginDouForm.validate((valid) => {
        if (valid) {
          this.loading = true
          if (this.loginDouForm.rememberMe) {
            Cookies.set('username', this.loginDouForm.username, { expires: 30 })
            Cookies.set('password', encrypt(this.loginDouForm.password), {
              expires: 30,
            })
            Cookies.set('rememberMe', this.loginDouForm.rememberMe, {
              expires: 30,
            })
          } else {
            Cookies.remove('username')
            Cookies.remove('password')
            Cookies.remove('rememberMe')
          }
          this.$store
            .dispatch('LoginDou', this.loginDouForm)
            .then(() => {
              this.$router.push({ path: this.redirect || '/' }).catch(() => {})
            })
            .catch(() => {
              this.loading = false
            })
        }
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.loading = true
          if (this.loginForm.rememberMe) {
            Cookies.set('username', this.loginForm.username, { expires: 30 })
            Cookies.set('password', encrypt(this.loginForm.password), {
              expires: 30,
            })
            Cookies.set('rememberMe', this.loginForm.rememberMe, {
              expires: 30,
            })
          } else {
            Cookies.remove('username')
            Cookies.remove('password')
            Cookies.remove('rememberMe')
          }
          this.$store
            .dispatch('Login', this.loginForm)
            .then(() => {
              this.$router.push({ path: this.redirect || '/' }).catch(() => {})
            })
            .catch(() => {
              this.loading = false
              if (this.captchaEnabled) {
                this.getCode()
              }
            })
        }
      })
    },
    handleVerLogin() {
      this.$refs.loginVerForm.validate((valid) => {
        if (valid) {
          this.loading = true
          this.$store
            .dispatch('smsLogin', this.loginVerForm)
            .then(() => {
              this.$router.push({ path: this.redirect || '/' }).catch(() => {})
            })
            .catch(() => {
              this.loading = false
            })
        }
      })
    },
  },
}
</script>

<style rel="stylesheet/scss" lang="scss">
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url('../assets/images/login-background.jpg');
  background-size: cover;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
.login-code-img {
  height: 38px;
}
</style>
