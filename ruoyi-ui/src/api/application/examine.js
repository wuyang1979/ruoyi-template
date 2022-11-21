import request from '@/utils/request'

export function listManagement(query) {
    return request({
      url: '/application/management/draftList',
      method: 'get',
      params: query
    })
}

// 查询应用信息详细
export function getManagement(id) {
  return request({
    url: '/application/management/' + id,
    method: 'get'
  })
}

//  审批
export function approval(data) {
  return request({
    url: '/application/management/approval',
    method: 'post',
    data: data
  })
}

//  批量审批
export function batchApproval(data) {
  return request({
    url: '/application/management/batchApproval',
    method: 'post',
    data: data
  })
}
