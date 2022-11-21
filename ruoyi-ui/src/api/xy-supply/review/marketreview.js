import request from '@/utils/request'

// 查询供需需求市场列表
export function listMarket(query) {
  return request({
    url: '/supply/market/draftList',
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

// 批量审批供需需求市场
export function batchMarket(data) {
  return request({
    url: '/supply/market/batchApproval',
    method: 'post',
    data: data
  })
}

// 审批供需需求市场
export function reviewMarket(data) {
  return request({
    url: '/supply/market/approval',
    method: 'post',
    data: data
  })
}



