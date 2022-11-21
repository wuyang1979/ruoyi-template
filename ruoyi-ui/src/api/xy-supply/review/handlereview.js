import request from '@/utils/request'


// 查询供需产品待审批信息列表
export function listInfo(query) {
    return request({
      url: '/supply/info/draftList',
      method: 'get',
      params: query
    })
  }

    //   产品批量审批
    export function updetainfois(data) {
        return request({
          url: '/supply/info/batchApproval',
          method: 'post',
          data: data
        })
      }

      //   产品审批
  export function updetareinfo(data) {
    return request({
      url: '/supply/info/approval',
      method: 'post',
      data: data
    })
  }

  // 查询供需产品信息详细
export function getInfo(id) {
    return request({
      url: '/supply/info/' + id,
      method: 'get'
    })
  }