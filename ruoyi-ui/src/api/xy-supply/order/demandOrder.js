import request from '@/utils/request'

// 查询需求订单列表
export function listDemandOrder(query) {
  return request({
    url: '/supply/demandOrder/list',
    method: 'get',
    params: query
  })
}

// 查询需求订单详细
export function getDemandOrder(id) {
  return request({
    url: '/supply/demandOrder/' + id,
    method: 'get'
  })
}

// 新增需求订单
export function addDemandOrder(data) {
  return request({
    url: '/supply/demandOrder',
    method: 'post',
    data: data
  })
}

// 修改需求订单
export function updateDemandOrder(data) {
  return request({
    url: '/supply/demandOrder',
    method: 'put',
    data: data
  })
}

// 删除需求订单
export function delDemandOrder(id) {
  return request({
    url: '/supply/demandOrder/' + id,
    method: 'delete'
  })
}
