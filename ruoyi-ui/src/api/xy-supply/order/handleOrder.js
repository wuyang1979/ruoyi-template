import request from '@/utils/request'

// 查询Handle品订单列表
export function listHandleOrder(query) {
  return request({
    url: '/supply/handleOrder/list',
    method: 'get',
    params: query
  })
}

// 查询Handle品订单详细
export function getHandleOrder(id) {
  return request({
    url: '/supply/handleOrder/' + id,
    method: 'get'
  })
}

// 新增Handle品订单
export function addHandleOrder(data) {
  return request({
    url: '/supply/handleOrder',
    method: 'post',
    data: data
  })
}

// 修改Handle品订单
export function updateHandleOrder(data) {
  return request({
    url: '/supply/handleOrder',
    method: 'put',
    data: data
  })
}

// 删除Handle品订单
export function delHandleOrder(id) {
  return request({
    url: '/supply/handleOrder/' + id,
    method: 'delete'
  })
}
