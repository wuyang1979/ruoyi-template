import request from '@/utils/request'

// 查询专家库列表
export function listExpert(query) {
  return request({
    url: '/thinkTank/expert/list',
    method: 'get',
    params: query
  })
}

// 查询专家库详细
export function getExpert(id) {
  return request({
    url: '/thinkTank/expert/' + id,
    method: 'get'
  })
}

// 籍贯，政治面貌，。。。
export function zzzExpert(id) {
  return request({
    url: '/thinkTank/expert/type?dictType=' + id,
    method: 'get'
  })
}


// 新增专家库
export function addExpert(data) {
  return request({
    url: '/thinkTank/expert',
    method: 'post',
    data: data
  })
}

// 新增专家库
export function afabuExpert(data) {
  return request({
    url: '/thinkTank/expert/releaseOrCancel',
    method: 'post',
    data: data
  })
}

// 修改专家库
export function updateExpert(data) {
  return request({
    url: '/thinkTank/expert',
    method: 'put',
    data: data
  })
}

// 删除专家库
export function delExpert(id) {
  return request({
    url: '/thinkTank/expert/' + id,
    method: 'delete'
  })
}

// 提交审核
export function submit(data) {
  return request({
    url: '/thinkTank/expert/submit',
    method: 'post',
    data: data
  })
}

// 撤回
export function withdraw(data) {
  return request({
    url: '/thinkTank/expert/withdraw',
    method: 'post',
    data: data
  })
}

// 推荐置顶
export function updateRecTop(data) {
  return request({
    url: '/thinkTank/expert/recommendTop',
    method: 'post',
    data: data
  })
}
