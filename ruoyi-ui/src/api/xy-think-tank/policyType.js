import request from '@/utils/request'

// 查询政策类型列表
export function listPolicyType(query) {
  return request({
    url: '/thinkTank/policyType/list',
    method: 'get',
    params: query
  })
}

// 查询政策类型详细
export function getPolicyType(id) {
  return request({
    url: '/thinkTank/policyType/' + id,
    method: 'get'
  })
}

// 查询政策库分类
export function fenleiPolicy(query) {
  return request({
    url: '/thinkTank/policyType/tree',
    method: 'get'
  })
}

// 新增政策类型
export function addPolicyType(data) {
  return request({
    url: '/thinkTank/policyType',
    method: 'post',
    data: data
  })
}

// 修改政策类型
export function updatePolicyType(data) {
  return request({
    url: '/thinkTank/policyType',
    method: 'put',
    data: data
  })
}

// 删除政策类型
export function delPolicyType(id) {
  return request({
    url: '/thinkTank/policyType/' + id,
    method: 'delete'
  })
}
