import request from '@/utils/request'

// 查询应用分类列表
export function listType(query) {
  return request({
    url: '/application/type/list',
    method: 'get',
    params: query
  })
}

// 查询应用分类详细
export function getType(id) {
  return request({
    url: '/application/type/' + id,
    method: 'get'
  })
}

// 新增应用分类
export function addType(data) {
  return request({
    url: '/application/type',
    method: 'post',
    data: data
  })
}

// 修改应用分类
export function updateType(data) {
  return request({
    url: '/application/type',
    method: 'put',
    data: data
  })
}

// 删除应用分类
export function delType(id) {
  return request({
    url: '/application/type/' + id,
    method: 'delete'
  })
}
