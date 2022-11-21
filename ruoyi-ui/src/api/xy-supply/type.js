import request from '@/utils/request'

// 查询供需分类列表
export function listType(query) {
  return request({
    url: '/supply/type/list',
    method: 'get',
    params: query
  })
}

// 查询供需分类详细
export function getType(id) {
  return request({
    url: '/supply/type/' + id,
    method: 'get'
  })
}

// 新增供需分类
export function addType(data) {
  return request({
    url: '/supply/type',
    method: 'post',
    data: data
  })
}

// 修改供需分类
export function updateType(data) {
  return request({
    url: '/supply/type',
    method: 'put',
    data: data
  })
}

// 删除供需分类
export function delType(id) {
  return request({
    url: '/supply/type/' + id,
    method: 'delete'
  })
}
