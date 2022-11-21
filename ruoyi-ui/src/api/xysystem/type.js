import request from '@/utils/request'

// 查询新闻类型列表
export function listType(query) {
  return request({
    url: '/xyNews/type/list',
    method: 'get',
    params: query
  })
}

// 查询新闻类型详细
export function getType(id) {
  return request({
    url: '/xyNews/type/' + id,
    method: 'get'
  })
}

// 新增新闻类型
export function addType(data) {
  return request({
    url: '/xyNews/type',
    method: 'post',
    data: data
  })
}

// 修改新闻类型
export function updateType(data) {
  return request({
    url: '/xyNews/type',
    method: 'put',
    data: data
  })
}

// 删除新闻类型
export function delType(id) {
  return request({
    url: '/xyNews/type/' + id,
    method: 'delete'
  })
}
