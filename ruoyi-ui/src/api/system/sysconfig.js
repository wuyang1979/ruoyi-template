import request from '@/utils/request'

// 查询系统列表
export function listSysConfig(query) {
  return request({
    url: '/system/config/xy/config/list',
    method: 'get',
    params: query
  })
}

// 系统详情
export function getSysDetail(configId) {
  return request({
    url: '/system/config/xy/config/' + configId,
    method: 'get'
  })
}

// 根据参数键名查询参数值
export function editSysConfig(data) {
  return request({
    url: '/system/config/xy/config/edit',
    method: 'post',
    data: data
  })
}


