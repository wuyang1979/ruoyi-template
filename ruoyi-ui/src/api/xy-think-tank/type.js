import request from '@/utils/request'

// 查询政策类型列表
export function listType(query) {
  return request({
    url: '/thinkTank/policyType/list',
    method: 'get',
    params: query
  })
}

// 查询政策类型详细
export function getType(id) {
  return request({
    url: '/thinkTank/policyType/' + id,
    method: 'get'
  })
}

// 新增政策类型
export function addType(data) {
  return request({
    url: '/thinkTank/policyType',
    method: 'post',
    data: data
  })
}

// 修改政策类型
export function updateType(data) {
  return request({
    url: '/thinkTank/policyType',
    method: 'put',
    data: data
  })
}

// 删除政策类型
export function delType(id) {
  return request({
    url: '/thinkTank/policyType/' + id,
    method: 'delete'
  })
}
