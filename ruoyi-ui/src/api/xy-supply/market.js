import request from '@/utils/request'

// 查询供需需求市场列表
export function listMarket(query) {
  return request({
    url: '/supply/market/list',
    method: 'get',
    params: query
  })
}

// 查询供需需求市场详细
export function getMarket(id) {
  return request({
    url: '/supply/market/' + id,
    method: 'get'
  })
}

// 新增供需需求市场
export function addMarket(data) {
  return request({
    url: '/supply/market',
    method: 'post',
    data: data
  })
}

// 修改供需需求市场
export function updateMarket(data) {
  return request({
    url: '/supply/market',
    method: 'put',
    data: data
  })
}

// 发布撤销供需需求市场
export function releaseMarket(data) {
  return request({
    url: '/supply/market/releaseOrCancel',
    method: 'post',
    data: data
  })
}

// 删除供需需求市场
export function delMarket(id) {
  return request({
    url: '/supply/market/' + id,
    method: 'delete'
  })
}

// 提交审核
export function submit(data) {
  return request({
    url: '/supply/market/submitApproval',
    method: 'post',
    data: data
  })
}

// 撤回
export function withdraw(data) {
  return request({
    url: '/supply/market/withdraw',
    method: 'post',
    data: data
  })
}

// 推荐置顶
export function recommendTop(data) {
  return request({
    url: '/supply/market/recommendTop',
    method: 'post',
    data: data
  })
}
