import request from '@/utils/request'

// 查询政策解读列表
export function listInterpret(query) {
  return request({
    url: '/thinkTank/policyInterpret/list',
    method: 'get',
    params: query
  })
}

// 查询政策解读详细
export function getInterpret(id) {
  return request({
    url: '/thinkTank/policyInterpret/' + id,
    method: 'get'
  })
}
// 查询政策库分类
export function fenleiPolicy(query) {
  return request({
    url: '/thinkTank/policyInterpretType/tree',
    method: 'get'
  })
}
// 新增政策解读
export function addInterpret(data) {
  return request({
    url: '/thinkTank/policyInterpret',
    method: 'post',
    data: data
  })
}
// 新增政策解读
export function fabuInterpret(data) {
  return request({
    url: '/thinkTank/policyInterpret/releaseOrCancel',
    method: 'post',
    data: data
  })
}

// 修改政策解读
export function updateInterpret(data) {
  return request({
    url: '/thinkTank/policyInterpret',
    method: 'put',
    data: data
  })
}

// 删除政策解读
export function delInterpret(id) {
  return request({
    url: '/thinkTank/policyInterpret/' + id,
    method: 'delete'
  })
}

// 提交审核
export function submit(data) {
  return request({
    url: '/thinkTank/policyInterpret/submit',
    method: 'post',
    data: data
  })
}

// 撤回
export function withdraw(data) {
  return request({
    url: '/thinkTank/policyInterpret/withdraw',
    method: 'post',
    data: data
  })
}


// 推荐置顶
export function updateRecTop(data) {
  return request({
    url: '/thinkTank/policyInterpret/recommendTop',
    method: 'post',
    data: data
  })
}