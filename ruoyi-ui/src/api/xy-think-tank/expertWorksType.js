import request from '@/utils/request'

// 查询专家作品类型列表
export function listExpertWorksType(query) {
  return request({
    url: '/thinkTank/expertWorksType/list',
    method: 'get',
    params: query
  })
}

// 查询专家作品类型详细
export function getExpertWorksType(id) {
  return request({
    url: '/thinkTank/expertWorksType/' + id,
    method: 'get'
  })
}

// 新增专家作品类型
export function addExpertWorksType(data) {
  return request({
    url: '/thinkTank/expertWorksType',
    method: 'post',
    data: data
  })
}

// 修改专家作品类型
export function updateExpertWorksType(data) {
  return request({
    url: '/thinkTank/expertWorksType',
    method: 'put',
    data: data
  })
}

// 删除专家作品类型
export function delExpertWorksType(id) {
  return request({
    url: '/thinkTank/expertWorksType/' + id,
    method: 'delete'
  })
}
