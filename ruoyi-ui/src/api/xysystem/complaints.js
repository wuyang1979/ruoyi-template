import request from '@/utils/request.js'

// 查询投诉建议列表
export function listComplaints(query) {
  return request({
    url: '/xySystem/complaints/list',
    method: 'get',
    params: query
  })
}

// 查询投诉建议详细
export function getComplaints(id) {
  return request({
    url: '/system/complaints/' + id,
    method: 'get'
  })
}

// 新增投诉建议
export function addComplaints(data) {
  return request({
    url: '/system/complaints',
    method: 'post',
    data: data
  })
}

// 修改投诉建议
export function updateComplaints(data) {
  return request({
    url: '/xySystem/complaints/',
    method: 'put',
    data: data
  })
}

// 删除投诉建议
export function delComplaints(id) {
  return request({
    url: '/system/complaints/' + id,
    method: 'delete'
  })
}
