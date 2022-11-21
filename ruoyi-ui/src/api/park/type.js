import request from '@/utils/request'

// 查询园区分类列表
export function listType(query) {
  return request({
    url: '/park/type/list',
    method: 'get',
    params: query
  })
}

// 查询园区分类详细
export function getType(id) {
  return request({
    url: '/park/type/' + id,
    method: 'get'
  })
}

// 新增园区分类
export function addType(data) {
  return request({
    url: '/park/type',
    method: 'post',
    data: data
  })
}

// 修改园区分类
export function updateType(data) {
  return request({
    url: '/park/type',
    method: 'put',
    data: data
  })
}

// 删除园区分类
export function delType(id) {
  return request({
    url: '/park/type/' + id,
    method: 'delete'
  })
}
