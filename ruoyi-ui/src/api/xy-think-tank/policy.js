import request from '@/utils/request'

// 查询政策库列表
export function listPolicy(query) {
  return request({
    url: '/thinkTank/policy/list',
    method: 'get',
    params: query
  })
}


// 查询政策库分类
export function fenleiPolicy(query) {
  return request({
    url: '/thinkTank/policyType/tree',
    method: 'get'
  })
}

// 查询政策库详细
export function getPolicy(id) {
  return request({
    url: '/thinkTank/policy/' + id,
    method: 'get'
  })
}

// 新增政策库
export function addPolicy(data) {
  return request({
    url: '/thinkTank/policy',
    method: 'post',
    data: data
  })
}
// 发布
export function fabuPolicy(data) {
  return request({
    url: '/thinkTank/policy/releaseOrCancel',
    method: 'post',
    data: data
  })
}

// 修改政策库
export function updatePolicy(data) {
  return request({
    url: '/thinkTank/policy',
    method: 'put',
    data: data
  })
}

// 删除政策库
export function delPolicy(id) {
  return request({
    url: '/thinkTank/policy/' + id,
    method: 'delete'
  })
}

// 提交审核
export function submit(data) {
  return request({
    url: '/thinkTank/policy/submit',
    method: 'post',
    data: data
  })
}

// 撤回
export function withdraw(data) {
  return request({
    url: '/thinkTank/policy/withdraw',
    method: 'post',
    data: data
  })
}

// 推荐置顶
export function updateRecTop(data) {
  return request({
    url: '/thinkTank/policy/recommendTop',
    method: 'post',
    data: data
  })
}
