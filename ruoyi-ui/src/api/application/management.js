import request from '@/utils/request'

// 查询应用信息列表
export function listManagement(query) {
  return request({
    url: '/application/management/list',
    method: 'get',
    params: query
  })
}

// 查询应用信息分类
export function fenleiInfo(query) {
  return request({
    url: '/application/type/tree',
    method: 'get',
    params: query
  })
}

// 二维码接口
export function generateQr(data) {
  return request({
    url: '/generateQr',
    method: 'post',
    data: data
  })
}

// 查询应用信息详细
export function getManagement(id) {
  return request({
    url: '/application/management/' + id,
    method: 'get'
  })
}

// 新增应用信息
export function addManagement(data) {
  return request({
    url: '/application/management',
    method: 'post',
    data: data
  })
}

// 修改应用信息
export function updateManagement(data) {
  return request({
    url: '/application/management',
    method: 'put',
    data: data
  })
}

// 删除应用信息
export function delManagement(id) {
  return request({
    url: '/application/management/' + id,
    method: 'delete'
  })
}

// 发布/取消发布
export function fabuManagement(data) {
  return request({
    url: '/application/management/releaseOrCancel',
    method: 'post',
    data: data
  })
}


// 提交审核
export function submit(data) {
  return request({
    url: '/application/management/submit',
    method: 'post',
    data: data
  })
}

// 撤回
export function withdraw(data) {
  return request({
    url: '/application/management/withdraw',
    method: 'post',
    data: data
  })
}

// 推荐置顶
export function recommendTop(data) {
  return request({
    url: '/application/management/recommendTop',
    method: 'post',
    data: data
  })
}
