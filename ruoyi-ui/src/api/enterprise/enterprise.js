import request from '@/utils/request'

// 查询企业基础信息列表
export function listEnterprise(query) {
  return request({
    url: '/enterprise/enterprise/list',
    method: 'get',
    params: query
  })
}

// 获取企业性质
export function getIndustryNature() {
  return request({
    url: '/enterprise/industry/nature',
    method: 'get'
  })
}
// 获取树形行业分类
export function getIndustryTree() {
  return request({
    url: '/enterprise/industry/tree',
    method: 'get'
  })
}
// 获取企业类型
export function getIndustryType() {
  return request({
    url: '/enterprise/industry/type',
    method: 'get'
  })
}

// 查询企业基础信息详细
export function getEnterprise(id) {
  return request({
    url: '/enterprise/enterprise/' + id,
    method: 'get'
  })
}

// 新增企业基础信息
export function addEnterprise(data) {
  return request({
    url: '/enterprise/enterprise',
    method: 'post',
    data: data
  })
}

// 发布企业基础信息
export function fabuEnterprise(data) {
  return request({
    url: '/enterprise/enterprise/publish',
    method: 'post',
    data: data
  })
}

// 修改企业基础信息
export function updateEnterprise(data) {
  return request({
    url: '/enterprise/enterprise',
    method: 'put',
    data: data
  })
}

// 删除企业基础信息
export function delEnterprise(id) {
  return request({
    url: '/enterprise/enterprise/' + id,
    method: 'delete'
  })
}

// 修改企业经营情况
export function updateManagement(data) {
  return request({
    url: '/enterprise/management',
    method: 'put',
    data: data
  })
}

// 获取企业经营情况详细信息
export function getManagement(enterpriseId) {
  return request({
    url: '/enterprise/management/' + enterpriseId,
    method: 'get'
  })
}
// 新增
export function postMangement(data) {
  return request({
    url: '/enterprise/management',
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
// 企业经营情况列表
export function getment(query) {
  return request({
    url: '/enterprise/management/list',
    method: 'get',
    params: query
  })
}

// 删除企业经营情况信息
export function delManagement(id) {
  return request({
    url: '/enterprise/management/' + id,
    method: 'delete'

  })
}

// 企业基础信息撤回
export function withdraw(data) {
  return request({
    url: '/enterprise/enterprise/withdraw',
    method: 'post',
    data: data
  })
}

// 提交审核
export function submit(data) {
  return request({
    url: '/enterprise/enterprise/submitApproval',
    method: 'post',
    data: data
  })
}

// 推荐置顶
export function recommendTop(data) {
  return request({
    url: '/enterprise/enterprise/recommendTop',
    method: 'post',
    data: data
  })
}
