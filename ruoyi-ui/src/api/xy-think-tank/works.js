import request from '@/utils/request'

// 查询专家作品列表
export function listWorks(query) {
  return request({
    url: '/thinkTank/expertWorks/list',
    method: 'get',
    params: query
  })
}

// 查询专家作品详细
export function getWorks(id) {
  return request({
    url: '/thinkTank/expertWorks/' + id,
    method: 'get'
  })
}
// 查询专家作品分类
export function classifyWorks(id) {
  return request({
    url: '/thinkTank/expertWorksType/tree',
    method: 'get'
  })
}
// 类型
export function typeWorks() {
  return request({
    url: '/xyNews/type/type',
    method: 'get'
  })
}
// 新增专家作品
export function addWorks(data) {
  return request({
    url: '/thinkTank/expertWorks',
    method: 'post',
    data: data
  })
}

// fabu专家作品
export function fabuWorks(data) {
  return request({
    url: '/thinkTank/expertWorks/releaseOrCancel',
    method: 'post',
    data: data
  })
}

// 修改专家作品
export function updateWorks(data) {
  return request({
    url: '/thinkTank/expertWorks',
    method: 'put',
    data: data
  })
}

// 删除专家作品
export function delWorks(id) {
  return request({
    url: '/thinkTank/expertWorks/' + id,
    method: 'delete'
  })
}

// 提交审核
export function submit(data) {
  return request({
    url: '/thinkTank/expertWorks/submit',
    method: 'post',
    data: data
  })
}

// 撤回
export function withdraw(data) {
  return request({
    url: '/thinkTank/expertWorks/withdraw',
    method: 'post',
    data: data
  })
}

// 推荐置顶
export function updateRecTop(data) {
  return request({
    url: '/thinkTank/expertWorks/recommendTop',
    method: 'post',
    data: data
  })
}

// 查询已发布专家库列表
export function getExpertList(data) {
  return request({
    url: '/thinkTank/expert/publishList',
    method: 'get',
    params: data
  })
}
