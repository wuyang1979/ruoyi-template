import request from '@/utils/request'

// 查询企业行业类型列表
export function listIndustry(query) {
  return request({
    url: '/enterprise/industry/list',
    method: 'get',
    params: query
  })
}

// 查询企业行业类型详细
export function getIndustry(id) {
  return request({
    url: '/enterprise/industry/' + id,
    method: 'get'
  })
}

// 新增企业行业类型
export function addIndustry(data) {
  return request({
    url: '/enterprise/industry',
    method: 'post',
    data: data
  })
}

// 修改企业行业类型
export function updateIndustry(data) {
  return request({
    url: '/enterprise/industry',
    method: 'put',
    data: data
  })
}

// 删除企业行业类型
export function delIndustry(id) {
  return request({
    url: '/enterprise/industry/' + id,
    method: 'delete'
  })
}
