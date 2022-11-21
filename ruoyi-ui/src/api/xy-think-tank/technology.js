import request from '@/utils/request'

// 查询技术库列表
export function listTechnology(query) {
  return request({
    url: '/thinkTank/technology/list',
    method: 'get',
    params: query
  })
}

// 查询技术库详细
export function getTechnology(id) {
  return request({
    url: '/thinkTank/technology/' + id,
    method: 'get'
  })
}
// 查询技术库分类
export function fenleiTechnology(query) {
  return request({
    url: '/thinkTank/technologyType/tree',
    method: 'get'
  })
}

// 新增技术库
export function addTechnology(data) {
  return request({
    url: '/thinkTank/technology',
    method: 'post',
    data: data
  })
}

// 修改技术库
export function updateTechnology(data) {
  return request({
    url: '/thinkTank/technology',
    method: 'put',
    data: data
  })
}

// 删除技术库
export function delTechnology(id) {
  return request({
    url: '/thinkTank/technology/' + id,
    method: 'delete'
  })
}
// 技术发布
export function fabuTechnology(data) {
  return request({
    url: '/thinkTank/technology/releaseOrCancel',
    method: 'post',
    data: data
  })
}

// 提交审核
export function submit(data) {
  return request({
    url: '/thinkTank/technology/submit',
    method: 'post',
    data: data
  })
}

// 推荐置顶
export function updateRecTop(data) {
  return request({
    url: '/thinkTank/technology/recommendTop',
    method: 'post',
    data: data
  })
}

// 撤回
export function withdraw(data) {
  return request({
    url: '/thinkTank/technology/withdraw',
    method: 'post',
    data: data
  })
}
