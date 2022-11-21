
import request from '@/utils/request.js'

// 消息列表请求
export function listMessage(query) {
    return request({
      url: '/xySystem/message/list/',
      method: 'get',
      params: query
    })
  }

//   新增提交
  export function addMessage(data) {
    return request({
      url: '/xySystem/message/add/',
      method: 'post',
      data: data
    })
  }

//   修改提交
  export function updateMessage(data) {
    return request({
      url: '/xySystem/message/edit/',
      method: 'put',
      data: data
    })
  }

  //  获取用户列表
  export function optionList(query) {
    return request({
      url: '/xySystem/message/userList/',
      method: 'get',
      params: query
    })
  }

// 查询消息详细
export function getMessage(id) {
  return request({
    url: '/xySystem/message/' + id,
    method: 'get'
  })
}

// 删除新闻动态
export function delMessage(id) {
  return request({
    url: '/xySystem/message/' + id,
    method: 'delete'
  })
}
