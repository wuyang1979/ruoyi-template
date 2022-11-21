import request from '@/utils/request'


// 查询平台新增/总量
export function queryPlatformData(query) {
  return request({
    url: '/home/totalCount',
    method: 'get',
    params: query
  })
}

// 查询板块数量
export function queryBlockData(query) {
  return request({
    url: '/home/moduleCount',
    method: 'get',
    params: query
  })
}