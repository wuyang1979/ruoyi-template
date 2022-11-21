import request from '@/utils/request.js'

// 友情链接接口
export function listLink(query) {
    return request({
      url: '/xySystem/link/list',
      method: 'get',
      params: query
    })
  }
// 新增接口
  export function addLink(data) {
    return request({
      url: '/xySystem/link/add',
      method: 'post',
      data: data
    })
  }
// 修改查询详情接口
  export function getLink(data) {
    return request({
      url: '/xySystem/link/'+ data,
      method: 'get',
    })
  }
// 修改提交接口
  export function updateLink(data) {
    return request({
      url: '/xySystem/link/',
      method: 'put',
      data: data
    })
  }
  // 删除
  export function delLink(ids) {
    return request({
      url: '/xySystem/link/' + ids,
      method: 'delete'
    })
  }
  // 禁用启用
  export function statusLink(data) {
    return request({
      url: '/xySystem/link/status',
      method: 'post',
      data: data
    })
  }

  