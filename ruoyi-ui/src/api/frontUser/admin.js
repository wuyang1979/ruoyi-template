import request from '@/utils/request'

// 查询前台用户管理列表
export function listAdmin(query) {
  return request({
    url: '/frontUser/admin/list',
    method: 'get',
    params: query
  })
}

// 查询前台用户类型
export function typeList(query) {
  return request({
    url: '/frontUser/admin/tree',
    method: 'get',
    params: query
  })
}

// 查询前台用户管理详细
export function getAdmin(userId) {
  return request({
    url: '/frontUser/admin/' + userId,
    method: 'get'
  })
}

// 新增前台用户管理
export function addAdmin(data) {
  return request({
    url: '/frontUser/admin',
    method: 'post',
    data: data
  })
}

// 修改前台用户管理
export function updateAdmin(data) {
  return request({
    url: '/frontUser/admin',
    method: 'put',
    data: data
  })
}

// 删除前台用户管理
export function delAdmin(userId) {
  return request({
    url: '/frontUser/admin/' + userId,
    method: 'delete'
  })
}

//  审批
export function approval(data) {
  return request({
    url: '/frontUser/admin/approval',
    method: 'post',
    data: data
  })
}
