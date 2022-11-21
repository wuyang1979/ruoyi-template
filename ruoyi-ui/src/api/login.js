import request from '@/utils/request'

// 登录方法
export function login(username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid
  }
  return request({
    url: '/login',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 登录方法（账号密码验证码）
export function loginDou(username, password, smsCode, uuid) {
  const data = {
    username,
    password,
    smsCode,
    uuid
  }
  return request({
    url: '/login',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}
// 登录方法（手机号验证码）
export function smsLogin(mobile, smsCode, uuid) {
  const data = {
    mobile,
    smsCode,
    uuid
  }
  return request({
    url: '/sms/login',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}
// 注册方法
export function register(data) {
  return request({
    url: '/register',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/getInfo',
    method: 'get'
  })
}
// 获取登录方式
export function loginType() {
  return request({
    url: '/login/type',
    method: 'get'
  })
}
// 获取验证码(手机号方式)
export function smsCode(data) {
  return request({
    url: '/sms/code',
    method: 'post',
    data: data
  })
}
// 获取验证码(用户名密码方式)
export function smsDouCode(data) {
  return request({
    url: '/sms/check/code',
    method: 'post',
    data: data
  })
}
// 退出方法
export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/captchaImage',
    headers: {
      isToken: false
    },
    method: 'get',
    timeout: 20000
  })
}
// 获取系统版权等信息
export function getSysInfo() {
  return request({
    url: '/web/config',
    headers: {
      isToken: false
    },
    method: 'get'
  })
}