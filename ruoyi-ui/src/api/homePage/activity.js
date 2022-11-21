import request from '@/utils/request'


// 查询平台新增/总量
export function queryUserActivity(query) {
    return request({
      url: '/home/userActivity',
      method: 'get',
      params: query
    })
  }