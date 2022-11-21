import request from '@/utils/request'

// 查询园区信息列表
export function listManagement(query) {
  return request({
    url: '/park/management/list',
    method: 'get',
    params: query
  })
}

// 查询园区信息详细
export function getManagement(id) {
  return request({
    url: '/park/management/' + id,
    method: 'get'
  })
}

// 新增园区信息
export function addManagement(data) {
  return request({
    url: '/park/management',
    method: 'post',
    data: data
  })
}

// 修改园区信息
export function updateManagement(data) {
  return request({
    url: '/park/management',
    method: 'put',
    data: data
  })
}

// 删除园区信息
export function delManagement(id) {
  return request({
    url: '/park/management/' + id,
    method: 'delete'
  })
}

// 查询应用信息分类
export function fenleiInfo(query) {
  return request({
    url: '/park/type/tree',
    method: 'get',
    params: query
  })
}

// 发布/取消发布
export function fabuManagement(data) {
  return request({
    url: '/park/management/releaseOrCancel',
    method: 'post',
    data: data
  })
}

// 获取省市区
export function getRegion(parentCode) {
  return request({
    url: '/system/region/list/' + parentCode,
    method: 'get'
  })
}

// 提交审核
export function submit(data) {
  return request({
    url: '/park/management/submitApproval',
    method: 'post',
    data: data
  })
}

// 撤回
export function withdraw(data) {
  return request({
    url: '/park/management/withdraw',
    method: 'post',
    data: data
  })
}

// 推荐置顶
export function recommendTop(data) {
  return request({
    url: '/park/management/recommendTop',
    method: 'post',
    data: data
  })
}
