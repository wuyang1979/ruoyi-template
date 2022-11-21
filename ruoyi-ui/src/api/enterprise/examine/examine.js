import request from '@/utils/request'


// 查询待审核企业基础信息列表
export function listEnterprise(query) {
    return request({
      url: '/enterprise/enterprise/draftList',
      method: 'get',
      params: query
    })
  }

  // 查询企业基础信息详细
export function getEnterprise(id) {
    return request({
      url: '/enterprise/enterprise/' + id,
      method: 'get'
    })
  }

  // 审核企业基础信息
export function examineEnterprise(data) {
    return request({
      url: '/enterprise/enterprise/approve',
      method: 'post',
      data: data
    })
  }

// 获取省市区
  export function getRegion(parentCode) {
    return request({
      url: '/system/region/list/' + parentCode,
      method: 'get'
    })
  }
