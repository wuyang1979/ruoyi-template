import request from '@/utils/request'

// 查询技术库分类列表
export function listTechnologyType(query) {
  return request({
    url: '/thinkTank/technologyType/list',
    method: 'get',
    params: query
  })
}

// 查询技术库分类详细
export function getTechnologyType(id) {
  return request({
    url: '/thinkTank/technologyType/' + id,
    method: 'get'
  })
}

// 新增技术库分类
export function addTechnologyType(data) {
  return request({
    url: '/thinkTank/technologyType',
    method: 'post',
    data: data
  })
}

// 修改技术库分类
export function updateTechnologyType(data) {
  return request({
    url: '/thinkTank/technologyType',
    method: 'put',
    data: data
  })
}

// 删除技术库分类
export function delTechnologyType(id) {
  return request({
    url: '/thinkTank/technologyType/' + id,
    method: 'delete'
  })
}
