import request from '@/utils/request'

// 查询政策解读类型列表
export function listPolicyInterpretType(query) {
  return request({
    url: '/thinkTank/policyInterpretType/list',
    method: 'get',
    params: query
  })
}

// 查询政策解读类型详细
export function getPolicyInterpretType(id) {
  return request({
    url: '/thinkTank/policyInterpretType/' + id,
    method: 'get'
  })
}

// 新增政策解读类型
export function addPolicyInterpretType(data) {
  return request({
    url: '/thinkTank/policyInterpretType',
    method: 'post',
    data: data
  })
}

// 修改政策解读类型
export function updatePolicyInterpretType(data) {
  return request({
    url: '/thinkTank/policyInterpretType',
    method: 'put',
    data: data
  })
}

// 删除政策解读类型
export function delPolicyInterpretType(id) {
  return request({
    url: '/thinkTank/policyInterpretType/' + id,
    method: 'delete'
  })
}
